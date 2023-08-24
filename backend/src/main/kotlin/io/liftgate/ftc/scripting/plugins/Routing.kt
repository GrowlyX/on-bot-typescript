package io.liftgate.ftc.scripting.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.liftgate.ftc.scripting.development
import io.liftgate.ftc.scripting.scriptUpdateHook
import io.liftgate.ftc.scripting.scripting.ScriptEngineService

fun Application.configureRouting()
{
    routing {
        configureSPA()

        get("/api/status") {
            call.respond(mapOf(
                "hotReloadEnabled" to (scriptUpdateHook != null),
                "scriptEngineBooted" to (ScriptEngineService.ktsEngine != null)
            ))
        }
    }
}

private fun Routing.configureSPA()
{
    // if we are in development, we don't want to serve SPA
    // with SPA enabled, we can't see 404s from the API with ease
    // as it redirects to the SPA
    if (development)
    {
        application.log.info("Skipping SPA configuration as a development environment has been detected.")
        return
    }

    // configure KTor to serve Svelte content
    singlePageApplication {
        useResources = true
        filesPath = "static"
        defaultPage = "index.html"
    }
}
