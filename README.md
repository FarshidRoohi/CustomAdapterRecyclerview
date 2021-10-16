# Custom Adapter RecyclerView

  ![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Custom%20Adapter%20RecyclerView-blue.svg?style=flat)](https://android-arsenal.com/details/1/7759)



Very simple use android recyclerView adapter and endlessScrolled in android support library recyclerView 

- Clean uses
- Ability add custom layout in progress pagination and default layout
- Support linear, Grid, StaggeredGrid LayoutManger for endless
- Manage Loading and Error Layout in The List Row

##### screenShot: 
 
 <img src="https://raw.githubusercontent.com/FarshidRoohi/CustomAdapterRecyclerview/master/art/custom_adapter.gif" alt="screen show" width="270px" height="450px">


 ###### gradle :   
  
```Gradle  
  implementation 'androidx.recyclerview:recyclerview:$VERSION'
  implementation 'io.github.farshidroohi:customAdapterRecycleView:2.0.3'
 ```  
 <hr>
 
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

    val myAdapter = MyAdapter()

           // onClick Error Button
           myAdapter.onRetryClicked = {
               myAdapter.loadingState()
               // fake request or load other items...
               recyclerView.postDelayed({
                   myAdapter.loadedState(tempItems)
               }, 2000)
           }

           // set Data in List
           myAdapter.loadedState(tempItems)

           recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 2)
           recyclerView.adapter = myAdapter


           // set onLoad More Listener for Pagination
           recyclerView.onLoadMoreListener {
               if (myAdapter.mustLoad) {
                   myAdapter.loadingState()
                   // failed state
                   recyclerView.postDelayed({
                       myAdapter.failedState()
                   }, 2000)
               }
           }


           // Set onClick Item Listener
           recyclerView.onItemClickListener({ position ->
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

    
    // Generate Fake Data For List
    private val tempItems: List<String>
      get() {
        val items: MutableList<String> = ArrayList()
        for (i in 0..21) {
          items.add("item ")
        }
        return items
      }
```
