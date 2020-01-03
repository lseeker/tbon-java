package kr.inode.tbon.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kr.inode.tbon.TBONGenerator;

public class TBONWriter implements AutoCloseable {
	private static interface IntenalWriter {
		void write(TBONWriter writer, Object obj) throws IOException;
	}

	private static final Map<Class<?>, IntenalWriter> DEFAULT_WRITERS = new HashMap<>();
	private static final Map<Class<?>, IntenalWriter> INTERFACE_WRITERS = new LinkedHashMap<>();

	static {
		DEFAULT_WRITERS.put(Boolean.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Boolean) obj).booleanValue());
			}
		});
		DEFAULT_WRITERS.put(Byte.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Byte) obj).byteValue());
			}
		});
		DEFAULT_WRITERS.put(Short.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Short) obj).shortValue());
			}
		});
		DEFAULT_WRITERS.put(Integer.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Integer) obj).intValue());
			}
		});
		DEFAULT_WRITERS.put(Long.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Long) obj).longValue());
			}
		});
		DEFAULT_WRITERS.put(Float.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Float) obj).floatValue());
			}
		});
		DEFAULT_WRITERS.put(Double.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Double) obj).doubleValue());
			}
		});
		DEFAULT_WRITERS.put(Character.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write(((Character) obj).charValue());
			}
		});
		DEFAULT_WRITERS.put(BigInteger.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((BigInteger) obj);
			}
		});
		DEFAULT_WRITERS.put(BigDecimal.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((BigDecimal) obj);
			}
		});
		DEFAULT_WRITERS.put(boolean[].class, new IntenalWriter() {
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
		DEFAULT_WRITERS.put(byte[].class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((byte[]) obj);
			}
		});
		DEFAULT_WRITERS.put(short[].class, new IntenalWriter() {
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
		DEFAULT_WRITERS.put(int[].class, new IntenalWriter() {
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
		DEFAULT_WRITERS.put(long[].class, new IntenalWriter() {
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
		DEFAULT_WRITERS.put(float[].class, new IntenalWriter() {
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
		DEFAULT_WRITERS.put(double[].class, new IntenalWriter() {
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
		DEFAULT_WRITERS.put(char[].class, new IntenalWriter() {
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
		DEFAULT_WRITERS.put(String.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((String) obj);
			}
		});

		INTERFACE_WRITERS.put(Map.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				Map<?, ?> map = (Map<?, ?>) obj;
				writer.generator.writeStartObject(map.size());
				for (Entry<?, ?> entry : map.entrySet()) {
					writer.generator.write(entry.getKey().toString());
					writer.writeObject(entry.getValue());
				}
				writer.generator.writeEndObject();
			}
		});
		INTERFACE_WRITERS.put(Collection.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				Collection<?> c = (Collection<?>) obj;
				writer.generator.writeStartArray(c.size());
				for (Object element : c) {
					writer.writeObject(element);
				}
				writer.generator.writeEndArray();
			}
		});
		INTERFACE_WRITERS.put(Date.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((Date) obj);
			}
		});
		INTERFACE_WRITERS.put(Calendar.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((Calendar) obj);
			}
		});
		INTERFACE_WRITERS.put(Iterable.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.writeStartArray();
				for (Object element : (Iterable<?>) obj) {
					writer.writeObject(element);
				}
				writer.generator.writeEndArray();
			}
		});
		INTERFACE_WRITERS.put(InputStream.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((InputStream) obj);
			}
		});
		INTERFACE_WRITERS.put(ReadableByteChannel.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((ReadableByteChannel) obj);
			}
		});
		INTERFACE_WRITERS.put(ByteBuffer.class, new IntenalWriter() {
			@Override
			public void write(TBONWriter writer, Object obj) throws IOException {
				writer.generator.write((ByteBuffer) obj);
			}
		});
	}

	private final TBONGenerator generator;
	private final Collection<TypeWriter> typeWriters;
	private final Map<Class<?>, TypeWriter> explicitTypeWriterMap;
	// guard for circular dependency
	private final Deque<Object> guard = new ArrayDeque<>();

	public TBONWriter(final TBONGenerator generator) {
		this(generator, null, null);
	}

	public TBONWriter(final TBONGenerator generator, final Collection<TypeWriter> typeWriters,
			final Map<Class<?>, TypeWriter> explicitTypeWriterMap) {
		this.generator = generator;
		this.typeWriters = typeWriters;
		this.explicitTypeWriterMap = explicitTypeWriterMap;
	}

	public TBONGenerator generator() {
		return generator;
	}

	public void writeObject(Object obj) throws IOException {
		if (obj == null || guard.contains(obj)) {
			generator.writeNull();
			return;
		}

		final Class<?> cls = obj.getClass();
		guard.push(obj);

		try {
			// explicit type writer
			if (explicitTypeWriterMap != null) {
				TypeWriter typeWriter = explicitTypeWriterMap.get(cls);

				if (typeWriter != null) {
					typeWriter.write(this, obj);
					handleAutoCloseable(obj);
					return;
				}
			}

			// Custom multi type writer
			if (typeWriters != null) {
				for (TypeWriter typeWriter : typeWriters) {
					if (typeWriter.canWrite(obj)) {
						typeWriter.write(this, obj);
						handleAutoCloseable(obj);
						return;
					}
				}
			}

			// default match writing
			final IntenalWriter typeWriter = DEFAULT_WRITERS.get(cls);
			if (typeWriter != null) {
				typeWriter.write(this, obj);
				return;
			}

			for (Entry<Class<?>, IntenalWriter> entry : INTERFACE_WRITERS.entrySet()) {
				if (entry.getKey().isAssignableFrom(cls)) {
					entry.getValue().write(this, obj);
					handleAutoCloseable(obj);
					return;
				}
			}

			// array handling
			if (cls.isArray()) {
				int len = Array.getLength(obj);
				generator.writeStartArray(len);
				for (int i = 0; i < len; ++i) {
					writeObject(Array.get(obj, i));
				}
				generator.writeEndArray();
				return;
			}

			// POJO handling
			Map<String, AccessibleObject> properties = new HashMap<>();

			for (Field field : cls.getFields()) {
				int modifiers = field.getModifiers();
				if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
					continue;
				}

				properties.put(field.getName(), field);
			}

			Class<?> methodCls = cls;
			List<String> methodNames = new ArrayList<>();
			do {
				for (Method method : cls.getDeclaredMethods()) {
					int modifiers = method.getModifiers();
					if (!Modifier.isPublic(modifiers) || Modifier.isStatic(method.getModifiers())
							|| method.getParameterTypes().length != 0) {
						continue;
					}
					final String methodName = method.getName();
					if (methodName.length() > 3 && methodName.startsWith("get")) {
						if (methodNames.contains(methodName)) {
							continue;
						}
						char[] name = methodName.toCharArray();
						name[3] = Character.toLowerCase(name[3]);
						properties.put(new String(name, 3, name.length - 3), method);
						methodNames.add(methodName);
					} else if (methodName.length() > 2 && methodName.startsWith("is")) {
						if (methodNames.contains(methodName)) {
							continue;
						}
						char[] name = methodName.toCharArray();
						name[2] = Character.toLowerCase(name[2]);
						properties.put(new String(name, 2, name.length - 2), method);
						methodNames.add(methodName);
					}
				}
				methodCls = methodCls.getSuperclass();
			} while (methodCls != Object.class);

			generator.writeCustomType(cls.getName());

			generator.writeStartObject(properties.size());
			for (Entry<String, AccessibleObject> entry : properties.entrySet()) {
				generator.write(entry.getKey());
				AccessibleObject ao = entry.getValue();
				if (ao instanceof Field) {
					try {
						Object value = ((Field) ao).get(obj);
						writeObject(value);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new IOException("cannot write " + cls, e);
					}
				} else if (ao instanceof Method) {
					try {
						Object value = ((Method) ao).invoke(obj);
						writeObject(value);
					} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
						throw new IOException("cannot write " + cls, e);
					}
				}
			}

			generator.writeEndObject();
		} finally {
			guard.pop();
		}
	}

	public void handleAutoCloseable(Object obj) throws IOException {
		if (obj instanceof AutoCloseable) {
			try {
				((AutoCloseable) obj).close();
			} catch (Exception e) {
				throw new IOException("exception on object closing.");
			}
		}
	}

	@Override
	public void close() throws IOException {
		generator.close();
	}
}
