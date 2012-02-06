package net.time4tea;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

@java.lang.annotation.Documented
@java.lang.annotation.Retention(CLASS)
@java.lang.annotation.Target({FIELD, METHOD, PARAMETER, LOCAL_VARIABLE})

public @interface Annotation {

}
