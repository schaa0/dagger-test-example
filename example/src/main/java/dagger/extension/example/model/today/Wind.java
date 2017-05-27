package dagger.extension.example.model.today;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind implements Parcelable
{

    public final static Creator<Wind> CREATOR = new Creator<Wind>()
    {


        @SuppressWarnings({
                                  "unchecked"
                          })
        public Wind createFromParcel(Parcel in)
        {
            Wind instance = new Wind();
            instance.speed = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.deg = ((Double) in.readValue((Double.class.getClassLoader())));
            return instance;
        }

        public Wind[] newArray(int size)
        {
            return (new Wind[size]);
        }

    };
    @SerializedName("speed")
    @Expose
    private Double speed;
    @SerializedName("deg")
    @Expose
    private Double deg;

    public Double getSpeed()
    {
        return speed;
    }

    public void setSpeed(Double speed)
    {
        this.speed = speed;
    }

    public Double getDeg()
    {
        return deg;
    }

    public void setDeg(Double deg)
    {
        this.deg = deg;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeValue(speed);
        dest.writeValue(deg);
    }

    public int describeContents()
    {
        return 0;
    }

}