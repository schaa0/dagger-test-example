package dagger.extension.example.stubs;

import android.support.v7.app.AppCompatActivity;

import dagger.extension.example.service.PermissionResult;
import dagger.extension.example.service.PermissionService;

public class PermissionServiceStub extends PermissionService {

    private final boolean grantsAllPermissions;

    public PermissionServiceStub(AppCompatActivity activity, boolean grantsAllPermissions) {
        super(activity);
        this.grantsAllPermissions = grantsAllPermissions;
    }

    @Override
    public boolean isPermissionGranted(String permission) {
        return grantsAllPermissions;
    }

}
