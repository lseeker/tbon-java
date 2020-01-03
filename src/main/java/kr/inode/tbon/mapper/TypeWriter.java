package kr.inode.tbon.mapper;

import java.io.IOException;

public interface TypeWriter {
	boolean canWrite(Object obj);

	void write(TBONWriter writer, Object obj) throws IOException;
}
