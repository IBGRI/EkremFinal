package com.ekrembgr.todo.shared.db

import kotlin.Boolean
import kotlin.Long
import kotlin.String

public data class ToDo(
  public val id: Long,
  public val content: String,
  public val complete: Boolean
) {
  public override fun toString(): String = """
  |ToDo [
  |  id: $id
  |  content: $content
  |  complete: $complete
  |]
  """.trimMargin()
}
