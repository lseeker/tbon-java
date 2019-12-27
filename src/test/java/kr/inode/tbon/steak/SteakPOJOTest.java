package kr.inode.tbon.steak;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kr.inode.tbon.mapper.TBONMapper;

public class SteakPOJOTest {
	public static class P {
		public String a;
		private String b;

		public String getB() {
			return b;
		}

		public void setB(String b) {
			this.b = b;
		}
	}

	@Test
	public void testPOJO() throws IOException {
		TBONMapper mapper = new TBONMapper(new SteakFactory());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			P p = new P();
			p.a = "한글";
			p.setB("ABC");
			mapper.writeTo(out, p);

			byte[] array = out.toByteArray();
			try (ByteArrayInputStream in = (new ByteArrayInputStream(array))) {
				Object obj = mapper.readFrom(in);

				Assertions.assertNotNull(obj);
				Assertions.assertTrue(obj instanceof P);
				P r = (P) obj;
				Assertions.assertEquals("한글", r.a);
				Assertions.assertEquals("ABC", r.getB());
			}
		}

	}

}
