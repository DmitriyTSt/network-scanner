package ru.dmitriyt.networkscanner.presentation.ui.network

import android.os.Bundle
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.data.model.NetInterface
import ru.dmitriyt.networkscanner.databinding.FragmentNetworkBinding
import ru.dmitriyt.networkscanner.presentation.extensions.appViewModels
import ru.dmitriyt.networkscanner.presentation.extensions.fitTopInsetsWithPadding
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
        arguments?.getParcelable<NetInterface.Connected>(ARGS_KEY)
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
        toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
        setupHeader()
        setupRecyclerView()
    }

    override fun onBindViewModel() = with(viewModel) {
        observeNavigationCommands(this)
        devicesLiveData.observe { state ->
            binding.stateViewFlipper.setState(state)
            state.doOnSuccess { devices ->
                adapter.submitList(devices)
            }
        }
    }

    private fun setupHeader() = with(binding) {
        textViewNetworkInterfaceName.text = netInterface.name
        textViewNetworkInterfaceAddress.text = netInterface.networkIpAddress
    }

    private fun setupRecyclerView() = with(binding) {
        recyclerView.adapter = adapter
    }
}