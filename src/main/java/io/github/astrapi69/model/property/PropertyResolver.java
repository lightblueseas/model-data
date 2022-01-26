/**
 * Copyright (C) 2015 Asterios Raptis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.astrapi69.model.property;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import io.github.astrapi69.model.api.IModel;
import lombok.extern.java.Log;

import org.apache.commons.lang3.StringUtils;

import io.github.astrapi69.model.api.ClassCache;
import io.github.astrapi69.model.api.GetAndSet;

/**
 * This class parses expressions to lookup or set a value on the object that is given. <br>
 * The supported expressions are: <div> "property": This could be a bean property with get and set
 * method. Or if a map is given as an object it will be lookup with the property as a key when there
 * is not get method for that property. </div> <div> "property1.property2": Both properties are
 * looked up as described above. If property1 evaluates to null then if there is a setMethod (or if
 * it is a map) and the Class of the property has a default constructor then the object will be
 * constructed and set on the object. </div> <div> "property.index": If the property is a List or
 * Array then the second property can be a index on that list like: 'mylist.0' this expression will
 * also map on a getProperty(index) or setProperty(index,value) methods. If the object is a List
 * then the list will grow automatically if the index is greater than the size </div> <div> Index or
 * map properties can also be written as: "property[index]" or "property[key]" </div> <div>
 * <strong>Note that the property resolver by default provides access to private members and
 * methods. If guaranteeing encapsulation of the target objects is a big concern, you should
 * consider using an alternative implementation.</strong> </div> <div> <strong>Note: If a property
 * evaluates to an instance of {@link IModel} then the expression should use '.object' to work with
 * its value.</strong> </div>
 *
 * @author jcompagner
 */
@Log
public final class PropertyResolver
{

	private final static ConcurrentHashMap<Object, ClassCache> applicationToClassesToGetAndSetters = new ConcurrentHashMap<>(
		2);
	private final static int CREATE_NEW_VALUE = 1;
	private static final String GET = "get";
	private static final String IS = "is";
	private final static int RESOLVE_CLASS = 2;
	private final static int RETURN_NULL = 0;

	/**
	 * Utility class: instantiation not allowed.
	 */
	private PropertyResolver()
	{
	}

	/**
	 * Clean up cache for this app.
	 *
	 * @param application
	 *            the application
	 */
	public static void destroy(Object application)
	{
		applicationToClassesToGetAndSetters.remove(application);
	}

	/**
	 * @param clz
	 * @param expression
	 * @return introspected field
	 */
	private static Field findField(final Class<?> clz, final String expression)
	{
		Field field = null;
		try
		{
			field = clz.getField(expression);
		}
		catch (final Exception e)
		{
			Class<?> tmp = clz;
			while (tmp != null && tmp != Object.class)
			{
				final Field[] fields = tmp.getDeclaredFields();
				for (final Field aField : fields)
				{
					if (aField.getName().equals(expression))
					{
						aField.setAccessible(true);
						return aField;
					}
				}
				tmp = tmp.getSuperclass();
			}
			log.log(Level.FINE, "Cannot find field " + clz + "." + expression);
		}
		return field;
	}

	/**
	 * @param clz
	 * @param expression
	 * @return The method for the expression null if not found
	 */
	private final static Method findGetter(final Class<?> clz, final String expression)
	{
		final String name = Character.toUpperCase(expression.charAt(0)) + expression.substring(1);
		Method method = null;
		try
		{
			method = clz.getMethod(GET + name, (Class[])null);
		}
		catch (final Exception ignored)
		{
		}
		if (method == null)
		{
			try
			{
				method = clz.getMethod(IS + name, (Class[])null);
			}
			catch (final Exception e)
			{
				log.log(Level.FINE, "Cannot find getter " + clz + "." + expression);
			}
		}
		return method;
	}

	private final static Method findMethod(final Class<?> clz, String expression)
	{
		if (expression.endsWith("()"))
		{
			expression = expression.substring(0, expression.length() - 2);
		}
		Method method = null;
		try
		{
			method = clz.getMethod(expression, (Class[])null);
		}
		catch (final Exception e)
		{
			log.log(Level.FINE, "Cannot find method " + clz + "." + expression);
		}
		return method;
	}

	private static ClassCache getClassesToGetAndSetters()
	{
		final Object key = PropertyResolver.class;

		ClassCache result = applicationToClassesToGetAndSetters.get(key);
		if (result == null)
		{
			final ClassCache tmpResult = applicationToClassesToGetAndSetters.putIfAbsent(key,
				result = new DefaultClassCache());
			if (tmpResult != null)
			{
				result = tmpResult;
			}
		}
		return result;
	}

	private final static GetAndSet getGetAndSetter(String exp, final Class<?> clz)
	{
		final ClassCache classesToGetAndSetters = getClassesToGetAndSetters();
		Map<String, GetAndSet> getAndSetters = classesToGetAndSetters.get(clz);
		if (getAndSetters == null)
		{
			getAndSetters = new ConcurrentHashMap<>(8);
			classesToGetAndSetters.put(clz, getAndSetters);
		}

		GetAndSet getAndSetter = getAndSetters.get(exp);
		if (getAndSetter == null)
		{
			Method method = null;
			Field field = null;
			if (exp.startsWith("["))
			{
				// if expression begins with [ skip method finding and use it as
				// a key/index lookup on a map.
				exp = exp.substring(1, exp.length() - 1);
			}
			else if (exp.endsWith("()"))
			{
				// if expression ends with (), don't test for setters just skip
				// directly to method finding.
				method = findMethod(clz, exp);
			}
			else
			{
				method = findGetter(clz, exp);
			}
			if (method == null)
			{
				if (List.class.isAssignableFrom(clz))
				{
					try
					{
						final int index = Integer.parseInt(exp);
						getAndSetter = new ListGetSet(index);
					}
					catch (final NumberFormatException ex)
					{
						// can't parse the exp as an index, maybe the exp was a
						// method.
						method = findMethod(clz, exp);
						if (method != null)
						{
							getAndSetter = new MethodGetAndSet(method,
								MethodGetAndSet.findSetter(method, clz), null);
						}
						else
						{
							field = findField(clz, exp);
							if (field != null)
							{
								getAndSetter = new FieldGetAndSetter(field);
							}
							else
							{
								throw new RuntimeException("The expression '" + exp
									+ "' is neither an index nor is it a method or field for the list "
									+ clz);
							}
						}
					}
				}
				else if (Map.class.isAssignableFrom(clz))
				{
					getAndSetter = new MapGetSet(exp);
				}
				else if (clz.isArray())
				{
					try
					{
						final int index = Integer.parseInt(exp);
						getAndSetter = new ArrayGetSet(clz.getComponentType(), index);
					}
					catch (final NumberFormatException ex)
					{
						if (exp.equals("length") || exp.equals("size"))
						{
							getAndSetter = new ArrayLengthGetSet();
						}
						else
						{
							throw new RuntimeException("Can't parse the expression '" + exp
								+ "' as an index for an array lookup");
						}
					}
				}
				else
				{
					field = findField(clz, exp);
					if (field == null)
					{
						method = findMethod(clz, exp);
						if (method == null)
						{
							final int index = exp.indexOf('.');
							if (index != -1)
							{
								final String propertyName = exp.substring(0, index);
								final String propertyIndex = exp.substring(index + 1);
								try
								{
									final int parsedIndex = Integer.parseInt(propertyIndex);
									// if so then it could be a getPropertyIndex(int)
									// and setPropertyIndex(int, object)
									final String name = Character.toUpperCase(
										propertyName.charAt(0)) + propertyName.substring(1);
									method = clz.getMethod(GET + name, new Class[] { int.class });
									getAndSetter = new ArrayPropertyGetSet(method, parsedIndex);
								}
								catch (final Exception e)
								{
									throw new RuntimeException("No get method defined for class: "
										+ clz + " expression: " + propertyName);
								}
							}
							else
							{
								// We do not look for a public FIELD because
								// that is not good programming with beans patterns
								throw new RuntimeException("No get method defined for class: " + clz
									+ " expression: " + exp);
							}
						}
						else
						{
							getAndSetter = new MethodGetAndSet(method,
								MethodGetAndSet.findSetter(method, clz), null);
						}
					}
					else
					{
						getAndSetter = new FieldGetAndSetter(field);
					}
				}
			}
			else
			{
				field = findField(clz, exp);
				getAndSetter = new MethodGetAndSet(method, MethodGetAndSet.findSetter(method, clz),
					field);
			}
			getAndSetters.put(exp, getAndSetter);
		}
		return getAndSetter;
	}

	/**
	 *
	 * @param expression
	 * @param start
	 * @return next dot index
	 */
	private static int getNextDotIndex(final String expression, final int start)
	{
		boolean insideBracket = false;
		for (int i = start; i < expression.length(); i++)
		{
			final char ch = expression.charAt(i);
			if (ch == '.' && !insideBracket)
			{
				return i;
			}
			else if (ch == '[')
			{
				insideBracket = true;
			}
			else if (ch == ']')
			{
				insideBracket = false;
			}
		}
		return -1;
	}

	/**
	 * Just delegating the call to the original getObjectAndGetSetter passing the object type as
	 * parameter.
	 *
	 * @param expression
	 *            the expression
	 * @param object
	 *            the object
	 * @param tryToCreateNull
	 *            the try to create null
	 * @return {@link ObjectAndGetSetter}
	 */
	private static ObjectAndGetSetter getObjectAndGetSetter(final String expression,
		final Object object, int tryToCreateNull)
	{
		return getObjectAndGetSetter(expression, object, tryToCreateNull, object.getClass());
	}


	/**
	 * Receives the class parameter also, since this method can resolve the type for some
	 * expression, only knowing the target class
	 *
	 * @param expression
	 * @param object
	 * @param tryToCreateNull
	 * @param clz
	 * @return {@link ObjectAndGetSetter}
	 */
	private static ObjectAndGetSetter getObjectAndGetSetter(final String expression,
		final Object object, final int tryToCreateNull, Class<?> clz)
	{
		String expressionBracketsSeperated = replaceAll(expression, "[", ".[").toString();
		int index = getNextDotIndex(expressionBracketsSeperated, 0);
		while (index == 0 && expressionBracketsSeperated.startsWith("."))
		{
			// eat dots at the beginning of the expression since they will confuse
			// later steps
			expressionBracketsSeperated = expressionBracketsSeperated.substring(1);
			index = getNextDotIndex(expressionBracketsSeperated, 0);
		}
		int lastIndex = 0;
		Object value = object;
		String exp = expressionBracketsSeperated;
		while (index != -1)
		{
			exp = expressionBracketsSeperated.substring(lastIndex, index);
			if (exp.length() == 0)
			{
				exp = expressionBracketsSeperated.substring(index + 1);
				break;
			}

			GetAndSet getAndSetter = null;
			try
			{
				getAndSetter = getGetAndSetter(exp, clz);
			}
			catch (final RuntimeException ex)
			{
				// expression by it self can't be found. try to find a
				// setPropertyByIndex(int,value) method
				index = getNextDotIndex(expressionBracketsSeperated, index + 1);
				if (index != -1)
				{
					final String indexExpression = expressionBracketsSeperated.substring(lastIndex,
						index);
					getAndSetter = getGetAndSetter(indexExpression, clz);
				}
				else
				{
					exp = expressionBracketsSeperated.substring(lastIndex);
					break;
				}
			}
			Object newValue = null;
			if (value != null)
			{
				newValue = getAndSetter.getValue(value);
			}
			if (newValue == null)
			{
				if (tryToCreateNull == CREATE_NEW_VALUE)
				{
					newValue = getAndSetter.newValue(value);
					if (newValue == null)
					{
						return null;
					}
				}
				else if (tryToCreateNull == RESOLVE_CLASS)
				{
					clz = getAndSetter.getTargetClass();
				}
				else
				{
					return null;
				}
			}
			value = newValue;
			if (value != null)
			{
				// value can be null if we are in the RESOLVE_CLASS
				clz = value.getClass();
			}

			lastIndex = index + 1;
			index = getNextDotIndex(expressionBracketsSeperated, lastIndex);
			if (index == -1)
			{
				exp = expressionBracketsSeperated.substring(lastIndex);
				break;
			}
		}
		final GetAndSet getAndSetter = getGetAndSetter(exp, clz);
		return new ObjectAndGetSetter(getAndSetter, value);
	}

	/**
	 * Gets the property class.
	 *
	 * @param <T>
	 *            the generic type
	 * @param expression
	 *            the expression
	 * @param clz
	 *            the class
	 * @return class of the target Class property expression
	 * @throws RuntimeException
	 *             if class cannot be resolved
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getPropertyClass(final String expression, final Class<?> clz)
	{
		final ObjectAndGetSetter setter = getObjectAndGetSetter(expression, null, RESOLVE_CLASS,
			clz);
		if (setter == null)
		{
			throw new RuntimeException("No Class returned for expression: " + expression
				+ " for getting the target class of: " + clz);
		}
		return (Class<T>)setter.getTargetClass();
	}

	/**
	 * Gets the property class.
	 *
	 * @param expression
	 *            the expression
	 * @param object
	 *            the object
	 * @return class of the target property object
	 * @throws RuntimeException
	 *             if the cannot be resolved
	 */
	public final static Class<?> getPropertyClass(final String expression, final Object object)
	{
		final ObjectAndGetSetter setter = getObjectAndGetSetter(expression, object, RESOLVE_CLASS);
		if (setter == null)
		{
			throw new RuntimeException("Null object returned for expression: " + expression
				+ " for getting the target class of: " + object);
		}
		return setter.getTargetClass();
	}

	/**
	 * Gets the property field.
	 *
	 * @param expression
	 *            the expression
	 * @param object
	 *            the object
	 * @return Field for the property expression
	 * @throws RuntimeException
	 *             if there is no such field
	 */
	public final static Field getPropertyField(final String expression, final Object object)
	{
		final ObjectAndGetSetter setter = getObjectAndGetSetter(expression, object, RESOLVE_CLASS);
		if (setter == null)
		{
			throw new RuntimeException("Null object returned for expression: " + expression
				+ " for getting the target class of: " + object);
		}
		return setter.getField();
	}

	/**
	 * Gets the property getter.
	 *
	 * @param expression
	 *            the expression
	 * @param object
	 *            the object
	 * @return Getter method for the property expression
	 * @throws RuntimeException
	 *             if there is no getter method
	 */
	public final static Method getPropertyGetter(final String expression, final Object object)
	{
		final ObjectAndGetSetter setter = getObjectAndGetSetter(expression, object, RESOLVE_CLASS);
		if (setter == null)
		{
			throw new RuntimeException("Null object returned for expression: " + expression
				+ " for getting the target class of: " + object);
		}
		return setter.getGetter();
	}


	/**
	 * Gets the property setter.
	 *
	 * @param expression
	 *            the expression
	 * @param object
	 *            the object
	 * @return Setter method for the property expression
	 * @throws RuntimeException
	 *             if there is no setter method
	 */
	public final static Method getPropertySetter(final String expression, final Object object)
	{
		final ObjectAndGetSetter setter = getObjectAndGetSetter(expression, object, RESOLVE_CLASS);
		if (setter == null)
		{
			throw new RuntimeException("Null object returned for expression: " + expression
				+ " for getting the target class of: " + object);
		}
		return setter.getSetter();
	}

	/**
	 * Looks up the value from the object with the given expression. If the expression, the object
	 * itself or one property evaluates to null then a null will be returned.
	 *
	 * @param expression
	 *            The expression string with the property to be lookup.
	 * @param object
	 *            The object which is evaluated.
	 * @return The value that is evaluated. Null something in the expression evaluated to null.
	 */
	public final static Object getValue(final String expression, final Object object)
	{
		if (expression == null || expression.equals("") || object == null)
		{
			return object;
		}

		final ObjectAndGetSetter getter = getObjectAndGetSetter(expression, object, RETURN_NULL);
		if (getter == null)
		{
			return null;
		}

		return getter.getValue();
	}

	/**
	 * Replace all occurrences of one string replaceWith another string.
	 *
	 * @param s
	 *            The string to process
	 * @param searchFor
	 *            The value to search for
	 * @param replaceWith
	 *            The value to searchFor replaceWith
	 * @return The resulting string with searchFor replaced with replaceWith
	 */
	public static CharSequence replaceAll(final CharSequence s, final CharSequence searchFor,
		CharSequence replaceWith)
	{
		if (s == null)
		{
			return null;
		}

		// If searchFor is null or the empty string, then there is nothing to
		// replace, so returning s is the only option here.
		if ((searchFor == null) || "".equals(searchFor))
		{
			return s;
		}

		// If replaceWith is null, then the searchFor should be replaced with
		// nothing, which can be seen as the empty string.
		if (replaceWith == null)
		{
			replaceWith = "";
		}

		final String searchString = searchFor.toString();
		// Look for first occurrence of searchFor
		int matchIndex = search(s, searchString, 0);
		if (matchIndex == -1)
		{
			// No replace operation needs to happen
			return s;
		}
		else
		{
			// Allocate a AppendingStringBuffer that will hold one replacement
			// with a
			// little extra room.
			int size = s.length();
			final int replaceWithLength = replaceWith.length();
			final int searchForLength = searchFor.length();
			if (replaceWithLength > searchForLength)
			{
				size += (replaceWithLength - searchForLength);
			}
			final StringBuilder buffer = new StringBuilder(size + 16);

			int pos = 0;
			do
			{
				// Append text up to the match
				buffer.append(s.subSequence(pos, matchIndex));

				// Add replaceWith text
				buffer.append(replaceWith);

				// Find next occurrence, if any
				pos = matchIndex + searchForLength;
				matchIndex = search(s, searchString, pos);
			}
			while (matchIndex != -1);

			// Add tail of s
			buffer.append(s.subSequence(pos, s.length()));

			// Return processed buffer
			return buffer;
		}
	}

	private static int search(final CharSequence s, final String searchString, final int pos)
	{
		if (s instanceof String)
		{
			return ((String)s).indexOf(searchString, pos);
		}
		else if (s instanceof StringBuffer)
		{
			return ((StringBuffer)s).indexOf(searchString, pos);
		}
		else if (s instanceof StringBuilder)
		{
			return ((StringBuilder)s).indexOf(searchString, pos);
		}
		else
		{
			return s.toString().indexOf(searchString, pos);
		}
	}

	/**
	 * Sets the {@link ClassCache} for the given application.
	 *
	 * If the Application is null then it will be the default if no application is found. So if you
	 * want to be sure that your {@link ClassCache} is handled in all situations then call this
	 * method twice with your implementations. One time for the application and the second time with
	 * null.
	 *
	 * @param application
	 *            to use or null if the default must be set.
	 * @param classCache
	 *            the class cache
	 */
	public static void setClassCache(final Object application, final ClassCache classCache)
	{
		if (application != null)
		{
			applicationToClassesToGetAndSetters.put(application, classCache);
		}
		else
		{
			applicationToClassesToGetAndSetters.put(PropertyResolver.class, classCache);
		}
	}

	/**
	 * Set the value on the object with the given expression. If the expression can't be evaluated
	 * then a RuntimeException will be thrown. If a null object is encountered then it will try to
	 * generate it by calling the default constructor and set it on the object.
	 *
	 * The value will be tried to convert to the right type with the given converter.
	 *
	 * @param expression
	 *            The expression string with the property to be set.
	 * @param object
	 *            The object which is evaluated to set the value on.
	 * @param value
	 *            The value to set.
	 * @throws RuntimeException
	 *             is thrown if expression is empty or the object is null or the setter could not be
	 *             found
	 */
	public final static void setValue(final String expression, final Object object,
		final Object value)
	{
		if (StringUtils.isEmpty(expression))
		{
			throw new RuntimeException(
				"Empty expression setting value: " + value + " on object: " + object);
		}
		if (object == null)
		{
			throw new RuntimeException(
				"Attempted to set property value on a null object. Property expression: "
					+ expression + " Value: " + value);
		}

		final ObjectAndGetSetter setter = getObjectAndGetSetter(expression, object,
			CREATE_NEW_VALUE);
		if (setter == null)
		{
			throw new RuntimeException("Null object returned for expression: " + expression
				+ " for setting value: " + value + " on: " + object);
		}
		setter.setValue(value);
	}

	private static class DefaultClassCache implements ClassCache
	{
		private final ConcurrentHashMap<Class<?>, Map<String, GetAndSet>> map = new ConcurrentHashMap<>(
			16);

		@Override
		public Map<String, GetAndSet> get(Class<?> clz)
		{
			return map.get(clz);
		}

		@Override
		public void put(Class<?> clz, Map<String, GetAndSet> values)
		{
			map.put(clz, values);
		}
	}
}
