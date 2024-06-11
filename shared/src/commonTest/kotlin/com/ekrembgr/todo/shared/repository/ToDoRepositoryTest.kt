package com.ekrembgr.todo.shared.repository

import app.cash.turbine.test
import com.ekrembgr.todo.shared.db.ToDo
import com.ekrembgr.todo.shared.db.ToDoDatabase
import com.ekrembgr.todo.shared.test.createTestDbDriver
import com.ekrembgr.todo.shared.test.suspendTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ToDoRepositoryTest {

    private val driver = createTestDbDriver()
    private val repository = ToDoRepository(ToDoDatabase(driver))

    @AfterTest
    fun resetDriver() {
        driver.close()
    }

    @Test
    fun happyPath() = suspendTest {
        val expectedToDos = listOf(
            ToDo(id = 1, content = "Make a list", complete = false),
            ToDo(id = 2, content = "Check it twice", complete = false)
        )

        repository.add(expectedToDos[0].content)
        repository.add(expectedToDos[1].content)

        repository.getList().test {
            assertEquals(expectedToDos, expectItem())
            expectNoEvents()

            repository.toggleComplete(expectedToDos[0])
            assertEquals(listOf(expectedToDos[0].copy(complete = true), expectedToDos[1]), expectItem())
            expectNoEvents()

            repository.remove(expectedToDos[1])
            assertEquals(listOf(expectedToDos[0].copy(complete = true)), expectItem())
            expectNoEvents()
        }
    }
}
