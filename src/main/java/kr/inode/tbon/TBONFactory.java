package kr.inode.tbon;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public interface TBONFactory {
	TBONParser createParser(InputStream in);

	TBONParser createParser(ReadableByteChannel in);

	TBONGenerator createGenerator(OutputStream out);

	TBONGenerator createGenerator(WritableByteChannel out);
}
