TBON - Typed Binary Object Notation

0000 0000 null
0000 0001
0000 0010 false
0000 0011 true

0000 0100 int8_t 0
0000 0101 int8_t 1B
0000 0110 uint8_t 0
0000 0111 uint8_t 1B

0000 1000 int16_t 0
0000 1001 int16_t 1B
0000 1010 int16_t 2B
0000 1011
0000 1100 uint16_t 0
0000 1101 uint16_t 1B
0000 1110 uint16_t 2B
0000 1111

0001 0000 int32_t 0
0001 0001 int32_t 4B
0001 0010 int32_t p 7be LSB first (up to 24bits)
0001 0011 int32_t n 7be LSB first (up to 24bits)
0001 0100 uint32_t 0
0001 0101 uint32_t 4B
0001 0110 uint32_t 7be LSB first (up to 24bits)
0001 0111

0001 1000 int64_t 0
0001 1001 int64_t 8B
0001 1010 int64_t p 7be
0001 1011 int64_t n 7be
0001 1100 uint64_t 0
0001 1101 uint64_t 8B
0001 1110 uint64_t 7be
0001 1111 End of Stream Marker

0010 0000 bin16
0010 0001 bin32
0010 0010 bin64
0010 0011 bin128
0010 0100 bin256
0010 0101 dec32
0010 0110 dec64
0010 0111 dec128

0010 1000 char
0010 1001 time
0010 1010 date
0010 1011 datetime
0010 1100 VInt 0
0010 1101 VInt positive
0010 1110 VInt negative
0010 1111 VDecimal

0011 xxxx Custom Type

0100 xxxx Array
0101 xxxx Typed Array
0110 xxxx Object
0111 xxxx Typed Object

10xx xxxx octet
11xx xxxx string
