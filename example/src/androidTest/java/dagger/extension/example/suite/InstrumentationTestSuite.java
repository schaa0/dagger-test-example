package dagger.extension.example.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import dagger.extension.example.view.SimpleEspressoTest;
import dagger.extension.example.view.WeatherFragmentTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({SimpleEspressoTest.class, WeatherFragmentTest.class})
public class InstrumentationTestSuite {}
