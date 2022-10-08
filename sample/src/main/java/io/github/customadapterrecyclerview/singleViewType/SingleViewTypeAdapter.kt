package io.github.customadapterrecyclerview.singleViewType

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import io.github.farshidroohi.AdapterRecyclerView
import ir.farshid_roohi.customadapterrecyclerview.R
import ir.farshid_roohi.customadapterrecyclerview.databinding.MyItemBinding

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerView | Copyrights 1/2/19.
 */
class SingleViewTypeAdapter : AdapterRecyclerView<String?>(
    R.layout.my_item,
    R.layout.progress_view,
    R.layout.item_error,
    R.id.btnTrayAgain
) {

    override fun onBindView(
        viewHolder: RecyclerView.ViewHolder,
        position: Int,
        context: Context,
        element: String?
    ) {
        val binding = MyItemBinding.bind(viewHolder.itemView)
        binding.txtTwo.text = position.toString()
    }

}