package io.liftgate.ftc.scripting.opmode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.util.Range
import io.liftgate.ftc.scripting.KotlinScript
import io.liftgate.ftc.scripting.logger.PersistentTelemetryLog
import io.liftgate.ftc.scripting.plugins.createScriptService
import io.liftgate.ftc.scripting.plugins.scriptService
import io.liftgate.ftc.scripting.scripting.ScriptEngineService
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * A production-ready script engine OpMode.
 *
 * @author GrowlyX
 * @since 8/20/2023
 */
abstract class ProdLinearOpMode : LinearOpMode(), KotlinScript
{
    private val logger by lazy {
        PersistentTelemetryLog(telemetry)
    }

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

    open fun packageImports() = emptyList<String>()

    private fun defaultPackageImports() = listOf(
        "com.qualcomm.robotcore",
        "org.firstinspires.ftc.robotcore"
    )

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
        val dbService = createScriptService()
        logger.log(
            "Initialized the H2 script database${
                if (existing) " using existing resources from the script web editor" else ""
            }"
        )

        val script = runBlocking {
            dbService.read(getScriptName())
        } ?: run {
            logger.destroy()
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
            script.run(
                listOf(
                    *defaultPackageImports().toTypedArray(),
                    *packageImports().toTypedArray()
                ),
                *defaultEnvironmentalVariables().toTypedArray(),
                *impliedVariables
                    .map { it.name to it.instance }
                    .toTypedArray(),
                failure = {
                    if (internal.joinLocalRunner)
                    {
                        throw ScriptRunException(it)
                    }

                    logger.log("Exception occurred!")
                    logger.log(it.stackTraceToString())
                }
            )

            internal.localRunnerThread = null
        }

        if (internal.joinLocalRunner)
        {
            runCatching {
                internal.localRunnerThread!!.join()
            }.onFailure {
                logger.destroy()
                // TODO: better exception reporting
                telemetry.addLine("Exception: ${it.message}")
                telemetry.addLine(it.stackTraceToString())
                telemetry.update()
            }.onSuccess {
                logger.destroy()
            }
        }
    }
}
