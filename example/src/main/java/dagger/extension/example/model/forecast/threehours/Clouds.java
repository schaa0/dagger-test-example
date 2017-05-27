
package dagger.extension.example.model.forecast.threehours;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clouds implements Parcelable
{

    @SerializedName("all")
    @Expose
    private int all;
    public final static Creator<Clouds> CREATOR = new Creator<Clouds>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Clouds createFromParcel(Parcel in) {
            Clouds instance = new Clouds();
            instance.all = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public Clouds[] newArray(int size) {
            return (new Clouds[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Clouds() {
    }

    /**
     * 
     * @param all
     */
    public Clouds(int all) {
        super();
        this.all = all;
    }

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(all);
    }

    public int describeContents() {
        return  0;
    }

}
