package me.timothy.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public abstract class JSONCompatible {
	public abstract void loadFrom(JSONObject jsonObject);
	public abstract void saveTo(JSONObject jsonObject);
	
	@Override
	public abstract boolean equals(Object o);
	@Override
	public abstract int hashCode();
	
	public static int getInt(JSONObject obj, String key) {
		return ((Number) obj.get(key)).intValue();
	}
	
	public static float getFloat(JSONObject obj, String key) {
		return ((Number) obj.get(key)).floatValue();
	}
	
	public static double getDouble(JSONObject obj, String key) {
		return ((Number) obj.get(key)).doubleValue();
	}
	
	public static String getString(JSONObject obj, String key) {
		return ((String) obj.get(key)) != null ? ((String) obj.get(key)).replace("\\n", "\n") : null;
	}
	
	public static JSONObject getObject(JSONObject obj, String key) {
		return (JSONObject) obj.get(key);
	}
	public static JSONArray getArray(JSONObject obj, String key) {
		return (JSONArray) obj.get(key);
	}
	
	public static boolean getBoolean(JSONObject obj, String key) {
		return obj.get(key) == Boolean.TRUE;
	}
}
