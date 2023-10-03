package io.liftgate.ftc.scripting

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.liftgate.ftc.scripting.plugins.configureRouting
import io.liftgate.ftc.scripting.plugins.configureDatabases
import io.liftgate.ftc.scripting.scripting.Script
import kotlinx.serialization.json.Json

var stopRequester: (() -> Unit)? = null
var scriptUpdateHook: ((Script) -> Unit)? = null

// used for local testing purposes
fun main() = main("0.0.0.0", 6969)

fun main(address: String, port: Int)
{
    embeddedServer(CIO, port = port, host = address, module = Application::module)
        .start(wait = true)
}

val json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

fun Application.module()
{
    configureDatabases()
    configureRouting()

    install(ContentNegotiation) {
        json(json)
    }

    if (environment is ApplicationEngineEnvironment)
    {
        stopRequester = {
            (environment as ApplicationEngineEnvironment).stop()
        }
    }
}

val development = System
    .getProperty("io.ktor.development")
    ?.toBoolean() ?: false
