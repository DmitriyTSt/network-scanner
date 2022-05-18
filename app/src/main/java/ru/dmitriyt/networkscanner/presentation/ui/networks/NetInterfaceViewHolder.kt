package ru.dmitriyt.networkscanner.presentation.ui.networks

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.data.model.NetInterface
import ru.dmitriyt.networkscanner.databinding.ItemNetworkInterfaceBinding
import ru.dmitriyt.networkscanner.presentation.extensions.inflate

class NetInterfaceViewHolder(
    parent: ViewGroup,
    private val onItemClick: (NetInterface.Connected) -> Unit,
) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_network_interface)) {
    private val binding by viewBinding(ItemNetworkInterfaceBinding::bind)

    fun bind(netInterface: NetInterface) = with(binding) {
        textViewName.text = netInterface.name
        textViewAddress.text = when (netInterface) {
            is NetInterface.Connected -> netInterface.ipAddress
            is NetInterface.Disconnected -> root.context.getString(R.string.net_interface_not_connected_status)
        }
        when (netInterface) {
            is NetInterface.Connected -> {
                root.setOnClickListener {
                    onItemClick(netInterface)
                }
            }
            is NetInterface.Disconnected -> {
                root.setOnClickListener(null)
            }
        }
    }
}