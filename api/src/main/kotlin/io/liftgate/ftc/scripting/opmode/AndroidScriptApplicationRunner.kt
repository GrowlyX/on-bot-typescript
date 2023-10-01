package io.liftgate.ftc.scripting.opmode

import io.liftgate.ftc.scripting.AndroidAssetsReflections
import io.liftgate.ftc.scripting.ScriptApplicationRunner
import io.liftgate.ftc.scripting.plugins.createScriptService
import io.liftgate.ftc.scripting.plugins.resourcesPath
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import java.io.File
import java.io.FileOutputStream

/**
 * @author GrowlyX
 * @since 10/1/2023
 */
class AndroidScriptApplicationRunner : ScriptApplicationRunner()
{
    override fun startApplication(address: String, port: Int)
    {
        if (thread != null)
        {
            throw IllegalStateException(
                "The script web application is already running!"
            )
        }

        val assets = File(
            AppUtil.ROBOT_DATA_DIR,
            "obtstatic"
        )

        if (assets.exists())
        {
            assets.deleteRecursively()
        }

        assets.mkdirs()
        copyAssetsToDataDir(
            "obtstatic",
            assets.absolutePath.removeSuffix("/")
        )

        resourcesPath = assets.absolutePath

        createScriptService(scriptsFile)
        super.startApplication(address, port)
    }

    private fun copyAssetsToDataDir(assetsPath: String, destinationPath: String)
    {
        val assetsList = AndroidAssetsReflections.list(assetsPath)

        for (asset in assetsList)
        {
            val sourcePath = "$assetsPath/$asset"
            val destinationFilePath = "$destinationPath/$asset"

            if (AndroidAssetsReflections.list(sourcePath).isEmpty())
            {
                val inputStream = AndroidAssetsReflections.open(sourcePath)
                val outputStream = FileOutputStream(destinationFilePath)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input!!.copyTo(output)
                    }
                }
            } else
            {
                val destinationDir = File(destinationFilePath)
                if (!destinationDir.exists() && !destinationDir.mkdirs())
                {
                    continue
                }

                copyAssetsToDataDir(sourcePath, destinationFilePath)
            }
        }
    }
}
