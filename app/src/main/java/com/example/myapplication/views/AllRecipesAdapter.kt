package com.example.myapplication.views

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.IngredientModel
import com.example.myapplication.model.RecipeModel
import com.example.myapplication.viewmodels.IngredientViewModel

class AllRecipesAdapter(private val recipes:ArrayList<RecipeModel>): RecyclerView.Adapter<AllRecipesAdapter.RecipeViewHolder>(){
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
            this.description.text = recipe.strMeal
        }
    }
}