package dagger.extension.example.view.search;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.extension.example.R;
import dagger.extension.example.databinding.LayoutSearchItemBinding;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<SearchItemViewModel> weatherInfo = new ArrayList<>();

    @Inject
    public SearchAdapter(List<SearchItemViewModel> weatherInfo) {
        this.weatherInfo.addAll(weatherInfo);
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutSearchItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_search_item, parent, false);
        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.binding.setVm(weatherInfo.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return this.weatherInfo.size();
    }

    public void updateWeatherInfos(List<SearchItemViewModel> searchItemViewModels) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(searchItemViewModels));
        this.weatherInfo.clear();
        this.weatherInfo.addAll(searchItemViewModels);
        diffResult.dispatchUpdatesTo(this);
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {

        public final LayoutSearchItemBinding binding;

        public SearchViewHolder(LayoutSearchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class DiffCallback extends DiffUtil.Callback {
        private final List<SearchItemViewModel> searchItemViewModels;

        public DiffCallback(List<SearchItemViewModel> searchItemViewModels) {
            this.searchItemViewModels = searchItemViewModels;
        }

        @Override
        public int getOldListSize() {
            return weatherInfo.size();
        }

        @Override
        public int getNewListSize() {
            return searchItemViewModels.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return weatherInfo.get(oldItemPosition) == searchItemViewModels.get(newItemPosition);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return weatherInfo.get(oldItemPosition).equals(searchItemViewModels.get(newItemPosition));
        }
    }
}
