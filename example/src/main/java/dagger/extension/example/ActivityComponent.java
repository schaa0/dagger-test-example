package dagger.extension.example;

import dagger.Component;
import dagger.Subcomponent;

@Subcomponent
@ActivityScope
public interface ActivityComponent
{
    void inject(MainActivity activity);
    @Subcomponent.Builder
    interface Builder {
        ActivityComponent build();
    }
}
