package io.liftgate.ftc.scripting.scripting

import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import io.liftgate.ftc.scripting.plugins.scriptService
import kotlinx.coroutines.runBlocking
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
                    val engine = ScriptEngineManager()
                        .getEngineByExtension("js")

                    // Seems to take around 5 seconds for initial start?
                    engine!!.eval("print(\"test\")")
                    tsEngine = engine
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

data class ScriptsContainer(
    val scripts: MutableList<Script> = mutableListOf()
)

private val gson = GsonBuilder()
    .setLongSerializationPolicy(LongSerializationPolicy.STRING)
    .setPrettyPrinting()
    .serializeNulls()
    .create()

class ScriptService(private val store: File)
{
    private val model = ScriptsContainer()

    init
    {
        if (!store.exists())
        {
            store.createNewFile()
            store.writeText(
                gson.toJson(model)
            )
        }

        model.scripts.addAll(
            gson
                .fromJson(
                    store.readText(),
                    ScriptsContainer::class.java
                )
                .scripts
        )
    }

    private fun save()
    {
        store.writeText(
            gson.toJson(model)
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
