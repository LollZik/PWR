.data
n1:	.word 7
n2:	.word 8
n3:	.word 254
n4:	.word 4096
n5:	.word 5127
n6:     .word 0x01020304
b1:     .byte 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c
any:    .space 40

.text
   lw $t0, n6
   lw $t1, b1
   
   li $v0, 34
   move $a0, $t0  
   syscall
