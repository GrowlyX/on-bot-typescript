package io.liftgate.ftc.scripting.scripting

import io.liftgate.ftc.scripting.plugins.scriptService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.reflections.util.ConfigurationBuilder
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import javax.script.Bindings
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

object ScriptEngineService
{
    var lock = Any()
    var ktsEngine: ScriptEngine? = null

    fun initializeEngine(): CompletableFuture<Void>
    {
        return synchronized(lock) {
            if (ktsEngine != null)
            {
                return@synchronized CompletableFuture
                    .completedFuture(null)
            }

            CompletableFuture
                .runAsync {
                    System.setProperty(
                        "kotlin.jsr223.experimental.resolve.dependencies.from.context.classloader",
                        true.toString()
                    )

                    val engine = ScriptEngineManager()
                        .getEngineByExtension("kts")

                    // Seems to take around 5 seconds for initial start?
                    engine!!.eval("println(\"test\")")
                    ktsEngine = engine
                }
        }
    }

    inline fun useEngine(
        block: (ScriptEngine, Bindings) -> Unit
    )
    {
        synchronized(lock) {
            checkNotNull(ktsEngine) {
                "KTS scripting engine has not yet been configured!"
            }

            val bindings = ktsEngine!!
                .createBindings()

            ktsEngine!!.setBindings(
                bindings,
                ScriptContext.ENGINE_SCOPE
            )
            block(ktsEngine!!, bindings)
        }
    }
}

val reflectionsMappings = ConcurrentHashMap<String, Reflections>()

@Serializable
data class Script(
    val fileName: String,
    var fileContent: String,
    var lastEdited: @Contextual LocalDateTime =
        java.time.LocalDateTime.now().toKotlinLocalDateTime()
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

            packageImports.forEach {
                script += reflectionsMappings
                    .computeIfAbsent(it) { pkg ->
                        Reflections(
                            ConfigurationBuilder()
                                .addScanners(Scanners.SubTypes)
                                .forPackages(pkg)
                        )
                    }
                    .getAll(Scanners.SubTypes)
                    .joinToString("\n") { type ->
                        "import $type"
                    }
            }

            // Apply shared script onto this script instance
            runBlocking {
                scriptService
                    ?.read("Shared.kts")
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
    object Scripts : Table()
    {
        val id = integer("id").autoIncrement()

        val fileName = varchar("name", length = 45)
        val fileContent = text("file_content", eagerLoading = true)

        val lastEdited = datetime("last_edited")
            .defaultExpression(CurrentDateTime)

        override val primaryKey = PrimaryKey(id)
    }

    init
    {
        transaction(database) {
            SchemaUtils.create(Scripts)
        }
    }

    suspend fun <T> asyncTransaction(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(user: Script): Int = asyncTransaction {
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
