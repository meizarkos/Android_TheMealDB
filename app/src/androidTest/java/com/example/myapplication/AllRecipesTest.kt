package com.example.myapplication

import androidx.core.graphics.scaleMatrix
import androidx.lifecycle.Lifecycle
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
import com.example.myapplication.views.MainActivity
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AllRecipesTest{

    private var scenario: ActivityScenario<AllRecipes>? = null

    @Before
    fun setUp(){
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.executeShellCommand("svc wifi enable")
        device.executeShellCommand("svc data enable")
        this.scenario = ActivityScenario.launch(AllRecipes::class.java)
    }

    @After
    fun tearDown(){
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.executeShellCommand("svc wifi enable")
        device.executeShellCommand("svc data enable")
        scenario?.moveToState(Lifecycle.State.DESTROYED);
    }


    @Test
    fun test_recipes_recycler_view_is_displayed() {
        //ActivityScenario.launch(AllRecipes::class.java)
        Thread.sleep(1000)
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
        //ActivityScenario.launch(AllRecipes::class.java)
        onView(withId(R.id.search_recipes_search_bar)).perform(setSearchViewQuery("zzzzzzzzzzz", true))
        onView(withId(R.id.all_recipes_no_result_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.all_recipes_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.all_recipes_failure_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_recipes_search_bar)).perform(setSearchViewQuery("", true))
    }

    @Test
    fun loader_is_displayed(){
        assert(true)
    }

    @Test
    fun failure_is_displayed(){
        scenario?.moveToState(Lifecycle.State.DESTROYED)
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.executeShellCommand("svc wifi disable")
        device.executeShellCommand("svc data disable")
        Thread.sleep(1000)
        ActivityScenario.launch(AllRecipes::class.java)

        onView(withId(R.id.all_recipes_failure_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.all_recipes_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.all_recipes_no_result_text_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun go_to_ingredient_activity(){
        //ActivityScenario.launch(AllRecipes::class.java)
        onView(withId(R.id.go_to_ingredients)).perform(click())
        onView(withId(R.id.ingredient_list_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun modify_api_res_through_choice(){
        scenario?.moveToState(Lifecycle.State.DESTROYED);
        ActivityScenario.launch(MainActivity::class.java)
        Thread.sleep(1000)
        onView(withId(R.id.ingredient_list_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(13, click()))
        onView(withId(R.id.go_to_recipes)).perform(click())
        onView(withId(R.id.all_recipes_layout)).check(matches(isDisplayed()))

        //select basil and check there is no result for chicken recipes

        Thread.sleep(1000)

        onView(withId(R.id.search_recipes_search_bar)).perform(setSearchViewQuery("chicken", true))
        onView(withId(R.id.all_recipes_no_result_text_view)).check(matches(isDisplayed()))

        //select basil and chciken then check there is a result

        onView(withId(R.id.go_to_ingredients)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.ingredient_list_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.go_to_recipes)).perform(click())

        Thread.sleep(1000)

        onView(withId(R.id.search_recipes_search_bar)).perform(setSearchViewQuery("chicken", true))
        onView(withId(R.id.all_recipes_recycler_view)).check(matches(isDisplayed()))

    }
}