package dagger.extension.example.view.error;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import javax.inject.Inject;

import dagger.extension.example.R;
import dagger.extension.example.databinding.ErrorBinding;

public class ErrorDialogFragment extends DialogFragment {

    @Inject
    public ErrorDialogFragment() { }

    @Inject
    ErrorDialogViewModel vm;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ErrorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.error, null, false);
        binding.setVm(vm);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());
        builder.setPositiveButton("OK", null);

        return builder.create();
    }
}
