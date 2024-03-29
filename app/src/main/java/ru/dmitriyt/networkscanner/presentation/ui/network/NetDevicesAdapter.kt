package ru.dmitriyt.networkscanner.presentation.ui.network

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.dmitriyt.networkscanner.presentation.diffutil.DiffUtilItemCallbackFactory
import ru.dmitriyt.networkscanner.presentation.model.UiNetDevice
import javax.inject.Inject

class NetDevicesAdapter @Inject constructor(
    diffUtilItemCallbackFactory: DiffUtilItemCallbackFactory,
) : ListAdapter<UiNetDevice, NetDeviceViewHolder>(diffUtilItemCallbackFactory.create()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetDeviceViewHolder {
        return NetDeviceViewHolder(parent)
    }

    override fun onBindViewHolder(holder: NetDeviceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}