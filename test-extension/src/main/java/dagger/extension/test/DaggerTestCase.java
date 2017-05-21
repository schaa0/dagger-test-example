package dagger.extension.test;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import org.junit.After;
import org.junit.Before;

public abstract class DaggerTestCase<T extends Application>
{

    private T app;

    public T dependencies()
    {
        return app;
    }

    @Before
    public void setUp() throws Exception {
        final DaggerRunner abstractRunner = (DaggerRunner) InstrumentationRegistry.getInstrumentation();
        T app = (T) abstractRunner.getApplication();
        this.app = app;
    }

    @After
    public void tearDown() throws Exception {

    }

}
