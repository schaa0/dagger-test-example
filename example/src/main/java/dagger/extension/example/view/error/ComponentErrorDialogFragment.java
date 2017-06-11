package dagger.extension.example.view.error;

import dagger.AllowStubGeneration;
import dagger.BindsInstance;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.extension.example.di.qualifier.ErrorDialog;

@Subcomponent
public interface ComponentErrorDialogFragment {

    ErrorDialogFragment errorDialog();

    @Subcomponent.Builder
    abstract class Builder {
        @AllowStubGeneration @BindsInstance
        public abstract Builder message(@ErrorDialog("message") String errorMessage);
        @BindsInstance
        public abstract Builder title(@ErrorDialog("title") String title);
        public abstract ComponentErrorDialogFragment build();
    }
}
