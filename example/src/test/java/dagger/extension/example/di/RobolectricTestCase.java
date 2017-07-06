package dagger.extension.example.di;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

public class RobolectricTestCase {

    private GraphDecorator decorator;
    private TestApplicationForRobolectric app;

    public GraphDecorator decorate() {
        return decorator;
    }

    public TestApplicationForRobolectric app() {
        return this.app;
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        app = (TestApplicationForRobolectric) RuntimeEnvironment.application;
        decorator = app.decorator();
    }

}
