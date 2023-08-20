package io.liftgate.ftc.scripting.opcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import io.liftgate.ftc.scripting.plugins.createScriptService
import io.liftgate.ftc.scripting.plugins.scriptService
import kotlinx.coroutines.runBlocking

/**
 * A production-ready script engine OpMode.
 *
 * @author GrowlyX
 * @since 8/20/2023
 */
abstract class ProdLinearOpMode : LinearOpMode()
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

    open fun packageImports() = emptyList<String>()

    private fun defaultPackageImports() = listOf(
        "com.qualcomm.robotcore",
    )

    private fun defaultEnvironmentalVariables() = listOf(
        "telemetry" to telemetry,
        "hardwareMap" to hardwareMap,
        "gamepad1" to gamepad1,
        "gamepad2" to gamepad2
    )

    override fun runOpMode()
    {
        val existing = scriptService != null
        val dbService = createScriptService()
        telemetry.addLine("Initialized the H2 script database${
            if (existing) " using existing resources from the script web editor" else ""
        }")
        telemetry.update()

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

        script.run(
            listOf(
                *defaultPackageImports().toTypedArray(),
                *packageImports().toTypedArray()
            ),
            *defaultEnvironmentalVariables().toTypedArray(),
            *environment().toTypedArray(),
            failure = {
                throw ScriptRunException(it)
            }
        )
    }
}
