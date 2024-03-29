package ru.dmitriyt.networkscanner.presentation.ui.networks

import android.os.Bundle
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.databinding.FragmentNetworkInterfacesBinding
import ru.dmitriyt.networkscanner.presentation.extensions.addLinearSpaceItemDecoration
import ru.dmitriyt.networkscanner.presentation.extensions.fitTopInsetsWithPadding
import ru.dmitriyt.networkscanner.presentation.navigation.observeNavigationCommands
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class NetworkInterfacesFragment : BaseFragment(R.layout.fragment_network_interfaces) {

    companion object {
        fun newInstance(): NetworkInterfacesFragment {
            return NetworkInterfacesFragment()
        }
    }

    private val binding by viewBinding(FragmentNetworkInterfacesBinding::bind)
    private val viewModel: NetworkInterfacesViewModel by viewModels()

    @Inject lateinit var adapter: NetInterfacesAdapter

    override fun callOperations() {
        viewModel.loadNetInterfaces()
    }

    override fun setupLayout(savedInstanceState: Bundle?) = with(binding) {
        appBarLayout.fitTopInsetsWithPadding()
        toolbar.menu.findItem(R.id.refresh).setOnMenuItemClickListener {
            viewModel.loadNetInterfaces()
            true
        }
        setupRecyclerView()
    }

    override fun onBindViewModel() = with(viewModel) {
        observeNavigationCommands(this)
        netInterfacesLiveData.observe { state ->
            binding.stateViewFlipper.setState(state)
            state.doOnSuccess { netInterfaces ->
                adapter.submitList(netInterfaces)
            }
            state.doOnError {
                val a = 1
            }
        }
    }

    private fun setupRecyclerView() = with(binding) {
        adapter.onItemClick = viewModel::openNetwork
        recyclerView.adapter = adapter
        recyclerView.addLinearSpaceItemDecoration()
    }
}