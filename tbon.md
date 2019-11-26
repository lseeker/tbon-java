TBON - Typed Binary Object Notation

0000 0000 uint8_t 0
0000 0001 uint8_t 1B
0000 0010 int8_t 0
0000 0011 int8_t 1B

0000 0100 uint16_t 0
0000 0101 uint16_t 1B
0000 0110 uint16_t 2B
0000 0111 NOT_USED

0000 1000 int16_t 0
0000 1001 int16_t p 1B
0000 1010 int16_t 2B
0000 1011 int16_t n 1B

0000 1100 uint32_t 0
0000 1101 uint32_t 1B
0000 1110 uint32_t 2B
0000 1111 uint32_t 3B

0001 0000 int32_t 0
0001 0001 int32_t p 1B
0001 0010 int32_t p 2B
0001 0011 int32_t p 3B
0001 0100 int32_t 4B
0001 0101 int32_t n 1B
0001 0110 int32_t n 2B
0001 0111 int32_t n 3B

0001 1000 uint64_t 0
0001 1001 uint64_t 1B
0001 1010 uint64_t 2B
0001 1011 uint64_t 3B
0001 1100 uint64_t 4B
0001 1101 uint64_t 5B
0001 1110 uint64_t 6B
0001 1111 uint64_t 7B

0010 0000 int64_t 0
0010 0001 int64_t p 1B
0010 0010 int64_t p 2B
0010 0011 int64_t p 3B
0010 0100 int64_t p 4B
0010 0101 int64_t p 5B
0010 0110 int64_t p 6B
0010 0111 int64_t p 7B
0010 1000 int64_t n 8B
0010 1001 int64_t n 1B
0010 1010 int64_t n 2B
0010 1011 int64_t n 3B
0010 1100 int64_t n 4B
0010 1101 int64_t n 5B
0010 1110 int64_t n 6B
0010 1111 int64_t n 7B

0011 0000 uint32_t 4B
0011 0001 uint64_t 8B
0011 0010 NOT_USED
0011 0011 NOT_USED
0011 0100 VInt 0
0011 0101 VInt p
0011 0110 VInt n
0011 0111 NOT_USED

* IEEE 754
0100 0000 bin16
0100 0001 bin32
0100 0010 bin64
0100 0011 bin128
0100 0100 bin256
0100 0101 decimal32
0100 0110 decimal64
0100 0111 decimal128

0101 xxxx string
0110 xxxx octet
0111 xxxx decimal string
1000 xxxx array
1001 xxxx typed array
1010 xxxx object
1011 xxxx typed object
1110 xxxx custom type

1111 0000 null
1111 0001 char
1111 0010 false
1111 0011 true
1111 0100 time
1111 0101 date
1111 0110 datetime
1111 0111 NOT_USED

1111 1xxx NOT_USED
