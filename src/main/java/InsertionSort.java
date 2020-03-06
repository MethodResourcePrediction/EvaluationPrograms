
import java.util.stream.IntStream;

import de.rherzog.master.thesis.annotations.Range;

public class InsertionSort {
	public static void main(String args[]) {
		// @Range(min = 0, max = 20000)
		int inputLength = Integer.parseInt(args[0]);
		
		int[] arr1 = IntStream.range(0, inputLength).map(n -> (int) (Math.random() * inputLength)).toArray();
//		int[] arr1 = { 10, 34, 2, 56, 7, 67, 88, 42 };
		int[] arr2 = doInsertionSort(arr1);
//		for (int i : arr2) {
//			System.out.print(i);
//			System.out.print(", ");
//		}
	}

	public static int[] doInsertionSort(int[] input) {
		int temp;
		for (int i = 1; i < input.length; i++) {
			for (int j = i; j > 0; j--) {
				if (input[j] < input[j - 1]) {
					temp = input[j];
					input[j] = input[j - 1];
					input[j - 1] = temp;
				}
			}
		}
		return input;
	}
}