package kr.inode.tbon.mapper;

public interface MultiTypeReader extends TypeReader {
	boolean canRead(String typeName);
}
