package io.github.farshidroohi.extensions

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.github.farshidroohi.listener.RecyclerTouchListener
import io.github.farshidroohi.listener.RecyclerTouchListener.OnItemListenerRecyclerViewListener
import kotlin.math.min

fun RecyclerView.onLoadMoreListener(extraCount: Int = 0, onLoadMore: () -> Unit) {

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (dy < 0) return

            if (layoutManager == null) return

            val totalItemCount = layoutManager!!.itemCount

            val visibleTotalCount = layoutManager!!.childCount
            val firstVisibleItem = when (layoutManager) {

                is GridLayoutManager -> {
                    (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                }

                is LinearLayoutManager -> {
                    (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                }

                is StaggeredGridLayoutManager -> {
                    val staggeredGridLayoutManager =
                            layoutManager as StaggeredGridLayoutManager
                    val spanCount = staggeredGridLayoutManager.spanCount
                    val lastPositions =
                            staggeredGridLayoutManager.findFirstVisibleItemPositions(IntArray(spanCount))
                    min(lastPositions[0], lastPositions[1])
                }

                else -> 0

            }

            if (totalItemCount <= visibleTotalCount) {
                return
            }

            if (firstVisibleItem + visibleTotalCount + extraCount >= totalItemCount) {
                recyclerView.post { // Prevent <<Inconsistency detected. Invalid item position... RecyclerView>> Bug
                    onLoadMore.invoke()
                }
            }

        }

    })

}

fun RecyclerView.onItemClickListener(onClickItem: (position: Int) -> Unit, onLongClickItem: (position: Int) -> Unit) {

    this.addOnItemTouchListener(RecyclerTouchListener(context, this, object : OnItemListenerRecyclerViewListener {
        override fun onClick(position: Int) {
            onClickItem(position)
        }

        override fun onLongClick(position: Int) {
            onLongClickItem(position)
        }
    }))

}