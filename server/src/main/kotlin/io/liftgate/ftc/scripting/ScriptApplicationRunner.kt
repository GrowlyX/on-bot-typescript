package io.liftgate.ftc.scripting

import kotlin.concurrent.thread

/**
 * @author GrowlyX
 * @since 8/20/2023
 */
class ScriptApplicationRunner
{
    private var thread: Thread? = null

    @JvmOverloads
    fun startApplication(
        address: String = "0.0.0.0",
        port: Int = 6969
    )
    {
        if (thread != null)
        {
            throw IllegalStateException(
                "The script web application is already running!"
            )
        }

        this.thread = thread(
            name = "script-application-runner",
            isDaemon = true
        ) {
            main(address, port)
        }
    }

    fun destroy()
    {
        stopRequester?.invoke() ?: thread?.interrupt()
    }
}
