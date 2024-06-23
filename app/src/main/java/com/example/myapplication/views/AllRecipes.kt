package com.example.myapplication.views

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.IngredientModel
import com.example.myapplication.model.RecipeModel
import com.example.myapplication.utils.filterArrayRecipe
import com.example.myapplication.utils.transformNullinEmpty
import com.example.myapplication.viewmodels.IngredientViewModel
import com.example.myapplication.viewmodels.RecipesViewModel

class AllRecipes: AppCompatActivity() {

    private var ingredientList: ArrayList<IngredientModel>? = null
    private lateinit var allRecipesRecyclerView: RecyclerView
    private lateinit var loader: ProgressBar
    private lateinit var error: TextView
    private lateinit var noResult: TextView
    private lateinit var searchRecipe : SearchView
    private var searchRecipeValue: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_recipes)

        this.searchRecipe = findViewById(R.id.search_recipes_search_bar)
        this.noResult = findViewById(R.id.all_recipes_no_result_text_view)
        this.loader = findViewById(R.id.all_recipes_progress_bar)
        this.error = findViewById(R.id.all_recipes_failure_text_view)


        this.ingredientList = intent.getParcelableArrayListExtra("CHOICE_LIST")

        observeRecipeListData()
        handleSearch()
        RecipesViewModel.recipesList.value = arrayListOf()
        RecipesViewModel.getRecipes(ingredientList,loader,error)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.ingredientList = intent.getParcelableArrayListExtra("CHOICE_LIST")
        RecipesViewModel.recipesList.value = arrayListOf()
        this.loader = findViewById(R.id.all_recipes_progress_bar)
        this.error = findViewById(R.id.all_recipes_failure_text_view)
        RecipesViewModel.getRecipes(ingredientList,loader,error)
    }

    private fun handleSearch(){
        searchRecipe.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //no submit in our case
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRecipeValue = newText
                IngredientViewModel.ingredientList.value?.let {observeRecipeListData()}
                return true
            }
        })
    }

    private fun setUpActivityViews(data: ArrayList<RecipeModel>) {
        this.allRecipesRecyclerView = findViewById(R.id.all_recipes_recycler_view)


        val dataFilter = filterArrayRecipe(data, transformNullinEmpty(searchRecipeValue))

        if(dataFilter.isEmpty()){
            noResult.visibility = View.VISIBLE
            allRecipesRecyclerView.visibility = View.GONE
        }
        else{
            val recipesAdapter = AllRecipesAdapter(dataFilter)
            noResult.visibility = View.GONE
            allRecipesRecyclerView.visibility = View.VISIBLE
            this.allRecipesRecyclerView.layoutManager = GridLayoutManager(this, 2)
            this.allRecipesRecyclerView.adapter = recipesAdapter
        }
    }

    private fun observeRecipeListData() {
        RecipesViewModel.recipesList.observe(this) { recipe ->
            this.setUpActivityViews(recipe)
        }
    }
}