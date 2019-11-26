package kr.inode.tbon;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import kr.inode.bons.Writer;

public class TBONWriter implements Writer {
	private OutputStream stream;
	
	TBONWriter(OutputStream stream) {
		this.stream = stream;
	}
	

	@Override
	public void writeNull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(byte b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(short s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(long l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(BigInteger i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(float f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(double d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(BigDecimal d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(char c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(Date d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeStartArray() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeStartArray(int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeStartArray(Class<?> elementType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeStartArray(Class<?> elementType, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeEndArray() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeStartObject() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeStartObject(int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeStartObject(Class<?> keyType, Class<?> valueType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeStartObject(Class<?> keyType, Class<?> valueType, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeEndObject() {
		// TODO Auto-generated method stub
		
	}

}
