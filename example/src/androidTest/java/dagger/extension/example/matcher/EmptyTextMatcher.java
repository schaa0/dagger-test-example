package dagger.extension.example.matcher;

import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class EmptyTextMatcher extends TypeSafeMatcher<View> {
    @Override
    protected boolean matchesSafely(View view) {
        return view instanceof TextView && ((TextView)view).getText().toString().isEmpty();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("text is empty");
    }

    public static EmptyTextMatcher emptyText() {
        return new EmptyTextMatcher();
    }

}
