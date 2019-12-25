package kr.inode.tbon.mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.inode.tbon.TBONParser;
import kr.inode.tbon.TBONToken;

public class TBONReader implements AutoCloseable {
	private static interface ReaderFunc {
		Object read(TBONReader reader) throws IOException;
	}

	private static final EnumMap<TBONToken, ReaderFunc> READER_FUNCS = new EnumMap<>(TBONToken.class);

	static {
		READER_FUNCS.put(TBONToken.NotAvailable, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				throw new IOException("TBONReader: EOS");
			}
		});
		READER_FUNCS.put(TBONToken.Null, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return null;
			}
		});
		READER_FUNCS.put(TBONToken.False, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return Boolean.FALSE;
			}
		});
		READER_FUNCS.put(TBONToken.True, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return Boolean.TRUE;
			}
		});
		READER_FUNCS.put(TBONToken.Int8, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getByte();
			}
		});
		READER_FUNCS.put(TBONToken.Int16, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getShort();
			}
		});
		READER_FUNCS.put(TBONToken.Int32, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getInt();
			}
		});
		READER_FUNCS.put(TBONToken.Int64, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getLong();
			}
		});
		READER_FUNCS.put(TBONToken.Integer, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getInteger();
			}
		});
		READER_FUNCS.put(TBONToken.Float32, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getFloat();
			}
		});
		READER_FUNCS.put(TBONToken.Float64, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getDouble();
			}
		});
		READER_FUNCS.put(TBONToken.Decimal, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getDecimal();
			}
		});
		READER_FUNCS.put(TBONToken.Time, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getDate();
			}
		});
		READER_FUNCS.put(TBONToken.Date, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getDate();
			}
		});
		READER_FUNCS.put(TBONToken.DateTime, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getDate();
			}
		});
		READER_FUNCS.put(TBONToken.DateTimeTZ, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getDate();
			}
		});
		READER_FUNCS.put(TBONToken.Character, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.getChar();
			}
		});
		READER_FUNCS.put(TBONToken.Octet, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.readOctet();
			}
		});
		READER_FUNCS.put(TBONToken.String, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				return reader.parser.readString();
			}
		});
		READER_FUNCS.put(TBONToken.PrimitiveArrayOfBoolean, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				TBONParser parser = reader.parser;
				int len = parser.getElementCount();
				boolean[] arr = new boolean[len];
				parser.next();
				for (int i = 0; i < len; ++i) {
					arr[i] = parser.getBoolean();
					parser.next();
				}
				return arr;
			}
		});
		READER_FUNCS.put(TBONToken.PrimitiveArrayOfShort, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				TBONParser parser = reader.parser;
				int len = parser.getElementCount();
				short[] arr = new short[len];
				parser.next();
				for (int i = 0; i < len; ++i) {
					arr[i] = parser.getShort();
					parser.next();
				}
				return arr;
			}
		});
		READER_FUNCS.put(TBONToken.PrimitiveArrayOfInt, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				TBONParser parser = reader.parser;
				int len = parser.getElementCount();
				int[] arr = new int[len];
				parser.next();
				for (int i = 0; i < len; ++i) {
					arr[i] = parser.getInt();
					parser.next();
				}
				return arr;
			}
		});
		READER_FUNCS.put(TBONToken.PrimitiveArrayOfLong, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				TBONParser parser = reader.parser;
				int len = parser.getElementCount();
				long[] arr = new long[len];
				parser.next();
				for (int i = 0; i < len; ++i) {
					arr[i] = parser.getLong();
					parser.next();
				}
				return arr;
			}
		});
		READER_FUNCS.put(TBONToken.PrimitiveArrayOfFloat, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				TBONParser parser = reader.parser;
				int len = parser.getElementCount();
				float[] arr = new float[len];
				parser.next();
				for (int i = 0; i < len; ++i) {
					arr[i] = parser.getFloat();
					parser.next();
				}
				return arr;
			}
		});
		READER_FUNCS.put(TBONToken.PrimitiveArrayOfDouble, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				TBONParser parser = reader.parser;
				int len = parser.getElementCount();
				double[] arr = new double[len];
				parser.next();
				for (int i = 0; i < len; ++i) {
					arr[i] = parser.getDouble();
					parser.next();
				}
				return arr;
			}
		});
		READER_FUNCS.put(TBONToken.PrimitiveArrayOfChar, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				TBONParser parser = reader.parser;
				int len = parser.getElementCount();
				char[] arr = new char[len];
				parser.next();
				for (int i = 0; i < len; ++i) {
					arr[i] = parser.getChar();
					parser.next();
				}
				return arr;
			}
		});
		READER_FUNCS.put(TBONToken.Array, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				TBONParser parser = reader.parser;
				int len = parser.getElementCount();
				List<Object> list = new ArrayList<>(len == -1 ? 16 : len);
				while (parser.next()) {
					if (parser.currentToken() == TBONToken.EndOfStructure) {
						break;
					}

					list.add(reader.currentValue());
				}
				return list;
			}
		});
		READER_FUNCS.put(TBONToken.Object, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				TBONParser parser = reader.parser;
				int len = parser.getElementCount();
				Map<String, Object> map = new HashMap<>(len == -1 ? 16 : len * 2);
				while (parser.next()) {
					if (parser.currentToken() == TBONToken.EndOfStructure) {
						break;
					}

					map.put((String) reader.currentValue(), reader.nextValue());
				}
				return map;
			}
		});
		READER_FUNCS.put(TBONToken.CustomType, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				TBONParser parser = reader.parser;
				String customTypeName = parser.getCustomTypeName();
				TypeHandler<?> typeHandler = reader.typeHandlers.get(customTypeName);
				if (typeHandler == null) {
					throw new IOException("TBONReader: Custom type handler not found for " + customTypeName);
				}
				return typeHandler.read(parser);
			}
		});
		READER_FUNCS.put(TBONToken.EndOfStructure, new ReaderFunc() {
			@Override
			public Object read(TBONReader reader) throws IOException {
				throw new IOException("TBONReader: EndOfStructure on not in structure.");
			}
		});
	}

	private final TBONParser parser;
	private final Map<String, TypeHandler<?>> typeHandlers;

	public TBONReader(TBONParser parser) {
		this(parser, null);
	}

	public TBONReader(TBONParser parser, Map<String, TypeHandler<?>> typeHandlers) {
		this.parser = parser;
		this.typeHandlers = typeHandlers;
	}

	public <T> T nextValue() throws IOException {
		if (!parser.next()) {
			throw new IOException("TBONReader: no more value");
		}

		return currentValue();
	}

	@SuppressWarnings("unchecked")
	public <T> T currentValue() throws IOException {
		return (T) READER_FUNCS.get(parser.currentToken()).read(this);
	}

	@Override
	public void close() {

	}

}
