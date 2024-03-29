package com.example.hw_3.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.data.mapper.toGitModel
import com.example.hw_3.*
import com.example.hw_3.presentation.adapter.PersonAdapter
import com.example.hw_3.databinding.FragmentListBinding
import com.example.hw_3.presentation.model.Paging
import com.example.hw_3.presentation.viewmodels.PersonViewModel
import com.example.hw_3.presentation.paging3.PagingDataLce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentList : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<PersonViewModel>()

    private val adapter = PersonAdapter { person ->
        findNavController().navigate(
            R.id.details, bundleOf(NAME to person.login)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentListBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val linearLayoutManager = LinearLayoutManager(view.context)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = linearLayoutManager

            recyclerView
                .onPaginationScrolling(linearLayoutManager, ITEMS_TO_LOAD)
                .onEach { viewModel.onLoadMore() }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            layoutSwipeRefresh
                .onRefresh()
                .onEach { viewModel.onRefresh() }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            viewModel
                .personPagingData
                .onEach { state ->
                    progress.isVisible =
                        state == PagingDataLce.StateLoading && !layoutSwipeRefresh.isRefreshing
                    layoutSwipeRefresh.isRefreshing =
                        state == PagingDataLce.StateLoading && layoutSwipeRefresh.isRefreshing

                    when (state) {
                        is PagingDataLce.Content -> {
                            adapter.submitList(
                                state.dataList.map { Paging.Content(it.toGitModel()) }
                                    .let {
                                        if (state.hasData)
                                            it.plus(Paging.Loading)
                                        else it
                                    }
                            )
                        }
                        is PagingDataLce.ContentWithError -> {
                            adapter.submitList(
                                state.dataList.map { Paging.Content(it.toGitModel()) }
                                    .plus(Paging.Error)
                            )
                        }
                        is PagingDataLce.StateLoadingError -> {
                            requireContext().toast("Loading error")
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            binding.appBar.addToolbarInset()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val NAME = "name"
        private const val ITEMS_TO_LOAD = 10
    }
}



