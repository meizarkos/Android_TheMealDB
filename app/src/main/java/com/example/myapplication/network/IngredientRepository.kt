package com.example.myapplication.network

import com.example.myapplication.model.IngredientListDto
import com.example.myapplication.model.RecipeListDto
import retrofit2.Call

class IngredientRepository(private val ingredientService: IngredientServices) {
    fun fetchIngredient(): Call<IngredientListDto> {
        return ingredientService.getIngredients()
    }

    fun fetchRecipes(ingredient:String): Call<RecipeListDto> {
        return ingredientService.getRecipes(ingredient)
    }
}