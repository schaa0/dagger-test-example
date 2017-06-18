package dagger.extension.example.model;

import android.os.Parcel;
import android.os.Parcelable;

public interface Weather extends Parcelable {
    String temperature();
    String humidity();
    String icon();
    String city();
    String description();
}
