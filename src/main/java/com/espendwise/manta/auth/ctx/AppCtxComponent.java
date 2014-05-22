package com.espendwise.manta.auth.ctx;


public @interface AppCtxComponent {

    String name() default "";

    String dependency() default "";
}
