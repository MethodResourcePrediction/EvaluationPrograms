
import java.util.stream.IntStream;

import de.rherzog.master.thesis.annotations.Range;

public class MergeSort {
	private int[] array;
	private int[] tempMergArr;

	public static void main(String args[]) {
		// @Range(min = 0, max = 1000000)
//		int length = Integer.parseInt(args[0]);
//		int length = (int) (Math.random() * Integer.parseInt(args[0]));
		int length = Integer.parseInt(args[0]);

		new MergeSort().sort(length);
//		for (int i : inputArr) {
//			System.out.print(i);
//			System.out.print(" ");
//		}
	}

	public void sort(int length) {
		int[] inputArr = IntStream.range(0, length).map(n -> (int) (Math.random() * length)).toArray();

		this.array = inputArr;
		this.tempMergArr = new int[length];

		doMergeSort(0, length - 1);
	}

	private void doMergeSort(int lowerIndex, int higherIndex) {
		if (lowerIndex < higherIndex) {
			int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
			// Below step sorts the left side of the array
			doMergeSort(lowerIndex, middle);
			// Below step sorts the right side of the array
			doMergeSort(middle + 1, higherIndex);
			// Now merge both sides
			mergeParts(lowerIndex, middle, higherIndex);
		}
	}

	private void mergeParts(int lowerIndex, int middle, int higherIndex) {
		for (int i = lowerIndex; i <= higherIndex; i++) {
			tempMergArr[i] = array[i];
		}
		int i = lowerIndex;
		int j = middle + 1;
		int k = lowerIndex;
		while (i <= middle && j <= higherIndex) {
			if (tempMergArr[i] <= tempMergArr[j]) {
				array[k] = tempMergArr[i];
				i++;
			} else {
				array[k] = tempMergArr[j];
				j++;
			}
			k++;
		}
		while (i <= middle) {
			array[k] = tempMergArr[i];
			k++;
			i++;
		}

	}
}