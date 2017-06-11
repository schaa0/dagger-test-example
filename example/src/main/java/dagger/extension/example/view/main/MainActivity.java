package dagger.extension.example.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.extension.example.R;
import dagger.extension.example.di.WeatherApplication;
import dagger.extension.example.service.PermissionResult;
import dagger.extension.example.service.PermissionManager;
import io.reactivex.Observable;

public class MainActivity extends DaggerAppCompatActivity
{

    @BindView(R.id.container) ViewPager mViewPager;
    @BindView(R.id.tab_layout1) TabLayout tabLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject SectionsPagerAdapter mSectionsPagerAdapter;
    @Inject PermissionManager permissionManager;

    @Inject MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setupWithViewPager(mViewPager, true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        Observable.<String>create(observableEmitter -> {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchView.clearFocus();
                    getWindow().getDecorView().requestFocus();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.length() > 2)
                    {
                        mViewPager.setCurrentItem(2, true);
                        observableEmitter.onNext(newText);
                    }
                    return false;
                }
            });
        }).debounce(1000, TimeUnit.MILLISECONDS)
          .subscribe(city -> mainViewModel.onSearchQuery(city));
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length > 0 && grantResults.length > 0)
        {
            permissionManager.dispatchEvent(new PermissionResult(requestCode, permissions, grantResults));
        }
    }
}
