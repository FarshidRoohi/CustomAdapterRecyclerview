package io.github.farshidroohi

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import io.github.farshidroohi.vh.BaseViewHolder


/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerview | Copyrights 10/8/22.
 */

abstract class DiffUtilAdapterRecyclerView<ViewBindingType : ViewBinding, T>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseViewHolder<ViewBindingType>>(diffCallback) {

    abstract fun initViewBinding(parent: ViewGroup): ViewBindingType
    abstract fun bind(binding: ViewBindingType, position: Int, item: T, payloads: MutableList<Any>?)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBindingType> {
        val binding = initViewBinding(parent)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewBindingType>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        bind(holder.binding, position, currentList[position], payloads)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBindingType>, position: Int) {
        bind(holder.binding, position, currentList[position], null)
    }

    override fun getItemCount(): Int = currentList.size

    fun changeItem(items: ArrayList<T>, element: T, newElement: T) {
        val index = items.indexOf(element)
        items[index] = newElement
        submitList(items.toMutableList())
    }

    fun clear() {
        submitList(emptyList())
    }

}