# TBON - Typed Binary Object Notation

## null, Boolean
bin | hex | type | value
--- | --- | -----| -------
0000 0000 | 0x00 | null | null
0000 0001 | 0x01 | |
0000 0010 | 0x02 | boolean | false
0000 0011 | 0x03 | boolena | true

## Integer
### int8
bin | hex | type | value
--- | --- | -----| -------
0000 0100 | 0x04 | int8 | 0
0000 0101 | 0x05 | int8 | 1B
0000 0110 | 0x06 | uint8 | 0
0000 0111 | 0x07 | uint8 | 1B

### int16, char
bin | hex | type | value
--- | --- | -----| -------
0000 1000 | 0x08 | int16 | 0
0000 1001 | 0x09 | int16 | 1B
0000 1010 | 0x0a | int16 | 2B LSB first
0000 1011 | 0x0b | |
0000 1100 | 0x0c | uint16 | 0
0000 1101 | 0x0d | uint16 | 1B
0000 1110 | 0x0e | uint16 | 2B LSB first
0000 1111 | 0x0f | char | UTF-8 one character

### int32
bin | hex | type | value
--- | --- | -----| -------
0001 0000 | 0x10 | int32 | 0
0001 0001 | 0x11 | int32 | 4B LSB first
0001 0010 | 0x12 | int32 p | 7be LSB first (up to 24bits)
0001 0011 | 0x13 | int32 n | 7be LSB first (up to 24bits)
0001 0100 | 0x14 | uint32 | 0
0001 0101 | 0x15 | uint32 | 4B LSB first
0001 0110 | 0x16 | uint32 | 7be LSB first (up to 24bits)
0001 0111 | | |

### int64
0001 1000 int64_t 0
0001 1001 int64_t 8B
0001 1010 int64_t p 7be
0001 1011 int64_t n 7be
0001 1100 uint64_t 0
0001 1101 uint64_t 8B
0001 1110 uint64_t 7be
0001 1111 End of Stream Marker

## IEEE 754 float
0010 0000 bin16
0010 0001 bin32
0010 0010 bin64
0010 0011 bin128
0010 0100 bin256
0010 0101 dec32
0010 0110 dec64
0010 0111 dec128

## Date/Time, Variable length numbers
0010 1000 time
0010 1001 date
0010 1010 datetime
0010 1011 VDecimal
0010 1100 VInt 0
0010 1101 VInt positive 7be
0010 1110 VInt negative 7be
0010 1111 BigInteger follows 7be length and 8bype octets

## Variable lengts
0011 xxxx Custom Type
0100 xxxx Array
0101 xxxx Typed Array
0110 xxxx Object
0111 xxxx Typed Object
10xx xxxx octet
11xx xxxx string
