package com.uiterwyk.touchswirl.app.utils;

/**
 * RandomNumbers is a utility to prepopulate an array with random numbers
 * This avoids the cost of doing it affecting fps
 * @author johnuiterwyk
 *
 */
public class RandomNumbers {
	private static float[] numbers;
	private static int index = 0;
	private static int size = 30000;
	public static float getNext()
	{
		if(numbers == null)
		{
			numbers = new float[size];
			for(int i = 0; i<size;i++)
			{
				numbers[i] = (float)Math.random();
			}
		}
		index++;
		if(index >= size) index =0;
		return numbers[index];
	}

}
