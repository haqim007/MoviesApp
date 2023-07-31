package dev.haqim.moviesapp.ui.movielist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import dev.haqim.moviesapp.R
import dev.haqim.moviesapp.data.mechanism.handleCollect
import dev.haqim.moviesapp.databinding.FragmentMovieListBinding
import dev.haqim.moviesapp.domain.model.Genre
import dev.haqim.moviesapp.domain.model.MovieListItem
import dev.haqim.moviesapp.ui.common.pagerLoadState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieListVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(layoutInflater, container, false)
        
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.app_name)
        actionBar?.setDisplayHomeAsUpEnabled(false)

        val uiState = viewModel.state
        val uiAction = {action: MovieListUiAction -> viewModel.processAction(action)}

        //collect genres
        val genresFlow =  uiState.map { it.genres }
        viewLifecycleOwner
            .lifecycleScope
            .launch {
               genresFlow.handleCollect(
                   onSuccess = {
                       binding.genresFilterShimmer.root.isVisible = false
                       setGenres(
                           it.data ?: listOf()
                       ){ genre ->
                           uiAction(MovieListUiAction.OnClickGenre(genre))
                       }
                   },
                   onLoading = {
                       binding.genresFilterShimmer.root.isVisible = true
                   },
                   onError = {
                       binding.genresFilterShimmer.root.isVisible = false
                       MaterialAlertDialogBuilder(requireContext())
                           .setTitle(getString(R.string.error))
                           .setMessage(resources.getString(R.string.failed_to_fetch_genres))
                           .setNeutralButton(resources.getString(R.string.close)) { dialog, which ->
                               dialog.dismiss()
                           }
                           .setPositiveButton(resources.getString(R.string.try_again)) { dialog, which ->
                               uiAction(MovieListUiAction.FetchGenres)
                           }
                           .show()
                   }
               )
            }

        // observe active/checked genres
        val selectedGenresFlow = uiState.map { it.activeGenres }.distinctUntilChanged()
        viewLifecycleOwner.lifecycleScope.launch {
            selectedGenresFlow.collect {
                uiAction(MovieListUiAction.FetchMovies)
            }
        }

        setMovies(
            onClickItem = {
               val action = MovieListFragmentDirections
                   .actionMovieListFragmentToMovieDetailFragment(it)
                findNavController().navigate(action)
            },
            onTryAgain = { uiAction(MovieListUiAction.FetchGenres) },
        )

        return binding.root
    }

    private fun setGenres(
        genres: List<Genre>,
        onClick: (Genre) -> Unit
    ){
        binding.cgGenres.removeAllViews()
        genres.forEach {genre: Genre ->
            val chip = Chip(requireContext())
            chip.setChipDrawable(
                ChipDrawable.createFromAttributes(
                    requireContext(),
                    null,
                    0,
                    R.style.ChipFilter
                ))
            chip.text = genre.name
            chip.isChecked = genre.isChecked
            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                onClick(genre)
            }
            binding.cgGenres.addView(chip)
        }
    }


    private fun setMovies(
        onClickItem: (MovieListItem) -> Unit,
        onTryAgain: () -> Unit,
    ){

        val adapter = MovieListAdapter(object : MovieListAdapterListener {
            override fun onClick(movie: MovieListItem) {
                onClickItem(movie)
            }
        })

        binding.rvMovies.adapter = adapter

        adapter.pagerLoadState(
            scope = viewLifecycleOwner.lifecycleScope,
            listView = binding.rvMovies,
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
        
        binding.srlMovieList.setOnRefreshListener { 
            adapter.refresh()
            onTryAgain()
            binding.srlMovieList.isRefreshing = false
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}