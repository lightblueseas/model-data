package de.alpharogroup.model.property;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.alpharogroup.model.api.GetAndSet;

public abstract class AbstractGetAndSet implements GetAndSet {
	protected static final String GET = "get";
	protected static final String IS = "is";
	protected static final String SET = "set";
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field getField() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Method getGetter() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Method getSetter() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getTargetClass() {
		return null;
	}
}