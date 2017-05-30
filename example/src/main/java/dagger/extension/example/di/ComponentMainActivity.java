package dagger.extension.example.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.BindsInstance;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.di.ActivityComponent;
import dagger.di.ActivityComponentBuilder;
import dagger.extension.example.scope.ActivityScope;
import dagger.extension.example.service.ViewPagerFragmentFactory;
import dagger.extension.example.view.main.MainActivity;

@ActivityScope
@Subcomponent(
        modules = ComponentMainActivity.MainActivityModule.class
)
public interface ComponentMainActivity extends ActivityComponent<MainActivity> {

    @Subcomponent.Builder
    interface Builder extends ActivityComponentBuilder<ComponentMainActivity> {
        @BindsInstance Builder activity(AppCompatActivity activity);
    }

    @Module(includes = {ActivityModule.class, FragmentBindingsModule.class})
    abstract class MainActivityModule {

        @Provides
        @ActivityScope
        public static ViewPagerFragmentFactory fragmentFactory(Context context) {
            return new ViewPagerFragmentFactory(context);
        }

    }

}
