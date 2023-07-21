package ru.dmitriyt.networkscanner.presentation.ui.networks

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.databinding.ItemNetworkInterfaceBinding
import ru.dmitriyt.networkscanner.domain.model.NetInterface
import ru.dmitriyt.networkscanner.presentation.extensions.inflate
import ru.dmitriyt.networkscanner.presentation.model.UiNetInterface

class NetInterfaceViewHolder(
    parent: ViewGroup,
    private val onItemClick: (NetInterface.Connected) -> Unit,
) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_network_interface)) {

    private val binding by viewBinding(ItemNetworkInterfaceBinding::bind)

    fun bind(netInterface: UiNetInterface) = with(binding) {
        textViewName.text = netInterface.name
        textViewAddress.text = netInterface.address
        textViewAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(netInterface.statusIconRes, 0, 0, 0)
        if (!netInterface.isLoopback && netInterface.isConnected) {
            root.setOnClickListener {
                onItemClick(netInterface.domainModel as NetInterface.Connected)
            }
        } else {
            root.setOnClickListener(null)
        }
    }
}