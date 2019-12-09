package kr.inode.tbon.mapper;

import java.util.Map;

public interface TypeHandlerRegistry {
	void register(TypeHandler<?>... customTypeHandlers);

	Map<Class<?>, TypeHandler<?>> mapForWriter();

	Map<String, TypeHandler<?>> mapForReader();
}
