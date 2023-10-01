package io.liftgate.ftc.scripting

import com.google.gson.LongSerializationPolicy
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.liftgate.ftc.scripting.plugins.configureRouting
import io.liftgate.ftc.scripting.plugins.configureDatabases
import io.liftgate.ftc.scripting.scripting.Script

var stopRequester: (() -> Unit)? = null
var scriptUpdateHook: ((Script) -> Unit)? = null

// used for local testing purposes
fun main() = main("0.0.0.0", 6969)

fun main(address: String, port: Int)
{
    embeddedServer(Netty, port = port, host = address, module = Application::module)
        .start(wait = true)
}

fun Application.module()
{
    configureDatabases()
    configureRouting()

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            setLongSerializationPolicy(LongSerializationPolicy.STRING)
        }
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
