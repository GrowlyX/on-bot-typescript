package io.liftgate.ftc.scripting

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.liftgate.ftc.scripting.plugins.configureRouting
import io.liftgate.ftc.scripting.plugins.configureDatabases
import io.liftgate.ftc.scripting.plugins.configureSerialization

fun main()
{
    embeddedServer(Netty, port = 6969, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module()
{
    configureSerialization()
    configureDatabases()
    configureRouting()
}

val development = System
    .getProperty("io.ktor.development")
    ?.toBoolean() ?: false
