/*
 ***************************************************************************************
 *  Copyright (C) 2006 EsperTech, Inc. All rights reserved.                            *
 *  http://www.espertech.com/esper                                                     *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 ***************************************************************************************
 */
package com.han.esper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SupportBean implements Serializable
{
	private String theString;

	private boolean boolPrimitive;
	private int intPrimitive;

	public SupportBean()
	{
	}

	public SupportBean(String theString, int intPrimitive)
	{
		this.theString = theString;
		this.intPrimitive = intPrimitive;
	}

	public String getTheString()
	{
		return theString;
	}

	public boolean isBoolPrimitive()
	{
		return boolPrimitive;
	}

	public int getIntPrimitive()
	{
		return intPrimitive;
	}

	public void setTheString(String theString)
	{
		this.theString = theString;
	}

	public void setBoolPrimitive(boolean boolPrimitive)
	{
		this.boolPrimitive = boolPrimitive;
	}

	public void setIntPrimitive(int intPrimitive)
	{
		this.intPrimitive = intPrimitive;
	}

	public SupportBean getThis()
	{
		return this;
	}

	public String toString()
	{
		return this.getClass().getSimpleName() + "(" + theString + ", " + intPrimitive + ")";
	}

	public static SupportBean[] getBeansPerIndex(SupportBean[] beans, int[] indexes)
	{
		if (indexes == null)
		{
			return null;
		}
		SupportBean[] array = new SupportBean[indexes.length];
		for (int i = 0; i < indexes.length; i++)
		{
			array[i] = beans[indexes[i]];
		}
		return array;
	}

	public static Object[] getOAStringAndIntPerIndex(SupportBean[] beans, int[] indexes)
	{
		SupportBean[] arr = getBeansPerIndex(beans, indexes);
		if (arr == null)
		{
			return null;
		}
		return toOAStringAndInt(arr);
	}

	private static Object[] toOAStringAndInt(SupportBean[] arr)
	{
		Object[][] values = new Object[arr.length][];
		for (int i = 0; i < values.length; i++)
		{
			values[i] = new Object[] { arr[i].getTheString(), arr[i].getIntPrimitive() };
		}
		return values;
	}
}
