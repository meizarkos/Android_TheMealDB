package com.example.myapplication

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.core.app.ActivityScenario
import com.example.myapplication.views.MainActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.uiautomator.UiDevice

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.viewmodels.IngredientViewModel
import org.hamcrest.CoreMatchers.not

import org.junit.Test
import org.junit.internal.runners.statements.Fail

fun setSearchViewQuery(query: String, submit: Boolean): ViewAction {
    return ViewActions.actionWithAssertions(object : ViewAction {
        override fun getConstraints(): org.hamcrest.Matcher<View>? {
            return ViewMatchers.isAssignableFrom(SearchView::class.java)
        }

        override fun getDescription(): String {
            return "Change view query"
        }

        override fun perform(uiController: UiController?, view: View?) {
            val searchView = view as SearchView
            searchView.setQuery(query, submit)
        }
    })
}

class IngredientListTest {
    @Test
    fun test_ingredient_recycler_view_is_displayed() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.ingredient_list_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.ingredient_list_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.ingredient_no_result_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.ingredient_failure_text_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_ingredient_list_has_no_result(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.search_ingredient_search_bar)).perform(setSearchViewQuery("zzzzzzzzzz", true))
        onView(withId(R.id.ingredient_no_result_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.ingredient_list_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.ingredient_failure_text_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun loader_is_displayed(){
        ActivityScenario.launch(MainActivity::class.java)
        if(R.id.ingredient_list_progress_bar == View.VISIBLE){
            onView(withId(R.id.ingredient_list_progress_bar)).check(matches(isDisplayed()))
        }
        else{
            assert(false)
        }
        onView(withId(R.id.ingredient_no_result_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.ingredient_failure_text_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun failure_is_displayed(){
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.executeShellCommand("svc wifi disable")
        device.executeShellCommand("svc data disable")
        ActivityScenario.launch(MainActivity::class.java)
        Thread.sleep(1000)

        onView(withId(R.id.ingredient_failure_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.ingredient_list_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.ingredient_no_result_text_view)).check(matches(not(isDisplayed())))

        device.executeShellCommand("svc wifi enable")
        device.executeShellCommand("svc data enable")
    }

    @Test
    fun go_to_next_activity(){
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.go_to_recipes)).perform(ViewActions.click())
        onView(withId(R.id.all_recipes_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun test_ingredient_on_click(){
        ActivityScenario.launch(MainActivity::class.java)

        if(IngredientViewModel.ingredientList.value?.get(0) != IngredientViewModel.choiceIngredientList[0])
        {
            assert(false)
        }
    }
}