package kr.inode.tbon.mapper;

public interface MultiTypeWriter extends TypeWriter {
	boolean canWrite(Object obj);
}
