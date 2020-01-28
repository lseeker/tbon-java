package kr.inode.tbon;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Calendar;
import java.util.Date;

public interface TBONGenerator extends Closeable {
	void writeNull() throws IOException;

	void write(boolean value) throws IOException;

	void write(byte value) throws IOException;

	void write(short value) throws IOException;

	void write(int value) throws IOException;

	void write(long value) throws IOException;

	void write(float value) throws IOException;

	void write(double value) throws IOException;

	void write(char value) throws IOException;

	void writeChar(int value) throws IOException;

	void write(BigInteger value) throws IOException;

	void write(BigDecimal value) throws IOException;

	void write(byte[] value) throws IOException;

	void write(ByteBuffer value) throws IOException;

	void write(InputStream value) throws IOException;

	void write(InputStream value, int size) throws IOException;

	void write(ReadableByteChannel value) throws IOException;

	void write(ReadableByteChannel value, int size) throws IOException;

	void write(String value) throws IOException;

	void write(Date value) throws IOException;

	void write(Calendar value) throws IOException;

	void writeStartArray() throws IOException;

	void writeStartArray(int count) throws IOException;

	void writeStartPrimitiveArray(Class<?> type, int count) throws IOException;

	void writeEndArray() throws IOException;

	void writeStartObject() throws IOException;

	void writeStartObject(int count) throws IOException;

	void writeEndObject() throws IOException;

	void writeCustomType(String typeName) throws IOException;
}
