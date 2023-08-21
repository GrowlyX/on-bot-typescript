package io.liftgate.ftc.scripting.opmode

import io.liftgate.ftc.scripting.logger.PersistentTelemetryLog
import io.liftgate.ftc.scripting.scriptApp
import io.liftgate.ftc.scripting.scriptUpdateHook

/**
 * @author GrowlyX
 * @since 8/20/2023
 */
abstract class ProdWithStartEditorLinearOpMode : ProdLinearOpMode()
{
    private val logger by lazy {
        PersistentTelemetryLog(telemetry)
    }

    override fun runOpMode()
    {
        // TODO: Since the robot will stay active, we could probably do an initial configuration of
        //  application and keep it running while the control hub app is running. Needs testing though.
        //  (the behavior of the code below is what is described above)
        if (!scriptApp.isRunning())
        {
            scriptApp.startApplication()
        }

        scriptUpdateHook = context@{
            if (it.fileName != getScriptName())
            {
                return@context
            }

            // used for hot reloading of scripts
            logger.log(
                "Received update for script ${it.fileName} (last edited ${it.lastEdited})"
            )

            internal.localRunnerThread?.interrupt()
            internal.localRunnerThread = null

            logger.log("Stopped current thread")
            super.runOpMode()

            logger.log("Restarted OpMode in the background")
            logger.log("-- Completed update process --")
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
