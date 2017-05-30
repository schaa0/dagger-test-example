package dagger.extension.example.view.main;

import javax.inject.Inject;

import dagger.extension.example.scope.ActivityScope;
import dagger.extension.example.service.NavigationController;

@ActivityScope
public class MainViewModel extends NavigationViewModel {

    @Inject
    public MainViewModel(NavigationController navigation){
        super(navigation);
    }

}
