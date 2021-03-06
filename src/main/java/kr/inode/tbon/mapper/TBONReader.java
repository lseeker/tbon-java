package kr.inode.tbon.mapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
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
				final int len = reader.parser.getElementCount();
				if (len != -1 && len < 32 * 1024) {
					return reader.parser.readOctet();
				} else {
					final File tempFile = File.createTempFile("tbon", ".octet");
					tempFile.deleteOnExit();

					try (final FileOutputStream out = new FileOutputStream(tempFile)) {
						reader.parser.readOctet(out);
					}

					return tempFile;
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
				final TBONParser parser = reader.parser;
				final int len = parser.getElementCount();
				final boolean[] arr = new boolean[len];
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
				final TBONParser parser = reader.parser;
				final int len = parser.getElementCount();
				final short[] arr = new short[len];
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
				final TBONParser parser = reader.parser;
				final int len = parser.getElementCount();
				final int[] arr = new int[len];
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
				final TBONParser parser = reader.parser;
				final int len = parser.getElementCount();
				final long[] arr = new long[len];
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
				final TBONParser parser = reader.parser;
				final int len = parser.getElementCount();
				final float[] arr = new float[len];
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
				final TBONParser parser = reader.parser;
				final int len = parser.getElementCount();
				final double[] arr = new double[len];
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
				final TBONParser parser = reader.parser;
				final int len = parser.getElementCount();
				final char[] arr = new char[len];
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
				final TBONParser parser = reader.parser;
				final int len = parser.getElementCount();
				final List<Object> list = new ArrayList<>(len == -1 ? 16 : len);
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
				final TBONParser parser = reader.parser;
				final int len = parser.getElementCount();
				final Map<String, Object> map = new LinkedHashMap<>(len == -1 ? 16 : len * 2);
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
				final TBONParser parser = reader.parser;
				final String customTypeName = parser.getCustomTypeName();
				if (reader.explicitTypeReaderMap != null) {
					final TypeReader typeReader = reader.explicitTypeReaderMap.get(customTypeName);
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
					final Class<?> cls = Class.forName(customTypeName, true,
							Thread.currentThread().getContextClassLoader());
					final Object obj = cls.getConstructor().newInstance();
					final Map<String, Object> properties = reader.nextValue();
					for (final Method method : cls.getMethods()) {
						if (Modifier.isStatic(method.getModifiers()) || method.getParameterTypes().length != 1) {
							continue;
						}

						final String methodName = method.getName();
						if (methodName.length() > 3 && methodName.startsWith("set")) {
							final char[] name = methodName.toCharArray();
							name[3] = Character.toLowerCase(name[3]);
							final String fieldName = new String(name, 3, name.length - 3);
							final Object value = properties.get(fieldName);
							if (value != null) {
								method.invoke(obj, convertOctet(value, method.getParameterTypes()[0]));
								properties.remove(fieldName);
							}
						}
					}

					for (final Entry<String, Object> entry : properties.entrySet()) {
						final Object value = entry.getValue();
						if (value == null) {
							continue;
						}

						try {
							final Field field = cls.getField(entry.getKey());
							final int modifiers = field.getModifiers();
							if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
								continue;
							}

							field.set(obj, convertOctet(value, field.getType()));
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

	private static Object convertOctet(Object source, Class<?> target) throws IOException {
		if (target == byte[].class) {
			if (source instanceof File) {
				return Files.readAllBytes(((File) source).toPath());
			}
		} else if (InputStream.class.isAssignableFrom(target)) {
			// source should byte[] or File
			if (source instanceof byte[]) {
				return new ByteArrayInputStream((byte[]) source);
			} else if (source instanceof File) {
				return new FileInputStream((File) source);
			}
		}

		return source;
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
