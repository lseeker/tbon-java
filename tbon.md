# TBON - Typed Binary Object Notation

## null, Boolean
bin | hex | type | value
--- | --- | -----| -------
0000 0000 | 0x00 | null | null
0000 0001 | 0x01 | |
0000 0010 | 0x02 | boolean | false
0000 0011 | 0x03 | boolean | true

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
0001 0111 | 0x17 | |

### int64
bin | hex | type | value
--- | --- | -----| -------
0001 1000 | 0x18 | int64 | 0
0001 1001 | 0x19 | int64 | 8B
0001 1010 | 0x1a | int64 p | 7be
0001 1011 | 0x1b | int64 n | 7be
0001 1100 | 0x1c | uint64 | 0
0001 1101 | 0x1d | uint64 | 8B
0001 1110 | 0x1e | uint64 | 7be
0001 1111 | 0x1f | | End of Stream Marker

## IEEE 754 float
bin | hex | presentation
--- | --- | ----
0010 0000 | 0x20 | bin16
0010 0001 | 0x21 | bin32
0010 0010 | 0x22 | bin64
0010 0011 | 0x23 | bin128
0010 0100 | 0x24 | bin256
0010 0101 | 0x25 | dec32
0010 0110 | 0x26 | dec64
0010 0111 | 0x27 | dec128

## Date/Time, Variable length numbers
bin | hex | presentation
--- | --- | ----
0010 1000 | 0x28 | time
0010 1001 | 0x29 | date
0010 1010 | 0x2a | datetime
0010 1011 | 0x2b | VDecimal
0010 1100 | 0x2c | VInt 0
0010 1101 | 0x2d | VInt positive 7be
0010 1110 | 0x2e | VInt negative 7be
0010 1111 | 0x2f |BigInteger follows 7be length and 8bype octets

## Variable lengths
bin | hex | presentation
--- | --- | ----
0011 xxxx | 0x3* | Custom Type
0100 xxxx | 0x4* | Array
0101 xxxx | 0x5* | Typed Array
0110 xxxx | 0x6* | Object
0111 xxxx | 0x7* | Typed Object
10xx xxxx | 0x8+ | octet
11xx xxxx | 0xc+ | string

# Specs

## 7bit encoded int (LSB first)

## Variable length stream
### Infinite variable length
length all 1 bits + 0000 0000 => end with EOS marker

