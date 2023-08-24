package io.liftgate.ftc.scripting.test

import org.jetbrains.kotlin.utils.addToStdlib.measureTimeMillisWithResult
import org.junit.Test
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder

/**
 * @author GrowlyX
 * @since 8/21/2023
 */
class Jsr223Test
{
    @Test
    fun test()
    {
        println(measureTimeMillisWithResult {
            Reflections(
                ConfigurationBuilder()
                    .forPackage(
                        "org.jetbrains.exposed"
                    )
                    .addScanners(
                        Scanners.SubTypes
                    )
            ).getAll(
                Scanners.SubTypes
            )
        }.second)
    }
}
