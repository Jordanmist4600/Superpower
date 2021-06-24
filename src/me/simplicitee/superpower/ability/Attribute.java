package me.simplicitee.superpower.ability;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface Attribute {

	/**
	 * The name for this attribute
	 * @return attribute name
	 */
	public String value();
}
