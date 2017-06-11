package dagger.extension.example.view.search;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

public class SearchItemViewModel extends BaseObservable {

    public final ObservableField<String> date = new ObservableField<>();
    public final ObservableField<String> temperature = new ObservableField<>();
    public final ObservableField<String> humidity = new ObservableField<>();

    public SearchItemViewModel(String date, String temperature, String humidity) {
        this.date.set(date);
        this.temperature.set(temperature);
        this.humidity.set(humidity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchItemViewModel that = (SearchItemViewModel) o;

        if (!date.get().equals(that.date.get())) return false;
        if (!temperature.get().equals(that.temperature.get())) return false;
        return humidity.get().equals(that.humidity.get());

    }

    @Override
    public int hashCode() {
        int result = date.get().hashCode();
        result = 31 * result + temperature.get().hashCode();
        result = 31 * result + humidity.get().hashCode();
        return result;
    }
}
