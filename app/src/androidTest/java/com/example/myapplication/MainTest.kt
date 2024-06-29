package com.example.myapplication

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.example.myapplication.viewmodels.IngredientViewModel
import com.example.myapplication.viewmodels.RecipesViewModel
import org.hamcrest.Matcher
import org.junit.runner.RunWith
import org.junit.runners.Suite


fun tearDownCustom(){
    Handler(Looper.getMainLooper()).post {
        IngredientViewModel.choiceIngredientList = mutableListOf()
        IngredientViewModel.ingredientList.value = mutableListOf()
        RecipesViewModel.recipesList.value = ArrayList()
        RecipesViewModel.recipesList = MutableLiveData()
    }
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    device.executeShellCommand("svc wifi enable")
    device.executeShellCommand("svc data enable")
}

fun setSearchViewQuery(query: String, submit: Boolean): ViewAction {
    return ViewActions.actionWithAssertions(object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
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


@RunWith(Suite::class)
@Suite.SuiteClasses(
    DetailRecipeTest::class,
    AllRecipesTest::class,
    IngredientListTest::class
    )


class AllTestsSuite {
}