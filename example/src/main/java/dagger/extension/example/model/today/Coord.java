package dagger.extension.example.model.today;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coord implements Parcelable
{

    public final static Creator<Coord> CREATOR = new Creator<Coord>()
    {


        @SuppressWarnings({
                                  "unchecked"
                          })
        public Coord createFromParcel(Parcel in)
        {
            Coord instance = new Coord();
            instance.lon = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.lat = ((Double) in.readValue((Double.class.getClassLoader())));
            return instance;
        }

        public Coord[] newArray(int size)
        {
            return (new Coord[size]);
        }

    };
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("lat")
    @Expose
    private Double lat;

    public Double getLon()
    {
        return lon;
    }

    public void setLon(Double lon)
    {
        this.lon = lon;
    }

    public Double getLat()
    {
        return lat;
    }

    public void setLat(Double lat)
    {
        this.lat = lat;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeValue(lon);
        dest.writeValue(lat);
    }

    public int describeContents()
    {
        return 0;
    }

}