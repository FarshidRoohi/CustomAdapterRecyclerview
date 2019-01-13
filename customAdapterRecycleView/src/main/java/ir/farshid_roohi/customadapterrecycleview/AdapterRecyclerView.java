package ir.farshid_roohi.customadapterrecycleview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.farshid_roohi.customadapterrecycleview.listener.IProgressLayout;
import ir.farshid_roohi.customadapterrecycleview.listener.OnLoadMoreListener;
import ir.farshid_roohi.customadapterrecycleview.viewHolder.ItemViewHolder;
import ir.farshid_roohi.customadapterrecycleview.viewHolder.ProgressViewHolder;

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerView | Copyrights 1/1/19.
 */
public abstract class AdapterRecyclerView<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IProgressLayout {

    private static int ITEM_VIEW     = 0;
    private static int ITEM_PROGRESS = 1;

    private List<T>            list = new ArrayList<>();
    private Context            context;
    private OnLoadMoreListener onLoadMoreListener;

    private boolean isLoading;
    private int     totalItemCount, visibleTotalCount, lastVisibleItem;


    @LayoutRes
    public abstract int getItemLayout(int viewType);

    public abstract void onBindView(ViewDataBinding binding, ItemViewHolder viewHolder, int position, int viewType, T element);

    public AdapterRecyclerView() {
    }

    public AdapterRecyclerView(RecyclerView recyclerView) {
        this.initRecyclerViewListener(recyclerView);
    }

    public AdapterRecyclerView(List<T> items) {
        this.list = items;
    }

    @Override
    public int onProgressLayout() {
        return R.layout.proggress_view;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        this.context = viewGroup.getContext();


        if (viewType == ITEM_VIEW) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    getItemLayout(viewType), viewGroup, false);

            return new ItemViewHolder(binding);
        }

        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                onProgressLayout(), viewGroup, false);

        return new ProgressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {

            onBindView(((ItemViewHolder) viewHolder).binding, ((ItemViewHolder) viewHolder), position, viewHolder.getItemViewType(), getItem(position));
        }
    }

    @Override
    public int getItemCount() {
        return this.list == null ? 0 : this.list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? ITEM_PROGRESS : ITEM_VIEW;
    }

    public void remove(int position) {
        if (this.list.isEmpty()) {
            return;
        }
        this.list.remove(position);
        this.notifyItemRemoved(position);
    }

    public void remove(T... item) {
        if (this.list.isEmpty()) {
            return;
        }
        this.list.removeAll(new ArrayList<>(Arrays.asList(item)));
        this.notifyDataSetChanged();
    }

    public void removeAll() {
        if (this.list.isEmpty()) {
            return;
        }
        this.list.clear();
        this.notifyDataSetChanged();
    }

    public void addItem(T... item) {
        this.hiddenLoading();
        List<T> items = new ArrayList<>(Arrays.asList(item));
        this.list.addAll(items);
        notifyItemRangeChanged(getItemCount() - items.size(), getItemCount());
    }

    public void addItems(List<T> items) {
        this.hiddenLoading();
        this.list.addAll(items);
        this.notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void endLessScrolled(RecyclerView recyclerView) {
        initRecyclerViewListener(recyclerView);
    }

    public Context getContext() {
        return context;
    }

    public List<T> getItems() {
        return this.list;
    }


    private void initRecyclerViewListener(RecyclerView recyclerView) {

        if (recyclerView == null) return;

        if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) return;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (onLoadMoreListener == null) return;

                totalItemCount = linearLayoutManager.getItemCount();
                visibleTotalCount = linearLayoutManager.getChildCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (totalItemCount <= visibleTotalCount) {
                    return;
                }

                if (!isLoading && (lastVisibleItem + visibleTotalCount) >= totalItemCount) {
                    onLoadMoreListener.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    @Nullable
    public T getItem(int position) {
        if (list.isEmpty()) return null;
        return list.get(position);
    }

    public void showLoading() {
        this.list.add(null);
        this.notifyItemInserted(list.size() - 1);
        isLoading = true;

    }

    public void hiddenLoading() {
        if (getItemCount() != 0 && isLoading) {
            this.list.remove(getItemCount() - 1);
            this.notifyItemRemoved(getItemCount());
        }
        isLoading = false;
    }
}