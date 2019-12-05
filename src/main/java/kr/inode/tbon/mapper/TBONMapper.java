package kr.inode.tbon.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import kr.inode.tbon.TBONFactory;
import kr.inode.tbon.TBONGenerator;

public class TBONMapper {
	private final TBONFactory factory;

	private Map<String, TypeHandler<?>> readerHandlerMap;
	private LinkedHashMap<Class<?>, TypeHandler<?>> writerHandlerMap;

	public TBONMapper(TBONFactory factory) {
		this.factory = factory;
	}

	public void registerTypeHandler(TypeHandler<?>... typeHandlers) {
		if (readerHandlerMap == null) {
			readerHandlerMap = new HashMap<>(typeHandlers.length * 2);
			writerHandlerMap = new LinkedHashMap<>(typeHandlers.length * 2);
		}

		for (TypeHandler<?> typeHandler : typeHandlers) {
			readerHandlerMap.put(typeHandler.typeName(), typeHandler);

			// order for subclass
			writerHandlerMap.remove(typeHandler.typeClass());
			Class<?> typeSuperClass = typeHandler.typeClass().getSuperclass();
			while (typeSuperClass != null) {
			}
		}

	}

	public <T> T readFrom(InputStream in) throws IOException {
		return readFrom(Channels.newChannel(in));
	}

	public <T> T readFrom(ReadableByteChannel in) throws IOException {
		return null;
	}

	public void writeTo(OutputStream out, Object obj) throws IOException {
		writeTo(Channels.newChannel(out), obj);
	}

	public void writeTo(WritableByteChannel out, Object obj) throws IOException {
		try (TBONGenerator generator = factory.createGenerator(out)) {

		}
	}

}
