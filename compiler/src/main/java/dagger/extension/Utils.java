package dagger.extension;

import javax.lang.model.util.Elements;

class Utils {

    static boolean isAndroidTest(Elements elements)
    {
        return elements.getPackageElement("android.test") != null;
    }

    static boolean isUnitTest(Elements elements)
    {
        return elements.getPackageElement("org.junit") != null;
    }

}
