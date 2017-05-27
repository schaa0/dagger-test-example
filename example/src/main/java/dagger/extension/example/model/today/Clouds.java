package dagger.extension.example.model.today;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clouds implements Parcelable
{

    public final static Creator<Clouds> CREATOR = new Creator<Clouds>()
    {


        @SuppressWarnings({
                                  "unchecked"
                          })
        public Clouds createFromParcel(Parcel in)
        {
            Clouds instance = new Clouds();
            instance.all = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public Clouds[] newArray(int size)
        {
            return (new Clouds[size]);
        }

    };
    @SerializedName("all")
    @Expose
    private Integer all;

    public Integer getAll()
    {
        return all;
    }

    public void setAll(Integer all)
    {
        this.all = all;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeValue(all);
    }

    public int describeContents()
    {
        return 0;
    }

}
