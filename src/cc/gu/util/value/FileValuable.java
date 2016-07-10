package cc.gu.util.value;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

public class FileValuable extends Valuable<byte[]> {
	final private File file;
	private long lastModified = Long.MIN_VALUE;
	
	private WeakReference<byte[]> cache;
	
	public FileValuable(File file) {
		this.file = file;
	}

	private void write(byte[] bytes) {
		
	}
	
	
	@Override
	synchronized public void set(byte[] value) {
		write(value);
		update();
	}
	
	private void update() {
		long lastModified = this.lastModified;
		try {
			lastModified = file.lastModified();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (lastModified != this.lastModified) {
			super.set(null);
		}
	}
	
	@Override
	public byte[] get() {
		return super.get();
	}
	private volatile long reading;
	synchronized private byte[] read() {
		ByteBuffer buffer = ByteBuffer.allocate(0);
		byte[] src = new byte[4096];
		int length;
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			length = in.read(src);
			while (length >= 0) {
				buffer.put(src, 0, length);
			}
			return buffer.array();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				in.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
}
