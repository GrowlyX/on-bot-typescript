package io.liftgate.ftc.scripting.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.liftgate.ftc.scripting.development
import io.liftgate.ftc.scripting.scripting.Script
import io.liftgate.ftc.scripting.scripting.ScriptService
import org.jetbrains.exposed.sql.Database

var scriptService: ScriptService? = null

fun createScriptService(location: String = "./scripts"): ScriptService
{
    if (scriptService != null)
    {
        return scriptService!!
    }

    // a default h2 database
    val url = "jdbc:h2:file:$location"
    val devUrl = "jdbc:h2:mem:myDb"

    val database = Database.connect(
        url = "${
            if (development) devUrl else url
        };DB_CLOSE_DELAY=-1",
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
            data class CreateScript(val fileName: String)

            val scriptCreation = call.receive<CreateScript>()

            if (scriptService.read(scriptCreation.fileName) != null)
            {
                call.respond(
                    mapOf("error" to "Script by file name already exists.")
                )
                return@post
            }

            if (!scriptCreation.fileName.endsWith(".ts"))
            {
                call.respond(
                    mapOf("error" to "Script name must end with the .ts extension!")
                )
                return@post
            }

            if (scriptCreation.fileName == ".ts")
            {
                call.respond(
                    mapOf("error" to "Script name cannot be .ts!")
                )
                return@post
            }

            val script = Script(
                fileName = scriptCreation.fileName,
                """
                    // This is a new script named: ${scriptCreation.fileName}
                    // Write your OpMode code below!
                """.trimIndent()
            )

            val id = runCatching { scriptService.create(script) }
                .getOrElse {
                    call.respond(
                        mapOf("error" to "Failed to create script: ${it.message}")
                    )
                    return@post
                }

            data class ScriptCreated(
                val id: Int,
                val creationDate: Long
            )

            call.respond(
                HttpStatusCode.Created,
                ScriptCreated(id.value, script.lastEdited)
            )
        }

        get("/api/scripts/find/{id}") {
            val id = call.parameters["id"]?.toInt()
                ?: return@get call.respond(
                    mapOf("error" to "Script id parameter is not an integer")
                )

            val script = scriptService.read(id)
                ?: return@get call.respond(
                    mapOf("error" to "Script $id does not exist in the database")
                )

            call.respond(script)
        }

        post("/api/scripts/update-content") {
            data class ScriptContent(val fileName: String, val fileContent: String)

            val scriptContent = call.receive<ScriptContent>()
            val script = scriptService.read(scriptContent.fileName)
                ?: return@post call.respond(
                    mapOf("error" to "Script ${scriptContent.fileName} does not exist in the database")
                )

            script.fileContent = scriptContent.fileContent
            script.lastEdited = System.currentTimeMillis()

            scriptService.update(script)
            call.respond(mapOf(
                "lastEdited" to script.lastEdited
            ))
        }

        delete("/api/scripts/delete/{id}") {
            val id = call.parameters["id"]?.toInt()
                ?: return@delete call.respond(
                    mapOf("error" to "Script id parameter is not an integer")
                )

            scriptService.delete(id)
            call.respond(HttpStatusCode.OK, mapOf(
                "lastEdited" to ""
            ))
        }

        data class ScriptReference(val name: String)

        post("/api/scripts/find-name/") {
            val ref = call.receive<ScriptReference>()

            val script = scriptService.read(ref.name)
                ?: return@post call.respond(
                    mapOf("error" to "Script ${ref.name} does not exist in the database")
                )

            call.respond(script)
        }

        post("/api/scripts/delete-name/") {
            val ref = call.receive<ScriptReference>()

            val script = scriptService.read(ref.name)
                ?: return@post call.respond(
                    mapOf("error" to "Script ${ref.name} does not exist in the database")
                )

            scriptService.delete(script.fileName)

            call.respond(mapOf(
                "lastEdited" to ""
            ))
        }
    }
}
