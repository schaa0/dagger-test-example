package dagger.extension.example.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.AllowStubGeneration;
import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.extension.example.R;
import dagger.extension.example.service.WeatherApi;
import dagger.extension.example.view.MainActivity;
import injector.Injector;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Component(modules = {ComponentSingleton.ModuleSingleton.class})
@Singleton
public interface ComponentSingleton
{
    void inject(WeatherApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance Builder context(Context context);
        ComponentSingleton build();
    }

    @Module(subcomponents = {ComponentActivity.class, ComponentForecastPresenter.class})
    abstract class ModuleSingleton
    {

        @Provides
        @Singleton
        public static LocationManager locationManager(Context context)
        {
            return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        @Provides
        @Singleton
        public static Application application(Context context) {
            return (Application) context;
        }

        @Provides
        @Singleton
        public static SharedPreferences sharedPreferences(Context context)
        {
            return PreferenceManager.getDefaultSharedPreferences(context);
        }

        @Provides
        @Named("apiKey")
        @AllowStubGeneration
        @Singleton
        public static String apiKey(Context context) {
            return context.getString(R.string.api_key);
        }

        @Provides
        @AllowStubGeneration
        @Singleton
        public static WeatherApi weatherAPI() {
            return new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("http://api.openweathermap.org")
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
        public static RequestManager glide(Context context){
            return Glide.with(context);
        }

    }


}
