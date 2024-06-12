package structure

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import example.EclipseDb
import example.EclipseDb.Companion.Schema

class DbSetup {
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:eclipse.db")
    private var version: Long
        get() {
            val queryResult = driver.executeQuery(
                identifier = null,
                sql = "PRAGMA user_version;",
                mapper = { sqlCursor: SqlCursor -> QueryResult.Value(sqlCursor.getLong(0)) },
                parameters = 0,
                binders = null
            )
            return queryResult.value!!
        }
        set(value) {
            driver.execute(
                identifier = null,
                sql = "PRAGMA user_version = $value;",
                parameters = 0,
                binders = null
            )
        }

    fun setUp() {
        val currentVer = version
        if (currentVer == 0L) {
            Schema.create(driver)
            version = Schema.version
        } else {
            val schemaVer = Schema.version
            if (schemaVer > currentVer) {
                Schema.migrate(driver, currentVer, schemaVer)
                version = schemaVer
            }
        }
    }
}

val driver = DbSetup().driver
val database = EclipseDb(driver)
val productQueries = database.productQueries
val userQueries = database.userQueries