
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
	private final String algo = "AES";
	private String key;

//	private int mode = Cipher.ENCRYPT_MODE;

	private Cipher cipher;

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		Encryption encryption = new Encryption();

		encryption.encrypt(3.0d, 50);
	}

	public Encryption() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		// 128 bit key
		key = "Foo12345Bar12345";

		// Create key and cipher
		SecretKeySpec aesKey = new SecretKeySpec(key.getBytes(), algo);
		cipher = Cipher.getInstance("AES");

		// encrypt the text
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
	}

	public void encrypt(double sizeMB, int iterations) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		char[] data = generateData(sizeMB);

		System.out.println("Encrypt " + iterations + " times " + sizeMB + " MB");
		for (int i = 0; i < iterations; i++) {
			cipher.doFinal(String.copyValueOf(data).getBytes());
		}
	}

	public char[] generateData(double sizeMB) {
		char data[] = new char[(int) (sizeMB * 1024 * 1024)];
		try {
			FileReader fileReader = new FileReader("/dev/zero");
			fileReader.read(data);
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
}
