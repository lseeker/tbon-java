package kr.inode.tbon.mapper;

import java.util.Collection;
import java.util.Map;

public interface TypeHandlerRegistry {
	void register(TypeHandler... typeHandlers);

	void register(MultiTypeReader... multiTypeReaders);

	void register(MultiTypeWriter... multiTypeWriters);

	Map<String, TypeHandler> handlerMapForReader();

	Map<Class<?>, TypeHandler> handlerMapForWriter();

	Collection<MultiTypeReader> multiTypeReaders();

	Collection<MultiTypeWriter> multiTypeWriters();
}
