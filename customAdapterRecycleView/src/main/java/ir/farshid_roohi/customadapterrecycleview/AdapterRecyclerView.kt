package ir.farshid_roohi.customadapterrecycleview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerView | Copyrights 1/1/19.
 */

abstract class AdapterRecyclerView<T>(@LayoutRes val itemViewLayout: Int, @LayoutRes val itemLoadingLayout: Int, @LayoutRes val itemFailedLayout: Int, @IdRes val retryButtonId: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM_VIEW = 0
        private const val ITEM_LOADING = 1
        private const val ITEM_FAILED = 2
    }

    private val list: MutableList<T?> = arrayListOf()
    private var lastItemItemState: ItemState = ItemState.LOADED
    private var layoutManager: RecyclerView.LayoutManager? = null
    var mustLoad: Boolean = false
        private set
        get() = lastItemItemState == ItemState.LOADED
    var onRetryClicked: () -> Unit = {}


    abstract fun onBindView(viewHolder: ItemViewHolder, position: Int, context: Context, element: T?)


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(viewGroup.context)

        return when (viewType) {
            ITEM_VIEW -> {
                val view = inflater.inflate(itemViewLayout, viewGroup, false)
                ItemViewHolder(view)
            }

            ITEM_LOADING -> {
                val view = inflater.inflate(itemLoadingLayout, viewGroup, false)
                LoadingViewHolder(view)
            }

            ITEM_FAILED -> {
                val view = inflater.inflate(itemFailedLayout, viewGroup, false)
                FailedViewHolder(view)
            }

            else -> {
                val view = inflater.inflate(itemViewLayout, viewGroup, false)
                ItemViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        when (viewHolder) {

            is ItemViewHolder -> {
                onBindView(viewHolder, position, viewHolder.itemView.context, getItem(position))
            }

            is FailedViewHolder -> {

                handleSingleRow(viewHolder, position)

                val view = viewHolder.itemView.findViewById<View?>(retryButtonId)
                view?.setOnClickListener {
                    onRetryClicked()
                }

            }

            is LoadingViewHolder -> {
                handleSingleRow(viewHolder, position)
            }

        }

    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {

        if (position == itemCount - 1) {

            if (lastItemItemState == ItemState.FAILED) {
                return ITEM_FAILED
            }

            if (lastItemItemState == ItemState.LOADING) {
                return ITEM_LOADING
            }

        }

        return ITEM_VIEW

    }


    fun removeAll() {
        if (list.isEmpty()) {
            return
        }
        list.clear()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T? {
        return if (list.isEmpty()) null else list[position]
    }


    fun loadedState(newItems: List<T?>?) {

        if (newItems == null) {
            return
        }

        var addedPosition = 0

        if (list.isNotEmpty()) {
            addedPosition = itemCount
            if (lastItemItemState != ItemState.LOADED) {
                list.removeAt(list.lastIndex)
            }
        }

        lastItemItemState = ItemState.LOADED
        list.addAll(newItems)

        if (addedPosition == 0) {
            notifyDataSetChanged()
            return
        }
        notifyItemInserted(addedPosition)

    }

    fun failedState() {

        if (lastItemItemState == ItemState.LOADING) {
            list.removeAt(list.lastIndex)
        }

        lastItemItemState = ItemState.FAILED
        list.add(null)

        notifyItemChanged(list.lastIndex)

    }


    fun loadingState() {

        var changed = false

        if (list.isNotEmpty() && lastItemItemState != ItemState.LOADED) {
            list.removeAt(list.lastIndex)
            changed = true
        }

        lastItemItemState = ItemState.LOADING
        list.add(null)

        if (changed) {
            notifyItemChanged(list.lastIndex)
            return
        }
        notifyItemInserted(list.lastIndex)
    }


    private fun handleSingleRow(viewHolder: RecyclerView.ViewHolder, position: Int) {

        if (layoutManager is StaggeredGridLayoutManager) {
            handleSingleRowStaggered(viewHolder)
        }

        if (layoutManager is GridLayoutManager) {
            handleSingleRowGrid(position)
        }

    }

    // When we use the grid, change span size because show progressView single row
    private fun handleSingleRowGrid(index: Int) {
        if (layoutManager != null && layoutManager is GridLayoutManager) {
            val gridLayoutManager = layoutManager as GridLayoutManager
            val spanCount = gridLayoutManager.spanCount
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if ((lastItemItemState == ItemState.LOADING || lastItemItemState == ItemState.FAILED) && position == index && list[index] == null) {
                        spanCount
                    } else 1
                }
            }
        }
    }

    // When we use the staggered, change span size because show progressView single row
    private fun handleSingleRowStaggered(viewHolder: RecyclerView.ViewHolder) {
        val layoutParams = viewHolder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = lastItemItemState == ItemState.LOADING
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutManager = recyclerView.layoutManager
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        layoutManager = null
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class FailedViewHolder(view: View) : RecyclerView.ViewHolder(view)

}