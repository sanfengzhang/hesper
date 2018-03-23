package com.escaf.esper.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
public @interface InjectEsper
{

	String[] statmentName() default "";

	String prefixStatmentName() default "";

}
