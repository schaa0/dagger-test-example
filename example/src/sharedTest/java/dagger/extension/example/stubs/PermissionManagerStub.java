package dagger.extension.example.stubs;

import android.support.v7.app.AppCompatActivity;

import dagger.extension.example.service.PermissionManager;
import dagger.extension.example.service.PermissionResult;

public class PermissionManagerStub extends PermissionManager {
    public PermissionManagerStub(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public boolean isPermissionGranted(String permission) {
        return false;
    }

    @Override
    public void requestPermission(String permission, int requestCode) {
        // don't forward permission request
    }

    @Override
    public void dispatchEvent(PermissionResult permissionEvent) {
        // don't dispatch permission result
    }
}
