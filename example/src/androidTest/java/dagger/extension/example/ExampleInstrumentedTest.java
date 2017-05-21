package dagger.extension.example;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import dagger.extension.test.DaggerActivityTestRule;
import dagger.extension.test.DaggerTestCase;
//import delegates.SharedPreferencesDelegate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest extends DaggerTestCase<TestExampleApplication>
{

    @Mock
    SomeService someService;

    SomeService stub;

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
        dependencies().decorateApplicationComponent()
                      .withContext(() -> mock)
                      .withSharedPreferences(context -> mock(SharedPreferences.class));
        rule.launchActivity(null);
        assertSame(mock, ((ExampleApplication)rule.getActivity().getApplication()).getContext());
    }

    @Test
    public void classWithInjectConstructorCanBeReplaced() throws Exception
    {
        dependencies().decorateActivityComponent()
                      .withSomeService((sharedPreferences, someStrings) -> someService);

        rule.launchActivity(null);

        verify(someService, times(1)).call();

    }

    @Test
    public void entriesInMapsCanBeReplaced() {
        dependencies().decorateApplicationComponent()
                      .withBInAppModule(() -> "Z")
                      .and()
                      .decorateActivityComponent()
                      .withSomeService((sharedPreferences, someStrings) -> {
                        return stub = new SomeService(sharedPreferences, someStrings);
                      });
        rule.launchActivity(null);
        assertEquals("Z", stub.getSomeStrings("B"));
    }


}
