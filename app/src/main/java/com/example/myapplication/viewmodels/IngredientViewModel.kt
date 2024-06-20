package com.example.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.IngredientListDto
import com.example.myapplication.model.IngredientModel
import com.example.myapplication.model.IngredientModelDto
import com.example.myapplication.network.IngredientRepository
import com.example.myapplication.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object IngredientViewModel {
    var ingredientList: MutableLiveData<MutableList<IngredientModel>> = MutableLiveData()
    var choiceIngredientList: MutableList<IngredientModel> = mutableListOf()

    fun fetchIngredientFromRepo(){
        val apiResponse = IngredientRepository(RetrofitClient.ingredientService).fetchIngredient()

        apiResponse.enqueue(object : Callback<IngredientListDto> {
            override fun onFailure(p0: Call<IngredientListDto>, t: Throwable) {
                Log.d("IngredientViewModel", "Failed to fetch ingredients")
            }

            override fun onResponse(p0: Call<IngredientListDto>, response: Response<IngredientListDto>) {
                val responseBody: List<IngredientModelDto> = response.body()?.meals ?: listOf()
                val mappedResponse = responseBody.map {
                    IngredientModel(
                        it.strIngredient,
                    )
                }
                ingredientList.value = ArrayList(mappedResponse)
            }
        })
    }
}