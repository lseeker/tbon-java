package kr.inode.tbon.steak;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kr.inode.tbon.mapper.TBONMapper;

public class SteakObjectTypesTest {
	@Test
	public void testDate() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Date d = new Date();
			mapper.writeTo(out, d);

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = (new ByteArrayInputStream(array))) {
				Date value = mapper.readFrom(in);

				Assertions.assertEquals(d, value);
			}
		}
	}

	@Test
	public void testBigInteger() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			mapper.writeTo(out, BigInteger.ZERO);
			mapper.writeTo(out, BigInteger.ONE);
			mapper.writeTo(out, new BigInteger("1234567890"));

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 0, 6)) {
				BigInteger value = mapper.readFrom(in);
				Assertions.assertEquals(BigInteger.ZERO, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 6, 7)) {
				BigInteger value = mapper.readFrom(in);
				Assertions.assertEquals(BigInteger.ONE, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 13, 10)) {
				BigInteger value = mapper.readFrom(in);
				Assertions.assertEquals(new BigInteger("1234567890"), value);
			}
		}
	}

	@Test
	public void testBigDecimal() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			mapper.writeTo(out, BigDecimal.ZERO);
			mapper.writeTo(out, BigDecimal.TEN);
			mapper.writeTo(out, new BigDecimal("0.12345"));

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 0, 6)) {
				BigDecimal value = mapper.readFrom(in);
				Assertions.assertEquals(BigDecimal.ZERO, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 6, 8)) {
				BigDecimal value = mapper.readFrom(in);
				Assertions.assertEquals(BigDecimal.TEN, value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 14, 9)) {
				BigDecimal value = mapper.readFrom(in);
				Assertions.assertEquals(new BigDecimal("0.12345"), value);
			}
		}
	}

	@Test
	public void testOctet() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			byte[] target = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".repeat(10).getBytes(StandardCharsets.UTF_8);
			mapper.writeTo(out, new byte[0]);
			mapper.writeTo(out, target);

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 0, 6)) {
				byte[] value = mapper.readFrom(in);
				Assertions.assertEquals(0, value.length);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 6, 268)) {
				byte[] value = mapper.readFrom(in);
				Assertions.assertArrayEquals(target, value);
			}
		}
	}

	@Test
	public void testString() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			String target = "가나다라마바사".repeat(10);
			mapper.writeTo(out, "");
			mapper.writeTo(out, target);

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 0, 6)) {
				String value = mapper.readFrom(in);
				Assertions.assertEquals("", value);
			}

			try (ByteArrayInputStream in = new ByteArrayInputStream(array, 6, 268)) {
				String value = mapper.readFrom(in);
				Assertions.assertEquals(target, value);
			}
		}
	}
}
