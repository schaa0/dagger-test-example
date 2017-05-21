package dagger.extension.example;

import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import dagger.AllowStubGeneration;

@ActivityScope
public class SomeService {

    private final SharedPreferences sharedPreferences;
    private final Map<String, String> someStrings;

    @AllowStubGeneration
    @Inject
    public SomeService(SharedPreferences sharedPreferences, Map<String, String> someStrings) {
        this.sharedPreferences = sharedPreferences;
        this.someStrings = someStrings;
    }

    public void call() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
    }

    public String getSomeStrings(String key) {
        return someStrings.get(key);
    }
}
