package io.liftgate.ftc.scripting.opcode

import io.liftgate.ftc.scripting.ScriptApplicationRunner

/**
 * @author GrowlyX
 * @since 8/20/2023
 */
abstract class ProdWithEditorEnabledLinearOpMode : ProdLinearOpMode()
{
    private val scriptApp by lazy(::ScriptApplicationRunner)
    override fun runOpMode()
    {
        scriptApp.startApplication()
        super.runOpMode()

        while (!isStopRequested)
        {
            // sleep while the opmode is running
            // this is only really effective on autonomous opmodes,
            Thread.sleep(100L)
        }

        scriptApp.destroy()
    }
}
