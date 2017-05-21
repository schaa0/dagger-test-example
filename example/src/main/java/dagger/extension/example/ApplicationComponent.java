package dagger.extension.example;

import android.content.Context;

import dagger.AllowStubGeneration;
import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {AppModule.class})
public interface ApplicationComponent
{
    void inject(ExampleApplication app);
    ActivityComponent.Builder activityBuilder();
    @Component.Builder
    interface Builder {
        @AllowStubGeneration @BindsInstance Builder context(Context context);
        Builder appModule(AppModule module);
        ApplicationComponent build();
    }
}
