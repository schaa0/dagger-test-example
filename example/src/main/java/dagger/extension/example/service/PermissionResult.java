package dagger.extension.example.service;

import java.util.Arrays;

public class PermissionResult {

    private final int requestCode;
    private final String[] permissions;
    private final int[] grantResults;

    public PermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.grantResults = grantResults;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public int[] getGrantResults() {
        return grantResults;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionResult that = (PermissionResult) o;

        if (requestCode != that.requestCode) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(permissions, that.permissions)) return false;
        return Arrays.equals(grantResults, that.grantResults);

    }

    @Override
    public int hashCode()
    {
        int result = requestCode;
        result = 31 * result + Arrays.hashCode(permissions);
        result = 31 * result + Arrays.hashCode(grantResults);
        return result;
    }
}
