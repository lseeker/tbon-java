package kr.inode.tbon.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import kr.inode.tbon.TBONFactory;
import kr.inode.tbon.TBONGenerator;

public class TBONMapper {
	private final TBONFactory factory;
	private TypeHandlerRegistry customTypeHandlerRegistry;

	public TBONMapper(TBONFactory factory) {
		this.factory = factory;
	}

	public TypeHandlerRegistry customTypeHandlerRegistry() {
		if (customTypeHandlerRegistry == null) {
			customTypeHandlerRegistry = new TypeHandlerRegistryImpl();
		}
		return customTypeHandlerRegistry;
	}

	public <T> T readFrom(InputStream in) throws IOException {
		return readFrom(Channels.newChannel(in));
	}

	public <T> T readFrom(ReadableByteChannel in) throws IOException {
		try (TBONReader reader = new TBONReader(factory.createParser(in))) {
			return reader.nextValue();
		}
	}

	public void writeTo(OutputStream out, Object obj) throws IOException {
		writeTo(Channels.newChannel(out), obj);
	}

	public void writeTo(WritableByteChannel out, Object obj) throws IOException {
		try (TBONGenerator generator = factory.createGenerator(out)) {

		}
	}

}
