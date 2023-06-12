package com.example.teethkids.ui.adapter.recyclerviewadapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.databinding.ReviewItemBinding
import com.example.teethkids.model.Review
import java.util.*

class ListReviewsAdapter(
    private val context: Context,
    private val reviews: List<Review>
) : RecyclerView.Adapter<ListReviewsAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reviews: Review) {
            binding.reviewImage.setImageDrawable(generateProfilePhoto(reviews.initials))
            binding.reviewName.text = reviews.name
            binding.reviewRating.rating = reviews.rating.toFloat()
            binding.reviewDate.text = reviews.date.toString()
            binding.reviewBody.text = reviews.review
        }

        private fun generateProfilePhoto(name: String): Drawable {
            val initials = name.take(2).toUpperCase(Locale.getDefault())
            val width = 120
            val height = 120

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint().apply {
                color = Color.parseColor("#FFC107")
                style = Paint.Style.FILL
            }

            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), paint)

            paint.apply {
                color = Color.WHITE
                textSize = 60f
                textAlign = Paint.Align.CENTER
            }

            val xPos = canvas.width / 2
            val yPos = (canvas.height / 2) - ((paint.descent() + paint.ascent()) / 2)

            canvas.drawText(initials, xPos.toFloat(), yPos, paint)

            return BitmapDrawable(binding.root.resources, bitmap)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ReviewItemBinding.inflate(inflater, parent, false)
        return ReviewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviews = reviews[position]
        holder.bind(reviews)
    }
}