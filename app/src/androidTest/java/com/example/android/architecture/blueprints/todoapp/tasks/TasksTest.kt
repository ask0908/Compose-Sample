/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.HiltTestActivity
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.TodoNavGraph
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Large End-to-End test for the tasks module.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class TasksTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    private val activity get() = composeTestRule.activity

    @Inject
    lateinit var repository: TasksRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun createOneTask_deleteTask() {
        setContent()

        val taskTitle = "TITLE1"
        // Add active task
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.add_task))
            .performClick()
        findTextField(R.string.title_hint).performTextInput(taskTitle)
        findTextField(R.string.description_hint).performTextInput("DESCRIPTION")
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.cd_save_task))
            .performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.label_all)).assertIsDisplayed()
        composeTestRule.onNodeWithText(taskTitle).assertIsDisplayed()

        // Open the task detail screen
        composeTestRule.onNodeWithText(taskTitle).performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.task_details))
            .assertIsDisplayed()
        // Click delete task in menu
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.menu_delete_task))
            .performClick()

        // Verify it was deleted
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.menu_filter))
            .performClick()
        composeTestRule.onNodeWithText(activity.getString(R.string.nav_all)).assertIsDisplayed()
        composeTestRule.onNodeWithText(taskTitle).assertDoesNotExist()
    }

    @Test
    fun createTask() {
        setContent()

        // Click on the "+" button, add details, and save
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.add_task))
            .performClick()
        findTextField(R.string.title_hint).performTextInput("title")
        findTextField(R.string.description_hint).performTextInput("description")
        composeTestRule.onNodeWithContentDescription(activity.getString(R.string.cd_save_task))
            .performClick()

        // Then verify task is displayed on screen
        composeTestRule.onNodeWithText(activity.getString(R.string.label_all)).assertIsDisplayed()
        composeTestRule.onNodeWithText("title").assertIsDisplayed()
    }

    private fun setContent() {
        composeTestRule.setContent {
            AppCompatTheme {
                TodoNavGraph()
            }
        }
    }

    private fun findTextField(textId: Int): SemanticsNodeInteraction {
        return composeTestRule.onNode(
            hasSetTextAction() and hasText(activity.getString(textId))
        )
    }

    private fun findTextField(text: String): SemanticsNodeInteraction {
        return composeTestRule.onNode(
            hasSetTextAction() and hasText(text)
        )
    }
}