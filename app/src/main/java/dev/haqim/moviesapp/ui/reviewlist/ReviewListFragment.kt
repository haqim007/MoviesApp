package dev.haqim.moviesapp.ui.reviewlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint
import dev.haqim.moviesapp.R
import dev.haqim.moviesapp.databinding.FragmentReviewListBinding
import dev.haqim.moviesapp.ui.common.pagerLoadState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewListFragment : Fragment() {
    private var _binding: FragmentReviewListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReviewListVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewListBinding.inflate(layoutInflater, container, false)

        val args: ReviewListFragmentArgs by navArgs()
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.reviews)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        val uiAction = {action: ReviewsListUiAction -> viewModel.processAction(action)}

        uiAction(ReviewsListUiAction.SetMovie(args.movie))

        val movieFlow = viewModel.state.map { it.movie }.distinctUntilChanged()
        viewLifecycleOwner.lifecycleScope.launch {
            movieFlow.collectLatest {
                if(it != null){
                    uiAction(ReviewsListUiAction.FetchReviews)
                    binding.tvTitle.text = it.title
                }
            }
        }

        setReviews{ uiAction(ReviewsListUiAction.FetchReviews) }

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            // Handle other menu items if needed
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setReviews(
        onTryAgain: () -> Unit
    ){
        val adapter = ReviewListAdapter()

        binding.rvReviews.adapter = adapter

        adapter.pagerLoadState(
            scope = viewLifecycleOwner.lifecycleScope,
            listView = binding.rvReviews,
            context = requireContext(),
            loader = binding.pbLoader,
            errorView = binding.errorList,
            onTryAgain = onTryAgain
        )

        viewLifecycleOwner
            .lifecycleScope
            .launch {
                viewModel
                    .pagingDataFlow
                    .collect(adapter::submitData)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}