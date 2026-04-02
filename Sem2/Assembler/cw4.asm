.data
        t: .word  3, 7, 8 , 9, 10, 25, 2
        n: .word  7
	a: .word 5
	b: .word 6
	c: .word 4
	d: .word 3
	m: .word 11
	nl: .asciiz "\n"
.text
main:
        lw $t0, n
        subi $t0, $t0, 1
        add $t0, $t0, $t0
        add $t0, $t0, $t0
        la  $t1, t
        add $t1, $t1, $t0
        lw  $t2, ($t1)
	lw  $t0, a
	lw  $t1, b
	lw  $t2, c
	lw  $t3, d
	add $t4, $t0, $t1 # a+b=11
	sub $t5, $t2, $t3 # c-d=1
	sub $t6, $t4, $t5 # (a+b)-(c-d)=10  - wynik w t6
	lw  $t0, m
	mul $t1, $t0, $t6 # (a+b)-(c-d)*m=110  - wynik w t1

	li  $v0, 1
	add $a0, $zero, $t6  # (a+b)-(c-d)=10
	syscall
	
	li  $v0, 4
	la  $a0, nl
	syscall
		
	li  $v0, 1
	add $a0, $zero, $t1  # (a+b)-(c-d)*m=110
	syscall
