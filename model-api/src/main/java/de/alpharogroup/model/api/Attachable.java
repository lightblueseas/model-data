package de.alpharogroup.model.api;

import java.io.Serializable;

/**
 * The interface {@link Attachable} provides the ability to attach an object from a context. This
 * reduces the amount of state required by an object. This makes the object cheaper to replicate in
 * a clustered environment.
 */
public interface Attachable extends Serializable
{

	/**
	 * Attach an object.
	 */
	void attach();
}
