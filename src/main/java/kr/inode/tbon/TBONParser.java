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

	char getChar();

	String getString();

	/**
	 * element count of array or object.
	 * 
	 * @return element count. -1 if undefined.
	 */
	int getElementCount();

	/**
	 * 
	 * @return
	 */
	String getCustomTypeName();
}
