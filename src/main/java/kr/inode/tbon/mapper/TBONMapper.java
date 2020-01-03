package kr.inode.tbon.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import kr.inode.tbon.TBONFactory;
import kr.inode.tbon.steak.SteakFactory;

public class TBONMapper {
	private final TBONFactory factory;
	private TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistryImpl();

	public TBONMapper() {
		this(new SteakFactory());
	}

	public TBONMapper(TBONFactory factory) {
		this.factory = factory;
	}

	public TypeHandlerRegistry typeHandlerRegistry() {
		return typeHandlerRegistry;
	}

	public <T> T readFrom(InputStream in) throws IOException {
		return readFrom(Channels.newChannel(in));
	}

	public <T> T readFrom(ReadableByteChannel in) throws IOException {
		try (TBONReader reader = new TBONReader(factory.createParser(in), typeHandlerRegistry.typeReaders(),
				typeHandlerRegistry.explicitTypeReaderMap())) {
			return reader.nextValue();
		}
	}

	public void writeTo(OutputStream out, Object obj) throws IOException {
		writeTo(Channels.newChannel(out), obj);
	}

	public void writeTo(WritableByteChannel out, Object obj) throws IOException {
		try (TBONWriter writer = new TBONWriter(factory.createGenerator(out), typeHandlerRegistry.typeWriters(),
				typeHandlerRegistry.explicitTypeWriterMap())) {
			writer.writeObject(obj);
		}
	}
}
