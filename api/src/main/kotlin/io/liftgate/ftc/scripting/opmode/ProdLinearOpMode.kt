package io.liftgate.ftc.scripting.opmode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.ftc.scripting.TSScript
import io.liftgate.ftc.scripting.plugins.createScriptService
import io.liftgate.ftc.scripting.plugins.scriptService
import io.liftgate.ftc.scripting.scripting.ScriptEngineService
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

/**
 * @author GrowlyX
 * @since 8/20/2023
 */
abstract class ProdLinearOpMode : LinearOpMode(), TSScript
{
    init
    {
        /**
         * Initializes the ScriptEngine which may take up to 10 seconds to properly
         * boot. This is done in the background, so there won't be any lag with the robot itself.
         *
         * ScriptEngineService is properly synchronized, so we won't get any issues with sub-implementations
         * of ProdLinearOpMode re-initializing the engine.
         */
        ScriptEngineService.initializeEngine()
    }

    private fun defaultEnvironmentalVariables() = listOf(
        "telemetry" to telemetry,
        "hardwareMap" to hardwareMap,
        "gamepad1" to gamepad1,
        "gamepad2" to gamepad2,
        "isStopRequested" to isStopRequested
    )

    protected class Internal
    {
        var localRunnerThread: Thread? = null
        var joinLocalRunner = true
    }

    protected val internal = Internal()

    override fun runOpMode()
    {
        val existing = scriptService != null
        val dbService = createScriptService(scriptsFile)
        telemetry.isAutoClear = false

        telemetry.addLine(
            "Initialized the H2 script database${
                if (existing) " using existing resources from the script web editor" else ""
            }"
        )
        telemetry.update()

        check(getScriptName() != "Shared.ts") {
            "Shared script cannot be executed by an OpMode"
        }

        val script = runBlocking {
            dbService.read(getScriptName())
        } ?: run {
            telemetry.addLine("No script by name ${getScriptName()} exists in our local database. Stopping!")
            telemetry.update()
            return
        }

        class ScriptRunException(
            throwable: Throwable
        ) : Exception(
            "Failed to run script (${throwable.message})",
            throwable
        )

        waitForStart()

        internal.localRunnerThread = thread {
            runBlocking {
                script.run(
                    *defaultEnvironmentalVariables().toTypedArray(),
                    *impliedVariables
                        .map { it.name to it.instance }
                        .toTypedArray(),
                    failure = {
                        if (internal.joinLocalRunner)
                        {
                            throw ScriptRunException(it)
                        }

                        telemetry.addLine("Exception occurred!")
                        telemetry.addLine(it.stackTraceToString())
                    }
                )
            }

            internal.localRunnerThread = null
        }

        if (internal.joinLocalRunner)
        {
            runCatching {
                internal.localRunnerThread!!.join()
            }.onFailure {
                // TODO: better exception reporting
                telemetry.addLine("Exception: ${it.message}")
                telemetry.addLine(it.stackTraceToString())
                telemetry.update()
            }
        }
    }
}
