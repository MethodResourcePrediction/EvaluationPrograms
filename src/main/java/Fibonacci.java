
public class Fibonacci {
	static int n1 = 0, n2 = 1, n3 = 0;

	static void fibonacci(int count) throws InterruptedException {
		if (count > 0) {
			n3 = n1 + n2;
			n1 = n2;
			n2 = n3;
			System.out.print(" " + n3);
			Thread.sleep(n3);
			fibonacci(count - 1);
		}
	}

	public static void main(String args[]) {
		int count = 17;
		System.out.print(n1 + " " + n2);// printing 0 and 1
		try {
			fibonacci(count - 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // n-2 because 2 numbers are already printed
	}
}
