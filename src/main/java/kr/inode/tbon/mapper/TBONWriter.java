package kr.inode.tbon.mapper;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import kr.inode.tbon.TBONGenerator;

public class TBONWriter implements AutoCloseable {
	private static interface WriterFunc {
		void write(TBONWriter writer, Object obj) throws IOException;
	}

	private static final Map<Class<?>, WriterFunc> WRITER_FUNCS = new HashMap<>();

	static {
		WRITER_FUNCS.put(Boolean.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Boolean) obj).booleanValue());
			}
		});
		WRITER_FUNCS.put(Byte.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Byte) obj).byteValue());
			}
		});
		WRITER_FUNCS.put(Short.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Short) obj).shortValue());
			}
		});
		WRITER_FUNCS.put(Integer.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Integer) obj).intValue());
			}
		});
		WRITER_FUNCS.put(Long.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Long) obj).longValue());
			}
		});
		WRITER_FUNCS.put(Float.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Float) obj).floatValue());
			}
		});
		WRITER_FUNCS.put(Double.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Double) obj).doubleValue());
			}
		});
		WRITER_FUNCS.put(Character.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Character) obj).charValue());
			}
		});
		WRITER_FUNCS.put(BigInteger.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((BigInteger) obj);
			}
		});
		WRITER_FUNCS.put(BigDecimal.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((BigDecimal) obj);
			}
		});
		WRITER_FUNCS.put(Date.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((Date) obj);
			}
		});
		WRITER_FUNCS.put(Calendar.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((Calendar) obj);
			}
		});
		WRITER_FUNCS.put(boolean[].class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				int len = Array.getLength(obj);
				writer.generator.writeStartPrimitiveArray(boolean.class, len);
				for (int i = 0; i < len; ++i) {
					writer.generator.write(Array.getBoolean(obj, i));
				}
				writer.generator.writeEndArray();
			}
		});
		WRITER_FUNCS.put(byte[].class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((byte[]) obj);
			}
		});
		WRITER_FUNCS.put(short[].class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				int len = Array.getLength(obj);
				writer.generator.writeStartPrimitiveArray(short.class, len);
				for (int i = 0; i < len; ++i) {
					writer.generator.write(Array.getShort(obj, i));
				}
				writer.generator.writeEndArray();
			}
		});
		WRITER_FUNCS.put(int[].class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				int len = Array.getLength(obj);
				writer.generator.writeStartPrimitiveArray(int.class, len);
				for (int i = 0; i < len; ++i) {
					writer.generator.write(Array.getInt(obj, i));
				}
				writer.generator.writeEndArray();
			}
		});
		WRITER_FUNCS.put(long[].class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				int len = Array.getLength(obj);
				writer.generator.writeStartPrimitiveArray(long.class, len);
				for (int i = 0; i < len; ++i) {
					writer.generator.write(Array.getLong(obj, i));
				}
				writer.generator.writeEndArray();
			}
		});
		WRITER_FUNCS.put(float[].class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				int len = Array.getLength(obj);
				writer.generator.writeStartPrimitiveArray(float.class, len);
				for (int i = 0; i < len; ++i) {
					writer.generator.write(Array.getFloat(obj, i));
				}
				writer.generator.writeEndArray();
			}
		});
		WRITER_FUNCS.put(double[].class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				int len = Array.getLength(obj);
				writer.generator.writeStartPrimitiveArray(double.class, len);
				for (int i = 0; i < len; ++i) {
					writer.generator.write(Array.getDouble(obj, i));
				}
				writer.generator.writeEndArray();
			}
		});
		WRITER_FUNCS.put(char[].class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				int len = Array.getLength(obj);
				writer.generator.writeStartPrimitiveArray(char.class, len);
				for (int i = 0; i < len; ++i) {
					writer.generator.write(Array.getChar(obj, i));
				}
				writer.generator.writeEndArray();
			}
		});
		WRITER_FUNCS.put(String.class, new WriterFunc() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((String) obj);
			}
		});
	}

	private final TBONGenerator generator;
	private final Map<Class<?>, TypeHandler<?>> typeHandlers;

	public TBONWriter(final TBONGenerator generator) {
		this(generator, null);
	}

	public TBONWriter(final TBONGenerator generator, final Map<Class<?>, TypeHandler<?>> typeHandlers) {
		this.generator = generator;
		this.typeHandlers = typeHandlers;
	}

	public void writeObject(Object obj) throws IOException {
		if (obj == null) {
			generator.writeNull();
			return;
		}

		final Class<?> cls = obj.getClass();
		if (typeHandlers != null) {
			TypeHandler<?> typeHandler = typeHandlers.get(cls);
			if (typeHandler == null) {
				for (Entry<Class<?>, TypeHandler<?>> entry : typeHandlers.entrySet()) {
					if (entry.getKey().isAssignableFrom(cls)) {
						typeHandler = entry.getValue();
						break;
					}
				}
			}

			if (typeHandler != null) {
				generator.writeCustomType(typeHandler.typeName());
				typeHandler.write(generator, obj);
				return;
			}
		}

		final WriterFunc writerFunc = WRITER_FUNCS.get(cls);
		if (writerFunc != null) {
			writerFunc.write(this, obj);
			return;
		}

		if (cls.isArray()) {
			int len = Array.getLength(obj);
			generator.writeStartArray(len);
			for (int i = 0; i < len; ++i) {
				writeObject(Array.get(obj, i));
			}
			generator.writeEndArray();
			return;
		}

		// TODO POJO handling
	}

	@Override
	public void close() throws IOException {
		generator.close();
	}
}
