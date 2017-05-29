package dagger.extension.example.di.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Andy on 29.05.2017.
 */

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface ErrorDialog {
    String value() default "";
}
