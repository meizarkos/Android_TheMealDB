package com.example.myapplication

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.example.myapplication.viewmodels.IngredientViewModel
import com.example.myapplication.viewmodels.RecipesViewModel
import com.example.myapplication.views.AllRecipes
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class DetailRecipeTest{

    @Before
    fun setUp(){
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        device.executeShellCommand("svc wifi enable")
        device.executeShellCommand("svc data enable")
    }

    @After
    fun tearDown(){
        tearDownCustom()
    }

    @Test
    fun detail_view_is_displayed() {
        ActivityScenario.launch(AllRecipes::class.java)

        Thread.sleep(2000)

        onView(withId(R.id.all_recipes_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        Thread.sleep(2000)

        onView(withId(R.id.recipe_detail_image_view)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_detail_title_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_detail_title_text_view)).check(matches(withText(RecipesViewModel.recipesList.value?.get(0)?.strMeal)))
        onView(withId(R.id.detail_recipe_instructions_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.recipe_detail_failure_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recipe_detail_progress_bar)).check(matches(not(isDisplayed())))

        //Check image cell and text cell is displayed
        onView(withId(R.id.recipe_detail_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, scrollTo()))
            .check(matches(isDisplayed()))

    }

    @Test
    fun failure_is_displayed_detail_recipes(){
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        ActivityScenario.launch(AllRecipes::class.java)
        Thread.sleep(1000)
        device.executeShellCommand("svc wifi disable")
        device.executeShellCommand("svc data disable")
        Thread.sleep(2000)
        onView(withId(R.id.all_recipes_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))


        onView(withId(R.id.recipe_detail_failure_text_view)).check(matches(isDisplayed()))
    }

    @Test
    fun loader_is_displayed(){

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        ActivityScenario.launch(AllRecipes::class.java)

        Thread.sleep(1000)
        device.executeShellCommand("svc wifi disable")
        device.executeShellCommand("svc data disable")
        onView(withId(R.id.all_recipes_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(2000)


        onView(withId(R.id.recipe_detail_progress_bar)).check(matches(isDisplayed()))
    }
}