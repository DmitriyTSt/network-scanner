package ru.dmitriyt.networkscanner.presentation.ui.network

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.databinding.ItemNetworkDeviceBinding
import ru.dmitriyt.networkscanner.presentation.extensions.inflate
import ru.dmitriyt.networkscanner.presentation.model.UiNetDevice

class NetDeviceViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_network_device)) {

    private val binding by viewBinding(ItemNetworkDeviceBinding::bind)

    fun bind(device: UiNetDevice) = with(binding) {
        imageViewIcon.setImageResource(device.iconRes)
        textViewHost.text = device.host
        textViewHostname.text = device.hostName
        textViewMacAddress.text = device.mac
    }
}