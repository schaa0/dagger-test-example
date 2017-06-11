package dagger.extension.example.testcase;

import dagger.android.testcase.EspressoTestCase;
import dagger.extension.example.di.Decorator;
import dagger.extension.example.di.TestWeatherApplication;

public class DecoratableEspressoTestCase extends EspressoTestCase<TestWeatherApplication> {

    public Decorator decorate() {
        return app();
    }

}
