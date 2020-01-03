package kr.inode.tbon.mapper;

import java.io.IOException;

public interface TypeReader {
	boolean canRead(String typeName);

	<T> T read(String typeName, TBONReader reader) throws IOException;
}
