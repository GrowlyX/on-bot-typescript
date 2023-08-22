package io.liftgate.ftc.scripting

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.liftgate.ftc.scripting.plugins.configureRouting
import io.liftgate.ftc.scripting.plugins.configureDatabases
import io.liftgate.ftc.scripting.scripting.Script

val scriptApp by lazy(::ScriptApplicationRunner)

var stopRequester: (() -> Unit)? = null
var scriptUpdateHook: ((Script) -> Unit)? = null
var scriptAppStartHook: (() -> Unit)? = null

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

    if (environment is ApplicationEngineEnvironment)
    {
        stopRequester = {
            (environment as ApplicationEngineEnvironment).stop()
        }
    }

    scriptAppStartHook?.invoke()
}

val development = System
    .getProperty("io.ktor.development")
    ?.toBoolean() ?: false
