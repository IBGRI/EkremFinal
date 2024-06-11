package com.ekrembgr.todo.shared.db

import com.ekrembgr.todo.shared.db.shared.newInstance
import com.ekrembgr.todo.shared.db.shared.schema
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver

public interface ToDoDatabase : Transacter {
  public val toDoQueries: ToDoQueries

  public companion object {
    public val Schema: SqlDriver.Schema
      get() = ToDoDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): ToDoDatabase =
        ToDoDatabase::class.newInstance(driver)
  }
}
