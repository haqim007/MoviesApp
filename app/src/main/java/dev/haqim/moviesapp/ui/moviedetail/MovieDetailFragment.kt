package dev.haqim.moviesapp.ui.moviedetail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import dev.haqim.moviesapp.R
import dev.haqim.moviesapp.data.mechanism.handleCollect
import dev.haqim.moviesapp.databinding.FragmentMovieDetailBinding
import dev.haqim.moviesapp.domain.model.Movie
import dev.haqim.moviesapp.domain.model.toStringGenres
import dev.haqim.moviesapp.ui.movielist.MovieListUiAction
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(layoutInflater, container, false)
        val args: MovieDetailFragmentArgs by navArgs()
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = args.movie.originalTitle
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        val uiAction = {action: MovieDetailUiAction -> viewModel.processAction(action)}

        // load detail movie
        uiAction(MovieDetailUiAction.FetchMovie(args.movie.id))

        val movieFlow = viewModel.state.map { it.movie }.distinctUntilChanged()
        viewLifecycleOwner.lifecycleScope.launch {
            movieFlow.handleCollect(
                onSuccess = {
                    binding.clMovieDetail.isVisible = true
                    binding.pbLoader.isVisible = false
                    it.data?.let { it1 -> setMovieDetail(it1) }
                },
                onLoading = {
                    binding.clMovieDetail.isVisible = false
                    binding.errorView.root.isVisible = false
                    binding.pbLoader.isVisible = true
                },
                onError = {
                    binding.clMovieDetail.isVisible = false
                    binding.errorView.root.isVisible = true
                    binding.errorView.tvErrorMessage.text = it.message ?: getString(R.string.an_error_occured)
                    binding.pbLoader.isVisible = false
                    binding.errorView.btnTryAgain.setOnClickListener {
                        uiAction(MovieDetailUiAction.FetchMovie(args.movie.id))
                    }
                }
            )
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setMovieDetail(movie: Movie){
        Glide.with(requireContext()).load(movie.posterUrl)
            .placeholder(R.drawable.outline_downloading_24)
            .transform(CenterInside(), RoundedCorners(24))
            .into(binding.ivPoster)
        binding.tvTitle.text = movie.title
        binding.rbMovieRating.rating = movie.vote.ratingProgressStar
        binding.llRating.setOnClickListener {
            findNavController().navigate(
                MovieDetailFragmentDirections
                    .actionMovieDetailFragmentToReviewListFragment(movie)
            )
        }
        binding.tvGenres.text = movie.genres.toStringGenres()
        binding.tvSummary.text = movie.plotSummary
        binding.tvVoteCount.text = movie.votes
        binding.tvReleasedDate.text = movie.releaseDate
        binding.tvStatus.text = movie.status
        binding.btnOpenTrailer.setOnClickListener {
            movie.trailerKey?.let {
              it1 -> watchTrailer(it1)
            } ?: run{
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.oops))
                    .setMessage(resources.getString(R.string.unavailable_trailer_for_this_movie))
                    .setPositiveButton(resources.getString(R.string.close)) { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun watchTrailer(key: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$key"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$key")
        )
        try {
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(webIntent)
        }
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}