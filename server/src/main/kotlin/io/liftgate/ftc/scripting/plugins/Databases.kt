package io.liftgate.ftc.scripting.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.liftgate.ftc.scripting.scripting.Script
import io.liftgate.ftc.scripting.scripting.ScriptService
import org.jetbrains.exposed.sql.*

fun Application.configureDatabases()
{
    // a default h2 database
    val url = "jdbc:h2:file:./scripts"
    val database = Database.connect(
        url = "$url;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )
    val scriptService = ScriptService(database)
    routing {
        post("/scripts/create") {
            val user = call.receive<Script>()
            val id = scriptService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }
        get("/scripts/get/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = scriptService.read(id)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound, "Script $id not found"
                )

            call.respond(HttpStatusCode.OK, user)
        }
        put("/scripts/update/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<Script>()
            scriptService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }
        delete("/scripts/delete/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            scriptService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
