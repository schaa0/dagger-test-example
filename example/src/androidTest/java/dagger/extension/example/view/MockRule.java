package dagger.extension.example.view;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.MockitoAnnotations;

public class MockRule implements TestRule {

    private final Object testClass;

    public MockRule(Object testClass) {
        this.testClass = testClass;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        MockitoAnnotations.initMocks(testClass);
        return base;
    }
}
