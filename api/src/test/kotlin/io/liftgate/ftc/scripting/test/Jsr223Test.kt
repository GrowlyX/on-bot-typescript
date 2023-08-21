package io.liftgate.ftc.scripting.test

import javax.script.ScriptEngineManager
import kotlin.system.measureTimeMillis

/**
 * @author GrowlyX
 * @since 8/21/2023
 */
fun main()
{
    val engine = ScriptEngineManager()
        .getEngineByExtension("kts")

    val script = """
        println(listOf("hi")) 
    """.trimIndent()

    println(measureTimeMillis {
        engine.eval(script)
    })

    println(measureTimeMillis {
        engine.eval(script)
    })
}
