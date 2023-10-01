package io.liftgate.ftc.scripting.opmode

import org.firstinspires.ftc.robotcore.internal.system.AppUtil

/**
 * @author GrowlyX
 * @since 10/1/2023
 */
val scriptsFile: String
    get() = AppUtil.getInstance()
        .getSettingsFile("scripts.mv.db")
        .absolutePath
        .removeSuffix(".mv.db")
