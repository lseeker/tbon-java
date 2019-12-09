# TBON - Typed Binary Object Notation

## Integer
### 0 (Zero)
bin | hex | type | value
--- | --- | -----| -------
0000 0000 | 0x00 | int8 | 0
0000 0001 | 0x01 | int16 | 0
0000 0010 | 0x02 | int32 | 0
0000 0011 | 0x03 | int64 | 0
0000 0100 | 0x04 | uint8 | 0
0000 0101 | 0x05 | uint16 | 0
0000 0110 | 0x06 | uint32 | 0
0000 0111 | 0x07 | uint64 | 0

### full bytes, MSB first
bin | hex | type | value
--- | --- | -----| -------
0000 1000 | 0x08 | int8 | 1B
0000 1001 | 0x09 | int16 | 2B
0000 1010 | 0x0a | int32 | 4B
0000 1011 | 0x0b | int64 | 8B
0000 1100 | 0x0c | uint8 | 1B
0000 1101 | 0x0d | uint16 | 2B
0000 1110 | 0x0e | uint32 | 4B
0000 1111 | 0x0f | uint64 | 8B

### int16 1B / variable bits integer(7bit enc LSB first)
bin | hex | type | value
--- | --- | -----| -------
0001 0000 | 0x10 | int32 p |
0001 0001 | 0x11 | int32 n | 
0001 0010 | 0x12 | uint32 |
0001 0011 | 0x13 | int16 | 1B
0001 0100 | 0x14 | int64 p |
0001 0101 | 0x15 | int64 n |
0001 0110 | 0x16 | uint64 |
0001 0111 | 0x17 | uint16 | 1B

### null, boolean, float and others
bin | hex | type | value
--- | --- | -----| -------
0001 1000 | 0x18 | boolean | false
0001 1001 | 0x19 | boolean | true
0001 1010 | 0x1a | float32 |
0001 1011 | 0x1b | float64 |
0001 1100 | 0x1c | null | NULL
0001 1101 | 0x1d | decimal | 0
0001 1110 | 0x1e | decimal | Scale 6sbv + BI
0001 1111 | 0x1f | EOS | End of Stream Marker

## Time, Date, and Character(uint32)
bin | hex | presentation
--- | --- | ----
0010 0000 | 0x20 | time
0010 0001 | 0x21 | date
0010 0010 | 0x22 | datetime
0010 0011 | 0x23 | datetime with timezone
0010 0100 | 0x24 | char 1B
0010 0101 | 0x25 | char 2B
0010 0110 | 0x26 | char 3B
0010 0111 | 0x27 | char 4B

## Java primitive arrays (must follows 7bit enc len)
bin | hex | type | element presentation
--- | --- | ---- | ---
0010 1000 | 0x28 | boolean[] | true or false
0010 1001 | 0x29 | short[] | 2BE
0010 1010 | 0x2a | int[] | signed 7bit encoding
0010 1011 | 0x2b | long[] | signed 7bit encoding
0010 1100 | 0x2c | float | 4BE
0010 1101 | 0x2d | double | 8BE
0010 1110 | 0x2e | char[] | 2BE (unsigned)
0010 1111 | 0x2f | - | not used

## Variable lengths
xx present length, all 1 bits is stream mode

bin | hex | presentation
--- | --- | ----
0011 xxxx | 0x3* | BigInteger
0100 xxxx | 0x4* | Array  (object)
0101 xxxx | 0x5* | Object (string, object)
0110 xxxx | 0x6* | Custom Type
0111 xxxx | 0x7* | RESERVED
10xx xxxx | 0x8+ | octet
11xx xxxx | 0xc+ | string 

# Specs

## 7bit encoded positive int (LSB first)
- 1xxxxxx continue
- 0xxxxxx last

## 7bit signed encoded int (LSB first)
second bit is sign


## Variable length stream
follows 7be length, should not 0. 0 is undefined length (see below).

### Undefined length structure
0 presents undefined length. follows values on struct type.

### octet and string stream
0 + 7be length + data + 7be length + data + ... + 0

last 0 is end of stream marker
