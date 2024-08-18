package structure

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import db.OrderDB
import example.EclipseDb
import example.EclipseDb.Companion.Schema
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

val orderDBDateAdapter = object : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String): LocalDate {
        return LocalDate.parse(databaseValue, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    override fun encode(value: LocalDate) = value.toString()
}

val driver = DbSetup().driver
val database = EclipseDb(
    driver = driver,
    OrderDBAdapter = OrderDB.Adapter(
        dateAdapter = orderDBDateAdapter
    )
)
val productQueries = database.productQueries
val userQueries = database.userQueries
val orderQueries = database.orderQueries
