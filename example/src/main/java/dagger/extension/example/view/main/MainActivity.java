package dagger.extension.example.view.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.di.ComponentBuilder;
import dagger.di.FragmentComponentBuilder;
import dagger.extension.example.R;
import dagger.extension.example.di.ComponentErrorDialogFragment;
import dagger.extension.example.di.ComponentTodayFragment;
import dagger.extension.example.di.ComponentTomorrowFragment;
import dagger.extension.example.di.WeatherApplication;
import dagger.extension.example.service.PermissionResult;
import dagger.extension.example.service.PermissionManager;
import dagger.extension.example.service.ViewPagerFragmentFactory;
import dagger.extension.example.view.weather.TodayWeatherFragment;
import dagger.extension.example.view.weather.TomorrowWeatherFragment;
import dagger.extension.example.view.error.ErrorDialogFragment;
import injector.Injector;

public class MainActivity extends AppCompatActivity
{

    @BindView(R.id.container) ViewPager mViewPager;
    @BindView(R.id.tab_layout1) TabLayout tabLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject SectionsPagerAdapter mSectionsPagerAdapter;
    @Inject PermissionManager permissionManager;

    @Inject ComponentBuilder<FragmentComponentBuilder> componentBuilder;

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
            permissionManager.dispatchEvent(new PermissionResult(requestCode, permissions, grantResults));
        }
    }

    public ComponentErrorDialogFragment inject(ErrorDialogFragment errorDialogFragment, String message, String title) {
        return componentBuilder.getComponent(ComponentErrorDialogFragment.Builder.class, errorDialogFragment, builder -> {
            return Injector.get(app()).componentErrorDialogFragment(builder, message, title);
        });
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

    public ComponentTodayFragment inject(TodayWeatherFragment fragment) {
        return componentBuilder.getComponent(ComponentTodayFragment.Builder.class, fragment, builder -> {
            return Injector.get(getApplication()).componentTodayFragment(builder);
        });
    }

    public ComponentTomorrowFragment inject(TomorrowWeatherFragment fragment) {
        return componentBuilder.getComponent(ComponentTomorrowFragment.Builder.class, fragment, builder -> {
            return Injector.get(getApplication()).componentTomorrowFragment(builder);
        });
    }
}
