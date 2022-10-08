package io.github.customadapterrecyclerview.multiViewType

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.farshidroohi.extensions.onItemClickListener
import io.github.farshidroohi.extensions.onLoadMoreListener
import ir.farshid_roohi.customadapterrecyclerview.R

class MultiViewTypeActivity : AppCompatActivity() {

    private val myAdapter = MultiViewTypeAdapter()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_view_type)

        recyclerView = findViewById(R.id.recyclerView)

        // onClick Error Button
        myAdapter.onRetryClicked = {
            handleOnRetryData()
        }

        // set Data in List
        myAdapter.loadedState(fakeItems)

        recyclerView.layoutManager = GridLayoutManager(this@MultiViewTypeActivity, 2)
        recyclerView.adapter = myAdapter

        // set onLoad More Listener for Pagination
        recyclerView.onLoadMoreListener {
            handleOnLoadMore()
        }


        // Set onClick Item Listener
        recyclerView.onItemClickListener(
            onClickItem = { position ->
                val item = myAdapter.getItem(position)
                showToast("onClickItem :  ${item?.name}")
            }, onLongClickItem = { position ->
                val item = myAdapter.getItem(position)
                showToast("onLongClickItem :  ${item?.name}")

            })
    }

    private fun handleOnLoadMore() {
        if (myAdapter.mustLoad) {
            myAdapter.loadingState()
            // failed state
            recyclerView.postDelayed({
                myAdapter.failedState()
            }, 2000)
        }
    }

    private fun handleOnRetryData() {
        myAdapter.loadingState()
        // fake request or load other items...
        recyclerView.postDelayed({
            myAdapter.loadedState(fakeItems)
        }, 2000)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val fakeItems: List<Model>
        get() {
            val items: MutableList<Model> = ArrayList()
            for (i in 0..6) {
                items.add(Model(name = "Item", MultiViewTypeAdapter.ITEM_ONE))
            }
            for (i in 0..3) {
                items.add(Model(name = "Item", MultiViewTypeAdapter.ITEM_TWO))
            }
            for (i in 0..5) {
                items.add(Model(name = "Item", MultiViewTypeAdapter.ITEM_THREE))
            }
            for (i in 0..4) {
                items.add(Model(name = "Item", MultiViewTypeAdapter.ITEM_FOUR))
            }
            return items
        }
}

data class Model(val name: String, val type: Int)