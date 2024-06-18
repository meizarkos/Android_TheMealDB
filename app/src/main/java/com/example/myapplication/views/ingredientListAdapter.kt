package com.example.myapplication.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.IngredientModel

class IngredientListAdapter(private val ingredients:ArrayList<IngredientModel>, private val ingredientClickHandler: IngredientOnClickLListener): RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val ingredientView = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_cell_layout, parent, false)
        return IngredientViewHolder(ingredientView)
    }

    override fun getItemCount(): Int {
        return this.ingredients.size
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val currentTodoData = this.ingredients[position] // Get the data at the right position
        holder.bind(currentTodoData)
        holder.itemView.setOnClickListener {
            //Toast.makeText(holder.itemView.context, "Selected note n°${position + 1}", Toast.LENGTH_LONG).show()
            //ingredientClickHandler.displayTodoDetail(currentTodoData, position)
        }
    }


    // Class representing the object linked to the XML
    // of on cell of the RV
    inner class IngredientViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var ingredient : TextView

        init {
            this.ingredient = itemView.findViewById(R.id.cell_layout_ingredient_text_view)
        }

        fun bind(ingredient:IngredientModel) {
            this.ingredient.text = ingredient.name
        }
    }
}