.data
N:		.word		1000

numall:		.word		1:1001		# Tablica N+1 liczb, gdzie każdy element ma wartość początkową == 1
nprimes:	.word		0		# Licznik liczb pierwszych
primes:		.space		4004		# Wielkość:   4 * (N+1) bajtów

msg1:		.asciiz		"Wybrane N: "
msg2:		.asciiz		"\nIlosc liczb pierwszych do N: "
msg3:		.asciiz		"\nLiczby pierwsze do N:\n"
space:		.asciiz		" "

.text
	lw   $s0,   N			# Wczytaj N do $s0
	la   $s1,   numall		# Wczytaj do $s1 adres numall (numall[0])
	sw   $zero, 0($s1)		# Ustaw numall[0] = 0
	sw   $zero, 4($s1)		# Ustaw numall[1] = 0 (4 bajty dalej niz [0])
	
	# Przygotowania do pętli
	# Iteracja najpierw nastąpi osobno dla i == 2, a następnie we wspólnej pętli dla nieparzystych i < sqrt(N) 
	li   $t5, 4			# j = i * i = 4
	
two_loop:
	bgt  $t5, $s0, two_done	# if j > N, skocz do two_done i rozpocznij nieparzystą pętle
	# else
	mul  $t2, $t5, 4		# t2 = 4 * j
	add  $t3, $s1, $t2		# Znajdz numall[j]
	sw   $zero, 0($t3)		# numall[j] = 0
	addi $t5, $t5, 2		# j += 2
	j    two_loop			# wróć na początek pętli
	
two_done:
	li   $t0, 3			# ustaw i = 3
	
loop_outer:
	mul  $t1, $t0, $t0		# i * i
	bgt  $t1, $s0, loop_done	# if i * i > N, skocz do loop_done
	# else:
	mul  $t2, $t0, 4		# offset t2 = 4 * i
	add  $t3, $s1, $t2		# adres numall[i]
	lw   $t4, 0($t3)		# wartość numall[i]
	beqz $t4, increment		# if numall[i] == 0, skocz do increment
	# else:
	move $t5, $t1			# "j" = i * i

loop_inner:
	bgt  $t5, $s0, increment	# Jeśli j > N, skocz do increment
	mul  $t2, $t5, 4		# offset t2 = 4 * j
	add  $t3, $s1, $t2		# Znajdz numall[j]
	sw   $zero, 0($t3)		# numall[j] = 0
	add  $t5, $t5, $t0		# j += i
	j loop_inner			# wróć na początek pętli
	
increment:
	addi $t0, $t0, 2		# i += 2
	j loop_outer			# wróć na początek pętli

loop_done:
	lw   $s2, nprimes		# Wczytaj nprimes do $s2
	la   $s3, primes		# Wczytaj adres primes do $s3
	li   $t0, 2
	sw   $t0, 0($s3)		# primes[0] = 2
	addi $s2, $s2, 1		# nprimes += 1
	li   $t0, 3			# i = 3
	
loop_copy:
	bgt  $t0, $s0, end		# if i > N, skocz do end
	# else:
	mul  $t2, $t0, 4		# offset t2 = 4 * i
	add  $t3, $s1, $t2		# adres numall[i]
	lw   $t4, 0($t3)		# wartość numall[i]
	beqz $t4, skip_copy		# if numall[i] == 0, skocz do skip_copy
	# else:
	mul  $t1, $s2, 4		# $t1 = 4* nprimes, ktore posluży jako indeks tablicy primes
	add  $t2, $s3, $t1		# t2 - adres primes[$t1]
	sw   $t0, 0($t2)		# primes[$t1] = i 
	addi $s2, $s2, 1		# nprimes += 1

skip_copy:
	
	addi $t0, $t0, 2		# i +=2
	j loop_copy

end:
	sw   $s2, nprimes		# Zapisz wartość $s2 w nprimes
	# Wypisz komunikaty w konsoli
	
	# Wypisz N
	li   $v0, 4 			# Print string
	la   $a0, msg1
	syscall
	
	li   $v0, 1 			# Print int
	move $a0, $s0
	syscall
	
	# Wypisz ilość liczb pierwszych
	li   $v0, 4 			# Print string
	la   $a0, msg2
	syscall
	
	li   $v0, 1 			# Print int
	move $a0, $s2
	syscall
	
	# Wypisz liczby pierwsze
	li   $t0, 0 			# i = 0
	lw   $t1, nprimes		# $t1 = nprimes
	la   $s0, primes		# $s0 = adres primes[0]
	
	li   $v0, 4
	la   $a0, msg3
	syscall
	
print_loop:
	bge  $t0, $t1, exit 		# if i > nprimes, skocz do exit
	# else
	mul  $t2, $t0, 4		# offset =  4 * i
	add  $t2, $s0, $t2		# t2 = adres primes[i]
	
	# print primes[i]
	lw   $a0, 0($t2)		# $a0 = wartość w $t4
	li   $v0, 1			# Print int
	syscall
	
	# print " "
	li   $v0, 4
	la   $a0, space
	syscall
	
	addi $t0, $t0, 1 		# i += 1
	j print_loop
	
exit:
	li  $v0, 10 			# exit
	syscall
