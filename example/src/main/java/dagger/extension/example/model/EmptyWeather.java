package dagger.extension.example.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.auto.factory.AutoFactory;

import javax.inject.Inject;

import dagger.extension.example.di.scope.ActivityScope;

@AutoFactory
public class EmptyWeather implements Weather {

    public EmptyWeather() { }

    protected EmptyWeather(Parcel in) {

    }

    @Override
    public String temperature() {
        return "";
    }

    @Override
    public String humidity() {
        return "";
    }

    @Override
    public String icon() {
        return "";
    }

    @Override
    public String city() {
        return "";
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }

    public static final Creator<EmptyWeather> CREATOR = new Parcelable.Creator<EmptyWeather>() {
        @Override
        public EmptyWeather createFromParcel(Parcel in) {
            return new EmptyWeather(in);
        }

        @Override
        public EmptyWeather[] newArray(int size) {
            return new EmptyWeather[size];
        }
    };
}
