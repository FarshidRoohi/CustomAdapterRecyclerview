package io.github.customadapterrecyclerview

import android.content.Context
import io.github.farshidroohi.AdapterRecyclerView
import ir.farshid_roohi.customadapterrecyclerview.R
import kotlinx.android.synthetic.main.my_item.view.*

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerView | Copyrights 1/2/19.
 */
class MyAdapter : AdapterRecyclerView<String?>(R.layout.my_item, R.layout.progress_view, R.layout.item_error,R.id.btnTrayAgain) {

    override fun onBindView(viewHolder: ItemViewHolder, position: Int, context: Context, element: String?) {
        val itemView = viewHolder.itemView
        itemView.txt_title.text = "$element $position"
    }

}