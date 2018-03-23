package com.escaf.esper.spring;

import java.awt.List;
import java.util.Map;

public class ClazzUtil
{

	public static Class<?> getClazzType(String type)
	{
		switch (type)
		{
		case "String":

			return String.class;
		case "int":
			return int.class;
		case "long":
			return long.class;
		case "boolean":
			return boolean.class;
		case "char":
			return char.class;
		case "byte":
			return byte.class;
		case "float":
			return float.class;
		case "double":
			return double.class;
		case "List":
			return List.class;
		case "Map":
			return Map.class;

		default:
			throw new IllegalArgumentException("unsupport class type=[" + type + "]");
		}

	}

}
