package com.example.myapplication.views

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
import com.example.myapplication.model.NameUrl

class DetailRecipeAdapter(private val ingredients: MutableList<NameUrl>,): RecyclerView.Adapter<DetailRecipeAdapter.DetailRecipeViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailRecipeViewHolder{
        val detailView = LayoutInflater.from(parent.context)
            .inflate(R.layout.detail_cell_layout, parent, false)
        return DetailRecipeViewHolder(detailView)
    }

    override fun getItemCount(): Int {
        return this.ingredients.size
    }

    override fun onBindViewHolder(holder: DetailRecipeViewHolder, position: Int) {
        val currentRecipeData = this.ingredients[position]

        holder.bind(currentRecipeData)
    }

    inner class DetailRecipeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var name : TextView
        private var ingredientImage: ImageView
        private var measure: TextView

        init {
            this.name = itemView.findViewById(R.id.detail_cell_layout_ingredient_name_text_view)
            this.ingredientImage = itemView.findViewById(R.id.detail_cell_layout_ingredient_image_view)
            this.measure = itemView.findViewById(R.id.detail_cell_layout_ingredient_measure_text_view)
        }


        fun bind(detail: NameUrl) {
            Glide.with(itemView.context)
                .load("https://www.themealdb.com/images/ingredients/${detail.name}.png")
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(500)))
                .placeholder(R.drawable.homard_black) // Optional placeholder
                .error(R.drawable.error_905_black) // Optional error image
                .into(ingredientImage)

            this.name.text = detail.name
            this.measure.text = detail.measure
        }
    }
}