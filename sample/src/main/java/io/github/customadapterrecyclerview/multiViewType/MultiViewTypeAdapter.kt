package io.github.customadapterrecyclerview.multiViewType

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.farshidroohi.AdapterRecyclerView
import ir.farshid_roohi.customadapterrecyclerview.R
import ir.farshid_roohi.customadapterrecyclerview.databinding.ItemFourBinding
import ir.farshid_roohi.customadapterrecyclerview.databinding.ItemThreeBinding
import ir.farshid_roohi.customadapterrecyclerview.databinding.ItemTwoBinding
import ir.farshid_roohi.customadapterrecyclerview.databinding.MyItemBinding

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerView | Copyrights 1/2/19.
 */
class MultiViewTypeAdapter : AdapterRecyclerView<Model?>(
    0, R.layout.progress_view,
    R.layout.item_error,
    R.id.btnTrayAgain
) {

    companion object {
        const val ITEM_ONE = 10
        const val ITEM_TWO = 11
        const val ITEM_THREE = 12
        const val ITEM_FOUR = 13
    }

    override fun onBindView(
        viewHolder: RecyclerView.ViewHolder,
        position: Int,
        context: Context,
        element: Model?
    ) {
        when (viewHolder) {
            is ItemOneViewHolder -> {
                populateView(viewHolder, position)
            }
            is ItemTwoViewHolder -> {
                populateView(viewHolder, position)
            }
            is ItemThreeViewHolder -> {
                populateView(viewHolder, position)
            }
            is ItemFourViewHolder -> {
                populateView(viewHolder, position)
            }
        }
    }

    private fun populateView(vh: ItemOneViewHolder, position: Int) {
        val binding = MyItemBinding.bind(vh.itemView)
        binding.txtTwo.text = position.toString()
    }

    private fun populateView(vh: ItemTwoViewHolder, position: Int) {
        val binding = ItemTwoBinding.bind(vh.itemView)
        binding.txtTitle.text = position.toString()
    }

    private fun populateView(vh: ItemThreeViewHolder, position: Int) {
        val binding = ItemThreeBinding.bind(vh.itemView)
        binding.txtTitle.text = position.toString()
    }

    private fun populateView(vh: ItemFourViewHolder, position: Int) {
        val binding = ItemFourBinding.bind(vh.itemView)
        binding.txtTitle.text = position.toString()
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        val defaultType = super.getItemViewType(position)
        if (defaultType == ITEM_LOADING || defaultType == ITEM_FAILED) {
            return defaultType
        }
        return item?.type ?: defaultType
    }


    override fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        if (viewType == ITEM_ONE) {
            val view = layoutInflater.inflate(R.layout.my_item, viewGroup, false)
            return ItemOneViewHolder(view)
        }

        if (viewType == ITEM_TWO) {
            val view = layoutInflater.inflate(R.layout.item_two, viewGroup, false)
            return ItemTwoViewHolder(view)
        }

        if (viewType == ITEM_THREE) {
            val view = layoutInflater.inflate(R.layout.item_three, viewGroup, false)
            return ItemThreeViewHolder(view)
        }
        if (viewType == ITEM_FOUR) {
            val view = layoutInflater.inflate(R.layout.item_four, viewGroup, false)
            return ItemFourViewHolder(view)
        }

        return super.onCreateViewHolder(viewGroup, viewType)
    }

    class ItemOneViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ItemTwoViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ItemThreeViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ItemFourViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
