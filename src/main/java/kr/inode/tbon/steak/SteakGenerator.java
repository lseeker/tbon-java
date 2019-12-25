package kr.inode.tbon.steak;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Calendar;
import java.util.Date;

import kr.inode.tbon.TBONGenerator;

public class SteakGenerator implements TBONGenerator {
	private final WritableByteChannel out;
	private final ByteBuffer buffer = ByteBuffer.allocateDirect(8192);

	public SteakGenerator(final WritableByteChannel out) throws IOException {
		this.out = out;
		out.write(ByteBuffer.wrap(SteakFactory.STEAK_HEADER));
	}

	private void ensureBuffer(int size) throws IOException {
		if (buffer.remaining() < size) {
			flush();
		}
	}

	private void writeByte(int b) {
		buffer.put((byte) b);
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

	private void flush() throws IOException {
		buffer.flip();
		while (buffer.hasRemaining()) {
			out.write(buffer);
		}
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
		// TODO Auto-generated method stub

	}

	@Override
	public void write(BigInteger value) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(BigDecimal value) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(byte[] value) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(String value) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(Date value) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(Calendar value) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeStartArray() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeStartArray(int count) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeEndArray() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeStartObject() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeStartObject(int count) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeEndObject() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeCustomType(String typeName) throws IOException {
		// TODO Auto-generated method stub

	}

}
