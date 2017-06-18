package dagger.extension.example.view.main;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.AllowStubGeneration;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.extension.example.R;
import dagger.extension.example.di.ActivityModule;
import dagger.extension.example.di.FragmentBindingsModule;
import dagger.extension.example.view.main.MainActivity;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;

@Module(includes = {ActivityModule.class, FragmentBindingsModule.class})
public abstract class MainActivityModule {

    @Binds
    public abstract AppCompatActivity appCompatActivity(MainActivity activity);

    @Provides
    @IntKey(0)
    @IntoMap
    public static String today(Context context) {
        return context.getString(R.string.today);
    }

    @Provides
    @IntKey(1)
    @AllowStubGeneration
    @IntoMap
    public static String tomorrow(Context context) {
        return context.getString(R.string.tomorrow);
    }

    @Provides
    @IntKey(2)
    @IntoMap
    public static String search(Context context) {
        return context.getString(R.string.search);
    }
}
