
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;

import de.rherzog.master.thesis.annotations.Range;

public class Sleep {
	public static void main(String[] args) throws InterruptedException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (args.length < 1) {
			throw new IllegalArgumentException("Missing argument: method name");
		}
		String methodName = args[0];

		Sleep sleep = new Sleep();
		Method[] methods = Sleep.class.getMethods();
		execute(methods, sleep, methodName, args);
	}

	/**
	 * Sleep a constant amount of time<br/>
	 * Expected: constant
	 */
	public void a(String[] args) throws InterruptedException {
		Thread.sleep(1000);
	}

	/**
	 * Sleep a constant amount of time loaded from a class variable<br/>
	 * Expected: linear in loaded variable
	 */
	public void b(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 2000)
		long sleep = Long.valueOf(args[0]);
		Thread.sleep(sleep);
	}

	/**
	 * Sleep a constant amount of time loaded from a class variable and multiplied
	 * by some value<br/>
	 * Expected: linear in multiplication
	 */
	public void c(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 1000)
		long sleep = Long.valueOf(args[0]);
		Thread.sleep(sleep * 2);
	}

	/**
	 * Sleep a linear amount of time depending on a local variable<br/>
	 * Expected: linear in multiplication
	 */
	public void d(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 1000)
		long sleep = Long.valueOf(args[0]);

		// @Range(min = 0, max = 5)
		double fraction = Double.valueOf(args[1]);

		Thread.sleep((long) (fraction * sleep));
	}

	/**
	 * Sleep a random amount of time depending on a local variable<br/>
	 * Expected: random (not predictable since the slice would evaluate another
	 * random value)
	 */
	public void e(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 3000)
		long sleep = Long.valueOf(args[0]);

		// @Range(min = 0, max = 1)
		double times = Math.random();
		Thread.sleep((long) (times * sleep));
	}

	/**
	 * Sleep a constant time, depending on boolean parameter (if true only)<br/>
	 * Expected: constant 200 or 0
	 */
	public void f(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 2)
		boolean b = Boolean.parseBoolean(args[0]);

		if (b) {
			Thread.sleep(200);
		}
	}

	/**
	 * Sleep a constant time, depending on boolean parameter (if true only)<br/>
	 * Expected: constant 200 or 400
	 */
	public void g(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 2)
		boolean b = Boolean.parseBoolean(args[0]);

		if (b) {
			Thread.sleep(200);
		} else {
			Thread.sleep(400);
		}
	}

	/**
	 * Sleep a time given by argument sleep only if argument b is true<br/>
	 * Expected: linear
	 */
	public void h(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 2)
		boolean b = Boolean.parseBoolean(args[0]);

		// @Range(min = 0, max = 3000)
		long sleep = Long.parseLong(args[1]);

		if (b) {
			Thread.sleep(sleep);
		}
	}

	/**
	 * Sleep {@code count} times for {@code sleep} ms within a loop.<br/>
	 * Expected: count * sleep
	 */
	public void i(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 5)
		int count = Integer.parseInt(args[0]);

		// @Range(min = 0, max = 1000)
		long sleep = Long.parseLong(args[1]);

		for (int i = 0; i < count; i++) {
			Thread.sleep(sleep);
		}
	}

	/**
	 * Sleep {@code count} times for {@code sleep} ms within a loop.<br/>
	 * Expected: count * sleep
	 */
	public void j(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 5)
		int count = Integer.parseInt(args[0]);

		// @Range(min = 0, max = 1000)
		long sleep = Long.parseLong(args[1]);

		int i = 0;
		while (i < 10) {
			Thread.sleep(sleep);

			if (i > count) {
				break;
			}
			i++;
		}
	}

	/**
	 * Sleep {@code count} * {@code count} times for {@code sleep} ms within a
	 * loop.<br/>
	 * Expected: count^2 * sleep
	 */
	public void k(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 5)
		int count = Integer.parseInt(args[0]);

		// @Range(min = 0, max = 500)
		long sleep = Long.parseLong(args[1]);

		for (int i = 0; i < count; i++) {
			for (int j = 0; j < count; j++) {
				Thread.sleep(sleep);
			}
		}
	}

	/**
	 * Sleep {@code count} times for {@code sleep} ms in a while loop.<br/>
	 * Expected: count * sleep
	 */
	public void l(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 2000)
		long sleep = Long.parseLong(args[1]);

		Iterator<?> iterator = l_getIterator(args);
		while (iterator.hasNext()) {
			iterator.next();
			Thread.sleep(sleep);
		}
	}

	private Iterator<?> l_getIterator(String[] args) {
		int count = Integer.parseInt(args[0]);
		return IntStream.range(0, count).iterator();
	}

	/**
	 * Sleep {@code count} times for {@code sleep} ms within a lambda.<br/>
	 * Expected: Not possible (no time consuming actions since the lambda is a own
	 * method)
	 */
	public void m(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 10)
		int count = Integer.parseInt(args[0]);

		// @Range(min = 0, max = 1000)
		long sleep = Long.parseLong(args[1]);

		IntStream.range(0, count).forEach(n -> {
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
			}
		});
	}

	/**
	 * Two for-loops with one waiting for count * sleep ms and the second with 5 *
	 * sleep<br/>
	 * Expected: count * sleep + 5 * sleep + 1000
	 */
	public void n(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 10)
		int count = Integer.parseInt(args[0]);

		// @Range(min = 0, max = 500)
		long sleep = Long.parseLong(args[1]);

		for (int i = 0; i < count; i++) {
			Thread.sleep(sleep);
		}

		for (int i = 0; i < 5; i++) {
			Thread.sleep(sleep);
		}
		Thread.sleep(1000);
	}

	/**
	 * A single for-loop with a fixed number of 5 iterations and sleep time
	 * Expected: 5 * sleep
	 */
	public void o(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 1000)
		long sleep = Long.parseLong(args[0]);

		for (int i = 0; i < 5; i++) {
			Thread.sleep(sleep);
		}
	}

	/**
	 * A single for-loop with a fixed number of 5 iterations and sleep time
	 * Expected: 5 * sleep + 1000
	 */
	public void p(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 1000)
		long sleep = Long.parseLong(args[0]);

		for (int i = 0; i < 5; i++) {
			Thread.sleep(sleep);
		}
		Thread.sleep(1000);
	}

	/**
	 * Recursive method call.<br/>
	 * Expected: count * sleep
	 */
	public void q(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 10)
		int count = Integer.parseInt(args[0]);

		// @Range(min = 0, max = 1000)
		long sleep = Long.parseLong(args[1]);

		if (count == 0) {
			return;
		}
		Thread.sleep(sleep);

		q(new String[] { String.valueOf(count - 1), String.valueOf(sleep) });
	}

	/**
	 * Recursive method call.<br/>
	 * Expected: 2 * count * 2 * sleep
	 */
	public void r(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 5)
		int count = Integer.parseInt(args[0]);

		// @Range(min = 0, max = 500)
		long sleep = Long.parseLong(args[1]);

		if (count == 0) {
			return;
		}
		Thread.sleep(sleep);
		r(new String[] { String.valueOf(count - 1), String.valueOf(sleep) });
		r(new String[] { String.valueOf(count - 1), String.valueOf(sleep) });
		Thread.sleep(sleep);
	}

	/**
	 * Recursive and loop method call.<br/>
	 * Expected: count ^ 2 * sleep
	 */
	public void s(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 5)
		int count = Integer.parseInt(args[0]);

		// @Range(min = 0, max = 500)
		long sleep = Long.parseLong(args[1]);

		if (count == 0) {
			return;
		}
		for (int i = 0; i < count; i++) {
			Thread.sleep(sleep);
		}
		s(new String[] { String.valueOf(count - 1), String.valueOf(sleep) });
	}

	/**
	 * Array length Expected: args.length * 100
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public void t(String[] args) throws InterruptedException {
		Iterator<String> iterator = Arrays.asList(args).iterator();
		while (iterator.hasNext()) {
			iterator.next();
			Thread.sleep(100);
		}
	}

	/**
	 * Expected: a + b + c
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public void u(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 2000)
		int a = Integer.parseInt(args[0]);
		Thread.sleep(a);

		// @Range(min = 0, max = 2000)
		int b = Integer.parseInt(args[0]);
		Thread.sleep(b);

		// @Range(min = 0, max = 2000)
		int c = Integer.parseInt(args[0]);
		Thread.sleep(c);
	}

	/**
	 * Expected: 5 * 3 * args[0] (random)
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public void v(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 1000)
		int sleep = Integer.parseInt(args[0]);

		Thread.sleep(sleep * 3);
		v2(args);
	}

	private boolean v2(String[] args) throws InterruptedException {
		int arg0 = Integer.parseInt(args[0]);
		String newArg0 = String.valueOf((int) (arg0 * Math.random()));
		if (args.length <= 1) {
			v(new String[] { newArg0, "3" });
		} else {
			int count = Integer.parseInt(args[1]);
			if (count > 0) {
				v(new String[] { newArg0, String.valueOf(count - 1) });
			}
		}
		return true;
	}

	/**
	 * Expected: 5 * 200 + 5 * 100
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public void w(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 1000)
		int i = 0;
		for (; i < 5; i++) {
			Thread.sleep(200);
		}
		for (; i < 10; i++) {
			Thread.sleep(100);
		}
	}

	/**
	 * Expected: 2 * 500 + 5 * 100
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public void x(String[] args) throws InterruptedException {
		// @Range(min = 0, max = 1000)
		int i = 0;
		for (; i < 2; i++) {
			Thread.sleep(500);
		}
		i = 0;
		for (; i < 5; i++) {
			Thread.sleep(100);
		}
	}

	private static Method execute(Method[] methods, Sleep sleep, String methodName, String[] args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		// Find method by name
		Method method = null;
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				method = m;
				break;
			}
		}
		if (method == null) {
			throw new NoSuchMethodException(methodName);
		}

		Object[] parameters = new String[args.length - 1];
		for (int parameterIndex = 0; parameterIndex < args.length - 1; parameterIndex++) {
			parameters[parameterIndex] = args[1 + parameterIndex];
		}
		method.invoke(sleep, new Object[] { parameters });
		return method;
	}
}
