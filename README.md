# Custom Adapter RecyclerView

 ###### via gradle:   
  
```Gradle  
 implementation 'ir.farshid_roohi:customAdapterRecycleView:0.1'
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
  
final MyAdapter adapter      = new MyAdapter();  
adapter.endLessScrolled(recyclerView);  
adapter.addItems(getTempItems());  
recyclerView.setAdapter(adapter);  
  
adapter.endLessScrolled(recyclerView);  
  
adapter.setOnLoadMoreListener(new OnLoadMoreListener() {  
  @Override  
  public void onLoadMore() {  
  
  adapter.showLoading();  
  
  // request or load other items...  
  new Handler().postDelayed(new Runnable() {  
  @Override  
  public void run() {  
  adapter.addItems(getTempItems());  
 } }, 3000);  
 }});
int countItemTemp = 0;  
  
public List<String> getTempItems() {  
  List<String> items = new ArrayList<>();  
  
  for (int i = countItemTemp; i <= 15; i++) {  
  items.add("item #" + new Random().nextInt(1000));  
 }  
  return items;  
}
```