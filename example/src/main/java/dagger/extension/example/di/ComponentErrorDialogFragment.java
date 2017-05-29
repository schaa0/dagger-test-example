package dagger.extension.example.di;

import javax.inject.Named;

import dagger.AllowStubGeneration;
import dagger.BindsInstance;
import dagger.Subcomponent;
import dagger.di.FragmentComponent;
import dagger.di.FragmentComponentBuilder;
import dagger.extension.example.di.qualifier.ErrorDialog;
import dagger.extension.example.view.error.ErrorDialogFragment;

@Subcomponent
public interface ComponentErrorDialogFragment extends FragmentComponent<ErrorDialogFragment> {

    @Subcomponent.Builder
    interface Builder extends FragmentComponentBuilder<ComponentErrorDialogFragment> {
        @AllowStubGeneration @BindsInstance Builder message(@ErrorDialog("message") String errorMessage);
        @BindsInstance  Builder title(@ErrorDialog("title") String title);
        ComponentErrorDialogFragment build();
    }
}
