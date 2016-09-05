package cc.gu.util.value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

import cc.gu.util.occlude.BasicOccluder;
import cc.gu.util.occlude.OccludHandler;
import cc.gu.util.occlude.Occluder;

public class FileValuable implements Valuable<byte[]> {
	
	final private ValuableObservable<byte[]> observable = new ValuableObservable<byte[]>(this);

	@Override
	public void addObserver(ValuableChangedListener<? super byte[]> observer, boolean weak) {
		observable.addObserver(observer, weak);
	}

	@Override
	public void removeObserver(ValuableChangedListener<? super byte[]> observer) {
		observable.removeObserver(observer);
	}

	final private File file;

	public FileValuable(File file) {
		this.file = file;
	}

	private Occluder writing;
	private Reference<byte[]> set;

	@Override
	public byte[] set(byte[] value) {
		Occluder writing;
		synchronized (this) {
			set = new WeakReference<byte[]>(value);
			Occluder write = this.writing;
			this.writing = writing = new BasicOccluder();
			if (write != null) {
				write.occlude();
			}
		}
		write(value, writing);
		return set.get();
	}

	protected void write(byte[] value, Occluder occluder) {
		occluder = OccludHandler.notNull(occluder);
		synchronized (file) {
			observable.observer();
			if (occluder.isOccluded()) {
				return;
			}
			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(file);
				for (int i = 0; i < value.length; i += 4096) {
					occluder.occluded();
					outputStream.write(value, i, Math.max(i - value.length + 1, 4096));
					outputStream.flush();
				}
			} catch (Throwable e) {
			} finally {
				try {
					outputStream.close();
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public byte[] get() throws Throwable {
		return read(null);
	}

	@Override
	public byte[] get(Occluder occluder) throws Throwable {
		return read(occluder);
	}

	protected byte[] read(Occluder occluder) throws Throwable {
		occluder = OccludHandler.notNull(occluder);
		synchronized (file) {
			occluder.occluded();
			ByteBuffer buffer = ByteBuffer.allocate(0);
			byte[] src = new byte[4096];
			int length;
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				length = in.read(src);
				while (length >= 0) {
					buffer.put(src, 0, length);
					occluder.occluded();
				}
				src = buffer.array();
				return src;
			} finally {
				try {
					buffer.clear();
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
