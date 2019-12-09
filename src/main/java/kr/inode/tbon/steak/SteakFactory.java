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

	@Override
	public TBONParser createParser(InputStream in) throws IOException {
		return createParser(Channels.newChannel(in));
	}

	@Override
	public TBONParser createParser(ReadableByteChannel in) throws IOException {
		return new SteakParser(in);
	}

	@Override
	public TBONGenerator createGenerator(OutputStream out) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TBONGenerator createGenerator(WritableByteChannel out) {
		// TODO Auto-generated method stub
		return null;
	}

}
