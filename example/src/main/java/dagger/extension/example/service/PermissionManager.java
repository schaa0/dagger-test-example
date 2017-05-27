package dagger.extension.example.service;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.extension.example.di.ActivityScope;
import dagger.extension.example.event.PermissionEvent;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

@ActivityScope
public class PermissionManager {

    private final AppCompatActivity activity;
    private final PublishSubject<PermissionEvent> container = PublishSubject.create();

    @Inject
    public PermissionManager(AppCompatActivity activity) {
        this.activity = activity;
    }

    public boolean isPermissionGranted(String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    public PublishSubject<PermissionEvent> onPermissionGranted() {
        return this.container;
    }

    public void dispatchEvent(PermissionEvent permissionEvent) {
        this.container.onNext(permissionEvent);
    }

}
