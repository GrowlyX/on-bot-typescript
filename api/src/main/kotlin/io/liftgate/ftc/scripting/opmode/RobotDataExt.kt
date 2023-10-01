package io.liftgate.ftc.scripting.opmode

import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import java.io.File

/**
 * @author GrowlyX
 * @since 10/1/2023
 */
val scriptsFile: File
    get() = AppUtil.getInstance()
        .getSettingsFile("scripts.json")
