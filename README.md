# Custom Adapter RecyclerView

[ ![Download](https://api.bintray.com/packages/farshidroohi/CustomAdapterRecyclerview/CustomAdapterRecyclerview/images/download.svg?version=0.2) ](https://bintray.com/farshidroohi/CustomAdapterRecyclerview/CustomAdapterRecyclerview/0.2/link)

simple use android recyclerView adapter and endlessScrolled in android support library recyclerView 

- clean uses
- ability add custom layout in progress pagination and default layout

 ###### via gradle:   
  
```Gradle  
  implementation 'com.android.support:recyclerview-v7:28.0.0'
  implementation 'ir.farshid_roohi:customAdapterRecycleView:0.2'
 ```  
 <hr>
 
> - Your project should support **DataBinding** 

# How to use DataBinding on your project

```Gradle
  
android {  
 dataBinding {  
	  enabled = true  
	  }
	...
  defaultConfig {  
	...
  }  
  buildTypes {  
  ...
 }  
```
for more information [It](https://developer.android.com/topic/libraries/data-binding)


## Example

#### Sample Adapter 

```Java

public class MyAdapter extends AdapterRecyclerView<String> {

    @Override
    public int getItemLayout(int viewType) {
        return R.layout.my_item;
    }

    // optional method override layout progress custom
    @Override
    public int onProgressLayout() {
        return R.layout.my_custom_progress_item;
    }

    @Override
    public void onBindView(ViewDataBinding viewDataBinding, int position, int viewType, String element) {
        MyItemBinding itemBinding = (MyItemBinding) viewDataBinding;
        itemBinding.txtTitle.setText(element);
    }
}
```

```Java
 RecyclerView recyclerView = findViewById(R.id.recycler_view);

        final MyAdapter adapter = new MyAdapter();
        adapter.endLessScrolled(recyclerView);
        adapter.addItems(getTempItems());
        adapter.endLessScrolled(recyclerView);

        recyclerView.setAdapter(adapter);


        // handled click item in recyclerView
        adapter.setOnClickItemListener(recyclerView, new OnClickItemListener<String>() {
            @Override
            public void onClickItem(int position, String element) {
                Toast.makeText(getApplicationContext(), "item click : " + element, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickItem(int position, String element) {
                Toast.makeText(getApplicationContext(), "item long click : " + element, Toast.LENGTH_SHORT).show();

            }
        });


        // handled endless recyclerView
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.showLoading();

                // request or load other items...
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addItems(getTempItems());
                    }
                }, 2500);
            }
        });
        
        
        
 // get temp list item
    public List<String> getTempItems() {
        List<String> items = new ArrayList<>();

        for (int i = 0; i <= 15; i++) {
            items.add("item ");
        }

        return items;
    }
```
 ##### screenShot: 
 
 <img src="https://raw.githubusercontent.com/FarshidRoohi/CustomAdapterRecyclerview/master/art/img.png" alt="screen show" width="270px" height="500px">
