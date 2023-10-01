package io.liftgate.ftc.scripting

import kotlin.concurrent.thread

/**
 * @author GrowlyX
 * @since 8/20/2023
 */
open class ScriptApplicationRunner
{
    protected var thread: Thread? = null

    fun isRunning() = thread != null && thread!!.isAlive

    @JvmOverloads
    open fun startApplication(
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

        Runtime.getRuntime()
            .addShutdownHook(object : Thread()
            {
                override fun run()
                {
                    destroy()
                }
            })
    }

    fun destroy()
    {
        stopRequester?.invoke() ?: thread?.interrupt()
            ?.apply {
                thread = null
            }
    }
}
