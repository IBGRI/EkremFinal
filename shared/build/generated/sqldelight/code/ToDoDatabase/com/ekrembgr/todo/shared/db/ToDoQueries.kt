package com.ekrembgr.todo.shared.db

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.Unit

public interface ToDoQueries : Transacter {
  public fun <T : Any> selectAll(mapper: (
    id: Long,
    content: String,
    complete: Boolean
  ) -> T): Query<T>

  public fun selectAll(): Query<ToDo>

  public fun insertToDo(content: String): Unit

  public fun deleteById(id: Long): Unit

  public fun updateComplete(complete: Boolean, id: Long): Unit
}
