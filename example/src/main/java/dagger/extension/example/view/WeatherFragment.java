package dagger.extension.example.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.extension.example.R;
import dagger.extension.example.databinding.LayoutWeatherFragmentBinding;
import dagger.extension.example.vm.WeatherViewModel;
import dagger.extension.example.service.PermissionManager;

public abstract class WeatherFragment extends Fragment
{

    @Inject PermissionManager permissionManager;
    private LayoutWeatherFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_weather_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setVm(getViewModel());
        binding.setFm(this);
        binding.executePendingBindings();
        getViewModel().onViewAttached();
    }

    @Override
    public void onDestroy() {
        getViewModel().onViewDetached();
        super.onDestroy();
    }

    public void onWeatherIconClicked(View v) {

    }
    protected abstract WeatherViewModel getViewModel();

}
