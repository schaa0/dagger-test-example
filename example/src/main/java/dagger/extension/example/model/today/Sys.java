package dagger.extension.example.model.today;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sys implements Parcelable
{

    public final static Creator<Sys> CREATOR = new Creator<Sys>()
    {


        @SuppressWarnings({
                                  "unchecked"
                          })
        public Sys createFromParcel(Parcel in)
        {
            Sys instance = new Sys();
            instance.message = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.country = ((String) in.readValue((String.class.getClassLoader())));
            instance.sunrise = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.sunset = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public Sys[] newArray(int size)
        {
            return (new Sys[size]);
        }

    };
    @SerializedName("message")
    @Expose
    private Double message;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sunrise")
    @Expose
    private Integer sunrise;
    @SerializedName("sunset")
    @Expose
    private Integer sunset;

    public Double getMessage()
    {
        return message;
    }

    public void setMessage(Double message)
    {
        this.message = message;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public Integer getSunrise()
    {
        return sunrise;
    }

    public void setSunrise(Integer sunrise)
    {
        this.sunrise = sunrise;
    }

    public Integer getSunset()
    {
        return sunset;
    }

    public void setSunset(Integer sunset)
    {
        this.sunset = sunset;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeValue(message);
        dest.writeValue(country);
        dest.writeValue(sunrise);
        dest.writeValue(sunset);
    }

    public int describeContents()
    {
        return 0;
    }

}