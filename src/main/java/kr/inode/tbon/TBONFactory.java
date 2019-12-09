package kr.inode.tbon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public interface TBONFactory {
	TBONParser createParser(InputStream in) throws IOException;

	TBONParser createParser(ReadableByteChannel in) throws IOException;

	TBONGenerator createGenerator(OutputStream out) throws IOException;

	TBONGenerator createGenerator(WritableByteChannel out) throws IOException;
}
