package dataTransfer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 
 * @author Zvi Liebskind
 *
 * This class helps with reading from an InputStream
 */
public class DataReader {

	private InputStream input;

	public DataReader(InputStream is) {
		this.input = is;
	}

	/**
	 * Close the stream.
	 */
	public void close() {
		try {
			input.close();
		} catch (IOException e) {
		}
	}

	/**
	 * @return an integer read from the input stream, -1 in case of an error
	 */
	public int readInt() {
		byte[] b = new byte[4];
		try {
			// read size
			readSize();
			// read int
			input.read(b);
			return ByteArrToInt(b);

		} catch (IOException e) {
		}
		return -1;
	}

	/** 
	 * @param b - a byte array
	 * @return b converted into an integer
	 */
	private int ByteArrToInt(byte[] b) {
		int value = 0;
		for (int i = 0; i < b.length; i++)
			value = (value << 8) | b[i];
		return value;
	}
	
	/**
	 * @return a byte array from the input stream converted to integer
	 */
	private int readSize(){
		byte[] b = new byte[4];
		//read size
		try {
			input.read(b);
		} catch (IOException e) {
		}
		return ByteArrToInt(b);
	}

	/**
	 * @return a string from the input stream, null in case of an error
	 */
	public String readString() {
		try {
			int size = readSize();
			byte[] b = new byte[size];
			input.read(b, 0, size);
			return new String(b, StandardCharsets.UTF_8);
		} catch (IOException e) {
		}
		return null;
	}
}
