package kr.inode.tbon;

import java.math.BigDecimal;
import java.math.BigInteger;

public enum TBONToken {
	/**
	 * before stream or end of stream.
	 */
	NotAvailable,
	/**
	 * null value
	 */
	Null,
	/**
	 * boolean false
	 */
	False,
	/**
	 * boolean true
	 */
	True,
	/**
	 * 8bit integer (byte)
	 */
	Int8,
	/**
	 * 16bit integer (short)
	 */
	Int16,
	/**
	 * 32bit integer (int)
	 */
	Int32,
	/**
	 * 64bit integer (long)
	 */
	Int64,
	/**
	 * integer number, would {@link BigInteger}
	 */
	Integer,
	/**
	 * 32bit float number (float)
	 */
	Float32,
	/**
	 * 64bit float number (double)
	 */
	Float64,
	/**
	 * decimal number, would {@link BigDecimal}
	 */
	Decimal,
	/**
	 * time, java 8 LocalTime
	 */
	Time,
	/**
	 * date, java 8 LocalDate
	 */
	Date,
	/**
	 * datetime, {@link java.util.Date} or java 8 LocalDateTime
	 */
	DateTime,
	/**
	 * datetime with timezone, java 8 ZonedDateTime
	 */
	DateTimeTZ,
	/**
	 * a character
	 */
	Character,
	/**
	 * String
	 */
	String,
	/**
	 * Octet, byte[]
	 */
	Octet,
	/**
	 * start of boolean[]
	 */
	PrimitiveArrayOfBoolean,
	/**
	 * start of short[]
	 */
	PrimitiveArrayOfShort,
	/**
	 * start of int[]
	 */
	PrimitiveArrayOfInt,
	/**
	 * start of long[]
	 */
	PrimitiveArrayOfLong,
	/**
	 * start of float[]
	 */
	PrimitiveArrayOfFloat,
	/**
	 * start of double[]
	 */
	PrimitiveArrayOfDouble,
	/**
	 * start of char[]
	 */
	PrimitiveArrayOfChar,
	/**
	 * start of array
	 */
	Array,
	/**
	 * start of Map<String, Object>
	 */
	Object,
	/**
	 * custom typed
	 */
	CustomType,
	/**
	 * end of array or end of object
	 */
	EndOfStructure
}
