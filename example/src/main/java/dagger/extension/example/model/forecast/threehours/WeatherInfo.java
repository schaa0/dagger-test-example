
package dagger.extension.example.model.forecast.threehours;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherInfo implements Parcelable, dagger.extension.example.model.Weather
{

    @SerializedName("dt")
    @Expose
    private int dt;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("weather")
    @Expose
    private java.util.List<Weather> weather = null;
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("rain")
    @Expose
    private Rain rain;
    @SerializedName("sys")
    @Expose
    private Sys sys;
    @SerializedName("dt_txt")
    @Expose
    private String dtTxt;
    public final static Creator<WeatherInfo> CREATOR = new Creator<WeatherInfo>() {


        @SuppressWarnings({
            "unchecked"
        })
        public WeatherInfo createFromParcel(Parcel in) {
            WeatherInfo instance = new WeatherInfo();
            instance.dt = ((int) in.readValue((int.class.getClassLoader())));
            instance.main = ((Main) in.readValue((Main.class.getClassLoader())));
            in.readList(instance.weather, (dagger.extension.example.model.forecast.threehours.Weather.class.getClassLoader()));
            instance.clouds = ((Clouds) in.readValue((Clouds.class.getClassLoader())));
            instance.wind = ((Wind) in.readValue((Wind.class.getClassLoader())));
            instance.rain = ((Rain) in.readValue((Rain.class.getClassLoader())));
            instance.sys = ((Sys) in.readValue((Sys.class.getClassLoader())));
            instance.dtTxt = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public WeatherInfo[] newArray(int size) {
            return (new WeatherInfo[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public WeatherInfo() {
    }

    /**
     * 
     * @param clouds
     * @param dt
     * @param wind
     * @param sys
     * @param dtTxt
     * @param weather
     * @param rain
     * @param main
     */
    public WeatherInfo(int dt, Main main, java.util.List<Weather> weather, Clouds clouds, Wind wind, Rain rain, Sys sys, String dtTxt) {
        super();
        this.dt = dt;
        this.main = main;
        this.weather = weather;
        this.clouds = clouds;
        this.wind = wind;
        this.rain = rain;
        this.sys = sys;
        this.dtTxt = dtTxt;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public java.util.List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(dt);
        dest.writeValue(main);
        dest.writeList(weather);
        dest.writeValue(clouds);
        dest.writeValue(wind);
        dest.writeValue(rain);
        dest.writeValue(sys);
        dest.writeValue(dtTxt);
    }

    public int describeContents() {
        return  0;
    }

    @Override
    public String temperature() {
        return String.valueOf(main.getTemp());
    }

    @Override
    public String humidity() {
        return String.valueOf(main.getHumidity());
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
}
