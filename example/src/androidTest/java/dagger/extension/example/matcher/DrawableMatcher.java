package dagger.extension.example.matcher;

import android.graphics.drawable.BitmapDrawable;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DrawableMatcher extends TypeSafeMatcher<View> {

    static final int NONE = -24;
    static final int ANY = 0;
    private final int type;

    DrawableMatcher(int type) {
        this.type = type;
    }

    @Override
    protected boolean matchesSafely(View item) {
        if (!(item instanceof ImageView)) {
            return false;
        }
        boolean hasDrawable = ((ImageView)item).getDrawable() != null;
        if (((ImageView) item).getDrawable() instanceof BitmapDrawable) {
            hasDrawable = ((BitmapDrawable) ((ImageView) item).getDrawable()).getBitmap() != null;
        }
        return (type == NONE) ? !hasDrawable : hasDrawable;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(type == NONE ? "has no drawable" : "has drawable");
    }

    public static TypeSafeMatcher<View> hasDrawable() {
        return new DrawableMatcher(ANY);
    }

    public static TypeSafeMatcher<View> hasNoDrawable() {
        return new DrawableMatcher(NONE);
    }

}
