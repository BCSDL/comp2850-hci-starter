package routes

import data.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.pebbletemplates.pebble.PebbleEngine
import java.io.StringWriter

/**
 * NOTE FOR NON-INTELLIJ IDEs (VSCode, Eclipse, etc.):
 * IntelliJ IDEA automatically adds imports as you type. If using a different IDE,
 * you may need to manually add imports. The commented imports below show what you'll need
 * for future weeks. Uncomment them as needed when following the lab instructions.
 *
 * When using IntelliJ: You can ignore the commented imports below - your IDE will handle them.
 */

// Week 7+ imports (inline edit, toggle completion):
// import model.Task               // When Task becomes separate model class
// import model.ValidationResult   // For validation errors
// import renderTemplate            // Extension function from Main.kt
// import isHtmxRequest             // Extension function from Main.kt

// Week 8+ imports (pagination, search, URL encoding):
// import io.ktor.http.encodeURLParameter  // For query parameter encoding
// import utils.Page                       // Pagination helper class

// Week 9+ imports (metrics logging, instrumentation):
// import utils.jsMode              // Detect JS mode (htmx/nojs)
// import utils.logValidationError  // Log validation failures
// import utils.timed               // Measure request timing

// Note: Solution repo uses storage.TaskStore instead of data.TaskRepository
// You may refactor to this in Week 10 for production readiness

/**
 * Week 6 Lab 1: Simple task routes with HTMX progressive enhancement.
 *
 * **Teaching approach**: Start simple, evolve incrementally
 * - Week 6: Basic CRUD with Int IDs
 * - Week 7: Add toggle, inline edit
 * - Week 8: Add pagination, search
 */

fun Route.taskRoutes() {
    val pebble =
        PebbleEngine
            .Builder()
            .loader(
                io.pebbletemplates.pebble.loader.ClasspathLoader().apply {
                    prefix = "templates/"
                },
            ).build()

    /**
     * Helper: Check if request is from HTMX
     */
    fun ApplicationCall.isHtmx(): Boolean = request.headers["HX-Request"]?.equals("true", ignoreCase = true) == true

    /**
     * GET /tasks - List all tasks
     * Returns full page (no HTMX differentiation in Week 6)
     */
    get("/tasks") {
        val errorParam = call.request.queryParameters["error"]
        val errorMessage = when (errorParam) {
            "blank" -> "Title is required. Please enter at least one character."
            else -> null
        }
        val model =
            mapOf(
                "title" to "Tasks",
                "tasks" to TaskRepository.all(),
                "errorMessage" to errorMessage,
                "editingId" to call.request.queryParameters["editingId"]?.toIntOrNull()
            )
        val template = pebble.getTemplate("tasks/index.peb")
        val writer = StringWriter()
        template.evaluate(writer, model)
        call.respondText(writer.toString(), ContentType.Text.Html)
    }

    /**
     * POST /tasks - Add new task
     * Dual-mode: HTMX fragment or PRG redirect
     */
    post("/tasks") {
        val title = call.receiveParameters()["title"].orEmpty().trim()

        if (title.isBlank()) {
            // Validation error handling
            if (call.isHtmx()) {
                val error = """<div id="status" hx-swap-oob="true" role="alert" aria-live="assertive">
                    Title is required. Please enter at least one character.
                </div>"""
                return@post call.respondText(error, ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                // No-JS: redirect back with error param
                call.respondRedirect("/tasks?error=blank")
                return@post
            }
        }

        val task = TaskRepository.add(title)

        if (call.isHtmx()) {
            // Return HTML fragment for new task (uses _item.peb partial)
            val itemTemplate = pebble.getTemplate("tasks/_item.peb")
            val itemWriter = StringWriter()
            itemTemplate.evaluate(itemWriter, mapOf("task" to task))
            
            val status = """<div id="status" hx-swap-oob="true">Task "${task.title}" added successfully.</div>"""
            return@post call.respondText(itemWriter.toString() + status, ContentType.Text.Html, HttpStatusCode.Created)
        }

        // No-JS: POST-Redirect-GET pattern (303 See Other)
        call.respondRedirect("/tasks")
    }

    /**
     * POST /tasks/{id}/delete - Delete task
     * Dual-mode: HTMX empty response or PRG redirect
     */
    post("/tasks/{id}/delete") {
        val id = call.parameters["id"]?.toIntOrNull()
        val removed = id?.let { TaskRepository.delete(it) } ?: false

        if (call.isHtmx()) {
            val message = if (removed) "Task deleted successfully." else "Could not delete task."
            val status = """<div id="status" hx-swap-oob="true">$message</div>"""
            // Return status to trigger OOB update, outerHTML swap removes the <li>
            return@post call.respondText(status, ContentType.Text.Html)
        }

        // No-JS: POST-Redirect-GET pattern (303 See Other)
        call.respondRedirect("/tasks")
    }

    /**
     * GET /tasks/{id}/edit - Show edit form (Week 7 required)
     * Dual-mode: HTMX fragment (swap in-place) or full page (no-JS)
     */
    get("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.NotFound)
        val task = TaskRepository.find(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        
        // Handle error param for no-JS validation failures
        val errorParam = call.request.queryParameters["error"]
        val errorMessage = when (errorParam) {
            "blank" -> "Title is required. Please enter at least one character."
            else -> null
        }

        if (call.isHtmx()) {
            // HTMX path: Return edit fragment (_edit.peb)
            val editTemplate = pebble.getTemplate("tasks/_edit.peb")
            val editWriter = StringWriter()
            editTemplate.evaluate(editWriter, mapOf("task" to task, "error" to errorMessage))
            call.respondText(editWriter.toString(), ContentType.Text.Html)
        } else {
            // No-JS path: Full page render with editingId
            val model = mapOf(
                "title" to "Edit Task",
                "tasks" to TaskRepository.all(),
                "editingId" to id,
                "errorMessage" to errorMessage,
                "task" to task
            )
            val template = pebble.getTemplate("tasks/index.peb")
            val writer = StringWriter()
            template.evaluate(writer, model)
            call.respondText(writer.toString(), ContentType.Text.Html)
        }
    }

    /**
     * POST /tasks/{id}/edit - Save edits (Week 7 required)
     * Dual-mode: HTMX fragment or PRG redirect
     */
    post("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@post call.respond(HttpStatusCode.NotFound)
        val task = TaskRepository.find(id) ?: return@post call.respond(HttpStatusCode.NotFound)
        val newTitle = call.receiveParameters()["title"].orEmpty().trim()

        // Validation: Reject blank title
        if (newTitle.isBlank()) {
            if (call.isHtmx()) {
                // HTMX path: Return edit fragment with error
                val editTemplate = pebble.getTemplate("tasks/_edit.peb")
                val editWriter = StringWriter()
                editTemplate.evaluate(editWriter, mapOf(
                    "task" to task,
                    "error" to "Title is required. Please enter at least one character."
                ))
                return@post call.respondText(editWriter.toString(), ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                // No-JS path: Redirect back to edit page with error
                call.respondRedirect("/tasks/$id/edit?error=blank")
                return@post
            }
        }

        // Update task (uses TaskRepository.update() method)
        val updatedTask = TaskRepository.update(task.copy(title = newTitle)) ?: return@post call.respond(HttpStatusCode.NotFound)

        if (call.isHtmx()) {
            // HTMX path: Return view fragment + OOB status
            val viewTemplate = pebble.getTemplate("tasks/_item.peb")
            val viewWriter = StringWriter()
            viewTemplate.evaluate(viewWriter, mapOf("task" to updatedTask))
            
            val status = """<div id="status" hx-swap-oob="true">Task "${updatedTask.title}" updated successfully.</div>"""
            return@post call.respondText(viewWriter.toString() + status, ContentType.Text.Html)
        }

        // No-JS path: PRG redirect to task list
        call.respondRedirect("/tasks")
    }

    /**
     * GET /tasks/{id}/view - Cancel edit (Week 7 required, HTMX only)
     * Returns view fragment to revert edit mode
     */
    get("/tasks/{id}/view") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.NotFound)
        val task = TaskRepository.find(id) ?: return@get call.respond(HttpStatusCode.NotFound)

        // Only handle HTMX requests (no-JS cancel uses /tasks redirect)
        if (call.isHtmx()) {
            val viewTemplate = pebble.getTemplate("tasks/_item.peb")
            val viewWriter = StringWriter()
            viewTemplate.evaluate(viewWriter, mapOf("task" to task))
            call.respondText(viewWriter.toString(), ContentType.Text.Html)
        } else {
            call.respondRedirect("/tasks")
        }
    }
}