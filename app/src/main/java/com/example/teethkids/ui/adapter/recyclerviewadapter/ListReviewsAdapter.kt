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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.databinding.AddressItemBinding
import com.example.teethkids.databinding.ReviewItemBinding
import com.example.teethkids.model.Address
import com.example.teethkids.model.Review
import com.example.teethkids.ui.dialog.ConfirmationMainAddressDialog
import com.example.teethkids.ui.dialog.ConfirmationReportReview
import com.example.teethkids.utils.AddressPrimaryId
import com.example.teethkids.utils.Utils
import java.util.*

class ListReviewsAdapter(
    private val context: Context,
) : ListAdapter<Review, ListReviewsAdapter.ReviewViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                // Check if the items have the same ID or unique identifier
                return oldItem.EmergencyId == newItem.EmergencyId
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                // Check if the contents of the items are the same, assuming the addressId is unique
                return oldItem == newItem
            }
        }
    }

    class ReviewViewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reviews: Review) {
            if(reviews.revision!!) {
                binding.btnSendRevision.isEnabled = false
            } else {
                binding.btnSendRevision.isEnabled = true
            }

            binding.reviewDate.text = Utils.formatTimestampReviews(reviews.createdAt!!)

            binding.reviewImage.setImageDrawable(generateProfilePhoto(Utils.getInitials(reviews.name!!)))
            binding.reviewName.text = reviews.name
            binding.reviewRating.rating = reviews.rating!!
        //    binding.reviewDate.text = Utils.formatTimestampReviews(reviews.createdAt!!)
            binding.reviewBody.text = reviews.review
            binding.btnSendRevision.setOnClickListener{
                val confirmationDialog = ConfirmationReportReview(binding.root.context,reviews)
                confirmationDialog.show()
            }
        }
        private fun generateProfilePhoto(name: String): Drawable {
            val initials = name.take(2).toUpperCase(Locale.getDefault())
            val width = 120
            val height = 120

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint().apply {
                val random = Random()
                color = Color.rgb(random.nextInt(255), random.nextInt(256), random.nextInt(256))
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

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviews = getItem(position)
        holder.bind(reviews)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListReviewsAdapter.ReviewViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ReviewItemBinding.inflate(inflater, parent, false)

        return ReviewViewHolder(binding)
    }
}