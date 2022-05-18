package ru.dmitriyt.networkscanner.presentation.ui.networks

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.databinding.FragmentNetworkInterfacesBinding
import ru.dmitriyt.networkscanner.presentation.extensions.addLinearSpaceItemDecoration
import ru.dmitriyt.networkscanner.presentation.extensions.appViewModels
import ru.dmitriyt.networkscanner.presentation.extensions.fitTopInsetsWithPadding
import ru.dmitriyt.networkscanner.presentation.navigation.observeNavigationCommands
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseFragment
import javax.inject.Inject

class NetworkInterfacesFragment : BaseFragment(R.layout.fragment_network_interfaces) {

    companion object {
        fun newInstance(): NetworkInterfacesFragment {
            return NetworkInterfacesFragment()
        }
    }

    private val binding by viewBinding(FragmentNetworkInterfacesBinding::bind)
    private val viewModel: NetworkInterfacesViewModel by appViewModels()

    @Inject lateinit var adapter: NetInterfacesAdapter

    override fun callOperations() {
        viewModel.loadNetInterfaces()
    }

    override fun setupLayout(savedInstanceState: Bundle?) = with(binding) {
        appBarLayout.fitTopInsetsWithPadding()
        setupRecyclerView()
    }

    override fun onBindViewModel() = with(viewModel) {
        observeNavigationCommands(this)
        netInterfacesLiveData.observe { state ->
            binding.stateViewFlipper.setState(state)
            state.doOnSuccess { netInterfaces ->
                adapter.submitList(netInterfaces)
            }
        }
    }

    private fun setupRecyclerView() = with(binding) {
        adapter.onItemClick = viewModel::openNetwork
        recyclerView.adapter = adapter
        recyclerView.addLinearSpaceItemDecoration()
    }
}