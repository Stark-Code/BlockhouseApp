package com.movsoftware.blockhouse.route_tracker.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //can be used in method only.
public @interface RequiresPermission {
    String[] value() default {};
}


