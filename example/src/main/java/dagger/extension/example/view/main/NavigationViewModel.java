package dagger.extension.example.view.main;

import android.databinding.BaseObservable;

import dagger.extension.example.service.NavigationController;

public class NavigationViewModel extends BaseObservable {

    private final NavigationController navigationController;

    public NavigationViewModel(NavigationController navigationController) {
        this.navigationController = navigationController;
    }

    protected NavigationController navigate() {
        return navigationController;
    }

    public void showError(String title, String errorMessage) {
        navigationController.showErrorIfNotAlreadyShowing(title, errorMessage);
    }

}
