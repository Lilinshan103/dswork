package dswork.core.util;

/**
 * 将long、int、float数组转化为Long、Integer、FLoat数组
 */
public class CollectionUtil
{
	public static Long[] toLongArray(long[] array)
	{
		Long[] arr = new Long[array.length];
		for (int i = 0; i < array.length; i++)
		{
			arr[i] = new Long(array[i]);
		}
		return arr;
	}

	public static Integer[] toIntegerArray(int[] array)
	{
		Integer[] arr = new Integer[array.length];
		for (int i = 0; i < array.length; i++)
		{
			arr[i] = new Integer(array[i]);
		}
		return arr;
	}

	public static Float[] toFloatArray(float[] array)
	{
		Float[] arr = new Float[array.length];
		for (int i = 0; i < array.length; i++)
		{
			arr[i] = new Float(array[i]);
		}
		return arr;
	}
}
