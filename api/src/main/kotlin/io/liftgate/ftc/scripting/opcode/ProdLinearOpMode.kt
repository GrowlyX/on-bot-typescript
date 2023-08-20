package io.liftgate.ftc.scripting.opcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.ftc.scripting.plugins.createScriptService
import io.liftgate.ftc.scripting.plugins.scriptService
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

/**
 * A production-ready script engine OpMode.
 *
 * @author GrowlyX
 * @since 8/20/2023
 */
abstract class ProdLinearOpMode : LinearOpMode()
{
    private val logger by lazy {
        TelemetryPersistentLogger(telemetry)
    }

    /**
     * Get the OpMode kts name.
     */
    abstract fun getScriptName(): String

    /**
     * Provide implicit local variables
     * or the script [getScriptName].
     */
    abstract fun environment(): List<Pair<String, Any>>

    open fun packageImports() = emptyList<String>()

    private fun defaultPackageImports() = listOf(
        "com.qualcomm.robotcore",
        "org.firstinspires.ftc.robotcore"
    )

    private fun defaultEnvironmentalVariables() = listOf(
        "telemetry" to telemetry,
        "hardwareMap" to hardwareMap,
        "gamepad1" to gamepad1,
        "gamepad2" to gamepad2
    )

    protected class Internal
    {
        var localRunnerThread: Thread? = null
        var joinLocalRunner: Boolean = true
    }

    protected val internal = Internal()

    override fun runOpMode()
    {
        val existing = scriptService != null
        val dbService = createScriptService()
        logger.log("Initialized the H2 script database${
            if (existing) " using existing resources from the script web editor" else ""
        }")

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

        internal.localRunnerThread = thread {
            script.run(
                listOf(
                    *defaultPackageImports().toTypedArray(),
                    *packageImports().toTypedArray()
                ),
                *defaultEnvironmentalVariables().toTypedArray(),
                *environment().toTypedArray(),
                failure = {
                    if (internal.joinLocalRunner)
                    {
                        throw ScriptRunException(it)
                    }

                    // TODO: what do we do now?
                    logger.log("Exception occurred!")
                    logger.log(it.stackTraceToString())
                },
                debug = logger::log
            )
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
