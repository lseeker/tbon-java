package kr.inode.tbon.steak;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

import kr.inode.tbon.TBONGenerator;

public class SteakGenerator implements TBONGenerator {
	private final WritableByteChannel out;
	private final ByteBuffer buffer = ByteBuffer.allocateDirect(8192);

	public SteakGenerator(final WritableByteChannel out) throws IOException {
		this.out = out;
		buffer.put(SteakFactory.STEAK_HEADER);
	}

	private void ensureBuffer(int size) throws IOException {
		if (buffer.remaining() < size) {
			flush();
		}
	}

	private void writeByte(int b) {
		buffer.put((byte) b);
	}

	private void writeVSInt(int i) {
		int first = 0;
		if (i < 0) {
			first |= 0x40;
			i = -i;
		}
		first |= i & 0x3f;
		if (i > 0x3f) {
			writeByte(first | 0x80);
			writeVPInt(i >> 6);
		} else {
			writeByte(first);
		}
	}

	private void writeVPInt(int i) {
		while (i > 0x7f) {
			buffer.put((byte) ((i & 0x7f) | 0x80));
			i >>= 7;
		}
		buffer.put((byte) i);
	}

	private void writeVPLong(long l) {
		while (l > 0x7fL) {
			buffer.put((byte) ((l & 0x7fL) | 0x80L));
			l >>= 7;
		}
		buffer.put((byte) l);
	}

	private void writeOctet(byte[] b) throws IOException {
		if (buffer.remaining() < b.length) {
			flush();
			flushBuffer(ByteBuffer.wrap(b));
		} else {
			buffer.put(b);
		}
	}

	private void writeOctet(ByteBuffer b) throws IOException {
		if (buffer.remaining() < b.remaining()) {
			flush();
			flushBuffer(b);
		} else {
			buffer.put(b);
		}
	}

	private void flushBuffer(ByteBuffer b) throws IOException {
		while (b.hasRemaining()) {
			out.write(b);
		}
	}

	private void flush() throws IOException {
		buffer.flip();
		flushBuffer(buffer);
		buffer.clear();
	}

	@Override
	public void close() throws IOException {
		flush();
	}

	@Override
	public void writeNull() throws IOException {
		ensureBuffer(1);
		writeByte(0x1e);
	}

	@Override
	public void write(boolean value) throws IOException {
		ensureBuffer(1);
		writeByte(value ? 0x1d : 0x1c);
	}

	@Override
	public void write(byte value) throws IOException {
		if (value == 0) {
			ensureBuffer(1);
			writeByte(0x00);
		} else {
			ensureBuffer(2);
			writeByte(0x08);
			writeByte(value);
		}
	}

	@Override
	public void write(short value) throws IOException {
		if (value == 0) {
			ensureBuffer(1);
			writeByte(0x01);
		} else if (value <= Byte.MAX_VALUE && value >= Byte.MIN_VALUE) {
			ensureBuffer(2);
			writeByte(0x13);
			writeByte(value & 0xff);
		} else {
			ensureBuffer(3);
			writeByte(0x09);
			buffer.putShort(value);
		}
	}

	private static int INT_VMAX = 0x1fffff;
	private static int INT_VMIN = -INT_VMAX;

	@Override
	public void write(int value) throws IOException {
		if (value == 0) {
			ensureBuffer(1);
			writeByte(0x02);
		} else if (value <= INT_VMAX && value >= INT_VMIN) {
			ensureBuffer(4);
			if (value > 0) {
				writeByte(0x10);
			} else {
				writeByte(0x11);
				value = -value;
			}
			writeVPInt(value);
		} else {
			ensureBuffer(5);
			writeByte(0x0a);
			buffer.putInt(value);
		}
	}

	private static long LONG_VMAX = 0x1ffffffffffffL;
	private static long LONG_VMIN = -LONG_VMAX;

	@Override
	public void write(long value) throws IOException {
		if (value == 0L) {
			ensureBuffer(1);
			writeByte(0x03);
		} else if (value <= LONG_VMAX && value >= LONG_VMIN) {
			ensureBuffer(8);
			if (value > 0L) {
				writeByte(0x14);
			} else {
				writeByte(0x15);
				value = -value;
			}
			writeVPLong(value);
		} else {
			ensureBuffer(9);
			writeByte(0x0b);
			buffer.putLong(value);
		}

	}

	@Override
	public void write(float value) throws IOException {
		if (value == 0) {
			ensureBuffer(1);
			writeByte(0x18);
		} else {
			ensureBuffer(5);
			writeByte(0x19);
			buffer.putFloat(value);
		}
	}

	@Override
	public void write(double value) throws IOException {
		if (value == 0) {
			ensureBuffer(1);
			writeByte(0x1a);
		} else {
			ensureBuffer(9);
			writeByte(0x1b);
			buffer.putDouble(value);
		}
	}

	@Override
	public void write(char value) throws IOException {
		if (value <= 0xff) {
			ensureBuffer(2);
			writeByte(0x24);
			writeByte(value);
		} else {
			ensureBuffer(3);
			writeByte(0x25);
			buffer.putChar(value);
		}
	}

	@Override
	public void writeChar(int value) throws IOException {
		if (!Character.isValidCodePoint(value)) {
			throw new IOException("SteakGenerator: invalid unicode code point " + value);
		}
		if (value < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
			write((char) value);
		} else {
			ensureBuffer(6);
			writeByte(0x26);
			writeVPInt(value);
		}
	}

	@Override
	public void write(BigInteger value) throws IOException {
		if (BigInteger.ZERO.equals(value)) {
			ensureBuffer(1);
			writeByte(0x40);
		} else {
			byte[] b = value.toByteArray();
			if (b.length < 15) {
				ensureBuffer(1 + b.length);
				writeByte(0x40 + b.length);
			} else {
				ensureBuffer(5 + b.length);
				writeByte(0x4f);
				writeVPInt(b.length);
			}
			writeOctet(b);
		}
	}

	@Override
	public void write(BigDecimal value) throws IOException {
		if (BigDecimal.ZERO.equals(value)) {
			ensureBuffer(1);
			writeByte(0x27);
		} else {
			int scale = value.scale();
			byte[] unscaled = value.unscaledValue().toByteArray();

			ensureBuffer(11 + unscaled.length);

			// write scale
			if (scale >= 0) {
				if (scale < 7) {
					writeByte(0x30 + scale);
				} else {
					writeByte(0x37);
					writeVPInt(scale);
				}
			} else {
				scale = -scale;
				if (scale < 8) {
					writeByte(0x38 + scale);
				} else {
					writeByte(0x38);
					writeVPInt(scale);
				}
			}

			// write big integer
			writeVPInt(unscaled.length);
			writeOctet(unscaled);
		}
	}

	@Override
	public void write(byte[] value) throws IOException {
		write(ByteBuffer.wrap(value));
	}

	@Override
	public void write(ByteBuffer value) throws IOException {
		int len = value.remaining();
		if (len < 63) {
			ensureBuffer(1 + len);
			writeByte(0x80 + len);
		} else {
			ensureBuffer(6 + len);
			writeByte(0xbf);
			writeVPInt(len);
		}
		writeOctet(value);
	}

	@Override
	public void write(InputStream value) throws IOException {
		write(Channels.newChannel(value));
	}

	@Override
	public void write(ReadableByteChannel value) throws IOException {
		ensureBuffer(3);
		writeByte(0x8bf);
		writeByte(0);

		ByteBuffer transferBuffer = ByteBuffer.allocate(4096);
		for (;;) {
			int read = value.read(transferBuffer);
			if (read == -1) {
				break;
			}
			ensureBuffer(5 + read);
			writeVPInt(read);
			flushBuffer(transferBuffer);
		}
		writeByte(0);
	}

	@Override
	public void write(String value) throws IOException {
		byte[] b = value.getBytes(StandardCharsets.UTF_8);
		ensureBuffer(6 + b.length);
		if (b.length < 63) {
			writeByte(0xc0 + b.length);
		} else {
			writeByte(0xff);
			writeVPInt(b.length);
		}
		writeOctet(b);
	}

	@Override
	public void write(Date value) throws IOException {
		final Calendar c = Calendar.getInstance();
		c.setTime(value);
		write(c);
	}

	@Override
	public void write(Calendar value) throws IOException {
		int year = value.get(Calendar.YEAR);
		int dayOfYear = value.get(Calendar.DAY_OF_YEAR);
		int secondOfDay = value.get(Calendar.HOUR_OF_DAY) * 3600 + value.get(Calendar.MINUTE) * 60
				+ value.get(Calendar.SECOND);
		long nanos = value.get(Calendar.MILLISECOND) * 1_000_000L;

		ensureBuffer(26);
		writeByte(0x22);
		writeVSInt(year);
		writeVPInt(dayOfYear);
		writeVPInt(secondOfDay);
		writeVPLong(nanos);
	}

	@Override
	public void writeStartArray() throws IOException {
		ensureBuffer(2);
		writeByte(0x6f);
		writeByte(0);
	}

	@Override
	public void writeStartArray(int count) throws IOException {
		if (count < 15) {
			ensureBuffer(1);
			writeByte(0x60 + count);
		} else {
			ensureBuffer(6);
			writeByte(0x6f);
			writeVPInt(count);
		}
	}

	@Override
	public void writeStartPrimitiveArray(Class<?> type, int count) throws IOException {
		if (!type.isPrimitive()) {
			throw new IOException(type + " is not primitive");
		}

		ensureBuffer(1);
		if (type == int.class) {
			writeByte(0x2a);
		} else if (type == double.class) {
			writeByte(0x2d);
		} else if (type == long.class) {
			writeByte(0x2b);
		} else if (type == float.class) {
			writeByte(0x2c);
		} else if (type == short.class) {
			writeByte(0x29);
		} else if (type == boolean.class) {
			writeByte(0x28);
		} else if (type == char.class) {
			writeByte(0x2e);
		} else if (type == byte.class) {
			throw new IOException("byte array should use write(byte[])");
		}
	}

	@Override
	public void writeEndArray() throws IOException {
		ensureBuffer(1);
		writeByte(0x1f);
	}

	@Override
	public void writeStartObject() throws IOException {
		ensureBuffer(2);
		writeByte(0x7f);
		writeByte(0);
	}

	@Override
	public void writeStartObject(int count) throws IOException {
		if (count < 15) {
			ensureBuffer(1);
			writeByte(0x70 + count);
		} else {
			ensureBuffer(6);
			writeByte(0x7f);
			writeVPInt(count);
		}
	}

	@Override
	public void writeEndObject() throws IOException {
		ensureBuffer(1);
		writeByte(0x1f);
	}

	@Override
	public void writeCustomType(String typeName) throws IOException {
		if (typeName.isEmpty()) {
			throw new IOException("SteakGenerator: custom type name should not empty");
		}
		byte[] b = typeName.getBytes(StandardCharsets.UTF_8);
		if (b.length < 16) {
			ensureBuffer(1 + b.length);
			writeByte(0x50 + b.length - 1);
		} else {
			ensureBuffer(6 + b.length);
			writeByte(0x5f);
			writeVPInt(b.length - 1);
		}
		writeOctet(b);
	}

}
