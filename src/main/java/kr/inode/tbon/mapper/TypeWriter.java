package kr.inode.tbon.mapper;

import java.io.IOException;

interface TypeWriter {
	void write(TBONWriter writer, Object obj) throws IOException;
}
