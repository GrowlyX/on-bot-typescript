package io.liftgate.ftc.scripting.scripting

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Contextual
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import java.time.LocalDateTime
import javax.script.ScriptEngineManager

@Serializable
data class Script(
    val fileName: String,
    val fileContent: String,
    val lastEdited: @Contextual LocalDateTime
)
{
    inline fun run(
        packageImports: List<String>,
        vararg context: Pair<String, Any>,
        failure: (Throwable) -> Unit,
        debug: (String) -> Unit = ::println
    )
    {
        val engine = ScriptEngineManager()
            .getEngineByExtension("kts")

        with(engine) {
            var script = ""
            context.forEach { (k, v) -> put(k, v) }

            packageImports.forEach {
                Reflections(it)
                    .getAll(Scanners.SubTypes)
                    .forEach { import ->
                        debug("importing $import")
                        script += "import $import\n"
                    }
            }

            script += fileContent
            debug("evaluating")

            runCatching {
                eval(script)
            }.onFailure {
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

        val fileName = varchar("name", length = 25)
        val fileContent = text("fileContent", eagerLoading = true)

        val lastEdited = datetime("last_edited")
            .clientDefault {
                LocalDateTime.now()
            }

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

    suspend fun read(id: Int) = read { Scripts.id eq id }
    suspend fun read(name: String) = read { Scripts.fileName eq name }

    suspend fun read(selection: SqlExpressionBuilder.() -> Op<Boolean>): Script?
    {
        return asyncTransaction {
            Scripts.select(selection)
                .map {
                    Script(
                        it[Scripts.fileName],
                        it[Scripts.fileContent],
                        it[Scripts.lastEdited]
                    )
                }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, user: Script)
    {
        asyncTransaction {
            Scripts.update({ Scripts.id eq id }) {
                it[fileName] = user.fileName
                it[fileContent] = user.fileContent
                it[lastEdited] = user.lastEdited
            }
        }
    }

    suspend fun delete(id: Int)
    {
        asyncTransaction {
            Scripts.deleteWhere { Scripts.id.eq(id) }
        }
    }
}
