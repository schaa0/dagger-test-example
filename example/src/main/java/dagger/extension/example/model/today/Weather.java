package dagger.extension.example.model.today;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather implements Parcelable
{

    public final static Creator<Weather> CREATOR = new Creator<Weather>()
    {


        @SuppressWarnings({
                                  "unchecked"
                          })
        public Weather createFromParcel(Parcel in)
        {
            Weather instance = new Weather();
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.main = ((String) in.readValue((String.class.getClassLoader())));
            instance.description = ((String) in.readValue((String.class.getClassLoader())));
            instance.icon = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Weather[] newArray(int size)
        {
            return (new Weather[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
    private String icon;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getMain()
    {
        return main;
    }

    public void setMain(String main)
    {
        this.main = main;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeValue(id);
        dest.writeValue(main);
        dest.writeValue(description);
        dest.writeValue(icon);
    }

    public int describeContents()
    {
        return 0;
    }

}