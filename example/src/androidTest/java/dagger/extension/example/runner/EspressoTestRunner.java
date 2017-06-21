package dagger.extension.example.runner;

import android.os.Bundle;
import android.support.test.InstrumentationRegistry;

import com.linkedin.android.testbutler.TestButler;

import dagger.android.testcase.DaggerRunner;
import dagger.extension.example.di.TestWeatherApplication;

public class EspressoTestRunner extends DaggerRunner<TestWeatherApplication>
{
    @Override
    public void onStart() {
        TestButler.setup(InstrumentationRegistry.getTargetContext());
        super.onStart();
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        TestButler.teardown(InstrumentationRegistry.getTargetContext());
        super.finish(resultCode, results);
    }
}
