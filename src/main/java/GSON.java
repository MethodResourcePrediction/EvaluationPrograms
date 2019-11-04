import java.util.stream.IntStream;

import com.google.gson.Gson;

public class GSON {
	public static void main(String[] args) {
		int inputLength = 1000000;
		int[] input = IntStream.range(0, inputLength).map(n -> (int) (Math.random() * inputLength)).toArray();

		// Serialization
		Gson gson = new Gson();
		String json = gson.toJson(input);
		gson.fromJson(json, int[].class);
	}
}
