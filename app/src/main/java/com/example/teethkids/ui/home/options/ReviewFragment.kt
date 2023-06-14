package com.example.teethkids.ui.home.options

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teethkids.R
import com.example.teethkids.dao.UserDao
import com.example.teethkids.databinding.FragmentReviewBinding
import com.example.teethkids.model.Review
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListReviewsAdapter
import com.example.teethkids.utils.AddressPrimaryId
import com.example.teethkids.utils.Utils
import com.example.teethkids.viewmodel.AddressViewModel
import com.example.teethkids.viewmodel.ReviewsViewModel
import com.example.teethkids.viewmodel.UserViewModel
import com.google.firebase.Timestamp

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel
    private lateinit var listReviewsAdapter: ListReviewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.ratingBar.rating = user.rating!!.toFloat()
            binding.tvRating.text = Utils.formatRating(user.rating)
        }
        setupListAdapter()
        loadReviews()
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

    private fun setupListAdapter() {
        listReviewsAdapter = ListReviewsAdapter(requireContext())
        binding.listReviews.adapter = listReviewsAdapter
    }

    private fun loadReviews() {
        val reviewsViewModel = ViewModelProvider(this).get(ReviewsViewModel::class.java)
        reviewsViewModel.reviewsList.observe(viewLifecycleOwner) { reviews ->
            listReviewsAdapter.submitList(reviews)
            val totalRating = reviews.map { it.rating }
            val average =  Utils.calculateAverageRating(totalRating as List<Float>)
            val dao = UserDao(requireContext())
            dao.updateRating(average, onSuccess = {})

            binding.tvTotalRating.text = "Voce tem ${reviews.size} avaliações "
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}