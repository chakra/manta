package com.espendwise.manta.spi;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AutoClean { 	String[] value() default {}; Class[] controller() default {}; }

