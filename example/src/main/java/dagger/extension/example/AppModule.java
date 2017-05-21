package dagger.extension.example;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.AllowStubGeneration;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;
import dagger.multibindings.StringKey;

@Module
public class AppModule
{

    @AllowStubGeneration
    @Provides
    public SharedPreferences sharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @StringKey("A")
    @IntoMap
    public String firstElement() {
        return "A";
    }

    @Provides
    @StringKey("B")
    @IntoMap
    @AllowStubGeneration
    public String secondElement() {
        return "B";
    }
}
