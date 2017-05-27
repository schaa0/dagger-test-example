
package dagger.extension.example.model.forecast.threehours;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rain implements Parcelable
{

    @SerializedName("3h")
    @Expose
    private double _3h;
    public final static Creator<Rain> CREATOR = new Creator<Rain>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Rain createFromParcel(Parcel in) {
            Rain instance = new Rain();
            instance._3h = ((int) in.readValue((double.class.getClassLoader())));
            return instance;
        }

        public Rain[] newArray(int size) {
            return (new Rain[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Rain() {
    }

    /**
     * 
     * @param _3h
     */
    public Rain(int _3h) {
        super();
        this._3h = _3h;
    }

    public double get3h() {
        return _3h;
    }

    public void set3h(int _3h) {
        this._3h = _3h;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(_3h);
    }

    public int describeContents() {
        return  0;
    }

}
