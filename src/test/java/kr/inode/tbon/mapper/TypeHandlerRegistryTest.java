package kr.inode.tbon.mapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TypeHandlerRegistryTest {
	private static class CharSequenceHandler implements TypeHandler, ExplicitTypeWriter {
		@Override
		public Class<?> typeClass() {
			return CharSequence.class;
		}

		@Override
		public boolean canRead(String typeName) {
			return "CS".equals(typeName);
		}

		@Override
		public boolean canWrite(Object obj) {
			return obj instanceof CharSequence;
		}

		@Override
		public <T> T read(String typeName, TBONReader reader) throws IOException {
			return null;
		}

		@Override
		public void write(TBONWriter writer, Object obj) throws IOException {

		}

	}

	private static class StringHandler implements TypeHandler, ExplicitTypeWriter {
		@Override
		public Class<?> typeClass() {
			return String.class;
		}

		@Override
		public boolean canRead(String typeName) {
			return "STR".equals(typeName);
		}

		@Override
		public boolean canWrite(Object obj) {
			return obj instanceof String;
		}

		@Override
		public <T> T read(String typeName, TBONReader reader) throws IOException {
			return null;
		}

		@Override
		public void write(TBONWriter writer, Object obj) throws IOException {
		}
	}

	@Test
	public void testWriterOrder() {
		TBONMapper mapper = new TBONMapper();

		mapper.typeHandlerRegistry().register(new CharSequenceHandler(), new StringHandler());

		Iterator<Entry<Class<?>, TypeWriter>> it = mapper.typeHandlerRegistry().explicitTypeWriterMap().entrySet()
				.iterator();
		Entry<Class<?>, TypeWriter> entry = it.next();

		Assertions.assertEquals(String.class, entry.getKey());

		entry = it.next();
		Assertions.assertEquals(CharSequence.class, entry.getKey());
	}

}
