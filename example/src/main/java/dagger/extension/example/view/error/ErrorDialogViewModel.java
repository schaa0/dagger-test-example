package dagger.extension.example.view.error;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.extension.example.di.qualifier.ErrorDialog;

public class ErrorDialogViewModel extends BaseObservable {

    public final ObservableField<String> errorTitle;
    public final ObservableField<String> errorMessage;

    @Inject
    public ErrorDialogViewModel(@ErrorDialog("title") String errorTitle, @ErrorDialog("message") String errorMessage) {
        this.errorTitle = new ObservableField<>(errorTitle);
        this.errorMessage = new ObservableField<>(errorMessage);
    }

}
