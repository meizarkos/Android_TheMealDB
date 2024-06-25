package com.example.myapplication.views

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.model.DetailRecipeDto
import com.example.myapplication.model.DetailRecipeModel
import com.example.myapplication.model.MealRecipeDetailDto
import com.example.myapplication.model.RecipeModel
import com.example.myapplication.network.IngredientRepository
import com.example.myapplication.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeDetail: AppCompatActivity() {
    private var recipe: RecipeModel?=null
    private var detailRecipe: DetailRecipeModel?=null

    private lateinit var recipeError : TextView
    private lateinit var recipeLoader : ProgressBar
    private lateinit var recipeImage : ImageView
    private lateinit var recipeTitle : TextView
    private lateinit var recipeInstructions : TextView
    private lateinit var scrollInstruction:ScrollView

    private lateinit var recipeIngredientsRecyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_recipe)
        this.recipe = intent.getParcelableExtra("RECIPE_MODEL_EXTRA")

        this.recipeError = findViewById(R.id.recipe_detail_failure_text_view)
        this.recipeLoader = findViewById(R.id.recipe_detail_progress_bar)
        this.recipeImage = findViewById(R.id.recipe_detail_image_view)
        val screenHeight = resources.displayMetrics.heightPixels
        recipeImage.layoutParams.height = screenHeight/3
        this.recipeTitle = findViewById(R.id.recipe_detail_title_text_view)

        this.fetchRecipeDetail(this.recipeLoader,this.recipeError)

    }

    private fun fetchRecipeDetail(loader:ProgressBar,error:TextView){
        val apiResponse = IngredientRepository(RetrofitClient.ingredientService).fetchDetailRecipe(this.recipe?.idMeal!!)
        loader.visibility = ProgressBar.VISIBLE

        apiResponse.enqueue(object : Callback<MealRecipeDetailDto> {
            override fun onFailure(p0: Call<MealRecipeDetailDto>, t: Throwable) {
                loader.visibility = ProgressBar.GONE
                error.visibility = TextView.VISIBLE
            }
            override fun onResponse(p0: Call<MealRecipeDetailDto>, response: Response<MealRecipeDetailDto>) {
                val responseBody: List<DetailRecipeDto> = response.body()?.meals ?: listOf()
                val listDetail = responseBody.map{ DetailRecipeModel(it.dateModified, it.idMeal, it.strArea, it.strCategory, it.strCreativeCommonsConfirmed, it.strDrinkAlternate, it.strImageSource, it.strIngredient1, it.strIngredient10, it.strIngredient11, it.strIngredient12, it.strIngredient13, it.strIngredient14, it.strIngredient15, it.strIngredient16, it.strIngredient17, it.strIngredient18, it.strIngredient19, it.strIngredient2, it.strIngredient20, it.strIngredient3, it.strIngredient4, it.strIngredient5, it.strIngredient6, it.strIngredient7, it.strIngredient8, it.strIngredient9, it.strInstructions, it.strMeal, it.strMealThumb, it.strMeasure1, it.strMeasure10, it.strMeasure11, it.strMeasure12, it.strMeasure13, it.strMeasure14, it.strMeasure15, it.strMeasure16, it.strMeasure17, it.strMeasure18, it.strMeasure19, it.strMeasure2, it.strMeasure20, it.strMeasure3, it.strMeasure4, it.strMeasure5, it.strMeasure6, it.strMeasure7, it.strMeasure8, it.strMeasure9, it.strSource, it.strTags, it.strYoutube) }

                detailRecipe = listDetail[0]
                loader.visibility = ProgressBar.GONE
                error.visibility = TextView.GONE



                if(detailRecipe?.strMealThumb != null){
                    Glide.with(this@RecipeDetail)
                        .load(detailRecipe!!.strMealThumb)
                        .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(70)))
                        .placeholder(R.drawable.homard) // Optional placeholder
                        .error(R.drawable.error_905) // Optional error image
                        .into(recipeImage)
                }
                recipeTitle.text = detailRecipe?.strMeal

                recipeIngredientsRecyclerView = findViewById(R.id.recipe_detail_recycler_view)

                val ingredients = detailRecipe?.getIngredientFromDetail()
                val linearLayoutManager = LinearLayoutManager(this@RecipeDetail)
                linearLayoutManager.orientation  = LinearLayoutManager.HORIZONTAL

                recipeIngredientsRecyclerView.layoutManager = linearLayoutManager
                recipeIngredientsRecyclerView.adapter = ingredients?.let { DetailRecipeAdapter(it) }

                recipeInstructions = findViewById(R.id.detail_recipe_instructions_text_view)
                recipeInstructions.text = detailRecipe?.strInstructions

                Log.d("SUICIDE", "onResponse: ${detailRecipe?.strInstructions}")

                scrollInstruction = findViewById(R.id.recipe_detail_instructions_scroll_view)
                scrollInstruction.setPadding(0,0,0,
                    detailRecipe?.strInstructions?.length!!/50 * 10
                )
            }
        })
    }
}

