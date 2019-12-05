package kr.inode.tbon.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kr.inode.tbon.TBONFactory;
import kr.inode.tbon.TBONGenerator;

public class TBONMapper {
	private final TBONFactory factory;

	private List<TypeHandler<?>> typeHandlers;
	private boolean regenerateHandlerMap = false;
	private Map<String, TypeHandler<?>> readerHandlerMap;
	private Map<Class<?>, TypeHandler<?>> writerHandlerMap;

	public TBONMapper(TBONFactory factory) {
		this.factory = factory;
	}

	public List<TypeHandler<?>> typeHandlers() {
		if (typeHandlers == null) {
			typeHandlers = new ArrayList<>();
		}
		regenerateHandlerMap = true;
		return typeHandlers;
	}

	private void regenerateHandlerMap() {
		Map<String, TypeHandler<?>> readerMap = new HashMap<>(typeHandlers.size() * 2);
		// writer important for order (failback to instanceof operation)
		Map<Class<?>, TypeHandler<?>> writerMap = new LinkedHashMap<>(typeHandlers.size() * 2);

		for (TypeHandler<?> typeHandler : typeHandlers) {
			readerMap.put(typeHandler.typeName(), typeHandler);
			writerMap.put(typeHandler.typeClass(), typeHandler);
		}

		this.readerHandlerMap = readerMap;
		this.writerHandlerMap = writerMap;

		regenerateHandlerMap = false;
	}

	public <T> T readFrom(InputStream in) throws IOException {
		return readFrom(Channels.newChannel(in));
	}

	public <T> T readFrom(ReadableByteChannel in) throws IOException {
		if (regenerateHandlerMap) {
			regenerateHandlerMap();
		}
		return null;
	}

	public void writeTo(OutputStream out, Object obj) throws IOException {
		writeTo(Channels.newChannel(out), obj);
	}

	public void writeTo(WritableByteChannel out, Object obj) throws IOException {
		if (regenerateHandlerMap) {
			regenerateHandlerMap();
		}

		try (TBONGenerator generator = factory.createGenerator(out)) {

		}
	}

}
