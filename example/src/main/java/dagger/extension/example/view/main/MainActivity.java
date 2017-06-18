package dagger.extension.example.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.extension.example.R;
import dagger.extension.example.service.PermissionResult;
import dagger.extension.example.service.PermissionService;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends DaggerAppCompatActivity
{

    @BindView(R.id.container) ViewPager mViewPager;
    @BindView(R.id.tab_layout1) TabLayout tabLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject Scheduler scheduler;
    @Inject SectionsPagerAdapter mSectionsPagerAdapter;
    @Inject PermissionService permissionService;
    @Inject MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        long start = System.nanoTime();
        AndroidInjection.inject(this);
        long end = System.nanoTime() - start;
        Log.d(MainActivity.class.getName(), String.format("Average inject time: %d ms", Math.round(end / 1000.0 / 1000.0)));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager, true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        this.searchViewObservable(searchView)
            .distinctUntilChanged()
            .debounce(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(disposable -> mViewPager.setCurrentItem(SectionsPagerAdapter.POSITION_SEARCH, true))
            .doOnNext(s -> searchView.clearFocus())
            .observeOn(this.scheduler)
            .subscribe(query -> {
                mainViewModel.onSearchQuery(query);
            }, Throwable::printStackTrace);
        return true;
    }

    private Observable<String> searchViewObservable(SearchView searchView) {
        return Observable.create(emitter-> internalCreateSearchViewObservable(searchView, emitter));
    }

    private void internalCreateSearchViewObservable(final SearchView searchView, final ObservableEmitter<String> observableEmitter) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    observableEmitter.onNext(query);
                }
                searchView.clearFocus();
                getWindow().getDecorView().requestFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    observableEmitter.onNext(newText);
                }
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length > 0 && grantResults.length > 0)
        {
            permissionService.dispatchEvent(new PermissionResult(requestCode, permissions, grantResults));
        }
    }
}
