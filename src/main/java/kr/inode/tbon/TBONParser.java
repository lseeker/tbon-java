package kr.inode.tbon;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public interface TBONParser extends Closeable {

	boolean next() throws IOException;

	TBONToken currentToken();

	TBONToken nextToken() throws IOException;

	boolean getBoolean();

	byte getByte();

	short getShort();

	int getInt();

	long getLong();

	BigInteger getInteger();

	// getNumberFit()
	// numberFitOn()

	float getFloat();

	double getDouble();

	BigDecimal getDecimal();

	Date getDate();

	// java 8
	// LocalDate getLocalDate();
	// LocalTime getLocalTime();
	// LocalDateTime getLocalDateTime();
	// ZonedDateTime getZonedDateTime();

	boolean isSurrogateChar();

	char getChar();

	char[] getChars();

	String getString();

	/**
	 * array depth for multidimension array
	 * 
	 * @return array depth. 0 if current token is not array.
	 */
	int getDepth();

	/**
	 * element count of array or object.
	 * 
	 * @return element count. -1 if undefined.
	 */
	int getElementCount();

	/**
	 * array element type.
	 * 
	 * @return array element type. null if undefined.
	 */
	Class<?> getElementType();

	/**
	 * object key type.
	 * 
	 * @return object key type. null if custom type.
	 */
	Class<?> getKeyType();

	/**
	 * object value type.
	 * 
	 * @return object value type. null if custom type.
	 */
	Class<?> getValueType();

	/**
	 * 
	 * @return
	 */
	String getCustomTypeName();
}
