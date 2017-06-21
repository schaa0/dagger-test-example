package dagger.extension.example.di.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface RxScheduler {
    String value() default "";
    class Type {
        public static final String MAIN = "main";
        public static final String NETWORK = "network";
    }
}
