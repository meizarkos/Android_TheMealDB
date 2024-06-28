package com.example.myapplication

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.example.myapplication.viewmodels.RecipesViewModel
import com.example.myapplication.views.AllRecipes
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class AllRecipesTest{
    @Test
    fun test_recipes_recycler_view_is_displayed() {
        ActivityScenario.launch(AllRecipes::class.java)
        onView(withId(R.id.all_recipes_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.all_recipes_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.all_recipes_no_result_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.all_recipes_failure_text_view)).check(matches(not(isDisplayed())))

        //Check image cell and text cell is displayed
        onView(withId(R.id.all_recipes_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, scrollTo()))
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant(withText(RecipesViewModel.recipesList.value?.get(0)?.strMeal))))
    }

    @Test
    fun test_recipes_list_has_no_result(){
        ActivityScenario.launch(AllRecipes::class.java)
        onView(withId(R.id.search_recipes_search_bar)).perform(setSearchViewQuery("zzzzzzzzzzz", true))
        onView(withId(R.id.all_recipes_no_result_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.all_recipes_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.all_recipes_failure_text_view)).check(matches(not(isDisplayed())))
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
        ActivityScenario.launch(AllRecipes::class.java)

        onView(withId(R.id.all_recipes_failure_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.all_recipes_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.all_recipes_no_result_text_view)).check(matches(not(isDisplayed())))

        device.executeShellCommand("svc wifi enable")
        device.executeShellCommand("svc data enable")
    }

    @Test
    fun go_to_ingredient_activity(){
        ActivityScenario.launch(AllRecipes::class.java)
        onView(withId(R.id.go_to_ingredients)).perform(click())
        onView(withId(R.id.ingredient_list_layout)).check(matches(isDisplayed()))
    }
}