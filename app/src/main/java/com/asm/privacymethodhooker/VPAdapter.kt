package com.asm.privacymethodhooker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.asm.privacymethodhooker.databinding.ItemViewpager2Binding

class VPAdapter : Adapter<VPAdapter.VH>() {

    var bind: ItemViewpager2Binding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        bind = DataBindingUtil.inflate<ItemViewpager2Binding>(
            inflater,
            R.layout.item_viewpager2,
            parent,
            false
        )
        return VH(bind!!)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bind?.tv?.text = "position $position"
        bind?.tv2?.text = "position $position"
    }

    override fun getItemCount() = 3

    class VH(bind: ItemViewpager2Binding) : ViewHolder(bind.root) {

    }
}