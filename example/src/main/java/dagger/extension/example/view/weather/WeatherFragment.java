package dagger.extension.example.view.weather;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import dagger.extension.example.R;
import dagger.extension.example.databinding.LayoutWeatherFragmentBinding;
import dagger.extension.example.service.PermissionService;

public abstract class WeatherFragment extends Fragment
{

    private static final String VIEW_MODEL_STATE = "ViewModelState";

    @Inject
    PermissionService permissionService;

    WeatherViewModel.WeatherViewModelState state;
    private LayoutWeatherFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = savedInstanceState != null ?
                savedInstanceState.getParcelable(VIEW_MODEL_STATE) :
                new WeatherViewModel.WeatherViewModelState();
        AndroidSupportInjection.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_weather_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setVm(viewModel());
        binding.setFm(this);
        binding.executePendingBindings();
        viewModel().onViewAttached();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(VIEW_MODEL_STATE, this.viewModel().saveState());
    }

    @Override
    public void onDestroy() {
        viewModel().onViewDetached();
        super.onDestroy();
    }

    public void onWeatherIconClicked(View v) { }

    protected abstract WeatherViewModel viewModel();

}
