import java.util.stream.IntStream;

import com.google.gson.Gson;

import de.rherzog.master.thesis.annotations.Range;

public class GSON {
	public static void main(String[] args) {
		deserialize(args);
	}
	
	public static void deserialize(String[] args) {
//		 @Range(min = 0, max = 1000000)
		int inputLength = Integer.parseInt(args[0]);

		int[] input = IntStream.range(0, inputLength).map(n -> (int) (Math.random() * inputLength)).toArray();

		// Serialization
		Gson gson = new Gson();
		String json = gson.toJson(input);
		gson.fromJson(json, int[].class);
	}
}
