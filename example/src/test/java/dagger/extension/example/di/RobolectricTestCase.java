package dagger.extension.example.di;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

public class RobolectricTestCase {

    private GraphDecorator decorator;

    public GraphDecorator decorate() {
        return decorator;
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        decorator = ((TestApplicationForRobolectric) RuntimeEnvironment.application).decorator();
    }

}
