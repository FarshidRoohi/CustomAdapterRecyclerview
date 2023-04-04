package io.github.farshidroohi

import android.annotation.SuppressLint
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

abstract class AdapterRecyclerView<T>(
    @LayoutRes val itemViewLayout: Int,
    @LayoutRes val itemLoadingLayout: Int,
    @LayoutRes val itemFailedLayout: Int,
    @IdRes val retryButtonId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val ITEM_VIEW = 0x2328
        const val ITEM_LOADING = 0x2329
        const val ITEM_FAILED = 0x232A
    }

    val items: MutableList<T?> = arrayListOf()
    private var lastItemItemState: ItemState = ItemState.LOADED
    private var layoutManager: RecyclerView.LayoutManager? = null
    var mustLoad: Boolean = false
        private set
        get() = lastItemItemState == ItemState.LOADED
    var onRetryClicked: () -> Unit = {}


    abstract fun onBindView(
        viewHolder: RecyclerView.ViewHolder,
        position: Int,
        context: Context,
        element: T?
    )

    open fun onCreateViewHolder(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder? {
        return null
    }

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
            else -> onCreateViewHolder(inflater, viewGroup, viewType)!!
        }

    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        when (viewHolder) {

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
            else -> {
                onBindView(viewHolder, position, viewHolder.itemView.context, getItem(position))
            }

        }

    }


    override fun getItemCount(): Int {
        return items.size
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


    @SuppressLint("NotifyDataSetChanged")
    fun removeAll() {
        if (items.isEmpty()) {
            return
        }
        items.clear()
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        if (this.items.isEmpty() || position > this.items.lastIndex) {
            return
        }
        this.items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun remove(vararg item: T) {
        if (this.items.isEmpty()) {
            return
        }
        this.items.removeAll(item.toSet())
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T? {
        return if (items.isEmpty()) null else items[position]
    }


    @SuppressLint("NotifyDataSetChanged")
    fun loadedState(newItems: List<T?>?) {

        if (newItems == null) {
            return
        }

        var addedPosition = 0

        if (items.isNotEmpty()) {
            addedPosition = itemCount
            if (lastItemItemState != ItemState.LOADED) {
                items.removeAt(items.lastIndex)
            }
        }

        lastItemItemState = ItemState.LOADED
        items.addAll(newItems)

        if (addedPosition == 0) {
            notifyDataSetChanged()
            return
        }
        notifyItemInserted(addedPosition)

    }

    fun failedState() {

        if (lastItemItemState == ItemState.LOADING) {
            items.removeAt(items.lastIndex)
        }

        lastItemItemState = ItemState.FAILED
        items.add(null)

        notifyItemChanged(items.lastIndex)

    }


    fun loadingState() {

        var changed = false

        if (items.isNotEmpty() && lastItemItemState != ItemState.LOADED) {
            items.removeAt(items.lastIndex)
            changed = true
        }

        lastItemItemState = ItemState.LOADING
        items.add(null)

        if (changed) {
            notifyItemChanged(items.lastIndex)
            return
        }
        notifyItemInserted(items.lastIndex)
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
                    return if ((lastItemItemState == ItemState.LOADING || lastItemItemState == ItemState.FAILED) && position == index && items[index] == null) {
                        spanCount
                    } else 1
                }
            }
        }
    }

    // When we use the staggered, change span size because show progressView single row
    private fun handleSingleRowStaggered(viewHolder: RecyclerView.ViewHolder) {
        val layoutParams =
            viewHolder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
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