package dagger.extension.example.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.AllowStubGeneration;
import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.di.ActivityComponentBuilder;
import dagger.di.ComponentBuilder;
import dagger.extension.example.R;
import dagger.extension.example.service.WeatherApi;
import dagger.extension.example.view.forecast.ForecastActivity;
import dagger.extension.example.view.main.MainActivity;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Component(modules = {ComponentSingleton.ModuleSingleton.class})
@Singleton
public interface ComponentSingleton {
    void inject(WeatherApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);
        ComponentSingleton build();
    }

    @Module(subcomponents = {ComponentMainActivity.class, ComponentForecastActivity.class})
    abstract class ModuleBuilder {
        @Binds
        @IntoMap
        @ClassKey(MainActivity.class)
        public abstract ActivityComponentBuilder mainActivity(ComponentMainActivity.Builder impl);

        @Binds
        @IntoMap
        @ClassKey(ForecastActivity.class)
        public abstract ActivityComponentBuilder forecastActivity(ComponentForecastActivity.Builder impl);
    }

    @Module(includes = ModuleBuilder.class)
    abstract class ModuleSingleton {

        @Provides
        public static ComponentBuilder<ActivityComponentBuilder> componentBuilder(Map<Class<?>, Provider<ActivityComponentBuilder>> builderMap) {
            return new ComponentBuilder<>(builderMap);
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
        @Singleton
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
