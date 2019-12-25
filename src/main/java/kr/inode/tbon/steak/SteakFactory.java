package kr.inode.tbon.steak;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import kr.inode.tbon.TBONFactory;
import kr.inode.tbon.TBONGenerator;
import kr.inode.tbon.TBONParser;

public class SteakFactory implements TBONFactory {
	static final byte[] STEAK_HEADER = { (byte) 0xf0, (byte) 0x9f, (byte) 0xa5, (byte) 0xa9, 0x00 };

	@Override
	public TBONParser createParser(InputStream in) throws IOException {
		return createParser(Channels.newChannel(in));
	}

	@Override
	public TBONParser createParser(ReadableByteChannel in) throws IOException {
		return new SteakParser(in);
	}

	@Override
	public TBONGenerator createGenerator(OutputStream out) throws IOException {
		return createGenerator(Channels.newChannel(out));
	}

	@Override
	public TBONGenerator createGenerator(WritableByteChannel out) throws IOException {
		return new SteakGenerator(out);
	}
}
