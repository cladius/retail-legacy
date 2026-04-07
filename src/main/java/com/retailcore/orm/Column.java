package com.retailcore.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name() default "";
    boolean primaryKey() default false;
    boolean autoIncrement() default false;
    boolean nullable() default true;
    int length() default 255;
    String defaultValue() default "";
    boolean insertable() default true;
    boolean updatable() default true;
}
