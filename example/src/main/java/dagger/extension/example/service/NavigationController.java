package dagger.extension.example.service;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.extension.example.di.qualifier.ActivityScope;
import dagger.extension.example.di.ComponentErrorDialogFragment;
import dagger.extension.example.view.error.ErrorDialogFragment;
import dagger.extension.example.view.forecast.ForecastActivity;

@ActivityScope
public class NavigationController {

    private final AppCompatActivity activity;
    private final FragmentManager fm;
    private final ComponentErrorDialogFragment.Builder errorBuilder;

    @Inject
    public NavigationController(AppCompatActivity activity, FragmentManager fm, ComponentErrorDialogFragment.Builder errorBuilder) {
        this.activity = activity;
        this.fm = fm;
        this.errorBuilder = errorBuilder;
    }

    public void showErrorIfNotAlreadyShowing(String title, String message) {
        String tag = String.format("%s::%s", title, message);
        if (fm.findFragmentByTag(tag) == null)
        {
            ErrorDialogFragment dialog = ErrorDialogFragment.newInstance(title, message);
            dialog.show(fm, tag);
            fm.executePendingTransactions();
        }
    }

    public void toForecastActivity(String item) {
        Intent intent = new Intent(activity, ForecastActivity.class);
        intent.putExtra(ForecastActivity.KEY_FORECAST, item);
        activity.startActivity(intent);
    }

}
