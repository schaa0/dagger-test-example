package dagger.extension.example.di;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ActivityModule<T extends AppCompatActivity> {

    @Binds
    public abstract AppCompatActivity bindToAppCompatActivity(T activity);

    @Provides
    public static FragmentManager fragmentManager(AppCompatActivity activity) {
        return activity.getSupportFragmentManager();
    }

    @Provides
    public static Intent provideIntent(AppCompatActivity activity) {
        return activity.getIntent();
    }
}
