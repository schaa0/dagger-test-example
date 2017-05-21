package dagger.extension.example;

import android.content.Context;

import javax.inject.Inject;

public class ExampleApplication extends DaggerApplication
{
    private ApplicationComponent component;

    @Inject
    Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.component = getInjector().applicationComponent(this);
        this.component.inject(this);
    }

    public ApplicationComponent getApplicationComponent()
    {
        return this.component;
    }

    public Context getContext() {
        return this.context;
    }
}
