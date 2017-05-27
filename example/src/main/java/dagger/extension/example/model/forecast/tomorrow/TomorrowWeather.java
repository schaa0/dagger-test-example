
package dagger.extension.example.model.forecast.tomorrow;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import dagger.extension.example.model.Weather;

public class TomorrowWeather implements Weather, Parcelable
{

    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private double message;
    @SerializedName("cnt")
    @Expose
    private int cnt;
    @SerializedName("list")
    @Expose
    private java.util.List<List> list = null;
    public final static Creator<TomorrowWeather> CREATOR = new Creator<TomorrowWeather>() {


        @SuppressWarnings({
            "unchecked"
        })
        public TomorrowWeather createFromParcel(Parcel in) {
            TomorrowWeather instance = new TomorrowWeather();
            instance.city = ((City) in.readValue((City.class.getClassLoader())));
            instance.cod = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((double) in.readValue((double.class.getClassLoader())));
            instance.cnt = ((int) in.readValue((int.class.getClassLoader())));
            in.readList(instance.list, (List.class.getClassLoader()));
            return instance;
        }

        public TomorrowWeather[] newArray(int size) {
            return (new TomorrowWeather[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public TomorrowWeather() {
    }

    /**
     * 
     * @param message
     * @param cnt
     * @param cod
     * @param list
     * @param city
     */
    public TomorrowWeather(City city, String cod, double message, int cnt, java.util.List<List> list) {
        super();
        this.city = city;
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public java.util.List<List> getList() {
        return list;
    }

    public void setList(java.util.List<List> list) {
        this.list = list;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(city);
        dest.writeValue(cod);
        dest.writeValue(message);
        dest.writeValue(cnt);
        dest.writeList(list);
    }

    public int describeContents() {
        return  0;
    }

    @Override
    public String temperature() {
        return String.valueOf(list.get(0).getTemp().getDay());
    }

    @Override
    public String humidity() {
        return String.valueOf(list.get(0).getHumidity());
    }

    @Override
    public String icon() {
        String iconId = list.get(0).getWeather().get(0).getIcon();
        return "http://openweathermap.org/img/w/" + iconId + ".png";
    }

    @Override
    public String city()
    {
        return city.getName();
    }

    @Override
    public String description()
    {
        return list.get(0).getWeather().get(0).getDescription();
    }
}
