package kr.inode.tbon.mapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TypeHandlerRegistryTest {
	private static class CharSequenceHandler implements TypeHandler {
		@Override
		public String typeName() {
			return "CS";
		}

		@Override
		public Class<CharSequence> typeClass() {
			return CharSequence.class;
		}

		@Override
		public <T> T read(String typeName, TBONReader reader) throws IOException {
			return null;
		}

		@Override
		public void write(TBONWriter writer, Object obj) throws IOException {

		}
	}

	private static class StringHandler implements TypeHandler {
		@Override
		public String typeName() {
			return "STR";
		}

		@Override
		public Class<String> typeClass() {
			return String.class;
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

		Iterator<Entry<Class<?>, TypeHandler>> it = mapper.typeHandlerRegistry().handlerMapForWriter().entrySet()
				.iterator();
		Entry<Class<?>, TypeHandler> entry = it.next();

		Assertions.assertEquals(String.class, entry.getKey());

		entry = it.next();
		Assertions.assertEquals(CharSequence.class, entry.getKey());
	}

}
