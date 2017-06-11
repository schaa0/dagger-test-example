# dagger-test-example

## The Goal

is to provide an alternative for replacing dependencies under test which is backed by annotation processing and simple to use. This repository showcases an approach based on a modified version of Dagger 2.

## Current Requirements

* Each component and subcomponent needs a Builder declaration.
* Provision methods in modules should be static

## Advantages with this approach
* Each dependency which is annotaed with `@AllowStubGeneration` can be replaced under test
* Dynamic dependency configuration for each test
* Test modules and test components are not needed

## Introduction

The modified version of Dagger 2 is based on version 2.11

### Setup

First, the annotation processor must be aware of the application class. This is achieved by a `@Config` annotation. 

```java
@Config(applicationClass = ExampleApplication.class)
public class AppConfig
{
}
```

Let application class derive from `DaggerHookApplication`. This class will be generated at compile time.

```java
public class ExampleApplication extends DaggerHookApplication {
    @Inject DispatchingAndroidInjector<Activity> activityInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerComponentSingleton.builder(this).create(this).inject(this);
    }

    public DispatchingAndroidInjector<Activity> activityInjector() {
        return this.activityInjector;
    }
}
```

Each Component and Subcomponent needs a Builder declaration.

```java
@Component(modules = {...})
@Singleton
public interface ComponentSingleton extends AndroidInjector<WeatherApplication>{
    void inject(ExampleApplication application);

    @Component.Builder
    public abstract class Builder extends AndroidInjector.Builder<WeatherApplication> {
        public abstract ComponentSingleton build();
    }
```

Declaring Activity Subcomponents

```java
@Module
public abstract class ActivityBindingsModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    public abstract MainActivity mainActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = ForecastActivityModule.class)
    public abstract ForecastActivity forecastActivity();
}
```

### Declaring which objects can be replaced under test

For dependency which is provided through a `@BindsInstance` builder method:

```java
@Component(modules = {...})
@Singleton
public interface ComponentSingleton extends AndroidInjector<WeatherApplication>{
    void inject(WeatherApplication application);

    @Component.Builder
    public abstract class Builder extends AndroidInjector.Builder<WeatherApplication> {
        @BindsInstance @AllowStubGeneration
        public Builder bindContext(Context context);
        public abstract ComponentSingleton build();
    }
```

For dependency which is provided through a `@Provides` method:

```java
    @Module(includes = ActivityBindingsModule.class)
    abstract class SingletonModule {

        @Provides @Singleton @AllowStubGeneration
        public static RequestManager glide(Context context) {
            return Glide.with(context);
        }

        @Provides @Singleton
        public static Scheduler scheduler() {
            return Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }    
```

For dependency which has an `@Inject` Constructor

```java
public class ExampleService {
    @Inject
    @AllowStubGeneration
    public ExampleService() { }
}
```

### Setting up test environment


Define a Custom Test Runner. The test application class will be created when the tests are
compiled.

```java
public class EspressoTestRunner extends DaggerRunner<TestExampleApplication> { 
}
```

Define a base class to be used for an espresso test

```java
public class DecoratableEspressoTestCase extends EspressoTestCase<TestExampleApplication> {
    public Decorator decorate() {
        return app();
    }
}
```

### Replacing objects

Objects can be replaced with a fluent api.

```java
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest extends DecoratableEspressoTestCase<TestExampleApplication>
{

    @Mock
    ExampleService exampleService;
    PermissionManagerStub pm;

    @Rule
    public DaggerActivityTestRule<MainActivity> rule = new DaggerActivityTestRule<>(MainActivity.class);

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void exampleTest() {
        //.. setup mock
        // Lazy replacement of dependencies before launch
        decorate().componentSingleton()
                    .withExampleService(() -> exampleService).and()
                  .subcomponentMainActivity()
                    .withPermissionManager(activity -> new PermissionManagerStub(activity));
        rule.launchActivity(null);
        //.. some assert
    }

    ...

}
```