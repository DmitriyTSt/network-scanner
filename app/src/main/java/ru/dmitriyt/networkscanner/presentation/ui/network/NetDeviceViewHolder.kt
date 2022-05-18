package ru.dmitriyt.networkscanner.presentation.ui.network

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.data.model.NetDevice
import ru.dmitriyt.networkscanner.databinding.ItemNetworkDeviceBinding
import ru.dmitriyt.networkscanner.presentation.extensions.inflate

class NetDeviceViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_network_device)) {
    private val binding by viewBinding(ItemNetworkDeviceBinding::bind)

    fun bind(device: NetDevice) = with(binding) {
        imageViewIcon.setImageResource(
            if (device.isCurrentDevice) {
                R.drawable.ic_device_current
            } else {
                R.drawable.ic_device_hub
            }
        )
        textViewHost.text = device.host
        textViewHostname.text = device.hostName ?: root.context.getString(R.string.unknown_device)
        textViewMacAddress.text = device.mac
    }
}