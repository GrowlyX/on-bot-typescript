package io.liftgate.ftc.scripting.logger

import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.concurrent.thread

/**
 * @author GrowlyX
 * @since 8/20/2023
 */
class PersistentTelemetryLog(
    private val telemetry: Telemetry
)
{
    private val logs = mutableListOf<String>()
    private var thread: Thread? = null

    fun clear() = synchronized(logs) { logs.clear() }

    init
    {
        thread = thread {
            while (true)
            {
                logs.forEach(telemetry::addLine)
                telemetry.update()

                Thread.sleep(100L)
            }
        }
    }

    fun log(message: String)
    {
        synchronized(logs) {
            logs += message
        }
    }

    fun destroy()
    {
        thread?.interrupt()
    }
}
