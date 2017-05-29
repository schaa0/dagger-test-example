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
import dagger.extension.example.view.main.MainActivity;

public class ErrorDialogFragment extends DialogFragment {

    public static ErrorDialogFragment newInstance(String title, String message) {

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        ErrorDialogFragment fragment = new ErrorDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    ErrorDialogViewModel vm;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ((MainActivity)getActivity()).inject(
                this,
                getArguments().getString("message"),
                getArguments().getString("title")
        );
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ErrorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.error, null, false);
        binding.setVm(vm);
        builder.setView(binding.getRoot());
        builder.setPositiveButton("OK", null);
        return builder.create();
    }
}
