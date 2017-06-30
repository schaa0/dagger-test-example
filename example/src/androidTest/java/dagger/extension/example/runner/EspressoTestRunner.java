package dagger.extension.example.runner;

import android.os.Build;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;

import com.linkedin.android.testbutler.TestButler;

import dagger.android.testcase.DaggerRunner;
import dagger.extension.example.di.TestWeatherApplication;

public class EspressoTestRunner extends DaggerRunner<TestWeatherApplication>
{
    protected boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic");
    }

    @Override
    public void onStart() {
        if (isEmulator()) {
            TestButler.setup(InstrumentationRegistry.getTargetContext());
        }
        super.onStart();
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        if (isEmulator()) {
            TestButler.teardown(InstrumentationRegistry.getTargetContext());
        }
        super.finish(resultCode, results);
    }
}
