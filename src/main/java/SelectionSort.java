
import java.util.stream.IntStream;

import de.rherzog.master.thesis.annotations.Range;

public class SelectionSort {
	public static void main(String args[]) {
		// @Range(min = 0, max = 10000)
		int inputLength = Integer.parseInt(args[0]);

		int[] arr1 = IntStream.range(0, inputLength).map(n -> (int) (Math.random() * inputLength)).toArray();
//		int[] arr1 = { 10, 34, 2, 56, 7, 67, 88, 42 };
		doSelectionSort(arr1);
//		for (int i : arr2) {
//			System.out.print(i);
//			System.out.print(", ");
//		}
	}

	private static void doSelectionSort(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			int index = i;
			for (int j = i + 1; j < arr.length; j++)
				if (arr[j] < arr[index])
					index = j;

			int smallerNumber = arr[index];
			arr[index] = arr[i];
			arr[i] = smallerNumber;
		}
//		return arr;
	}
}