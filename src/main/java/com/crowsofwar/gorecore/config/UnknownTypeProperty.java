package com.crowsofwar.gorecore.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a property where the type is unknown. The type must be assumed based on the
 * configuration file. Use {@link #as(ObjectLoader)} or a similar method to find the actual value by
 * casting.
 * 
 * @author CrowsOfWar
 */
public class UnknownTypeProperty {
	
	private final Object object;
	private final String name;
	
	public UnknownTypeProperty(String name, Object obj) {
		this.name = name;
		this.object = obj;
	}
	
	public Object getObject() {
		return object;
	}
	
	public <T> T as(ObjectLoader<T> factory) {
		if (!(object instanceof Map)) throw new ConfigException(name + " isn't a Dictionary");
		return factory.load(new Configuration((Map) object));
	}
	
	public String asString() {
		return object.toString();
	}
	
	public int asInt() {
		try {
			return (int) object;
		} catch (ClassCastException e) {
			throw new ConfigException(name + " isn't an integer");
		}
	}
	
	public boolean asBoolean() {
		try {
			return (boolean) object;
		} catch (ClassCastException e) {
			throw new ConfigException(name + " isn't a boolean");
		}
	}
	
	public double asDouble() {
		try {
			return (double) object;
		} catch (ClassCastException e) {
			throw new ConfigException(name + " isn't a double");
		}
	}
	
	public <T> List<T> asList(ListLoader<T> factory) {
		if (!(object instanceof List)) throw new ConfigException(name + " isn't a List");
		
		List<T> out = new ArrayList<>();
		
		List<?> list = (List<?>) object;
		for (Object obj : list) {
			out.add(factory.load(obj));
		}
		
		return out;
	}
	
	public List<String> asStringList() {
		return asList(obj -> (String) obj);
	}
	
	public Configuration asMapping() {
		return new Configuration((Map) object);
	}
	
}