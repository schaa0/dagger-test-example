package dagger.extension.example;

import dagger.Config;
import dagger.extension.example.BuildConfig;
import dagger.extension.example.di.WeatherApplication;

@Config(applicationClass = WeatherApplication.class, generateExtendedComponents = true)
class AppConfig {}
