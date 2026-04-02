.data
coefs:		.float		2.3, 3.45, 7.67, 5.32		# Wspolczynniki przy x
degree:		.word		3				# Stopien wielomianu

msg_input:	.asciiz		"Podaj wartosc x: "
msg_output:	.asciiz		"\nWartosc wielmianu dla tego x wynosi: "
.text
main:	# Petla pobierania X i wypisywania wyniku
read_loop:
	# Print string
	li	$v0, 4
	la	$a0, msg_input
	syscall
	
	# Double input
	li	$v0, 7
	syscall
	mov.d	$f12, $f0
	
	# Przygotuj reszte argumentow
	la	$a0, coefs
	lw	$a1, degree
	
	jal eval_poly
	
	# Print string
	li	$v0, 4
	la	$a0, msg_output
	syscall
	
	# Wypisz wynik
	mov.d	$f12, $f0
	li 	$v0, 3			# Print double
	syscall
	
	# Print '\n'
	li	$a0, 10			# '\n
	li	$v0, 11			# print char
	syscall
	
	j	read_loop
		
# eval_poly(*addr, degree, x)
eval_poly:
	# $a0  - addr
	# $a1  - degree
	# $f12 - x
	
	# Wczytaj coefs[0]
	l.s	$f4, 0($a0)		# Wczytaj coefs[0] do $f4
	# $f2 przechowuje wyniki posrednie
	cvt.d.s	$f2, $f4		# Rzutuj coefs[0] na double
	
	addi	$a0, $a0, 4		# Przesun $a0 by wskazywal na coefs[1]
	li	$t0, 1			# $t0 = i = 1
	
horner_loop:
	bgt	$t0, $a1, horner_end	# Jesli i > degree, skocz do horner_end
	#else
	l.s	$f4, 0($a0)		# Załaduj float
	cvt.d.s	$f4, $f4		# Rzutuj $f4 na double
	
	mul.d	$f2, $f2, $f12		# wynik *= x
	add.d	$f2, $f2, $f4		# wynik += coefs[i]
	
	addi	$t0, $t0, 1		# i++
	addi	$a0, $a0, 4		# offset
	j horner_loop
	
horner_end:
	mov.d	$f0, $f2		# Zapisz wynik w $f0
	jr $ra				# return
