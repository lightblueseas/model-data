package de.alpharogroup.model.api;

import java.io.Serializable;

/**
 * The interface {@link Detachable} provides the ability to detach an object that was previously
 * attached to a context. This reduces the amount of state required by an object. This makes the
 * object cheaper to replicate in a clustered environment.
 */
public interface Detachable extends Serializable
{

	/**
	 * Detach an object.
	 */
	void detach();
}
