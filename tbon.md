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

### signed variable bits (7bit enc LSB first)
bin | hex | type | value
--- | --- | -----| -------
0001 0000 | 0x10 | positive integer |
0001 0001 | 0x11 | int16 p | 
0001 0010 | 0x12 | int32 p |
0001 0011 | 0x13 | int64 p |
0001 0100 | 0x14 | negative integer |
0001 0101 | 0x15 | int16 n |
0001 0110 | 0x16 | int32 n |
0001 0111 | 0x17 | int64 n |

### unsigned variable, null, and Boolean
bin | hex | type | value
--- | --- | -----| -------
0001 1000 | 0x18 | integer | 0
0001 1001 | 0x19 | uint16 |
0001 1010 | 0x1a | uint32 |
0001 1011 | 0x1b | uint64 |
0001 1100 | 0x1c | boolean | false
0001 1101 | 0x1d | boolean | true
0001 1110 | 0x1e | null | NULL
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

## Date/Time, char, decimal
bin | hex | presentation
--- | --- | ----
0010 1000 | 0x28 | time
0010 1001 | 0x29 | date
0010 1010 | 0x2a | datetime
0010 1011 | 0x2b | char
0010 1100 | 0x2c | decimal, positive scale
0010 1101 | 0x2d | decimal, negative scale
0010 1110 | 0x2e |
0010 1111 | 0x2f | BigInteger follows 7be length and 8bype octets

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

