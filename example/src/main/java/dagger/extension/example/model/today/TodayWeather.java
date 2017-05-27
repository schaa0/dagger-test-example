package dagger.extension.example.model.today;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dagger.extension.example.model.Weather;

public class TodayWeather implements Parcelable, Weather
{

    public final static Creator<TodayWeather> CREATOR = new Creator<TodayWeather>()
    {


        @SuppressWarnings({
                                  "unchecked"
                          })
        public TodayWeather createFromParcel(Parcel in)
        {
            TodayWeather instance = new TodayWeather();
            instance.coord = ((Coord) in.readValue((Coord.class.getClassLoader())));
            in.readList(instance.weather, (dagger.extension.example.model.today.Weather.class.getClassLoader()));
            instance.base = ((String) in.readValue((String.class.getClassLoader())));
            instance.main = ((Main) in.readValue((Main.class.getClassLoader())));
            instance.wind = ((Wind) in.readValue((Wind.class.getClassLoader())));
            instance.clouds = ((Clouds) in.readValue((Clouds.class.getClassLoader())));
            instance.dt = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.sys = ((Sys) in.readValue((Sys.class.getClassLoader())));
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.cod = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public TodayWeather[] newArray(int size)
        {
            return (new TodayWeather[size]);
        }

    };
    @SerializedName("coord")
    @Expose
    private Coord coord;
    @SerializedName("weather")
    @Expose
    private List<dagger.extension.example.model.today.Weather> weather = null;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("clouds")
    @Expose
    private Clouds clouds;
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("sys")
    @Expose
    private Sys sys;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cod")
    @Expose
    private Integer cod;

    public Coord getCoord()
    {
        return coord;
    }

    public void setCoord(Coord coord)
    {
        this.coord = coord;
    }

    public List<dagger.extension.example.model.today.Weather> getWeather()
    {
        return weather;
    }

    public void setWeather(List<dagger.extension.example.model.today.Weather> weather)
    {
        this.weather = weather;
    }

    public String getBase()
    {
        return base;
    }

    public void setBase(String base)
    {
        this.base = base;
    }

    public Main getMain()
    {
        return main;
    }

    public void setMain(Main main)
    {
        this.main = main;
    }

    public Wind getWind()
    {
        return wind;
    }

    public void setWind(Wind wind)
    {
        this.wind = wind;
    }

    public Clouds getClouds()
    {
        return clouds;
    }

    public void setClouds(Clouds clouds)
    {
        this.clouds = clouds;
    }

    public Integer getDt()
    {
        return dt;
    }

    public void setDt(Integer dt)
    {
        this.dt = dt;
    }

    public Sys getSys()
    {
        return sys;
    }

    public void setSys(Sys sys)
    {
        this.sys = sys;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getCod()
    {
        return cod;
    }

    public void setCod(Integer cod)
    {
        this.cod = cod;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeValue(coord);
        dest.writeList(weather);
        dest.writeValue(base);
        dest.writeValue(main);
        dest.writeValue(wind);
        dest.writeValue(clouds);
        dest.writeValue(dt);
        dest.writeValue(sys);
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(cod);
    }

    public int describeContents()
    {
        return 0;
    }

    @Override
    public String temperature()
    {
        return String.valueOf(main.getTemp());
    }

    @Override
    public String humidity()
    {
        return String.valueOf(main.getHumidity());
    }

    @Override
    public String icon()
    {
        String iconId = weather.get(0).getIcon();
        return "http://openweathermap.org/img/w/" + iconId + ".png";
    }

    @Override
    public String city()
    {
        return name;
    }

    @Override
    public String description()
    {
        return weather.get(0).getDescription();
    }
}
