package io.liftgate.ftc.scripting.opmode

import io.liftgate.ftc.scripting.opmode.editor.scriptApp
import io.liftgate.ftc.scripting.scriptUpdateHook

/**
 * @author GrowlyX
 * @since 8/20/2023
 */
abstract class DevLinearOpMode : ProdLinearOpMode()
{
    override fun runOpMode()
    {
        internal.developmentMode = true

        if (!scriptApp.isRunning())
        {
            scriptApp.startApplication()
        }

        scriptUpdateHook = context@{
            if (it.fileName != getScriptName())
            {
                return@context
            }

            telemetry.clearAll()
            // used for hot reloading of scripts
            telemetry.addLine(
                "Received update for script ${it.fileName} (last edited ${it.lastEdited})"
            )

            internal.localRunnerThread?.interrupt()
            internal.localRunnerThread = null

            telemetry.addLine("Stopped current thread")
            super.runOpMode()

            telemetry.addLine("Restarted OpMode in the background")
            telemetry.addLine("-- Completed update process --")
        }

        // don't join the OpMode runner, it'll run in the background
        internal.joinLocalRunner = false
        super.runOpMode()

        while (!isStopRequested)
        {
            // continue to sleep until a stop request while the
            // OpMode is running in the background
            Thread.sleep(100L)
        }

        // stop receiving updates for this OpMode's lifecycle
        scriptUpdateHook = null
        internal.localRunnerThread?.interrupt()
    }
}
