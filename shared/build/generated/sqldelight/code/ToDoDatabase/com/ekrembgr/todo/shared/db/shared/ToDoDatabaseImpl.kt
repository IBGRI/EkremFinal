package com.ekrembgr.todo.shared.db.shared

import com.ekrembgr.todo.shared.db.ToDo
import com.ekrembgr.todo.shared.db.ToDoDatabase
import com.ekrembgr.todo.shared.db.ToDoQueries
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.`internal`.copyOnWriteList
import com.squareup.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList
import kotlin.reflect.KClass

internal val KClass<ToDoDatabase>.schema: SqlDriver.Schema
  get() = ToDoDatabaseImpl.Schema

internal fun KClass<ToDoDatabase>.newInstance(driver: SqlDriver): ToDoDatabase =
    ToDoDatabaseImpl(driver)

private class ToDoDatabaseImpl(
  driver: SqlDriver
) : TransacterImpl(driver), ToDoDatabase {
  public override val toDoQueries: ToDoQueriesImpl = ToDoQueriesImpl(this, driver)

  public object Schema : SqlDriver.Schema {
    public override val version: Int
      get() = 1

    public override fun create(driver: SqlDriver): Unit {
      driver.execute(null, """
          |CREATE TABLE ToDo (
          |    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |    content TEXT NOT NULL,
          |    complete INTEGER NOT NULL DEFAULT 0
          |)
          """.trimMargin(), 0)
    }

    public override fun migrate(
      driver: SqlDriver,
      oldVersion: Int,
      newVersion: Int
    ): Unit {
    }
  }
}

private class ToDoQueriesImpl(
  private val database: ToDoDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), ToDoQueries {
  internal val selectAll: MutableList<Query<*>> = copyOnWriteList()

  public override fun <T : Any> selectAll(mapper: (
    id: Long,
    content: String,
    complete: Boolean
  ) -> T): Query<T> = Query(255705758, selectAll, driver, "ToDo.sq", "selectAll",
      "SELECT * FROM ToDo") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getLong(2)!! == 1L
    )
  }

  public override fun selectAll(): Query<ToDo> = selectAll { id, content, complete ->
    ToDo(
      id,
      content,
      complete
    )
  }

  public override fun insertToDo(content: String): Unit {
    driver.execute(1959633478, """
    |INSERT INTO ToDo(content)
    |VALUES (?)
    """.trimMargin(), 1) {
      bindString(1, content)
    }
    notifyQueries(1959633478, {database.toDoQueries.selectAll})
  }

  public override fun deleteById(id: Long): Unit {
    driver.execute(-1537436764, """DELETE FROM ToDo WHERE id = ?""", 1) {
      bindLong(1, id)
    }
    notifyQueries(-1537436764, {database.toDoQueries.selectAll})
  }

  public override fun updateComplete(complete: Boolean, id: Long): Unit {
    driver.execute(2051728681, """UPDATE ToDo SET complete = ? WHERE id = ?""", 2) {
      bindLong(1, if (complete) 1L else 0L)
      bindLong(2, id)
    }
    notifyQueries(2051728681, {database.toDoQueries.selectAll})
  }
}
