package ru.dmitriyt.networkscanner.presentation.ui.networks

import android.view.ViewGroup
import ru.dmitriyt.networkscanner.domain.model.NetInterface
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseAdapter
import javax.inject.Inject

class NetInterfacesAdapter @Inject constructor() : BaseAdapter<NetInterface, NetInterfaceViewHolder>() {
    lateinit var onItemClick: (NetInterface.Connected) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetInterfaceViewHolder {
        return NetInterfaceViewHolder(parent, onItemClick)
    }

    override fun onBindViewHolder(holder: NetInterfaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}