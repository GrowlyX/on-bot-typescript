package io.liftgate.ftc.scripting.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.liftgate.ftc.scripting.development

fun Application.configureRouting()
{
    routing {
        configureSPA()
    }
}

private fun Routing.configureSPA()
{
    // if we are in development, we don't want to serve SPA
    // with SPA enabled, we can't see 404s from the API with ease
    // as it redirects to the SPA

    if (development)
    {
        println("Skipping SPA configuration as a development environment has been detected.")
        return
    }

    // configure KTor to serve React content
    singlePageApplication {
        react("static")
        useResources = true
    }
}
