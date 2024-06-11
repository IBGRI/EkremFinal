package com.ekrembgr.todo.shared

import android.content.Context
import com.ekrembgr.todo.shared.db.ToDoDatabase
import com.ekrembgr.todo.shared.repository.ToDoRepository
import com.squareup.sqldelight.android.AndroidSqliteDriver

fun createRepository(context: Context): ToDoRepository {
    val driver = AndroidSqliteDriver(ToDoDatabase.Schema, context, "ToDoDatabase.db")
    val database = ToDoDatabase(driver)
    return ToDoRepository(database)
}