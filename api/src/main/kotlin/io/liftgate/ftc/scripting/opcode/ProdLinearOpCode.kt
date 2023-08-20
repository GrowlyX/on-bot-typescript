package io.liftgate.ftc.scripting.opcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.ftc.scripting.plugins.createScriptService
import kotlinx.coroutines.runBlocking

/**
 * A production-ready script engine OpMode.
 *
 * @author GrowlyX
 * @since 8/20/2023
 */
abstract class ProdLinearOpCode : LinearOpMode()
{
    /**
     * Get the OpMode kts name.
     */
    abstract fun getScriptName(): String

    /**
     * Provide implicit local variables
     * or the script [getScriptName].
     */
    abstract fun environment(): List<Pair<String, Any>>

    private fun defaultEnvironmentalVariables() = listOf(
        "telemetry" to telemetry,
        "hardwareMap" to hardwareMap,
        "gamepad1" to gamepad1,
        "gamepad2" to gamepad2
    )

    override fun runOpMode()
    {
        val dbService = createScriptService()
        telemetry.addLine("Initialized the H2 script database")
        telemetry.update()

        val script = runBlocking {
            dbService.read(getScriptName())
        } ?: run {
            telemetry.addLine("[!] No script by name ${getScriptName()} exists in our local database!")
            telemetry.update()
            return
        }

        class ScriptRunException(
            throwable: Throwable
        ) : Exception(
            "Failed to run script (${throwable.message})",
            throwable
        )

        script.run(
            *defaultEnvironmentalVariables().toTypedArray(),
            *environment().toTypedArray()
        ) {
            throw ScriptRunException(it)
        }
    }
}
