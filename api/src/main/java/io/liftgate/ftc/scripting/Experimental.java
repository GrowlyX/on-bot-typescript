package io.liftgate.ftc.scripting;

import java.lang.annotation.*;

/**
 * Features marked with {@link Experimental} may not work as
 * intended. Implementations of these features may not be created yet.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE,
        ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE, ElementType.TYPE_PARAMETER
})
public @interface Experimental {}
