package com.example.teethkids.ui.home.options

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentReviewBinding
import com.example.teethkids.model.Review
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListReviewsAdapter
import com.google.firebase.Timestamp

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reviews = createFakeReviews()

        val adapter = ListReviewsAdapter(requireContext(), reviews)
        binding.listReviews.adapter = adapter
        binding.listReviews.layoutManager = LinearLayoutManager(requireContext())
        binding.toolbar.screenName.text = "Avaliações"

        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_reviewFragment_to_profileMainFragment)
        }

        val mUnderlineSpan = UnderlineSpan()

        val descriptionText = binding.pageDiscription.text.toString()
        val mBSpannableStringDescription = SpannableString(descriptionText)
        mBSpannableStringDescription.setSpan(mUnderlineSpan,57, 82, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


        val infoIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.outline_info_24)
        if (infoIcon != null) {
            val iconWidth = 34
            val iconHeight = 34

            infoIcon.setBounds(0, 0, iconWidth, iconHeight)

            val imageSpan = ImageSpan(infoIcon, ImageSpan.ALIGN_BOTTOM)
            mBSpannableStringDescription.setSpan(imageSpan, mBSpannableStringDescription.length - 1, mBSpannableStringDescription.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        binding.pageDiscription.text = mBSpannableStringDescription
    }

    private fun createFakeReviews(): List<Review> {
        val reviews = mutableListOf<Review>()

        val review1 = Review(
            image = R.drawable.outline_info_24,
            name = "John Doe",
            rating = 4.5,
            date = Timestamp.now(),
            review = "Avaliação teste",
            initials = "JD"
        )
        reviews.add(review1)

        val review2 = Review(
            image = R.drawable.outline_info_24,
            name = "Tereza Santos",
            rating = 1.0,
            date = Timestamp.now(),
            review = "Avaliação teste 3",
            initials = "TS"
        )
        reviews.add(review2)

        return reviews
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}