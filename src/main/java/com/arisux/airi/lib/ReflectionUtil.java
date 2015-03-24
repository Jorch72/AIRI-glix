package com.arisux.airi.lib;

import java.lang.reflect.Field;

import com.arisux.airi.AIRI;

public class ReflectionUtil
{
	public static double getDouble(Object obj, String deobfName, String obfName)
	{
		return (double) get(obj, deobfName, obfName);
	}
	
	public static float getFloat(Object obj, String deobfName, String obfName)
	{
		return (float) get(obj, deobfName, obfName);
	}
	
	public static int getInt(Object obj, String deobfName, String obfName)
	{
		return (int) get(obj, deobfName, obfName);
	}
	
	public static boolean getBoolean(Object obj, String deobfName, String obfName)
	{
		return (boolean) get(obj, deobfName, obfName);
	}
	
	public static long getLong(Object obj, String deobfName, String obfName)
	{
		return (long) get(obj, deobfName, obfName);
	}
	
	public static byte getByte(Object obj, String deobfName, String obfName)
	{
		return (byte) get(obj, deobfName, obfName);
	}
	
	public static String getString(Object obj, String deobfName, String obfName)
	{
		return (String) get(obj, deobfName, obfName);
	}
	
	public static void set(Object obj, String deobfName, String obfName, Object value)
	{
		String fieldName = ModUtil.isDevEnvironment() ? deobfName : obfName;

		try
		{
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(obj, value);
		}
		catch (Exception e)
		{
			AIRI.logger.bug("Failed setting field %s to %s: %s", fieldName, value, e);
		}
	}
	
	public static Object get(Object obj, String deobfName, String obfName)
	{
		String fieldName = ModUtil.isDevEnvironment() ? deobfName : obfName;
		
		try
		{
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		}
		catch (Exception e)
		{
			AIRI.logger.bug("Failed getting field %s: %s", fieldName, e);
		}
		return null;
	}
}
