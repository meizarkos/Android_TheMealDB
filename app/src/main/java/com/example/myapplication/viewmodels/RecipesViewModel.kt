package com.example.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.IngredientModel
import com.example.myapplication.model.RecipeDto
import com.example.myapplication.model.RecipeListDto
import com.example.myapplication.model.RecipeModel
import com.example.myapplication.network.IngredientRepository
import com.example.myapplication.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RecipesViewModel {
    var recipesList: MutableLiveData<ArrayList<RecipeModel>> = MutableLiveData()

    fun getRecipes(ingredients: ArrayList<IngredientModel>?){
        val apiResponse = IngredientRepository(RetrofitClient.ingredientService).fetchRecipes("EGGS")
        //loader.visibility = ProgressBar.VISIBLE

        apiResponse.enqueue(object : Callback<RecipeListDto> {
            override fun onFailure(p0: Call<RecipeListDto>, t: Throwable) {
                /*loader.visibility = ProgressBar.GONE
                error.visibility = TextView.VISIBLE*/
                Log.d("IS ERROR ?", "onResponse: ${t.message}")
            }

            override fun onResponse(p0: Call<RecipeListDto>, response: Response<RecipeListDto>) {
                val responseBody: List<RecipeDto> = response.body()?.meals ?: listOf()
                val mappedResponse = responseBody.map {
                    RecipeModel(
                        it.idMeal,
                        it.strMealThumb,
                        it.strMeal,
                    )
                }
                recipesList.value = ArrayList(mappedResponse)
                /*loader.visibility = ProgressBar.GONE
                error.visibility = TextView.GONE*/
                Log.d("IS OK ?", "onResponse: $recipesList")
            }
        })
    }
}