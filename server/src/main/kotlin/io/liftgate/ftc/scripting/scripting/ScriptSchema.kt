package io.liftgate.ftc.scripting.scripting

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Contextual
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import javax.script.ScriptEngineManager

@Serializable
data class Script(
    val fileName: String,
    val fileContent: String,
    val lastEdited: @Contextual LocalDateTime
)
{
    fun run(
        context: Map<String, Any>,
        failure: (Throwable) -> Unit
    )
    {
        ScriptEngineManager()
            .getEngineByExtension("kts")
            .apply {
                context.forEach { (k, v) -> put(k, v) }

                runCatching {
                    eval(fileContent)
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

    suspend fun read(id: Int): Script?
    {
        return asyncTransaction {
            Scripts.select { Scripts.id eq id }
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
