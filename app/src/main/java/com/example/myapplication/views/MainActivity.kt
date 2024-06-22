package com.example.myapplication.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.IngredientModel
import com.example.myapplication.utils.filterListBySubstring
import com.example.myapplication.utils.transformNullinEmpty
import com.example.myapplication.viewmodels.IngredientViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(){

    private lateinit var ingredientListRecyclerView: RecyclerView
    private lateinit var searchBar:SearchView
    private var searchBarValue:String? = ""
    private lateinit var noResultTextView:TextView
    private lateinit var loader:ProgressBar
    private lateinit var errorTextView:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.searchBar = findViewById(R.id.search_ingredient_search_bar)
        this.noResultTextView = findViewById(R.id.ingredient_no_result_text_view)
        this.loader = findViewById(R.id.ingredient_list_progress_bar)
        this.errorTextView = findViewById(R.id.ingredient_failure_text_view)

        observeIngredientListData()
        setGoToRecipesButton()
        handleSearch()
        IngredientViewModel.fetchIngredientFromRepo(loader,errorTextView)
    }

    private fun setUpActivityViews(data: MutableList<IngredientModel>) {
        this.ingredientListRecyclerView = findViewById(R.id.ingredient_list_recycler_view)

        val dataFilter = filterListBySubstring(data, transformNullinEmpty(searchBarValue))
        if(dataFilter.isEmpty()){
            noResultTextView.visibility = View.VISIBLE
            ingredientListRecyclerView.visibility = View.GONE
        }
        else{
            noResultTextView.visibility = View.GONE
            ingredientListRecyclerView.visibility = View.VISIBLE
            val ingredientAdapter = IngredientListAdapter(dataFilter)

            // Setup Linear layout manager
            val flexboxLayoutManager = FlexboxLayoutManager(this).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }

            this.ingredientListRecyclerView.layoutManager = flexboxLayoutManager
            this.ingredientListRecyclerView.setAdapter(ingredientAdapter)
        }
    }

    private fun handleSearch(){
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //no submit in our case
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchBarValue = newText
                IngredientViewModel.ingredientList.value?.let {observeIngredientListData()}
                return true
            }
        })
    }

    private fun observeIngredientListData() {
       IngredientViewModel.ingredientList.observe(this) { ingredient ->
           this.setUpActivityViews(ingredient)
       }
    }




    private fun setGoToRecipesButton(){
        val redirectionButton = findViewById<FloatingActionButton>(R.id.go_to_recipes)
        redirectionButton.setOnClickListener {
            val ingredientArrayList = ArrayList(IngredientViewModel.choiceIngredientList)
            Intent(this, AllRecipes::class.java).also {
                it.putParcelableArrayListExtra("CHOICE_LIST", ingredientArrayList)
                startActivity(it)
            }
        }
    }

}