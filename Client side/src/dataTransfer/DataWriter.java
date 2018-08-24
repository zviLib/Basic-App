package com.example.zvi.basicApp.dataTransfer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class DataWriter {

	private OutputStream os;

	public DataWriter(OutputStream os) {
		this.os = os;
	}
	
	public void close(){
		try {
			os.close();
		} catch (IOException e) {
		}
	}

	public void write(int i) {
		write(intToBytes(i));
	}

	public void write(String s) {
		write(stringToBytes(s));
	}

	public void write(byte[] bytes) {
		try {
			// write size
			os.write(intToBytes(bytes.length));
			// write bytes
			os.write(bytes);
		} catch (IOException e) {
		}
	}

	private byte[] intToBytes(int i) {
		return ByteBuffer.allocate(4).putInt(i).array();
	}

	private byte[] stringToBytes(String s) {
		try {
			return s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s.getBytes();
		}

	}
}
