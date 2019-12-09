package kr.inode.tbon.mapper;

import kr.inode.tbon.TBONGenerator;
import kr.inode.tbon.TBONParser;

class POJOHandler implements TypeHandler<Object> {
	@Override
	public String typeName() {
		return null;
	}

	@Override
	public Class<Object> typeClass() {
		return Object.class;
	}

	@Override
	public Object read(TBONParser parser) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(TBONGenerator generator, Object obj) {
		// TODO Auto-generated method stub

	}
}
