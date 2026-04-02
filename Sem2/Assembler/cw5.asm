.data
        t: .word  3, 7, 8 , 9, 10, 25, 2
        n: .word  7
	nl: .asciiz "\n"
.text
main:
        lw $t0, n
        subi $t0, $t0, 1
        add $t0, $t0, $t0
        add $t0, $t0, $t0  # t0 - wilkosc tablicy-4
        
        la  $t1, t         # $t1 - poczatkowy        
        add $t2, $t1, $t0  # $t2- adres koñcowy 
        
loop:   bgt $t1, $t2, done # czy ju¿ za ostatnim elementem
        lw  $a0, ($t1)     # pobranie elementu tablicy
        
	li  $v0, 1
	syscall            # wyswietlenie liczby
	
	li $v0,  4
	la $a0,  nl
	syscall            # nowa linia	
	                        
        addi $t1, $t1, 4  # nastêpny element
        j    loop

done:   li $v0, 10        # zakoñcz
	la $a0, 0
	syscall             

