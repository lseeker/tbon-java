package kr.inode.tbon.mapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
				int len = reader.parser.getElementCount();
				if (len != -1 && len < 32 * 1024) {
					return reader.parser.readOctet();
				} else {
					// TODO use temp file with stream
					return reader.parser.readOctet();
				}
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
				Map<String, Object> map = new LinkedHashMap<>(len == -1 ? 16 : len * 2);
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
				if (reader.explicitTypeReaderMap != null) {
					TypeReader typeReader = reader.explicitTypeReaderMap.get(customTypeName);
					if (typeReader != null) {
						return typeReader.read(customTypeName, reader);
					}
				}

				if (reader.typeReaders != null) {
					for (TypeReader typeReader : reader.typeReaders) {
						if (typeReader.canRead(customTypeName)) {
							return typeReader.read(customTypeName, reader);
						}
					}
				}

				// POJO handling
				try {
					Class<?> cls = Class.forName(customTypeName, true, Thread.currentThread().getContextClassLoader());
					Object obj = cls.getConstructor().newInstance();
					Map<String, Object> properties = reader.nextValue();
					for (Method method : cls.getMethods()) {
						if (Modifier.isStatic(method.getModifiers()) || method.getParameterTypes().length != 1) {
							continue;
						}

						final String methodName = method.getName();
						if (methodName.length() > 3 && methodName.startsWith("set")) {
							char[] name = methodName.toCharArray();
							name[3] = Character.toLowerCase(name[3]);
							String fieldName = new String(name, 3, name.length - 3);
							Object value = properties.get(fieldName);
							if (value != null) {
								// TODO type check for octet
								method.invoke(obj, value);
								properties.remove(fieldName);
							}
						}
					}

					for (Entry<String, Object> entry : properties.entrySet()) {
						try {
							Field field = cls.getField(entry.getKey());
							int modifiers = field.getModifiers();
							if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
								continue;
							}

							// TODO type check for octet
							field.set(obj, entry.getValue());
						} catch (NoSuchFieldException e) {
							// continue to next entry, cannot set
						}
					}

					return obj;
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException | NoSuchMethodException
						| SecurityException e) {
					throw new IOException("TBONReader: cannot read custom type " + customTypeName, e);
				}
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
	private final Collection<TypeReader> typeReaders;
	private final Map<String, TypeReader> explicitTypeReaderMap;

	public TBONReader(TBONParser parser) {
		this(parser, null, null);
	}

	public TBONReader(TBONParser parser, Collection<TypeReader> typeReaders,
			Map<String, TypeReader> explicitTypeReaderMap) {
		this.parser = parser;
		this.typeReaders = typeReaders;
		this.explicitTypeReaderMap = explicitTypeReaderMap;
	}

	public TBONParser parser() {
		return parser;
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
	public void close() throws IOException {
		parser.close();
	}

}
