package kr.inode.tbon.mapper;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kr.inode.tbon.TBONFactory;
import kr.inode.tbon.TBONGenerator;
import kr.inode.tbon.TBONParser;

public class TypeHandlerRegistryTest {
	private static class CharSequenceHandler implements TypeHandler<CharSequence> {
		@Override
		public String typeName() {
			return "CS";
		}

		@Override
		public Class<CharSequence> typeClass() {
			return CharSequence.class;
		}

		@Override
		public CharSequence read(TBONParser parser) {
			return null;
		}

		@Override
		public void write(TBONGenerator generator, CharSequence obj) {

		}
	}

	private static class StringHandler implements TypeHandler<String> {
		@Override
		public String typeName() {
			return "STR";
		}

		@Override
		public Class<String> typeClass() {
			return String.class;
		}

		@Override
		public String read(TBONParser parser) {
			return null;
		}

		@Override
		public void write(TBONGenerator generator, String obj) {

		}
	}

	@Test
	public void testWriterOrder() {
		TBONMapper mapper = new TBONMapper(new TBONFactory() {

			@Override
			public TBONParser createParser(ReadableByteChannel in) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public TBONParser createParser(InputStream in) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public TBONGenerator createGenerator(WritableByteChannel out) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public TBONGenerator createGenerator(OutputStream out) {
				// TODO Auto-generated method stub
				return null;
			}
		});

		mapper.customTypeHandlerRegistry().register(new CharSequenceHandler(), new StringHandler());

		Iterator<Entry<Class<?>, TypeHandler<?>>> it = mapper.customTypeHandlerRegistry().mapForWriter().entrySet()
				.iterator();
		Entry<Class<?>, TypeHandler<?>> entry = it.next();

		Assertions.assertEquals(String.class, entry.getKey());
		
		entry = it.next();
		Assertions.assertEquals(CharSequence.class, entry.getKey());
		
	}

}
