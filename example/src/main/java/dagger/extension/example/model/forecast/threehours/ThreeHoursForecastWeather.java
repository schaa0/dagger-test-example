
package dagger.extension.example.model.forecast.threehours;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThreeHoursForecastWeather implements Parcelable
{

    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private double message;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("cnt")
    @Expose
    private int cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<WeatherInfo> weatherInfo = null;
    public final static Creator<ThreeHoursForecastWeather> CREATOR = new Creator<ThreeHoursForecastWeather>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ThreeHoursForecastWeather createFromParcel(Parcel in) {
            ThreeHoursForecastWeather instance = new ThreeHoursForecastWeather();
            instance.cod = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((double) in.readValue((double.class.getClassLoader())));
            instance.city = ((City) in.readValue((City.class.getClassLoader())));
            instance.cnt = ((int) in.readValue((int.class.getClassLoader())));
            in.readList(instance.weatherInfo, (WeatherInfo.class.getClassLoader()));
            return instance;
        }

        public ThreeHoursForecastWeather[] newArray(int size) {
            return (new ThreeHoursForecastWeather[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ThreeHoursForecastWeather() {
    }

    /**
     * 
     * @param message
     * @param cnt
     * @param cod
     * @param weatherInfo
     * @param city
     */
    public ThreeHoursForecastWeather(String cod, double message, City city, int cnt, java.util.List<WeatherInfo> weatherInfo) {
        super();
        this.cod = cod;
        this.message = message;
        this.city = city;
        this.cnt = cnt;
        this.weatherInfo = weatherInfo;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public java.util.List<WeatherInfo> getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(java.util.List<WeatherInfo> weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(cod);
        dest.writeValue(message);
        dest.writeValue(city);
        dest.writeValue(cnt);
        dest.writeList(weatherInfo);
    }

    public int describeContents() {
        return  0;
    }

}
