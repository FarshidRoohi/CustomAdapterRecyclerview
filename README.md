# Custom Adapter RecyclerView

[ ![Download](https://api.bintray.com/packages/farshidroohi/CustomAdapterRecyclerview/CustomAdapterRecyclerview/images/download.svg?version=1.0.5) ](https://bintray.com/farshidroohi/CustomAdapterRecyclerview/CustomAdapterRecyclerview/1.0.5/link)
  ![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Custom%20Adapter%20RecyclerView-blue.svg?style=flat)](https://android-arsenal.com/details/1/7759)



Very simple use android recyclerView adapter and endlessScrolled in android support library recyclerView 

- Clean uses
- Ability add custom layout in progress pagination and default layout
- Support linear, Grid, StaggeredGrid LayoutManger for endless
- Manage Proggress and Error Layout in List

##### screenShot: 
 
 <img src="https://raw.githubusercontent.com/FarshidRoohi/CustomAdapterRecyclerview/master/art/custom_adapter.gif" alt="screen show" width="270px" height="450px">


 ###### gradle :   
  
```Gradle  
  implementation 'androidx.recyclerview:recyclerview:1.1.0'
  implementation 'ir.farshid_roohi:customAdapterRecycleView:2.0.1'
 ```  
 <hr>
 
> - Your project should support **DataBinding** 

## Example

#### Sample Adapter 

```Kotiln

class MyAdapter : AdapterRecyclerView<String?>(R.layout.my_item, R.layout.progress_view, R.layout.item_error,R.id.btnTrayAgain) {

    override fun onBindView(viewHolder: ItemViewHolder, position: Int, context: Context, element: String?) {
        val itemView = viewHolder.itemView
        itemView.txt_title.setText("$element $position")
    }

}
```

```Kotlin

     private lateinit var myAdapter: MyAdapter

     private val tempItems: List<String>
            get() {
                val items: MutableList<String> = ArrayList()
                for (i in 0..21) {
                    items.add("item ")
                }
                return items
            }
    
      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
    
            myAdapter = MyAdapter().also { adapter ->
                adapter.onRetryClicked = {
                    adapter.loadingState()
                    // fake request or load other items...
                    recyclerView.postDelayed({
                        adapter.loadedState(tempItems)
                    }, 2000)
                }
            }
            myAdapter.loadedState(tempItems)
    
            initRecyclerView()
    
       }
    
     private fun initRecyclerView() {
    
            recyclerView.apply {
                layoutManager = GridLayoutManager(this@MainActivity, 2)
                adapter = myAdapter
    
                // handled endless recyclerView
                onLoadMoreListener {
                    if (myAdapter.mustLoad) {
                        myAdapter.loadingState()
                        // failed state
                        recyclerView.postDelayed({
                            myAdapter.failedState()
                        }, 2000)
                    }
                }
    
                // handled click item in recyclerView
                onItemClickListener({ position ->
                    if (position == myAdapter.itemCount - 1 && !myAdapter.mustLoad) {
                        return@onItemClickListener
                    }
                    Toast.makeText(applicationContext, "item click :  ${myAdapter.getItem(position)}$position", Toast.LENGTH_SHORT).show()
                }, { position ->
                    if (position == myAdapter.itemCount - 1 && !myAdapter.mustLoad) {
                        return@onItemClickListener
                    }
                    Toast.makeText(applicationContext, "item long click :  ${myAdapter.getItem(position)}$position", Toast.LENGTH_SHORT).show()
                })
    
            }
        }
```
