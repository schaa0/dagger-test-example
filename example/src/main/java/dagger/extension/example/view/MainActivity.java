package dagger.extension.example.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import java.io.IOException;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.extension.example.R;
import dagger.extension.example.di.ComponentActivity;
import dagger.extension.example.di.ComponentTodayPresenter;
import dagger.extension.example.di.ComponentTomorrowPresenter;
import dagger.extension.example.di.WeatherApplication;
import dagger.extension.example.event.PermissionEvent;
import dagger.extension.example.service.PermissionManager;
import dagger.extension.example.service.ViewPagerFragmentFactory;
import injector.Injector;

public class MainActivity extends AppCompatActivity
{

    @BindView(R.id.container) ViewPager mViewPager;
    @BindView(R.id.tab_layout1) TabLayout tabLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject SectionsPagerAdapter mSectionsPagerAdapter;
    @Inject PermissionManager permissionManager;

    @Inject ComponentTodayPresenter.Builder todayPresenterBuilder;
    @Inject ComponentTomorrowPresenter.Builder tomorrowPresenterBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        app().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setupWithViewPager(mViewPager, true);

    }

    protected WeatherApplication app() {
        return (WeatherApplication) getApplication();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length > 0 && grantResults.length > 0)
        {
            permissionManager.dispatchEvent(new PermissionEvent(requestCode, permissions, grantResults));
        }
    }

    public void onLoadingWeatherFailed(IOException e){
        Toast.makeText(this, String.format("Loading weather failed: %s", e.toString()), Toast.LENGTH_SHORT).show();
    }

    public void inject(TodayWeatherFragment fragment) {
        Injector.get(app()).componentTodayPresenter(todayPresenterBuilder).inject(fragment);
    }

    public void inject(TomorrowWeatherFragment fragment) {
        Injector.get(app()).componentTomorrowPresenter(tomorrowPresenterBuilder).inject(fragment);
    }

    public static class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        private ViewPagerFragmentFactory viewPagerFragmentFactory;

        @Inject
        public SectionsPagerAdapter(FragmentManager fm, ViewPagerFragmentFactory viewPagerFragmentFactory)
        {
            super(fm);
            this.viewPagerFragmentFactory = viewPagerFragmentFactory;
        }

        @Override
        public Fragment getItem(int position)
        {
            return viewPagerFragmentFactory.getItem(position);
        }

        @Override
        public int getCount()
        {
            return viewPagerFragmentFactory.getCount();
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return viewPagerFragmentFactory.getPageTitle(position);
        }
    }
}
