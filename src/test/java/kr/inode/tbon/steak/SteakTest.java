package kr.inode.tbon.steak;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kr.inode.tbon.mapper.TBONMapper;

public class SteakTest {
	@Test
	public void testNull() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			mapper.writeTo(out, null);

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = (new ByteArrayInputStream(array))) {
				Object obj = mapper.readFrom(in);

				Assertions.assertNull(obj);
			}
		}
	}

	@Test
	public void testBoolean() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			mapper.writeTo(out, true);
			mapper.writeTo(out, false);

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 0, 6)) {
				Boolean value = mapper.readFrom(in);
				Assertions.assertTrue(value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 6, 6)) {
				Boolean value = mapper.readFrom(in);
				Assertions.assertFalse(value);
			}
		}
	}

	@Test
	public void testByte() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			mapper.writeTo(out, (byte) 0);
			mapper.writeTo(out, (byte) 1);
			mapper.writeTo(out, (byte) -1);
			mapper.writeTo(out, Byte.MAX_VALUE);
			mapper.writeTo(out, Byte.MIN_VALUE);

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 0, 6)) {
				Byte value = mapper.readFrom(in);
				Assertions.assertEquals((byte) 0, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 6, 7)) {
				Byte value = mapper.readFrom(in);
				Assertions.assertEquals((byte) 1, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 13, 7)) {
				Byte value = mapper.readFrom(in);
				Assertions.assertEquals((byte) -1, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 20, 7)) {
				Byte value = mapper.readFrom(in);
				Assertions.assertEquals(Byte.MAX_VALUE, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 27, 7)) {
				Byte value = mapper.readFrom(in);
				Assertions.assertEquals(Byte.MIN_VALUE, value);
			}
		}
	}

	@Test
	public void testShort() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			mapper.writeTo(out, (short) 0);
			mapper.writeTo(out, (short) 1);
			mapper.writeTo(out, (short) -1);
			mapper.writeTo(out, Short.MAX_VALUE);
			mapper.writeTo(out, Short.MIN_VALUE);

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 0, 6)) {
				Short value = mapper.readFrom(in);
				Assertions.assertEquals((short) 0, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 6, 7)) {
				Short value = mapper.readFrom(in);
				Assertions.assertEquals((short) 1, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 13, 7)) {
				Short value = mapper.readFrom(in);
				Assertions.assertEquals((short) -1, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 20, 8)) {
				Short value = mapper.readFrom(in);
				Assertions.assertEquals(Short.MAX_VALUE, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 28, 8)) {
				Short value = mapper.readFrom(in);
				Assertions.assertEquals(Short.MIN_VALUE, value);
			}
		}
	}

	@Test
	public void testInt() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			mapper.writeTo(out, 0);
			mapper.writeTo(out, 1);
			mapper.writeTo(out, -1);
			mapper.writeTo(out, Integer.MAX_VALUE);
			mapper.writeTo(out, Integer.MIN_VALUE);

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 0, 6)) {
				Integer value = mapper.readFrom(in);
				Assertions.assertEquals(0, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 6, 7)) {
				Integer value = mapper.readFrom(in);
				Assertions.assertEquals(1, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 13, 7)) {
				Integer value = mapper.readFrom(in);
				Assertions.assertEquals(-1, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 20, 10)) {
				Integer value = mapper.readFrom(in);
				Assertions.assertEquals(Integer.MAX_VALUE, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 30, 10)) {
				Integer value = mapper.readFrom(in);
				Assertions.assertEquals(Integer.MIN_VALUE, value);
			}
		}
	}

	@Test
	public void testLong() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			mapper.writeTo(out, 0L);
			mapper.writeTo(out, 1L);
			mapper.writeTo(out, -1L);
			mapper.writeTo(out, Long.MAX_VALUE);
			mapper.writeTo(out, Long.MIN_VALUE);

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 0, 6)) {
				Long value = mapper.readFrom(in);
				Assertions.assertEquals(0L, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 6, 7)) {
				Long value = mapper.readFrom(in);
				Assertions.assertEquals(1L, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 13, 7)) {
				Long value = mapper.readFrom(in);
				Assertions.assertEquals(-1L, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 20, 14)) {
				Long value = mapper.readFrom(in);
				Assertions.assertEquals(Long.MAX_VALUE, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 34, 14)) {
				Long value = mapper.readFrom(in);
				Assertions.assertEquals(Long.MIN_VALUE, value);
			}
		}
	}

	@Test
	public void testFloat() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			mapper.writeTo(out, 0f);
			mapper.writeTo(out, 1f);
			mapper.writeTo(out, -1f);
			mapper.writeTo(out, Float.MIN_VALUE);
			mapper.writeTo(out, Float.MAX_VALUE);
			mapper.writeTo(out, Float.POSITIVE_INFINITY);
			mapper.writeTo(out, Float.NEGATIVE_INFINITY);

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 0, 6)) {
				Float value = mapper.readFrom(in);
				Assertions.assertEquals(0f, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 6, 10)) {
				Float value = mapper.readFrom(in);
				Assertions.assertEquals(1f, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 16, 10)) {
				Float value = mapper.readFrom(in);
				Assertions.assertEquals(-1f, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 26, 10)) {
				Float value = mapper.readFrom(in);
				Assertions.assertEquals(Float.MIN_VALUE, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 36, 10)) {
				Float value = mapper.readFrom(in);
				Assertions.assertEquals(Float.MAX_VALUE, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 46, 10)) {
				Float value = mapper.readFrom(in);
				Assertions.assertEquals(Float.POSITIVE_INFINITY, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 56, 10)) {
				Float value = mapper.readFrom(in);
				Assertions.assertEquals(Float.NEGATIVE_INFINITY, value);
			}
		}
	}

	@Test
	public void testDouble() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			mapper.writeTo(out, 0d);
			mapper.writeTo(out, 1d);
			mapper.writeTo(out, -1d);
			mapper.writeTo(out, Double.MIN_VALUE);
			mapper.writeTo(out, Double.MAX_VALUE);
			mapper.writeTo(out, Double.POSITIVE_INFINITY);
			mapper.writeTo(out, Double.NEGATIVE_INFINITY);

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 0, 6)) {
				Double value = mapper.readFrom(in);
				Assertions.assertEquals(0d, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 6, 14)) {
				Double value = mapper.readFrom(in);
				Assertions.assertEquals(1d, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 20, 14)) {
				Double value = mapper.readFrom(in);
				Assertions.assertEquals(-1d, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 34, 14)) {
				Double value = mapper.readFrom(in);
				Assertions.assertEquals(Double.MIN_VALUE, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 48, 14)) {
				Double value = mapper.readFrom(in);
				Assertions.assertEquals(Double.MAX_VALUE, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 62, 14)) {
				Double value = mapper.readFrom(in);
				Assertions.assertEquals(Double.POSITIVE_INFINITY, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 76, 14)) {
				Double value = mapper.readFrom(in);
				Assertions.assertEquals(Double.NEGATIVE_INFINITY, value);
			}
		}
	}
}
