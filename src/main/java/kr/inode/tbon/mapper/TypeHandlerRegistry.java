package kr.inode.tbon.mapper;

import java.util.Collection;
import java.util.Map;

public interface TypeHandlerRegistry {
	void register(TypeHandler... typeHandlers);

	void register(TypeReader... typeReaders);

	void register(TypeWriter... typeWriters);

	Collection<TypeReader> typeReaders();

	Collection<TypeWriter> typeWriters();

	Map<String, TypeReader> explicitTypeReaderMap();

	Map<Class<?>, TypeWriter> explicitTypeWriterMap();

}
