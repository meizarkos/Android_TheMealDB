package com.example.myapplication.viewmodels

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
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

    fun fetchIngredientFromRepo(loader:ProgressBar,error:TextView){
        val apiResponse = IngredientRepository(RetrofitClient.ingredientService).fetchIngredient()
        loader.visibility = ProgressBar.VISIBLE

        apiResponse.enqueue(object : Callback<IngredientListDto> {
            override fun onFailure(p0: Call<IngredientListDto>, t: Throwable) {
                loader.visibility = ProgressBar.GONE
                error.visibility = TextView.VISIBLE
            }

            override fun onResponse(p0: Call<IngredientListDto>, response: Response<IngredientListDto>) {
                val responseBody: List<IngredientModelDto> = response.body()?.meals ?: listOf()
                val mappedResponse = responseBody.map {
                    IngredientModel(
                        it.idIngredient,
                        it.strIngredient,
                    )
                }
                ingredientList.value = ArrayList(mappedResponse)
                loader.visibility = ProgressBar.GONE
                error.visibility = TextView.GONE
            }
        })
    }
}