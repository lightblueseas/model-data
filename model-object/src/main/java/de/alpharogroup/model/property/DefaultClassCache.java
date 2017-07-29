package de.alpharogroup.model.property;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.alpharogroup.model.api.ClassCache;
import de.alpharogroup.model.api.GetAndSet;

public class DefaultClassCache implements ClassCache
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