package com.example.myapplication.views

import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.model.RecipeModel

class AllRecipesAdapter(private val recipes: Array<RecipeModel>,private val detailRecipeClickHanlder:RecipeOnClickListener): RecyclerView.Adapter<AllRecipesAdapter.RecipeViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val ingredientView = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_recipes_cell_layout, parent, false)
        return RecipeViewHolder(ingredientView)
    }

    override fun getItemCount(): Int {
        return this.recipes.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipeData = this.recipes[position]

        holder.bind(currentRecipeData)
        holder.itemView.setOnClickListener {
            detailRecipeClickHanlder.displayRecipeDetail(currentRecipeData, position)
        }
    }

    inner class RecipeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var description : TextView
        private var backImage: ImageView

        init {
            this.description = itemView.findViewById(R.id.recipe_text_view)
            this.backImage = itemView.findViewById(R.id.recipe_image_image_view)
        }


        fun bind(recipe:RecipeModel) {
            Glide.with(itemView.context)
                .load(recipe.strMealThumb)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(50)))
                .placeholder(R.drawable.homard) // Optional placeholder
                .error(R.drawable.error_905) // Optional error image
                .into(backImage)

            this.description.text = recipe.strMeal
        }
    }
}