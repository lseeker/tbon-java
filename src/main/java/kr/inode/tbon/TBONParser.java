package kr.inode.tbon;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
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

	Calendar getCalendar();

	// java 8
	// LocalDate getLocalDate();
	// LocalTime getLocalTime();
	// LocalDateTime getLocalDateTime();
	// ZonedDateTime getZonedDateTime();

	char getChar();

	String readString() throws IOException;

	byte[] readOctet() throws IOException;

	void readOctet(OutputStream out) throws IOException;

	/**
	 * element count of array, object, or octet.
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
