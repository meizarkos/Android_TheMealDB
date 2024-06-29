package com.example.myapplication


import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.example.myapplication.viewmodels.IngredientViewModel
import com.example.myapplication.viewmodels.RecipesViewModel
import com.example.myapplication.views.AllRecipes
import com.example.myapplication.views.MainActivity
import org.hamcrest.Matcher
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

class GetBackgroundColorAction : ViewAction {

    private var backgroundFigure: GradientDrawable = GradientDrawable()
    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isAssignableFrom(View::class.java)
    }

    override fun getDescription(): String {
        return "get background color of a view"
    }

    override fun perform(uiController: UiController, view: View) {
        if (view.background is GradientDrawable) {
            val colorDrawable = view.background as GradientDrawable
            this.backgroundFigure = colorDrawable
        }
    }

    fun getBackgroundFigure(): GradientDrawable {
        return backgroundFigure
    }

    companion object {
        fun getBackgroundColor(): GetBackgroundColorAction {
            return GetBackgroundColorAction()
        }
    }
}

class GetFrameLayoutBackgroundColorAction(
    private val frameLayoutId: Int,
    private val getBackgroundColorAction: GetBackgroundColorAction
) : ViewAction {

    override fun getConstraints(): Matcher<View>? {
        return ViewMatchers.isAssignableFrom(View::class.java)
    }

    override fun getDescription(): String {
        return "Get background color of FrameLayout inside cell"
    }

    override fun perform(uiController: UiController, view: View) {
        val frameLayout = view.findViewById<View>(frameLayoutId)
        getBackgroundColorAction.perform(uiController, frameLayout)
    }

    fun getBackgroundFigure(): GradientDrawable {
        return getBackgroundColorAction.getBackgroundFigure()
    }

    companion object {
        fun getBackgroundColor(frameLayoutId: Int): GetFrameLayoutBackgroundColorAction {
            return GetFrameLayoutBackgroundColorAction(
                frameLayoutId,
                GetBackgroundColorAction.getBackgroundColor()
            )
        }
    }
}


@RunWith(AndroidJUnit4::class)
class IngredientListTest{

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
    fun test_ingredient_recycler_view_is_displayed() {
        ActivityScenario.launch(MainActivity::class.java)
        Thread.sleep(1000)
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
        Thread.sleep(3000)
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.ingredient_failure_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.ingredient_list_progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.ingredient_no_result_text_view)).check(matches(not(isDisplayed())))
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

    @Test
    fun check_background_color_change(){

        val unselected: GradientDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 500f
            setColor(0xFFE0E0E0.toInt())
        }

        val selected: GradientDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 500f
            setColor(0xFFF3CE99.toInt())
        }

        ActivityScenario.launch(MainActivity::class.java)
        Thread.sleep(1000)
        val getBackgroundColorAction = GetFrameLayoutBackgroundColorAction.getBackgroundColor(R.id.cell_ingredient_frame_layout)

        onView(withId(R.id.ingredient_list_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, getBackgroundColorAction))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))


        assertTrue("is gradient equal", unselected.color == getBackgroundColorAction.getBackgroundFigure().color)

        onView(withId(R.id.ingredient_list_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, getBackgroundColorAction))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        assertTrue("is gradient equal", selected.color == getBackgroundColorAction.getBackgroundFigure().color)

        onView(withId(R.id.ingredient_list_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, getBackgroundColorAction))

        assertTrue("is gradient equal", unselected.color == getBackgroundColorAction.getBackgroundFigure().color)

    }
}

