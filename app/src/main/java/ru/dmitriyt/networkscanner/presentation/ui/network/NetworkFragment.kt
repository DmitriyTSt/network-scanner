package ru.dmitriyt.networkscanner.presentation.ui.network

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.data.model.NetInterface
import ru.dmitriyt.networkscanner.databinding.FragmentNetworkBinding
import ru.dmitriyt.networkscanner.presentation.extensions.addVerticalDividerItemDecoration
import ru.dmitriyt.networkscanner.presentation.extensions.appViewModels
import ru.dmitriyt.networkscanner.presentation.extensions.fitTopInsetsWithPadding
import ru.dmitriyt.networkscanner.presentation.extensions.getParcelableCompat
import ru.dmitriyt.networkscanner.presentation.navigation.observeNavigationCommands
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseFragment
import javax.inject.Inject

class NetworkFragment : BaseFragment(R.layout.fragment_network) {

    companion object {
        private const val ARGS_KEY = "network_fragment_args_key"

        fun newInstance(netInterface: NetInterface.Connected): NetworkFragment {
            return NetworkFragment().apply {
                arguments = bundleOf(ARGS_KEY to netInterface)
            }
        }
    }

    private val netInterface by lazy {
        arguments?.getParcelableCompat<NetInterface.Connected>(ARGS_KEY)
            ?: throw IllegalArgumentException("NetworkFragment fail to get args")
    }

    private val binding by viewBinding(FragmentNetworkBinding::bind)
    private val viewModel: NetworkViewModel by appViewModels()

    @Inject lateinit var adapter: NetDevicesAdapter

    override fun callOperations() {
        viewModel.loadDevices(netInterface)
    }

    override fun setupLayout(savedInstanceState: Bundle?) = with(binding) {
        toolbar.fitTopInsetsWithPadding()
        setupToolbar()
        setupHeader()
        setupRecyclerView()
    }

    override fun onBindViewModel() = with(viewModel) {
        observeNavigationCommands(this)
        devicesDataLiveData.observe { devices ->
            adapter.submitList(devices)
            binding.recyclerView.invalidateItemDecorations()
        }
        scanStateLiveEvent.observe { state ->
            binding.toolbar.menu.findItem(R.id.refresh).isVisible = !state.isLoading
            binding.progressBarScan.isVisible = state.isLoading
        }
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
        toolbar.menu.findItem(R.id.refresh).setOnMenuItemClickListener {
            viewModel.loadDevices(netInterface)
            true
        }
    }

    private fun setupHeader() = with(binding) {
        textViewNetworkInterfaceName.text = netInterface.name
        textViewNetworkInterfaceAddress.text = netInterface.networkIpAddress
    }

    private fun setupRecyclerView() = with(binding) {
        recyclerView.adapter = adapter
        recyclerView.addVerticalDividerItemDecoration()
    }
}