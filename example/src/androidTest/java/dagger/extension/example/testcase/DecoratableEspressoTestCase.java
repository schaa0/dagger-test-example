package dagger.extension.example.testcase;

import android.os.Build;

import dagger.android.testcase.EspressoTestCase;
import dagger.extension.example.di.GraphDecorator;
import dagger.extension.example.di.TestWeatherApplication;

public class DecoratableEspressoTestCase extends EspressoTestCase<TestWeatherApplication> {
    public GraphDecorator decorate() {
        return app();
    }
    protected boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic");
    }
}
