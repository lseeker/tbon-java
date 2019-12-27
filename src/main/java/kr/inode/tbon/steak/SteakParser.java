package kr.inode.tbon.steak;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
							byte[] bi = new byte[9];
							ByteBuffer bb = ByteBuffer.wrap(bi);
							bb.put((byte) 0);
							bb.putLong(parser.longValue);
							parser.objectValue = new BigInteger(bi);
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
						byte s = parser.readByte();
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
						byte s = parser.readByte();
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

						int year = parser.readVSInt();
						int dayOfYear = parser.readVInt();
						int secondOfDay = parser.readVInt();
						long nanos = parser.readVLong();

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
					int biLen = parser.readVInt();
					byte[] bi = new byte[biLen];
					parser.readToBuffer(biLen);
					parser.buffer.get(bi);

					parser.currentToken = TBONToken.Decimal;
					parser.objectValue = new BigDecimal(new BigInteger(bi), scale);
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
					int biLen = parser.readVInt();
					byte[] bi = new byte[biLen];
					parser.readToBuffer(biLen);
					parser.buffer.get(bi);

					parser.currentToken = TBONToken.Decimal;
					parser.objectValue = new BigDecimal(new BigInteger(bi), scale);
				}
			} };

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

	public SteakParser(ReadableByteChannel in) throws IOException {
		this.in = in;
		buffer.flip();
		readToBuffer(5);
		byte[] header = new byte[5];
		buffer.get(header);
		if (!Arrays.equals(header, SteakFactory.STEAK_HEADER)) {
			throw new IOException("SteakParser: header not matched");
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

	private byte[] readOctet(int len) throws IOException {
		byte[] b = new byte[len];
		if (len <= buffer.capacity()) {
			readToBuffer(len);
			buffer.get(b);
		} else {
			int index = 0;
			while (index < b.length) {
				int target = Math.min(buffer.capacity(), b.length - index);
				buffer.get(b, index, target);
				index += target;
			}
		}
		return b;
	}

	private int readVSInt() throws IOException {
		byte r = readByte();
		boolean neg = (r & 0x40) == 0x40;
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

		byte b = buffer.get();
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
					readToBuffer(len);
					byte[] buf = new byte[len];
					buffer.get(buf);
					objectValue = new BigInteger(buf);
				}
				break;
			case 1: // CustomType
				currentToken = TBONToken.CustomType;
				readToBuffer(len);
				byte[] buf = new byte[len];
				buffer.get(buf);
				objectValue = new String(buf, StandardCharsets.UTF_8);
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

			int len = b & 0x3f;
			if (len == 0x3f) {
				len = readVInt();
				if (len == 0) {
					inStream = true;
					return true;
				}
			}

			inStream = false;
			byte[] octet = readOctet(len);
			if (currentToken == TBONToken.String) {
				objectValue = new String(octet, StandardCharsets.UTF_8);
			} else {
				objectValue = octet;
			}
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
	public byte[] readOctet() throws IOException {
		if (inStream) {
			try (ByteArrayOutputStream out = new ByteArrayOutputStream(1024)) {
				int len = readVInt();
				while (len > 0) {
					out.write(readOctet(len));
					len = readVInt();
				}

				inStream = false;
				return out.toByteArray();
			}
		}
		return (byte[]) objectValue;
	}

	@Override
	public String readString() throws IOException {
		if (inStream) {
			return new String(readOctet(), StandardCharsets.UTF_8);
		}
		return (String) objectValue;
	}

	@Override
	public int getElementCount() {
		return elementCount;
	}

	@Override
	public String getCustomTypeName() {
		return (String) objectValue;
	}
}