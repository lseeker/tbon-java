package kr.inode.tbon.mapper;

import java.io.IOException;

interface TypeReader {
	<T> T read(String typeName, TBONReader reader) throws IOException;
}
