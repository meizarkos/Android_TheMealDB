package com.example.myapplication.views

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.IngredientModel
import com.example.myapplication.viewmodels.IngredientViewModel
import okhttp3.internal.ignoreIoExceptions


class IngredientListAdapter(private val ingredients:MutableList<IngredientModel>): RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val ingredientView = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_cell_layout, parent, false)
        return IngredientViewHolder(ingredientView)
    }

    override fun getItemCount(): Int {
        return this.ingredients.size
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val currentIngredientData = this.ingredients[position]

        holder.bind(currentIngredientData)
        holder.itemView.setOnClickListener {
            holder.updateBackgroundLayout(currentIngredientData)
        }
    }

    inner class IngredientViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var ingredientTextView : TextView
        private var layout: LinearLayout
        private var ingredientImage : ImageView

        init {
            this.ingredientTextView = itemView.findViewById(R.id.cell_layout_ingredient_text_view)
            this.layout = itemView.findViewById(R.id.cell_layout_ingredient_linear_layout)
            this.ingredientImage = itemView.findViewById(R.id.cell_layout_ingredient_image_view)
        }

        private fun createLayoutBackground(color:Int): GradientDrawable {
            return GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 40f
                setColor(color)
            }
        }

        private fun manageLayoutBackground(ingredient:IngredientModel) {
            if (ingredient.isSelected) {
                this.layout.background = createLayoutBackground(0xFFF3CE99.toInt())
            } else {
                this.layout.background = createLayoutBackground(0xFFE0E0E0.toInt())
            }
        }


        fun bind(ingredient:IngredientModel) {
            this.ingredientTextView.text = ingredient.name
            manageLayoutBackground(ingredient)

            Glide.with(itemView.context)
                .load("https://www.themealdb.com/images/ingredients/${ingredient.name}.png")
                .placeholder(R.drawable.homard) // Optional placeholder
                .error(R.drawable.error_905) // Optional error image
                .into(ingredientImage)
        }

        private fun setList(ingredient: IngredientModel){
            val index = IngredientViewModel.ingredientList.value?.indexOfFirst { it.id == ingredient.id }
            index?.let {
                IngredientViewModel.ingredientList.value?.set(it, ingredient)
            }
        }

        fun updateBackgroundLayout(ingredient:IngredientModel) {
            ingredient.isSelected = !ingredient.isSelected

            var isInList = false
            for(choice in IngredientViewModel.choiceIngredientList){
                if(choice.id == ingredient.id){
                    isInList = true
                    break
                }
            }
            setList(ingredient)
            if(isInList) {
                IngredientViewModel.choiceIngredientList.remove(ingredient)
            }
            else {
                IngredientViewModel.choiceIngredientList.add(ingredient)
            }
            Log.d("CHOICES", "updateBackgroundLayout: ${IngredientViewModel.choiceIngredientList}")
            manageLayoutBackground(ingredient)
        }
    }
}