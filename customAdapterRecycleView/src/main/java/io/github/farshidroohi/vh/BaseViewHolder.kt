package io.github.farshidroohi.vh

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerview | Copyrights 10/8/22.
 */

class BaseViewHolder<ViewBindingType : ViewBinding>(val binding: ViewBindingType) :
    RecyclerView.ViewHolder(binding.root) {
}