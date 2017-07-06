package dagger.extension.example.view.error;

import dagger.AllowStubGeneration;
import dagger.BindsInstance;
import dagger.Replaceable;
import dagger.Subcomponent;
import dagger.extension.example.di.qualifier.ErrorDialog;

@Subcomponent
public interface ComponentErrorDialogFragment {

    ErrorDialogFragment errorDialog();

    @Subcomponent.Builder
    abstract class Builder {
        @BindsInstance @Replaceable public abstract Builder message(@ErrorDialog("message") String errorMessage);
        @BindsInstance public abstract Builder title(@ErrorDialog("title") String title);
        public abstract ComponentErrorDialogFragment build();
    }
}
