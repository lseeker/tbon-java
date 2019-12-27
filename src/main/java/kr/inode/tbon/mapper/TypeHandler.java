package kr.inode.tbon.mapper;

public interface TypeHandler extends TypeReader, TypeWriter {
	String typeName();

	Class<?> typeClass();
}
