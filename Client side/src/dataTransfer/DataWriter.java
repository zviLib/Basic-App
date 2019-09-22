package com.example.zvi.basicApp.dataTransfer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * 
 * @author Zvi Liebskind
 *
 * This class helps with writing to an OutputStream
 */
public class DataWriter {

	private OutputStream os;

	public DataWriter(OutputStream os) {
		this.os = os;
	}
	
	/**
	 * Close the stream
	 */
	public void close(){
		try {
			os.close();
		} catch (IOException e) {
		}
	}

	/**
	 * @param i - an integer to write to the stream
	 */
	public void write(int i) {
		write(intToBytes(i));
	}

	/**
	 * @param s - a string to write to the stream
	 */
	public void write(String s) {
		write(stringToBytes(s));
	}

	/**
	 * @param bytes - a byte array to write to the stream
	 */
	public void write(byte[] bytes) {
		try {
			// write size
			os.write(intToBytes(bytes.length));
			// write bytes
			os.write(bytes);
		} catch (IOException e) {
		}
	}

	/**
	 * @param i - an integer to convert
	 * @return - i converted into a byte array
	 */
	private byte[] intToBytes(int i) {
		return ByteBuffer.allocate(4).putInt(i).array();
	}

	/**
	 * @param s - a string in UTF-8 format
	 * @return - the string converted to byte array
	 */
	private byte[] stringToBytes(String s) {
		try {
			return s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s.getBytes();
		}

	}
}
