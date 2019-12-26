package edivad.dimstorage.tools.extra;

import java.util.function.Function;

public class ArrayUtils {

	/**
	 * Counts elements in the array that conform to the Function check.
	 *
	 * @param array The array to check.
	 * @param check The Function to apply to each element.
	 * @param <T>   What we are dealing with.
	 * @return The count.
	 */
	public static <T> int count(T[] array, Function<T, Boolean> check)
	{
		int counter = 0;
		for(T value : array)
		{
			if(check.apply(value))
			{
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Fills the array with the specified value.
	 *
	 * @param array Array to fill.
	 * @param value Value to fill with.
	 * @param <T>   What we are dealing with.
	 */
	public static <T> T[] fill(T[] array, T value)
	{
		for(int i = 0; i < array.length; i++)
		{
			array[i] = value;
		}
		return array;
	}
}
