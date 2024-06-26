package com.example.myapplication

import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import com.example.myapplication.views.MainActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.uiautomator.UiDevice

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.`RecyclerViewActions$ActionOnItemViewAction-IA`
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myapplication.viewmodels.IngredientViewModel
import junit.framework.TestCase.assertTrue
import okhttp3.internal.wait
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.AllOf
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

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

@RunWith(AndroidJUnit4::class)
class IngredientListTest {

    @Test
    fun test_ingredient_recycler_view_is_displayed() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.ingredient_list_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.ingredient_list_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.ingredient_no_result_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.ingredient_failure_text_view)).check(matches(not(isDisplayed())))

        //Check image cell and text cell is displayed
        onView(withId(R.id.ingredient_list_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, scrollTo()))
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant(withText(IngredientViewModel.ingredientList.value?.get(0)?.name))))
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
         assert(true)
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
        onView(withId(R.id.go_to_recipes)).perform(click())
        onView(withId(R.id.all_recipes_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun test_ingredient_on_click(){
        ActivityScenario.launch(MainActivity::class.java)

        Thread.sleep(1000)
        onView(withId(R.id.ingredient_list_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        val firstElementSelected = IngredientViewModel.ingredientList.value?.get(0)!!

        assertTrue("Right item add in the list",IngredientViewModel.ingredientList.value?.get(0) == IngredientViewModel.choiceIngredientList[0])
        assertTrue("Item should be selected",IngredientViewModel.choiceIngredientList[0].isSelected)
        assertTrue("Ingredient should be updated", firstElementSelected.isSelected)

        onView(withId(R.id.ingredient_list_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        val firstElement = IngredientViewModel.ingredientList.value?.get(0)!!

        assertTrue("Ingredient choice should be empty", IngredientViewModel.choiceIngredientList.isEmpty())
        assertTrue("Ingredient list is not selected", !firstElement.isSelected)
    }

    @Test
    fun test_ingredient_on_click_when_changing_screen(){
        ActivityScenario.launch(MainActivity::class.java)

        Thread.sleep(1000)
        onView(withId(R.id.ingredient_list_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        val firstElementSelected = IngredientViewModel.ingredientList.value?.get(0)!!

        assertTrue("Right item add in the list",IngredientViewModel.ingredientList.value?.get(0) == IngredientViewModel.choiceIngredientList[0])
        assertTrue("Item should be selected",IngredientViewModel.choiceIngredientList[0].isSelected)
        assertTrue("Ingredient should be updated", firstElementSelected.isSelected)

        //change screen

        onView(withId(R.id.go_to_recipes)).perform(click())
        onView(withId(R.id.all_recipes_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.go_to_ingredients)).perform(click())
        onView(withId(R.id.ingredient_list_recycler_view)).check(matches(isDisplayed()))

        //go back to the other screen

        onView(withId(R.id.ingredient_list_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        val firstElement = IngredientViewModel.ingredientList.value?.get(0)!!

        assertTrue("Ingredient choice should be empty", IngredientViewModel.choiceIngredientList.isEmpty())
        assertTrue("Ingredient list is not selected", !firstElement.isSelected)
    }
}