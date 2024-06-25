package com.example.myapplication.viewmodels

import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.IngredientModel
import com.example.myapplication.model.RecipeDto
import com.example.myapplication.model.RecipeListDto
import com.example.myapplication.model.RecipeModel
import com.example.myapplication.network.IngredientRepository
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.utils.transformNullinEmpty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object RecipesViewModel {
    var recipesList: MutableLiveData<ArrayList<RecipeModel>> = MutableLiveData()

    private fun handleRequests(apiResponse: Call<RecipeListDto>, loader: ProgressBar, error: TextView,noResult: TextView){
        apiResponse.enqueue(object : Callback<RecipeListDto> {
            override fun onFailure(p0: Call<RecipeListDto>, t: Throwable) {
                loader.visibility = ProgressBar.GONE
                error.visibility = TextView.VISIBLE
                noResult.visibility = TextView.GONE
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
                for(recipe in mappedResponse){
                   recipesList.value = recipesList.value?.plus(recipe) as ArrayList<RecipeModel>? ?: arrayListOf(recipe)
                }
                loader.visibility = ProgressBar.GONE
                error.visibility = TextView.GONE
            }
        })
    }

    fun getRecipes(ingredientChoices: ArrayList<IngredientModel>?, loader: ProgressBar, error: TextView,noResult:TextView){
        loader.visibility = ProgressBar.VISIBLE
        noResult.visibility = TextView.GONE
        var apiResponse: Call<RecipeListDto>

        if (!ingredientChoices.isNullOrEmpty()) {
            for(ingredient in ingredientChoices){
                apiResponse = IngredientRepository(RetrofitClient.ingredientService).fetchRecipes(transformNullinEmpty(ingredient.name))
                handleRequests(apiResponse, loader, error,noResult)
            }
        }
        else{
            apiResponse = IngredientRepository(RetrofitClient.ingredientService).fetchRecipes("")
            handleRequests(apiResponse, loader, error, noResult)
        }
    }
}