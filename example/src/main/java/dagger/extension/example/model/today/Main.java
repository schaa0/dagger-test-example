package dagger.extension.example.model.today;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main implements Parcelable
{

    public final static Creator<Main> CREATOR = new Creator<Main>()
    {


        @SuppressWarnings({
                                  "unchecked"
                          })
        public Main createFromParcel(Parcel in)
        {
            Main instance = new Main();
            instance.temp = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.pressure = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.humidity = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.tempMin = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.tempMax = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.seaLevel = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.grndLevel = ((Double) in.readValue((Double.class.getClassLoader())));
            return instance;
        }

        public Main[] newArray(int size)
        {
            return (new Main[size]);
        }

    };
    @SerializedName("temp")
    @Expose
    private Double temp;
    @SerializedName("pressure")
    @Expose
    private Double pressure;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;
    @SerializedName("temp_min")
    @Expose
    private Double tempMin;
    @SerializedName("temp_max")
    @Expose
    private Double tempMax;
    @SerializedName("sea_level")
    @Expose
    private Double seaLevel;
    @SerializedName("grnd_level")
    @Expose
    private Double grndLevel;

    public Double getTemp()
    {
        return temp;
    }

    public void setTemp(Double temp)
    {
        this.temp = temp;
    }

    public Double getPressure()
    {
        return pressure;
    }

    public void setPressure(Double pressure)
    {
        this.pressure = pressure;
    }

    public Integer getHumidity()
    {
        return humidity;
    }

    public void setHumidity(Integer humidity)
    {
        this.humidity = humidity;
    }

    public Double getTempMin()
    {
        return tempMin;
    }

    public void setTempMin(Double tempMin)
    {
        this.tempMin = tempMin;
    }

    public Double getTempMax()
    {
        return tempMax;
    }

    public void setTempMax(Double tempMax)
    {
        this.tempMax = tempMax;
    }

    public Double getSeaLevel()
    {
        return seaLevel;
    }

    public void setSeaLevel(Double seaLevel)
    {
        this.seaLevel = seaLevel;
    }

    public Double getGrndLevel()
    {
        return grndLevel;
    }

    public void setGrndLevel(Double grndLevel)
    {
        this.grndLevel = grndLevel;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeValue(temp);
        dest.writeValue(pressure);
        dest.writeValue(humidity);
        dest.writeValue(tempMin);
        dest.writeValue(tempMax);
        dest.writeValue(seaLevel);
        dest.writeValue(grndLevel);
    }

    public int describeContents()
    {
        return 0;
    }

}