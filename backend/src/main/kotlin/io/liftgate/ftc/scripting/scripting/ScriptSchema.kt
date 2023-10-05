package io.liftgate.ftc.scripting.scripting

import com.xafero.ts4j.TypeScriptEngineFactory
import io.liftgate.ftc.scripting.json
import io.liftgate.ftc.scripting.plugins.scriptService
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.util.concurrent.CompletableFuture
import javax.script.Bindings
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

object ScriptEngineService
{
    var lock = Any()
    var tsEngine: ScriptEngine? = null

    fun initializeEngine(): CompletableFuture<Void>
    {
        return synchronized(lock) {
            if (tsEngine != null)
            {
                return@synchronized CompletableFuture
                    .completedFuture(null)
            }

            CompletableFuture
                .runAsync {
                    /*val nashorn = NashornScriptEngineFactory().scriptEngine
                    val engine = TypeScriptEngine(
                        TypeScriptEngineFactory(),
                        nashorn,
                        TypeScriptCompiler.create<TypeScriptEngine>(nashorn)
                    )*/

                    val engine = ScriptEngineManager()
                        .apply {
                            registerEngineExtension("ts", TypeScriptEngineFactory())
                        }
                        .getEngineByExtension("ts")

                    println("Created engine ${engine.factory.engineName}.")

                    // Seems to take around 5 seconds for initial start?
                    engine.eval("print(\"test\")")
                    tsEngine = engine
                }
                .exceptionally {
                    it.printStackTrace()
                    return@exceptionally null
                }
        }
    }

    inline fun useEngine(
        block: (ScriptEngine, Bindings) -> Unit
    )
    {
        synchronized(lock) {
            checkNotNull(tsEngine) {
                "TS scripting engine has not yet been configured!"
            }

            val bindings = tsEngine!!
                .createBindings()

            tsEngine!!.setBindings(
                bindings,
                ScriptContext.ENGINE_SCOPE
            )
            block(tsEngine!!, bindings)
        }
    }
}

@Serializable
data class Script(
    val fileName: String,
    var fileContent: String,
    var lastEdited: Long = System.currentTimeMillis()
)
{
    inline fun run(
        vararg context: Pair<String, Any>,
        failure: (Throwable) -> Unit
    )
    {
        ScriptEngineService.useEngine { engine, bindings ->
            var script = ""
            bindings.putAll(context)

            // Apply shared script onto this script instance
            runBlocking {
                scriptService
                    ?.read("Shared.ts")
                    ?.apply {
                        script += fileContent
                    }
            }

            script += fileContent

            runCatching { engine.eval(script) }
                .onFailure {
                    failure(it)
                }
        }
    }
}

@Serializable
data class ScriptsContainer(
    val scripts: MutableList<Script> = mutableListOf()
)

@OptIn(ExperimentalSerializationApi::class)
class ScriptService(private val store: File)
{
    private val model = ScriptsContainer()

    init
    {
        if (!store.exists())
        {
            store.createNewFile()
            store.writeText(
                json.encodeToString(model)
            )
        }

        val container = json
            .decodeFromStream<ScriptsContainer>(
                store.inputStream()
            )
        model.scripts.addAll(container.scripts)
    }

    private fun save()
    {
        store.writeText(
            json.encodeToString(model)
        )
    }

    private fun <T> synchronizedModel(block: ScriptsContainer.() -> T) =
        synchronized(model) {
            block(model)
        }

    private fun <T> synchronizedModelSaving(block: ScriptsContainer.() -> T) =
        synchronized(model) {
            block(model)
            save()
        }

    fun create(script: Script) = synchronizedModelSaving {
        scripts.add(script)
    }

    fun readAll() = synchronizedModel {
        scripts.toList()
    }

    fun read(name: String) = synchronizedModel {
        scripts.firstOrNull { it.fileName == name }
    }

    fun update(script: Script) = synchronizedModelSaving {
        scripts.removeIf { it.fileName == script.fileName }
        scripts.add(script)
    }

    fun delete(name: String) = synchronizedModelSaving {
        scripts.removeIf { it.fileName == name }
    }
}
