package io.liftgate.ftc.scripting.opmode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.ftc.scripting.TSScript
import io.liftgate.ftc.scripting.opmode.editor.scriptApp
import io.liftgate.ftc.scripting.plugins.createScriptService
import io.liftgate.ftc.scripting.plugins.scriptService
import io.liftgate.ftc.scripting.scripting.ScriptEngineService
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

/**
 * @author GrowlyX
 * @since 8/20/2023
 */
abstract class ProdLinearOpMode : LinearOpMode(), TSScript
{
    private fun defaultEnvironmentalVariables() = listOf(
        "telemetry" to telemetry,
        "hardwareMap" to hardwareMap,
        "gamepad1" to gamepad1,
        "gamepad2" to gamepad2,
        "isStopRequested" to isStopRequested,
        "opMode" to this
    )

    protected class Internal
    {
        var localRunnerThread: Thread? = null
        var joinLocalRunner = true
        var developmentMode = false
    }

    protected val internal = Internal()

    override fun runOpMode()
    {
        if (!internal.developmentMode && scriptApp.isRunning())
        {
            telemetry.addLine("You cannot have the script app running during a production OpMode!")
            return
        }

        val existing = scriptService != null
        val dbService = createScriptService(scriptsFile)
        telemetry.isAutoClear = false

        telemetry.addLine(
            "Initialized the script database${
                if (existing) " using existing resources from the script web editor" else ""
            }"
        )
        telemetry.update()

        scriptApp.moveTSAssets()

        telemetry.addLine("Initialized the TypeScript scripting engine in ${measureTimeMillis {
            ScriptEngineService.initializeEngine().join()
        }}ms")
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
