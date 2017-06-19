package dagger.extension.example.view.main;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Named;

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
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

@Module(includes = {ActivityModule.class, FragmentBindingsModule.class})
public abstract class MainActivityModule {

    private static final int TODAY = 0;
    private static final int TOMORROW = 1;
    private static final int SEARCH = 2;

    @Binds
    public abstract AppCompatActivity appCompatActivity(MainActivity activity);

    @ActivityScope
    @Provides @RxObservable("search") @AllowStubGeneration
    public static PublishSubject<String> searchQuerySubject() { return PublishSubject.create(); }

    @ActivityScope
    @Provides @RxObservable("page") @AllowStubGeneration
    public static PublishSubject<Integer> pageChangedSubject() { return PublishSubject.create(); }

    @Binds @RxObservable("search")
    public abstract Observable<String> bindSearchSubjectToObservable(@RxObservable("search") PublishSubject<String> s);

    @Binds @RxObservable("page")
    public abstract Observable<Integer> bindPageChangeSubjectToObservable(@RxObservable("page") PublishSubject<Integer> s);

    @Provides @IntKey(TODAY) @IntoMap
    public static String today(Context context) {
        return context.getString(R.string.today);
    }

    @Provides @IntKey(TOMORROW) @IntoMap @AllowStubGeneration
    public static String tomorrow(Context context) {
        return context.getString(R.string.tomorrow);
    }

    @Provides @IntKey(SEARCH) @IntoMap
    public static String search(Context context) {
        return context.getString(R.string.search);
    }
}
