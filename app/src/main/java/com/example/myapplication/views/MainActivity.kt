package com.example.myapplication.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.IngredientModel
import com.example.myapplication.viewmodels.IngredientViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(){

    private lateinit var ingredientListRecyclerView: RecyclerView
    private lateinit var searchBar:SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.searchBar = findViewById(R.id.search_ingredient_search_bar)
        observeIngredientListData()
        setGoToRecipesButton()
        IngredientViewModel.fetchIngredientFromRepo()
    }

    private fun setUpActivityViews(data: MutableList<IngredientModel>) {
        this.ingredientListRecyclerView = findViewById(R.id.ingredient_list_recycler_view)

        // Setup RV Adapter
        val ingredientAdapter = IngredientListAdapter(data)

        // Setup Linear layout manager
        val flexboxLayoutManager = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }

        this.ingredientListRecyclerView.layoutManager = flexboxLayoutManager
        this.ingredientListRecyclerView.setAdapter(ingredientAdapter)
    }

    private fun observeIngredientListData() {
       IngredientViewModel.ingredientList.observe(this) { ingredient ->
           this.setUpActivityViews(ingredient)
       }
    }




    private fun setGoToRecipesButton(){
        val redirectionButton = findViewById<FloatingActionButton>(R.id.go_to_recipes)
        redirectionButton.setOnClickListener {
            Log.d("MainActivity", "Redirecting to recipes")
        }
    }

}