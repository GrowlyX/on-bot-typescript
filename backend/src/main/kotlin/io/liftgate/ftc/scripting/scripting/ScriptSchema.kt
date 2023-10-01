package io.liftgate.ftc.scripting.scripting

import io.liftgate.ftc.scripting.plugins.scriptService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
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
        packageImports: List<String>,
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

class ScriptService(database: Database)
{
    object Scripts : IntIdTable()
    {
        val fileName = varchar("name", length = 45)
        val fileContent = text("file_content", eagerLoading = true)

        val lastEdited = long("last_edited")
    }

    init
    {
        transaction(database) {
            SchemaUtils.create(Scripts)
        }
    }

    suspend fun <T> asyncTransaction(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(user: Script): EntityID<Int> = asyncTransaction {
        Scripts.insert {
            it[fileName] = user.fileName
            it[fileContent] = user.fileContent
            it[lastEdited] = user.lastEdited
        }[Scripts.id]
    }

    private fun Query.mapToScript() = map {
        Script(
            it[Scripts.fileName],
            it[Scripts.fileContent],
            it[Scripts.lastEdited]
        )
    }

    suspend fun readAll() = asyncTransaction {
        Scripts.selectAll()
            .mapToScript()
            .toList()
    }

    suspend fun read(id: Int) = read { Scripts.id eq id }
    suspend fun read(name: String) = read { Scripts.fileName eq name }

    suspend fun read(selection: SqlExpressionBuilder.() -> Op<Boolean>): Script?
    {
        return asyncTransaction {
            Scripts
                .select(selection)
                .mapToScript()
                .singleOrNull()
        }
    }

    suspend fun update(script: Script)
    {
        asyncTransaction {
            Scripts.update({
                Scripts.fileName eq script.fileName
            }) {
                it[fileName] = script.fileName
                it[fileContent] = script.fileContent
                it[lastEdited] = script.lastEdited
            }
        }
    }

    suspend fun delete(name: String)
    {
        asyncTransaction {
            Scripts.deleteWhere { fileName eq name }
        }
    }

    suspend fun delete(id: Int)
    {
        asyncTransaction {
            Scripts.deleteWhere { Scripts.id.eq(id) }
        }
    }
}
