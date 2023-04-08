package io.github.customadapterrecyclerview.singleViewType

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.farshidroohi.extensions.onItemClickListener
import io.github.farshidroohi.extensions.onLoadMoreListener
import ir.farshid_roohi.customadapterrecyclerview.R
import ir.farshid_roohi.customadapterrecyclerview.databinding.ActivitySingleViewTypeBinding

class SingleViewTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingleViewTypeBinding
    private lateinit var recyclerView: RecyclerView
    private val singleViewTypeAdapter = SingleViewTypeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleViewTypeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = findViewById(R.id.recyclerView)

        // onClick Error Button
        singleViewTypeAdapter.onRetryClicked = {
            handleOnRetryData()
        }

        // set Data in List
        singleViewTypeAdapter.loadedState(fakeItems)

        recyclerView.layoutManager = GridLayoutManager(this@SingleViewTypeActivity, 2)
        recyclerView.adapter = singleViewTypeAdapter


        // set onLoad More Listener for Pagination
        recyclerView.onLoadMoreListener {
            handleOnLoadMore()
        }

        // Set onClick Item Listener
        recyclerView.onItemClickListener(
            onClickItem = { position ->
                val item = singleViewTypeAdapter.getItem(position)
                showToast("onClickItem :  $item")
            }, onLongClickItem = { position ->
                val item = singleViewTypeAdapter.getItem(position)
                showToast("onLongClickItem :  $item")
            })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleOnLoadMore() {
        if (singleViewTypeAdapter.mustLoad) {
            singleViewTypeAdapter.loadingState()
            // failed state
            recyclerView.postDelayed({
                singleViewTypeAdapter.failedState()
            }, 2000)
        }
    }

    private fun handleOnRetryData() {
        singleViewTypeAdapter.loadingState()
        // fake request or load other items...
        recyclerView.postDelayed({
            singleViewTypeAdapter.loadedState(fakeItems)
        }, 2000)
    }

    // Generate Fake Data For List
    private val fakeItems: List<String>
        get() {
            val items: MutableList<String> = ArrayList()
            for (i in 0..31) {
                items.add("item ")
            }
            return items
        }
}