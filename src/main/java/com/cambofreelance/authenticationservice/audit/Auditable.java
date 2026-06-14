package com.cambofreelance.authenticationservice.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    String action();
    String module();
    String description() default "";
    /** Entity class to fetch before the method runs for old-value capture. Void = disabled. */
    Class<?> entityClass() default Void.class;
}
