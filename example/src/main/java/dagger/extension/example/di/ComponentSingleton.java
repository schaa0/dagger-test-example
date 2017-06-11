package dagger.extension.example.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.AllowStubGeneration;
import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.extension.example.R;
import dagger.extension.example.service.WeatherApi;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Component(modules = {ComponentSingleton.ModuleSingleton.class, AndroidSupportInjectionModule.class})
@Singleton
public interface ComponentSingleton extends AndroidInjector<WeatherApplication>{
    void inject(WeatherApplication application);

    @Component.Builder
    public abstract class Builder extends AndroidInjector.Builder<WeatherApplication> {
        public abstract ComponentSingleton build();
    }

    @Module(includes = ActivityBindingsModule.class)
    abstract class ModuleSingleton {

        @Provides
        public static Context context(WeatherApplication application) {
            return (Context)application;
        }

        @Provides
        @Singleton
        public static LocationManager locationManager(Context context) {
            return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        @Provides
        @Singleton
        public static SharedPreferences sharedPreferences(Context context) {
            return PreferenceManager.getDefaultSharedPreferences(context);
        }

        @Provides
        @Named("apiKey")
        @Singleton
        @AllowStubGeneration
        public static String apiKey(Context context) {
            return context.getString(R.string.api_key);
        }

        @Provides
        @Named("endpointUrl")
        @Singleton
        @AllowStubGeneration
        public static String endpointUrl(Context context) {
            return context.getString(R.string.weather_endpoint_url);
        }

        @Provides
        @AllowStubGeneration
        public static WeatherApi weatherAPI(@Named("endpointUrl") String url) {
            return new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(url)
                    .client(new OkHttpClient.Builder()
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(5, TimeUnit.SECONDS)
                            .build())
                    .build()
                    .create(WeatherApi.class);
        }

        @Provides
        @AllowStubGeneration
        @Singleton
        public static RequestManager glide(Context context) {
            return Glide.with(context);
        }

        @Provides @Singleton
        public static Scheduler scheduler() {
            return Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

}
