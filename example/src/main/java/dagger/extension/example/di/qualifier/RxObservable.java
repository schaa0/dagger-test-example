package dagger.extension.example.di.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface RxObservable {
    String value() default "";
    class Type {
        public static final String SEARCH = "search";
        public static final String PAGE = "page";
    }
}
