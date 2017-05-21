# dagger-test-example
Demonstrates testability with a modified version of dagger 2

## Current Requirements

* Each component and subcomponent needs a Builder declaration
* Each Module must be declared public

## Advantages with this approach
* All dependencies available in the object graph can be replaced under test if they are annotated with @AllowStubGeneration
* Works for @Inject, @Provides and @BindsInstance
* Allows dynamic configuration for each test
* Overriding module methods or component methods not necessary

## Introduction

The modified version of dagger 2 is based on version 2.9

Define application class

```java
@Config(applicationClass = ExampleApplication.class)
public class AppConfig
{
}
```

Let application class derive from "DaggerApplication" which will be generated at compile time

```java
public class ExampleApplication extends DaggerApplication
{

}
```

Each Component and Subcomponent needs a Builder declaration.
Dependencies which needs to be replaced under test can be annotated with "@AllowStubGeneration".

```java

// Component 
//@AllowStubGeneration For @BindsInstance
@Singleton
@Component(modules = {AppModule.class})
public interface ApplicationComponent
{
    ActivityComponent.Builder activityBuilder();
    @Component.Builder
    interface Builder {
        @AllowStubGeneration @BindsInstance Builder context(Context context);
        Builder appModule(AppModule module);
        ApplicationComponent build();
    }
}

//Subcomponent

@Subcomponent
@ActivityScope
public interface ActivityComponent
{
    void inject(MainActivity activity);
    @Subcomponent.Builder
    interface Builder {
        ActivityComponent build();
    }
}

//@AllowStubGeneration For @Inject constructor

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
}

//@AllowStubGeneration For @Provides method

@Module
public class AppModule
{
    @AllowStubGeneration
    @Provides
    public SharedPreferences sharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}

Inject the dependencies in production code through the Injector.get() call.

```java
public class MainActivity extends AppCompatActivity {

    @Inject
    SomeService someService;

    private ActivityComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = Injector.get(getApplication()).activityComponent(this.getParentComponent());
        component.inject(this);

    }
    ...
}
```

Define custom TestRunner. The test application class will be generated at compile time

```java
public class EspressoTestRunner extends DaggerRunner<TestExampleApplication> { 
}
```

Code will be generated to make replacing dependencies under test possible.

```java
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest extends DaggerTestCase<TestExampleApplication>
{

    @Mock
    SomeService someService;

    @Rule
    public DaggerActivityTestRule<MainActivity> rule = new DaggerActivityTestRule<>(MainActivity.class);

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void bindsInstanceParameterIsReplaceable() {
        Context mock = mock(Context.class);
        // Lazy replacement of dependencies before launch
        dependencies().decorateApplicationComponent()
                      .withContext(() -> mock)
                      .withSharedPreferences(context -> mock(SharedPreferences.class));
        rule.launchActivity(null);
        assertSame(mock, ((ExampleApplication)rule.getActivity().getApplication()).getContext());
    }

    @Test
    public void classWithInjectConstructorCanBeReplaced() throws Exception
    {
    	// Lazy replacement of dependencies before launch
        dependencies().decorateActivityComponent()
                      .withSomeService((sharedPreferences, someStrings) -> someService);

        rule.launchActivity(null);

        verify(someService, times(1)).call();

    }

    ...

}
```