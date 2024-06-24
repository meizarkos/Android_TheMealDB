package com.example.myapplication.network

import com.example.myapplication.model.IngredientListDto
import com.example.myapplication.model.MealRecipeDetailDto
import com.example.myapplication.model.RecipeListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IngredientServices {
    @GET("list.php?i=list")
    fun getIngredients(): Call<IngredientListDto>

    @GET("filter.php")
    fun getRecipes(
        @Query("i") ingredient: String
    ): Call<RecipeListDto>

    @GET("lookup.php")
    fun getDetailRecipe(
        @Query("i") idRecipe: String
    ): Call<MealRecipeDetailDto>
}