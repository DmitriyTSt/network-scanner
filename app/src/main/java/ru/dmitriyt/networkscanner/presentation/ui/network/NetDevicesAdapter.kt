package ru.dmitriyt.networkscanner.presentation.ui.network

import android.view.ViewGroup
import ru.dmitriyt.networkscanner.data.model.NetDevice
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseAdapter
import javax.inject.Inject

class NetDevicesAdapter @Inject constructor() : BaseAdapter<NetDevice, NetDeviceViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetDeviceViewHolder {
        return NetDeviceViewHolder(parent)
    }

    override fun onBindViewHolder(holder: NetDeviceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}