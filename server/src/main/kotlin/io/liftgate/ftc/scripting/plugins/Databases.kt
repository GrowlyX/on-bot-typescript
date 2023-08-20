package io.liftgate.ftc.scripting.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.liftgate.ftc.scripting.scripting.Script
import io.liftgate.ftc.scripting.scripting.ScriptService
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime

var scriptService: ScriptService? = null

fun createScriptService(): ScriptService
{
    if (scriptService != null)
    {
        return scriptService!!
    }

    // a default h2 database
    val url = "jdbc:h2:file:./scripts"
    val database = Database.connect(
        url = "$url;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )
    return ScriptService(database)
        .apply {
            scriptService = this
        }
}

fun Application.configureDatabases()
{
    val scriptService = createScriptService()

    routing {
        get("/api/scripts/list") {
            call.respond(
                scriptService.readAll()
            )
        }

        post("/api/scripts/create") {
            data class CreateScript(
                val fileName: String
            )

            val user = call.receive<CreateScript>()

            if (scriptService.read(user.fileName) != null)
            {
                throw IllegalArgumentException(
                    "Script by file name already exists."
                )
            }

            val id = scriptService.create(
                Script(
                    fileName = user.fileName,
                    "// Write your code here!",
                    LocalDateTime.now()
                )
            )

            call.respond(HttpStatusCode.Created, id)
        }

        get("/api/scripts/{id}") {
            val id = call.parameters["id"]?.toInt()
                ?: throw IllegalArgumentException("Invalid ID")

            val script = scriptService.read(id)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound, "Script $id not found"
                )

            call.respond(HttpStatusCode.OK, script)
        }

        put("/api/scripts/{id}") {
            val id = call.parameters["id"]?.toInt()
                ?: throw IllegalArgumentException("Invalid ID")

            val script = call.receive<Script>()
            script.lastEdited = LocalDateTime.now()

            scriptService.update(id, script)
            call.respond(HttpStatusCode.OK)
        }

        delete("/api/scripts/{id}") {
            val id = call.parameters["id"]?.toInt()
                ?: throw IllegalArgumentException("Invalid ID")

            scriptService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
