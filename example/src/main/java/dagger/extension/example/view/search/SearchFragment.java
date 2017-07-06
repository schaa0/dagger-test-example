package dagger.extension.example.view.search;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import dagger.extension.example.R;
import dagger.extension.example.databinding.LayoutSearchBinding;
import io.reactivex.disposables.CompositeDisposable;

public class SearchFragment extends DaggerFragment {
    
    @Inject SearchViewModel searchViewModel;
    @Inject CompositeDisposable compositeDisposable;

    private LayoutSearchBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_search, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setVm(searchViewModel);
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        );
        compositeDisposable.add(
            searchViewModel.onNewAdapterAvailable().subscribe(searchAdapter -> {
                binding.recyclerView.setAdapter(searchAdapter);
            })
        );
        searchViewModel.onAttach();
    }

    @Override
    public void onDestroy() {
        searchViewModel.onDetach();
        compositeDisposable.dispose();
        binding.unbind();
        super.onDestroy();
    }
}
