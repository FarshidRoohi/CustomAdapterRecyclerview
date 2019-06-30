package ir.farshid_roohi.customadapterrecycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import ir.farshid_roohi.customadapterrecycleview.listener.IProgressLayout;
import ir.farshid_roohi.customadapterrecycleview.listener.OnClickItemListener;
import ir.farshid_roohi.customadapterrecycleview.listener.OnLoadMoreListener;
import ir.farshid_roohi.customadapterrecycleview.listener.RecyclerTouchListener;
import ir.farshid_roohi.customadapterrecycleview.viewHolder.ItemViewHolder;
import ir.farshid_roohi.customadapterrecycleview.viewHolder.ProgressViewHolder;

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerView | Copyrights 1/1/19.
 */
public abstract class AdapterRecyclerView<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IProgressLayout {

    private static int ITEM_VIEW     = 0;
    private static int ITEM_PROGRESS = 1;

    private List<T>                    list = new ArrayList<>();
    private Context                    context;
    private OnLoadMoreListener         onLoadMoreListener;
    private RecyclerView.LayoutManager layoutManager;

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ItemViewHolder) {
            onBindView(((ItemViewHolder) viewHolder).binding, ((ItemViewHolder) viewHolder), position, viewHolder.getItemViewType(), getItem(position));
        } else if (viewHolder instanceof ProgressViewHolder) {
            // When we use the staggered grid layout manager, change span size because show progressView single row
            if (layoutManager != null && layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(isLoading);
            }
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
        this.layoutManager = recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (onLoadMoreListener == null) return;
                if (layoutManager == null) return;

                totalItemCount = layoutManager.getItemCount();
                visibleTotalCount = layoutManager.getChildCount();


                if (layoutManager instanceof GridLayoutManager) {
                    lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof LinearLayoutManager) {
                    lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;

                    int   spanCount     = staggeredGridLayoutManager.getSpanCount();
                    int[] lastPositions = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(new int[spanCount]);
                    lastVisibleItem = Math.max(lastPositions[0], lastPositions[1]);
                }


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
        final int index = list.size() - 1;
        this.notifyItemInserted(index);
        this.isLoading = true;
        handledShowProgressViewRow(index);

    }

    // When we use the grid, change span size because show progressView single row
    private void handledShowProgressViewRow(final int index) {
        if (layoutManager != null && layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = ((GridLayoutManager) layoutManager);
            final int         spanCount         = gridLayoutManager.getSpanCount();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isLoading && position == index && list.get(index) == null) {
                        return spanCount;
                    }
                    return 1;
                }
            });
        }
    }

    public void hiddenLoading() {

        if (getItemCount() != 0 && isLoading) {
            this.list.remove(getItemCount() - 1);
            this.notifyItemRemoved(getItemCount());
        }
        isLoading = false;

        if (layoutManager != null && layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = ((GridLayoutManager) layoutManager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 1;
                }
            });
        }

    }


    public void setOnClickItemListener(RecyclerView recyclerView, final OnClickItemListener<T> listener) {

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.OnItemListenerRecyclerViewListener() {
            @Override
            public void onClick(int position) {
                listener.onClickItem(position, list.get(position));
            }

            @Override
            public void onLongClick(int position) {
                listener.onLongClickItem(position, list.get(position));
            }
        }));
    }

}