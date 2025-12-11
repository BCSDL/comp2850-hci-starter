package routes

import data.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.pebbletemplates.pebble.PebbleEngine
import java.io.StringWriter
import io.ktor.http.encodeURLParameter
import utils.Page
import model.Task
import renderTemplate
import isHtmxRequest

/**
 * Week 8 Lab: Enhanced task routes with pagination, search, and fragment support.
 *
 * **Key Features**:
 * - Pagination for task lists (10 items per page)
 * - Search/filter functionality with query parameters
 * - Dual-mode routing (HTMX fragments + full-page No-JS)
 * - Preserved query state across actions (search/page)
 * - Fragment-based updates for HTMX efficiency
 */

fun Route.taskRoutes() {
    // Configure Pebble engine with classpath template loading
    val pebble = PebbleEngine.Builder()
        .loader(io.pebbletemplates.pebble.loader.ClasspathLoader().apply {
            prefix = "templates/"
        })
        .build()

    /**
     * Core pagination and filtering logic
     * Returns paginated results based on search query and page number
     */
    private fun getPaginatedTasks(query: String, pageNumber: Int, pageSize: Int = 10): Page<Task> {
        // 1. Filter tasks by search query (case-insensitive)
        val filteredTasks = TaskRepository.all().filter { task ->
            query.isBlank() || task.title.contains(query, ignoreCase = true)
        }
        
        // 2. Apply pagination using Page utility
        return Page.from(
            items = filteredTasks,
            currentPage = pageNumber,
            pageSize = pageSize
        )
    }

    /**
     * GET /tasks - Full page render with pagination and search
     * Supports: ?q=searchterm&page=2&error=blank
     */
    get("/tasks") {
        // Extract query parameters
        val query = call.request.queryParameters["q"].orEmpty()
        val pageNumber = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
        val errorParam = call.request.queryParameters["error"]
        val editingId = call.request.queryParameters["editingId"]?.toIntOrNull()

        // Get paginated data
        val taskPage = getPaginatedTasks(query, pageNumber)

        // Prepare model with all necessary data
        val model = mapOf(
            "title" to "Tasks",
            "page" to taskPage,          // Pagination data + tasks
            "query" to query,            // Preserve search term
            "error" to errorParam,       // Validation error flag
            "editingId" to editingId,    // For inline edit state
            "msg" to when (errorParam) { // Error message
                "blank" -> "Title is required"
                "too_long" -> "Title is too long (max 200 characters)"
                else -> null
            }
        )

        // Render full page
        call.respondText(
            text = renderTemplate("tasks/index.peb", model),
            contentType = ContentType.Text.Html
        )
    }

    /**
     * GET /tasks/fragment - HTMX fragment endpoint
     * Returns only dynamic content: task list + pagination + status
     * Used for live updates without full page reloads
     */
    get("/tasks/fragment") {
        // Extract query parameters
        val query = call.request.queryParameters["q"].orEmpty()
        val pageNumber = call.request.queryParameters["page"]?.toIntOrNull() ?: 1

        // Get paginated data
        val taskPage = getPaginatedTasks(query, pageNumber)

        // Render partial templates
        val listHtml = renderTemplate("tasks/_list.peb", mapOf(
            "page" to taskPage,
            "query" to query
        ))
        val pagerHtml = renderTemplate("tasks/_pager.peb", mapOf(
            "page" to taskPage,
            "query" to query
        ))
        val statusHtml = """<div id="status" hx-swap-oob="true" role="status">
            Found ${taskPage.totalItems} task${if (taskPage.totalItems != 1) "s" else ""}
        </div>"""

        // Combine and return fragments
        call.respondText(
            text = listHtml + pagerHtml + statusHtml,
            contentType = ContentType.Text.Html
        )
    }

    /**
     * POST /tasks - Add new task (dual-mode)
     * - HTMX: Returns new task fragment + status
     * - No-JS: PRG redirect with preserved search/page
     */
    post("/tasks") {
        val params = call.receiveParameters()
        val title = params["title"].orEmpty().trim()
        val query = params["q"].orEmpty() // Preserve search term
        val currentPage = params["page"]?.toIntOrNull() ?: 1

        // Validation
        when {
            title.isBlank() -> {
                // Handle blank title error
                if (call.isHtmxRequest()) {
                    call.respondText(
                        text = """<div id="status" hx-swap-oob="true" role="alert">
                            Title is required. Please enter at least one character.
                        </div>""",
                        contentType = ContentType.Text.Html,
                        status = HttpStatusCode.BadRequest
                    )
                } else {
                    // No-JS: Redirect with error and preserved state
                    val encodedQuery = if (query.isNotBlank()) "&q=${query.encodeURLParameter()}" else ""
                    call.respondRedirect("/tasks?error=blank$encodedQuery")
                }
                return@post
            }
            title.length > 200 -> {
                // Handle too long title error
                if (call.isHtmxRequest()) {
                    call.respondText(
                        text = """<div id="status" hx-swap-oob="true" role="alert">
                            Title is too long (max 200 characters).
                        </div>""",
                        contentType = ContentType.Text.Html,
                        status = HttpStatusCode.BadRequest
                    )
                } else {
                    val encodedQuery = if (query.isNotBlank()) "&q=${query.encodeURLParameter()}" else ""
                    call.respondRedirect("/tasks?error=too_long$encodedQuery")
                }
                return@post
            }
        }

        // Create task
        val newTask = TaskRepository.add(title)

        if (call.isHtmxRequest()) {
            // HTMX: Return new task fragment + success status
            val taskHtml = renderTemplate("tasks/_item.peb", mapOf("task" to newTask))
            val statusHtml = """<div id="status" hx-swap-oob="true">
                Task "${newTask.title}" added successfully.
            </div>"""
            call.respondText(
                text = taskHtml + statusHtml,
                contentType = ContentType.Text.Html,
                status = HttpStatusCode.Created
            )
        } else {
            // No-JS: PRG redirect with preserved search
            val encodedQuery = if (query.isNotBlank()) "?q=${query.encodeURLParameter()}" else ""
            call.respondRedirect("/tasks$encodedQuery", HttpStatusCode.SeeOther)
        }
    }

    /**
     * POST /tasks/{id}/delete - Delete task (dual-mode)
     * Preserves search and page state in redirects
     */
    post("/tasks/{id}/delete") {
        val id = call.parameters["id"]?.toIntOrNull() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val query = call.request.queryParameters["q"].orEmpty()
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1

        // Delete task
        val removed = TaskRepository.delete(id)

        // Build preserved query parameters
        val params = mutableListOf<String>()
        if (query.isNotBlank()) params.add("q=${query.encodeURLParameter()}")
        if (page > 1) params.add("page=$page")
        val queryString = if (params.isNotEmpty()) "?${params.joinToString("&")}" else ""

        if (call.isHtmxRequest()) {
            // HTMX: Return status message (OOB swap)
            val message = if (removed) "Task deleted successfully." else "Could not delete task."
            call.respondText(
                text = """<div id="status" hx-swap-oob="true">$message</div>""",
                contentType = ContentType.Text.Html
            )
        } else {
            // No-JS: PRG redirect with preserved state
            call.respondRedirect("/tasks$queryString", HttpStatusCode.SeeOther)
        }
    }

    /**
     * GET /tasks/{id}/edit - Edit form (dual-mode)
     * - HTMX: Returns edit fragment for inline editing
     * - No-JS: Returns full page with edit form
     */
    get("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull() ?: run {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        val task = TaskRepository.find(id) ?: run {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        val query = call.request.queryParameters["q"].orEmpty()
        val errorParam = call.request.queryParameters["error"]

        val model = mapOf(
            "task" to task,
            "query" to query,
            "error" to when (errorParam) {
                "blank" -> "Title is required"
                "too_long" -> "Title is too long (max 200 characters)"
                else -> null
            }
        )

        if (call.isHtmxRequest()) {
            // HTMX: Return edit fragment
            call.respondText(
                text = renderTemplate("tasks/partials/edit.peb", model),
                contentType = ContentType.Text.Html
            )
        } else {
            // No-JS: Return full page with edit state
            val taskPage = getPaginatedTasks(query, 1) // Reset to page 1 for edit
            call.respondText(
                text = renderTemplate("tasks/index.peb", mapOf(
                    "title" to "Edit Task",
                    "page" to taskPage,
                    "query" to query,
                    "editingId" to id,
                    "error" to errorParam,
                    "msg" to model["error"]
                )),
                contentType = ContentType.Text.Html
            )
        }
    }

    /**
     * POST /tasks/{id}/edit - Save task edits (dual-mode)
     */
    post("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val task = TaskRepository.find(id) ?: run {
            call.respond(HttpStatusCode.NotFound)
            return@post
        }
        val params = call.receiveParameters()
        val newTitle = params["title"].orEmpty().trim()
        val query = params["q"].orEmpty()
        val page = params["page"]?.toIntOrNull() ?: 1

        // Build preserved query parameters
        val paramsList = mutableListOf<String>()
        if (query.isNotBlank()) paramsList.add("q=${query.encodeURLParameter()}")
        if (page > 1) paramsList.add("page=$page")
        val queryString = if (paramsList.isNotEmpty()) "?${paramsList.joinToString("&")}" else ""

        // Validation
        when {
            newTitle.isBlank() -> {
                if (call.isHtmxRequest()) {
                    // HTMX: Return edit fragment with error
                    call.respondText(
                        text = renderTemplate("tasks/partials/edit.peb", mapOf(
                            "task" to task,
                            "query" to query,
                            "error" to "Title is required"
                        )),
                        contentType = ContentType.Text.Html,
                        status = HttpStatusCode.BadRequest
                    )
                } else {
                    // No-JS: Redirect back to edit with error
                    call.respondRedirect("/tasks/$id/edit?error=blank$queryString")
                }
                return@post
            }
            newTitle.length > 200 -> {
                if (call.isHtmxRequest()) {
                    call.respondText(
                        text = renderTemplate("tasks/partials/edit.peb", mapOf(
                            "task" to task,
                            "query" to query,
                            "error" to "Title is too long (max 200 characters)"
                        )),
                        contentType = ContentType.Text.Html,
                        status = HttpStatusCode.BadRequest
                    )
                } else {
                    call.respondRedirect("/tasks/$id/edit?error=too_long$queryString")
                }
                return@post
            }
        }

        // Update task
        val updatedTask = TaskRepository.update(task.copy(title = newTitle)) ?: run {
            call.respond(HttpStatusCode.NotFound)
            return@post
        }

        if (call.isHtmxRequest()) {
            // HTMX: Return view fragment + success status
            val viewHtml = renderTemplate("tasks/partials/view.peb", mapOf("task" to updatedTask))
            val statusHtml = """<div id="status" hx-swap-oob="true">
                Task "${updatedTask.title}" updated successfully.
            </div>"""
            call.respondText(
                text = viewHtml + statusHtml,
                contentType = ContentType.Text.Html
            )
        } else {
            // No-JS: PRG redirect with preserved state
            call.respondRedirect("/tasks$queryString", HttpStatusCode.SeeOther)
        }
    }

    /**
     * GET /tasks/{id}/view - Cancel edit (HTMX only)
     * Returns view fragment to revert from edit mode
     */
    get("/tasks/{id}/view") {
        val id = call.parameters["id"]?.toIntOrNull() ?: run {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        val task = TaskRepository.find(id) ?: run {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        val query = call.request.queryParameters["q"].orEmpty()

        if (call.isHtmxRequest()) {
            // HTMX: Return view fragment
            call.respondText(
                text = renderTemplate("tasks/partials/view.peb", mapOf(
                    "task" to task,
                    "query" to query
                )),
                contentType = ContentType.Text.Html
            )
        } else {
            // No-JS: Redirect to task list
            val encodedQuery = if (query.isNotBlank()) "?q=${query.encodeURLParameter()}" else ""
            call.respondRedirect("/tasks$encodedQuery")
        }
    }
}