package dagger.extension.example.view.main;

import android.content.Context;

import dagger.AllowStubGeneration;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.extension.example.R;
import dagger.extension.example.di.ActivityModule;
import dagger.extension.example.di.FragmentBindingsModule;
import dagger.extension.example.di.qualifier.RxObservable;
import dagger.extension.example.di.scope.ActivityScope;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static dagger.extension.example.di.qualifier.RxObservable.Type.*;
import static dagger.extension.example.view.main.SectionsPagerAdapter.*;

@Module(includes = {FragmentBindingsModule.class})
public abstract class MainActivityModule extends ActivityModule<MainActivity> {

    @ActivityScope @Provides @RxObservable(SEARCH) @AllowStubGeneration
    public static PublishSubject<String> searchQuerySubject() { return PublishSubject.create(); }

    @ActivityScope @Provides @RxObservable(PAGE) @AllowStubGeneration
    public static PublishSubject<Integer> pageChangedSubject() { return PublishSubject.create(); }

    @Binds @RxObservable(SEARCH)
    public abstract Observable<String> bindSearchSubjectToObservable(@RxObservable(SEARCH) PublishSubject<String> s);

    @Binds @RxObservable(PAGE)
    public abstract Observable<Integer> bindPageChangeSubjectToObservable(@RxObservable(PAGE) PublishSubject<Integer> s);

    @Provides @IntKey(POSITION_TODAY) @IntoMap
    public static String today(Context context) {
        return context.getString(R.string.today);
    }

    @Provides @IntKey(POSITION_TOMORROW) @IntoMap @AllowStubGeneration
    public static String tomorrow(Context context) {
        return context.getString(R.string.tomorrow);
    }

    @Provides @IntKey(POSITION_SEARCH) @IntoMap
    public static String search(Context context) {
        return context.getString(R.string.search);
    }

}
