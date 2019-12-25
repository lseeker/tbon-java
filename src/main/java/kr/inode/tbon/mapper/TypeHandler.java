package kr.inode.tbon.mapper;

import kr.inode.tbon.TBONGenerator;
import kr.inode.tbon.TBONParser;

public interface TypeHandler<T> {
	String typeName();

	Class<T> typeClass();

	T read(TBONParser parser);

	void write(TBONGenerator generator, Object obj);
}
