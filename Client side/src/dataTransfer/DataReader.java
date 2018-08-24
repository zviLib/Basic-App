package com.example.zvi.basicApp.dataTransfer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataReader {

	private InputStream input;

	public DataReader(InputStream is) {
		this.input = is;
	}

	public void close() {
		try {
			input.close();
		} catch (IOException e) {
		}
	}

	public int readInt() {
		byte[] b = new byte[4];
		try {
			// don't save size - it is not needed.
			readSize();
			// read int
			input.read(b);
			return ByteArrToInt(b);

		} catch (IOException e) {
		}
		return -1;
	}

	private int ByteArrToInt(byte[] b) {
		int value = 0;
		for (int i = 0; i < b.length; i++)
			value = (value << 8) | b[i];
		return value;
	}
	
	private int readSize(){
		byte[] b = new byte[4];
		//read size
		try {
			input.read(b);
		} catch (IOException e) {
		}
		return ByteArrToInt(b);
	}

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
