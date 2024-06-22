package com.example.myapplication.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.IngredientModel
import com.example.myapplication.model.RecipeModel
import com.example.myapplication.utils.filterListBySubstring
import com.example.myapplication.utils.transformNullinEmpty
import com.example.myapplication.viewmodels.IngredientViewModel
import com.example.myapplication.viewmodels.RecipesViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class AllRecipes: AppCompatActivity() {

    private var ingredientList: ArrayList<IngredientModel>? = null
    private lateinit var allRecipesRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_recipes)
        this.ingredientList = intent.getParcelableArrayListExtra("CHOICE_LIST")

        observeRecipeListData()

        RecipesViewModel.getRecipes(ingredientList)
    }

    private fun setUpActivityViews(data: ArrayList<RecipeModel>) {
        this.allRecipesRecyclerView = findViewById(R.id.all_recipes_recycler_view)
        val recipesAdapter = AllRecipesAdapter(data)

        this.allRecipesRecyclerView.layoutManager = GridLayoutManager(this, 2)
        this.allRecipesRecyclerView.adapter = recipesAdapter
    }

    private fun observeRecipeListData() {
        RecipesViewModel.recipesList.observe(this) { recipe ->
            this.setUpActivityViews(recipe)
        }
    }
}