package kr.inode.tbon.steak;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import kr.inode.tbon.TBONParser;
import kr.inode.tbon.TBONToken;

public class SteakParser implements TBONParser {
	private static interface ParserFunc {
		void parse(byte b, SteakParser parser) throws IOException;
	}

	private static final TBONToken[] INTEGER_TOKENS = { TBONToken.Int8, TBONToken.Int16, TBONToken.Int32,
			TBONToken.Int64, };

	private static final ParserFunc[] PARSER_FUNCS = {
			// 0 zero parser
			new ParserFunc() {
				@Override
				public void parse(byte b, SteakParser parser) throws IOException {
					parser.currentToken = INTEGER_TOKENS[b & 0x03];
					parser.byteValue = 0;
					parser.shortValue = 0;
					parser.intValue = 0;
					parser.longValue = 0L;
				}
			},
			// 1 full bytes parser
			new ParserFunc() {
				@Override
				public void parse(byte b, SteakParser parser) throws IOException {
					parser.currentToken = INTEGER_TOKENS[b & 0x03];
					switch (b & 0x07) {
					case 0: // int8
						parser.readToBuffer(1);
						parser.byteValue = parser.buffer.get();
						break;
					case 1: // int16
						parser.readToBuffer(2);
						parser.shortValue = parser.buffer.getShort();
						break;
					case 2: // int32
						parser.readToBuffer(4);
						parser.intValue = parser.buffer.getInt();
						break;
					case 3: // int64
						parser.readToBuffer(8);
						parser.longValue = parser.buffer.getLong();
						break;
					case 4: // uint8
						parser.readToBuffer(1);
						parser.byteValue = parser.buffer.get();
						if (parser.byteValue < 0) {
							parser.currentToken = TBONToken.Int16;
							parser.shortValue = (short) (parser.byteValue & 0xff);
						}
						break;
					case 5: // uint16
						parser.readToBuffer(2);
						parser.shortValue = parser.buffer.getShort();
						if (parser.shortValue < 0) {
							parser.currentToken = TBONToken.Int32;
							parser.intValue = parser.shortValue & 0xffff;
						}
					case 6: // uint32
						parser.readToBuffer(4);
						parser.intValue = parser.buffer.getInt();
						if (parser.intValue < 0) {
							parser.currentToken = TBONToken.Int64;
							parser.longValue = parser.intValue & 0xffffffffL;
						}
					case 7: // uint64
						parser.readToBuffer(8);
						parser.longValue = parser.buffer.getLong();
						if (parser.longValue < 0) {
							parser.currentToken = TBONToken.Integer;
							ByteBuffer bb = ByteBuffer.wrap(parser.sharedBuffer);
							bb.put((byte) 0);
							bb.putLong(parser.longValue);
							parser.objectValue = new BigInteger(Arrays.copyOf(parser.sharedBuffer, 9));
						}
					}
				}
			},
			// 2 variable bits
			new ParserFunc() {
				@Override
				public void parse(byte b, SteakParser parser) throws IOException {
					int code = b & 0x07;
					switch (code) {
					case 0:
					case 1:
					case 2: {
						int i = parser.readVInt();
						if (code == 1) {
							i = -i;
						}
						parser.currentToken = TBONToken.Int32;
						parser.intValue = i;
						break;
					}
					case 3: {
						final byte s = parser.readByte();
						parser.currentToken = TBONToken.Int16;
						parser.shortValue = (short) s;
						break;
					}
					case 4:
					case 5:
					case 6: {
						long l = parser.readVLong();
						if (code == 5) {
							l = -l;
						}
						parser.currentToken = TBONToken.Int64;
						parser.longValue = l;
						break;
					}
					case 7: {
						final byte s = parser.readByte();
						parser.currentToken = TBONToken.Int16;
						parser.shortValue = (short) (s & 0xff);
						break;
					}
					}
				}
			},
			// 3 null boolean float decimal
			new ParserFunc() {
				@Override
				public void parse(byte b, SteakParser parser) throws IOException {
					switch (b & 0x07) {
					case 0:
						parser.currentToken = TBONToken.Float32;
						parser.floatValue = 0f;
						break;
					case 1:
						parser.currentToken = TBONToken.Float32;
						parser.readToBuffer(4);
						parser.floatValue = parser.buffer.getFloat();
						break;
					case 2:
						parser.currentToken = TBONToken.Float64;
						parser.doubleValue = 0d;
						break;
					case 3:
						parser.currentToken = TBONToken.Float64;
						parser.readToBuffer(8);
						parser.doubleValue = parser.buffer.getDouble();
						break;
					case 4:
						parser.currentToken = TBONToken.False;
						break;
					case 5:
						parser.currentToken = TBONToken.True;
						break;
					case 6:
						parser.currentToken = TBONToken.Null;
						break;
					case 7:
						parser.currentToken = TBONToken.EndOfStructure;
						break;
					}
				}
			},
			// 4 date time char
			new ParserFunc() {
				@Override
				public void parse(byte b, SteakParser parser) throws IOException {
					switch (b & 0x07) {
					case 0: // time
						throw new UnsupportedOperationException("time");
					case 1: // date
						throw new UnsupportedOperationException("date");
					case 2: // datetime
						parser.currentToken = TBONToken.DateTime;

						final int year = parser.readVSInt();
						final int dayOfYear = parser.readVInt();
						final int secondOfDay = parser.readVInt();
						final long nanos = parser.readVLong();

						Calendar c = Calendar.getInstance();
						c.clear();
						c.set(Calendar.YEAR, year);
						c.set(Calendar.DAY_OF_YEAR, dayOfYear);
						c.set(Calendar.SECOND, secondOfDay);
						c.set(Calendar.MILLISECOND, (int) (nanos / 1_000_000L));
						parser.objectValue = c;
						break;
					case 3: // datetimetz
						throw new UnsupportedOperationException("datetimetz");
					case 4:
						parser.currentToken = TBONToken.Character;
						parser.readToBuffer(1);
						parser.charValue = (char) (parser.buffer.get() & 0xff);
						break;
					case 5:
						parser.currentToken = TBONToken.Character;
						parser.readToBuffer(2);
						parser.charValue = parser.buffer.getChar();
						break;
					case 6:
						parser.currentToken = TBONToken.Character;
						int i = parser.readVInt();
						parser.intValue = i;
						break;
					case 7:
						parser.currentToken = TBONToken.Decimal;
						parser.objectValue = BigDecimal.ZERO;
						break;
					}
				}
			},
			// 5 primitive array
			new ParserFunc() {
				@Override
				public void parse(byte b, SteakParser parser) throws IOException {
					switch (b & 0x07) {
					case 0:
						parser.currentToken = TBONToken.PrimitiveArrayOfBoolean;
						break;
					case 1:
						parser.currentToken = TBONToken.PrimitiveArrayOfShort;
						break;
					case 2:
						parser.currentToken = TBONToken.PrimitiveArrayOfInt;
						break;
					case 3:
						parser.currentToken = TBONToken.PrimitiveArrayOfLong;
						break;
					case 4:
						parser.currentToken = TBONToken.PrimitiveArrayOfFloat;
						break;
					case 5:
						parser.currentToken = TBONToken.PrimitiveArrayOfDouble;
						break;
					case 6:
						parser.currentToken = TBONToken.PrimitiveArrayOfChar;
						break;
					case 7:
						throw new IOException("SteakParser: NOT USED type byte 0x2f");
					}
					parser.elementCount = parser.readVInt();
				}
			},
			// 6 decimal, positive scale
			new ParserFunc() {
				@Override
				public void parse(byte b, SteakParser parser) throws IOException {
					int scale = b & 0x7;
					if (scale == 7) {
						scale = parser.readVInt();
					}
					final int biLen = parser.readVInt();
					parser.readOnSharedBuffer(0, biLen);

					parser.currentToken = TBONToken.Decimal;
					parser.objectValue = new BigDecimal(new BigInteger(Arrays.copyOf(parser.sharedBuffer, biLen)),
							scale);
				}
			},
			// 7 decimal, negative scale
			new ParserFunc() {
				@Override
				public void parse(byte b, SteakParser parser) throws IOException {
					int scale = b & 0x7;
					if (scale == 0) {
						scale = parser.readVInt();
					}
					scale = -scale;
					final int biLen = parser.readVInt();
					parser.readOnSharedBuffer(0, biLen);

					parser.currentToken = TBONToken.Decimal;
					parser.objectValue = new BigDecimal(new BigInteger(Arrays.copyOf(parser.sharedBuffer, biLen)),
							scale);
				}
			} };

	private static int initialBufferSize = 32;

	private final ReadableByteChannel in;
	private TBONToken currentToken = TBONToken.NotAvailable;
	private final ByteBuffer buffer = ByteBuffer.allocate(8192);

	private int elementCount = -1;

	private boolean inStream = false;
	private byte byteValue;
	private short shortValue;
	private int intValue;
	private long longValue;
	private float floatValue;
	private double doubleValue;
	private char charValue;
	private Object objectValue;

	private byte[] sharedBuffer = new byte[initialBufferSize];

	public SteakParser(ReadableByteChannel in) throws IOException {
		this.in = in;
		buffer.flip();

		// check header bytes
		readOnSharedBuffer(0, 5);
		if (!Arrays.equals(SteakFactory.STEAK_HEADER, Arrays.copyOf(sharedBuffer, 5))) {
			throw new IOException("SteakParser: header not matched");
		}
	}

	private void readOnSharedBuffer(int offset, int len) throws IOException {
		final int limit = offset + len;
		if (limit > sharedBuffer.length) {
			int bufferSize = limit + limit % 8;
			initialBufferSize = Math.max(initialBufferSize, bufferSize);
			sharedBuffer = new byte[bufferSize];
		}

		if (len <= buffer.capacity()) {
			readToBuffer(len);
			buffer.get(sharedBuffer, offset, len);
		} else {
			while (offset < limit) {
				int target = Math.min(buffer.capacity(), limit - offset);
				readToBuffer(target);

				buffer.get(sharedBuffer, offset, target);
				offset += target;
			}
		}
	}

	private void readToBuffer(int size) throws IOException {
		if (buffer.remaining() >= size) {
			return;
		}

		int read = buffer.remaining();
		buffer.compact();
		while (read < size) {
			int r = in.read(buffer);
			if (r == -1) {
				throw new IOException("EOS on read");
			}
			read += r;
		}
		buffer.flip();
	}

	@Override
	public void close() throws IOException {
	}

	private byte readByte() throws IOException {
		readToBuffer(1);
		return buffer.get();
	}

	private int readVSInt() throws IOException {
		byte r = readByte();
		final boolean neg = (r & 0x40) == 0x40;
		int i = r & 0x3f;
		int shift = 6;
		while (r < 0) {
			r = readByte();
			i |= (r & 0x7f) << shift;
			shift += 7;
		}
		if (neg) {
			i = -i;
		}
		return i;
	}

	private int readVInt() throws IOException {
		int i = 0;
		byte r;
		int shift = 0;
		do {
			r = readByte();
			i |= (r & 0x7f) << shift;
			shift += 7;
		} while (r < 0);
		return i;
	}

	private long readVLong() throws IOException {
		long i = 0;
		byte r;
		int shift = 0;
		do {
			r = readByte();
			i |= (r & 0x7fL) << shift;
			shift += 7;
		} while (r < 0);
		return i;
	}

	@Override
	public boolean next() throws IOException {
		readToBuffer(1);

		final byte b = buffer.get();
		if ((b & 0xc0) == 0) {
			PARSER_FUNCS[(b & 0x38) >> 3].parse(b, this);
		} else if ((b & 0x80) == 0) {
			// variable lengths
			int len = b & 0x0f;
			if (len == 0x0f) {
				len = readVInt();
				if (len == 0) {
					len = -1;
				}
			}
			switch ((b & 0x30) >> 4) {
			case 0: // BigInteger
				currentToken = TBONToken.Integer;
				if (len == 0) {
					objectValue = BigInteger.ZERO;
				} else {
					readOnSharedBuffer(0, len);
					objectValue = new BigInteger(Arrays.copyOf(sharedBuffer, len));
				}
				break;
			case 1: // CustomType
				++len;
				currentToken = TBONToken.CustomType;
				readOnSharedBuffer(0, len);
				objectValue = new String(sharedBuffer, 0, len, StandardCharsets.UTF_8);
				break;
			case 2: // Array
				currentToken = TBONToken.Array;
				elementCount = len;
				break;
			case 3: // Object
				currentToken = TBONToken.Object;
				elementCount = len;
				break;
			}
		} else {
			// octet or string
			if ((b & 0x40) == 0) {
				currentToken = TBONToken.Octet;
			} else {
				currentToken = TBONToken.String;
			}

			inStream = false;
			int len = b & 0x3f;
			if (len == 0x3f) {
				len = readVInt();
				if (len == 0) {
					inStream = true;
					len = -1;
				}
			}

			elementCount = len;
			return true;
		}

		return true;
	}

	@Override
	public TBONToken currentToken() {
		return currentToken;
	}

	@Override
	public TBONToken nextToken() throws IOException {
		next();
		return currentToken;
	}

	@Override
	public boolean getBoolean() {
		return currentToken == TBONToken.True;
	}

	@Override
	public byte getByte() {
		return byteValue;
	}

	@Override
	public short getShort() {
		return shortValue;
	}

	@Override
	public int getInt() {
		return intValue;
	}

	@Override
	public long getLong() {
		return longValue;
	}

	@Override
	public BigInteger getInteger() {
		return (BigInteger) objectValue;
	}

	@Override
	public float getFloat() {
		return floatValue;
	}

	@Override
	public double getDouble() {
		return doubleValue;
	}

	@Override
	public BigDecimal getDecimal() {
		return (BigDecimal) objectValue;
	}

	@Override
	public Date getDate() {
		return getCalendar().getTime();
	}

	@Override
	public Calendar getCalendar() {
		return (Calendar) objectValue;
	}

	@Override
	public char getChar() {
		return charValue;
	}

	@Override
	public String readString() throws IOException {
		return new String(readOctet(), StandardCharsets.UTF_8);
	}

	@Override
	public byte[] readOctet() throws IOException {
		int capa = elementCount;
		if (capa == -1) {
			capa = 4096;
		}
		try (ByteArrayOutputStream out = new ByteArrayOutputStream(capa)) {
			readOctet(out);
			return (byte[]) out.toByteArray();
		}
	}

	public void readOctet(OutputStream out) throws IOException {
		if (inStream) {
			int len = readVInt();
			while (len > 0) {
				readOnSharedBuffer(0, len);
				out.write(sharedBuffer, 0, len);
				len = readVInt();
			}

			inStream = false;
		} else {
			readOnSharedBuffer(0, elementCount);
			out.write(sharedBuffer, 0, elementCount);
		}
	};

	@Override
	public int getElementCount() {
		return elementCount;
	}

	@Override
	public String getCustomTypeName() {
		return (String) objectValue;
	}
}
