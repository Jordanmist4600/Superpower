package me.simplicitee.superpower.configuration;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface Configure {

	/**
	 * The config path for this field
	 * @return config path
	 */
	public String value();
}
