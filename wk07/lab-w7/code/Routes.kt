// Routes.kt (Week 7 Complete) — Inline Edit with Dual-Path Support
package comp2850

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.pebbletemplates.pebble.PebbleEngine
import io.pebbletemplates.pebble.loader.ClasspathLoader
import java.io.StringWriter

data class Task(val id: Int, var title: String)  // Updated to var for mutation

// HTMX detection helper
fun ApplicationCall.isHtmx() = request.headers["HX-Request"]?.equals("true", ignoreCase = true) == true

// Pebble engine configuration (matches Week 7 template structure)
private val pebble = PebbleEngine.Builder()
    .loader(ClasspathLoader().apply { prefix = "templates/" })
    .build()

// Template rendering helper
private fun renderTemplate(templatePath: String, model: Map<String, Any?>): String {
    val template = pebble.getTemplate(templatePath)
    val writer = StringWriter()
    template.evaluate(writer, model)
    return writer.toString()
}

fun Route.inlineEditRoutes(repo: TaskRepo) {
    // GET /tasks/{id}/edit - Dual-path edit form
    get("/tasks/{id}/edit") {
        // Validate task ID
        val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.NotFound)
        val task = repo.getOrNull(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        
        // Handle error parameter (for no-JS validation feedback)
        val errorParam = call.request.queryParameters["error"]
        val errorMessage = when (errorParam) {
            "blank" -> "Title is required. Please enter at least one character."
            else -> null
        }

        if (call.isHtmx()) {
            // HTMX path: return edit fragment
            val html = renderTemplate(
                "tasks/partials/edit.peb",  // Week 7 partial template
                mapOf("task" to task, "error" to errorMessage)
            )
            call.respondText(html, ContentType.Text.Html)
        } else {
            // No-JS path: return full page with editing context
            val model = mapOf(
                "title" to "Tasks",
                "tasks" to repo.all(),
                "editingId" to id,
                "errorMessage" to errorMessage
            )
            val html = renderTemplate("tasks/index.peb", model)
            call.respondText(html, ContentType.Text.Html)
        }
    }

    // POST /tasks/{id}/edit - Handle edit submission
    post("/tasks/{id}/edit") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@post call.respond(HttpStatusCode.NotFound)
        val task = repo.getOrNull(id) ?: return@post call.respond(HttpStatusCode.NotFound)
        val newTitle = call.receiveParameters()["title"].orEmpty().trim()

        // Validation
        if (newTitle.isBlank()) {
            if (call.isHtmx()) {
                // HTMX path: return edit fragment with error
                val html = renderTemplate(
                    "tasks/partials/edit.peb",
                    mapOf(
                        "task" to task,
                        "error" to "Title is required. Please enter at least one character."
                    )
                )
                call.respondText(html, ContentType.Text.Html, HttpStatusCode.BadRequest)
            } else {
                // No-JS path: redirect with error flag (PRG pattern)
                call.respondRedirect("/tasks/$id/edit?error=blank")
            }
            return@post
        }

        // Update task in repository
        val updatedTask = repo.update(id, newTitle)

        if (call.isHtmx()) {
            // HTMX path: return view fragment + out-of-band status
            val viewHtml = renderTemplate(
                "tasks/partials/view.peb",  // Week 7 partial template
                mapOf("task" to updatedTask)
            )
            val status = """<div id="status" hx-swap-oob="true" role="status">
                Task "${updatedTask.title}" updated successfully.
            </div>"""
            call.respondText(viewHtml + status, ContentType.Text.Html)
        } else {
            // No-JS path: PRG redirect to task list
            call.respondRedirect("/tasks")
        }
    }

    // GET /tasks/{id}/view - Cancel edit (HTMX-only)
    get("/tasks/{id}/view") {
        val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.NotFound)
        val task = repo.getOrNull(id) ?: return@get call.respond(HttpStatusCode.NotFound)

        // Only handle HTMX requests (no-JS uses regular links)
        val html = renderTemplate(
            "tasks/partials/view.peb",
            mapOf("task" to task)
        )
        call.respondText(html, ContentType.Text.Html)
    }
}

// Enhanced TaskRepo with null safety and full list access (matches Week 7 requirements)
interface TaskRepo {
    fun getOrNull(id: Int): Task?  // Safer alternative to get()
    fun all(): List<Task>          // For full-page rendering in no-JS path
    fun update(id: Int, title: String): Task  // Updates and returns updated task
}