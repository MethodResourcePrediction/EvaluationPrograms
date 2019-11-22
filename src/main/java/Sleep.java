
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.stream.IntStream;

public class Sleep {
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	private @interface InvokeMultiple {
	}

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
		long sleep = Long.valueOf(args[0]);
		Thread.sleep(sleep);
	}

	/**
	 * Sleep a constant amount of time loaded from a class variable and multiplied
	 * by some value<br/>
	 * Expected: linear in multiplication
	 */
	public void c(String[] args) throws InterruptedException {
		long sleep = Long.valueOf(args[0]);
		Thread.sleep(sleep * 2);
	}

	/**
	 * Sleep a linear amount of time depending on a local variable<br/>
	 * Expected: linear in multiplication
	 */
	public void d(String[] args) throws InterruptedException {
		long sleep = Long.valueOf(args[0]);
		double fraction = Double.valueOf(args[1]);
		Thread.sleep((long) (fraction * sleep));
	}

	/**
	 * Sleep a random amount of time depending on a local variable<br/>
	 * Expected: random (not predictable since the slice would evaluate another
	 * random value)
	 */
	public void e(String[] args) throws InterruptedException {
		long sleep = Long.valueOf(args[0]);
		double times = Math.random();
		Thread.sleep((long) (times * sleep));
	}

	/**
	 * Sleep a constant time, depending on boolean parameter (if true only)<br/>
	 * Expected: constant 200 or 0
	 */
	public void f(String[] args) throws InterruptedException {
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
		boolean b = Boolean.parseBoolean(args[0]);
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
		int count = Integer.parseInt(args[0]);
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
		int count = Integer.parseInt(args[0]);
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
		int count = Integer.parseInt(args[0]);
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
		int count = Integer.parseInt(args[0]);
		long sleep = Long.parseLong(args[1]);

		Iterator<?> iterator = IntStream.range(0, count).iterator();
		while (iterator.hasNext()) {
			iterator.next();
			Thread.sleep(sleep);
		}
	}

	/**
	 * Sleep {@code count} times for {@code sleep} ms within a lambda.<br/>
	 * Expected: Not possible (no time consuming actions since the lambda is a own
	 * method)
	 */
	public void m(String[] args) throws InterruptedException {
		int count = Integer.parseInt(args[0]);
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
		int count = Integer.parseInt(args[0]);
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
		int count = Integer.parseInt(args[0]);
		long sleep = Long.parseLong(args[1]);

		if (count == 0) {
			return;
		}
		Thread.sleep(sleep);

		q(new String[] { String.valueOf(count - 1), String.valueOf(sleep) });
	}

	/**
	 * Recursive method call.<br/>
	 * Expected: count * sleep
	 */
	public void r(String[] args) throws InterruptedException {
		int count = Integer.parseInt(args[0]);
		long sleep = Long.parseLong(args[1]);

		if (count == 0) {
			return;
		}
		Thread.sleep(sleep);
		r(new String[] { String.valueOf(count - 1), String.valueOf(sleep) });
		Thread.sleep(sleep);
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
