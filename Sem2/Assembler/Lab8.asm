.data
RAM:		.space 		8192

msg1_in:	.asciiz		"Podaj liczbe wierszy: "
msg2_in:	.asciiz		"\nPodaj liczbe kolumn: "
msg3_in:	.asciiz		"\n(0 - exit; 1 - odczyt; 2 - zapis)\nWybierz rodzaj operacji: "

NUM_ROWS:	.word		0			# Liczba wierszy
NUM_COLS:	.word		0			# Liczba kolumn

newline:	.asciiz		"\n"

msg_read:	.asciiz		"\nWybrano opcje odczytu"
msg_write:	.asciiz		"\nWybrano opcje zapisu"
invalid_input:	.asciiz		"\nWystapil blad, sprobuj ponownie"

user_in1:	.asciiz		"\nPodaj indeks wiersza: "
user_in2:	.asciiz		"\nPodaj indeks kolumny: "
write_in:	.asciiz		"\nPodaj wartosc do zapisu: "

read_fin:	.asciiz		"\nWartosc w wybranym miejscu: "
write_fin:	.asciiz		"\nPoprawnie zapisano w wybranym miejscu wartosc "

.text
	# Wybór wymiarów tablicy

	# Wypisz pierwszy komunikat
	li 	$v0, 4 				# Print string
	la 	$a0, msg1_in
	syscall
	
	# Zapisz pierwszy argument w NUM_ROWS
	li 	$v0, 5 				# Int input
	syscall
	move 	$t0, $v0
	sw 	$t0, NUM_ROWS
	
	# Wypisz drugi komunikat
	li 	$v0, 4 				# Print string
	la 	$a0, msg2_in
	syscall
	
	# Zapisz drugi argument w NUM_COLS
	li 	$v0, 5 				# Int input
	syscall
	move 	$t1, $v0
	sw 	$t1, NUM_COLS
	
	
main:	
	# Wypełnianie tablicy
	
	# Przygotowani
	la	$s0	  RAM			# Wczytaj adres poczatku RAM
	lw	$t0	  NUM_ROWS		# Wczytaj liczbe wierszy do $t0
	lw	$t1	  NUM_COLS		# Wczytaj liczbe kolumn do $t1
	
	# Oblicz adres pierwszego wiersza 
	sll	$t2, $t0, 2			# offset ilosci wierszy
	add	$s1, $s0, $t2			# poczatek RAM + 4 * NUM_ROWS = adres pierwszego wiersza
	move	$s2, $s0			# $s2 = wskaznik aktualnego wiersza
	
	# Inne zmienne
	li	$t3, 0 				# i = 0 
	sll	$t4, $t1, 2			# offset ilosci kolumn
	li	$t5, 0				# zmienna do wypelniania wartosci tablicy (100 * i)
	move	$s2, $s0			# $s2 = wskaznik aktualnego wiersza
	
loop_rows:
	beq 	$t3, $t0, loop_user		# jesli i == NUM_ROWS - skocz do done
	#else
	sw	$s1, ($s2)			# Zapisz adres poczatku wiersza w jego wskazniku
	
	# Wypelnij ten wiersz liczbami
	
	move 	$a0, $s1			# $a0 = adres poczatku wiersza
	lw	$a1, NUM_COLS			# $a1 = liczba kolumn
	move	$a2, $t5			# $a2 = 100 * i
	jal	fill_row
	
	# Przesuniecia
	add	$s1, $s1, $t4			# $s1 = adres poczatku nastepnego wiersza
	addi	$s2, $s2, 4			# $s2 - nastepny wskaznik na poczatek wiersza
	addi	$t3, $t3, 1			# i++
	addi	$t5, $t5, 100			# 100 * i
	j 	loop_rows
	
loop_user:
	li 	$v0, 4 				# Print string
	la 	$a0, msg3_in
	syscall
	
	li 	$v0, 5 				# Integer input
	syscall
	move 	$t0, $v0			# $t0 = wybor uzytkownika
	
	beq 	$t0, $zero, exit		# wybor == 0: skocz do exit
	li 	$t1, 1
	beq 	$t0, $t1,  do_read		# wybor == 1: skocz do do_read
	li 	$t1, 2
	beq	$t0, $t1, do_write		# wybor == 2: skocz do do_write
	
invalid:
	# Jesli nie wybrano poprawnej opcji, wyswietl o tym komunikat
	li 	$v0, 4 				# Print string
	la 	$a0, invalid_input
	syscall
	j loop_user				# wroc do loop_user i pobierz nowy input
	
do_read:
	li 	$v0, 4				# Print string
	la	$a0, msg_read
	syscall	
	
	jal 	inputs				# tutaj $a0 i $a1 to inputy indeksow wiersza i kolumny
	jal 	read_elem
	
	j 	loop_user 			# Po zakonczonym pomyslnie odczycie wroc do petli uzytkownika
	
do_write:
	li 	$v0, 4				# Print string
	la	$a0, msg_write
	syscall	
	
	jal 	inputs
	move	$t0, $a0			# Zachowaj wynik $a0
	
	li 	$v0, 4				# Print string
	la	$a0, write_in
	syscall		
	move	$a0, $t0			# Przywroc do $a0 wartosc poprzedniego inputu
	
	li 	$v0, 5				# int input
	syscall
	move	$a2, $v0			# $a2 = wartosc do zapisania w [i][j]
	
	jal 	write_elem
	j 	loop_user 			# Po zakonczonym pomyslnie odczycie wroc do petli uzytkownika


# --------------------------------------------------------------------------------------
inputs:
	li 	$v0, 4				# Print string
	la	$a0, user_in1
	syscall		
	
	li 	$v0, 5				# int input
	syscall
	move	$t0, $v0			# $t0 = indeks wiersza "i"
	
	li 	$v0, 4				# Print string
	la	$a0, user_in2
	syscall		
	
	li 	$v0, 5				# int input
	syscall
	move	$t1, $v0			# $t1 = indeks kolumny "j"
	
	move 	$a0, $t0			# $a0 = i
	move	$a1, $t1			# $a1 = j
	
	# Sprawdz czy input jest prawidlowy
	lw	$t0, NUM_ROWS
	lw	$t1, NUM_COLS
	
	bgt 	$a0, $t0, invalid		# Jesli i > NUM_ROWS skocz do invalid
	bgt 	$a1, $t1, invalid		# Jesli j > NUM_COLS skocz do invalid
	
	jr 	$ra

# --------------------------------------------------------------------------------------
# $a0 = adres poczatku wiersza
# $a1 = liczba kolumn
# $a2 = 100 * i
fill_row:
	li	$t6, 0				# j = 0
	move	$t7, $a0			# t7 = poczatek wiersza
fill_loop:
	beq	$t6, $a1, fill_done		# jesli j == $a1, skocz do fill_done
	#else
	
	# wylicz wartosc do zapisania w komorce
	move	$t8, $a2			# $t8 = 100 * i
	add	$t8, $t8,$t6			# $t8 += j
	addi	$t8, $t8, 1			# $t8 += 1
	
	# Zapisz wartosc w odpowiednim miejscu
	sw	$t8, ($t7)			# kolumna[i][j] = 100*i + (j+1)
	
	addi	$t6, $t6, 1			# j++
	addi	$t7, $t7, 4			# $t7 += 4 (wskazuj na nastepna komorke wiersza)
	
	j fill_loop
fill_done:
	jr 	$ra				# wroc do loop_rows
# -------------------------------------------------------------------------------------
	
exit:
	li 	$v0, 10
	syscall

# -------------------------------------------------------------------------------------
# $a0 - indeks wiersza [i]
# $a1 - indeks kolumny [j]	
read_elem:	
	move    $t0, $a0
	move	$t1, $a1
	sll 	$t0, $t0, 2			# offset == 4 * i
	sll	$t1, $t1, 2			# offset == 4 * j
	
	add	$t0, $s0, $t0			# $t0 += $s0 (poczatek RAM + 4*i) - wskazuje na wartosc z adresem wiersza i-tego
	lw	$t2, ($t0)			# $t2 = wartosc $t0 (adres wiersza[i])
	add	$t2, $t2, $t1			# $t2 += $t1 - wskazuje na wartosc szukana [i][j]
	lw	$t2, ($t2)			# wartosc pod adresem $t2
	
	li 	$v0, 4				# Print string
	la	$a0, read_fin
	syscall		
	
	li 	$v0, 1				# print int
	move	$a0, $t2
	syscall
	
	jr 	$ra				# Wroc do do_read

# -------------------------------------------------------------------------------------
# $a0 - indeks wiersza [i]
# $a1 - indeks kolumny [j]
# $a2 - wartosc do wpisania w [i][j]
write_elem:
	move    $t0, $a0
	move	$t1, $a1
	move	$t2, $a2
	la	$s0, RAM
	
	sll 	$t0, $t0, 2			# offset == 4 * i
	sll	$t1, $t1, 2			# offset == 4 * j
	add	$t0, $s0, $t0			# $t0 += $s0 (poczatek RAM + 4*i) - wskazuje na wartosc z adresem wiersza i-tego
	lw	$t3, ($t0)			# $t3 = wartosc $t0 (adres wiersza[i])
	add	$t3, $t3, $t1			# $t3 += $t1 - wskazuje na wartosc szukana [i][j]
	sw	$t2, ($t3)			# Zapisz pod adresem $t3 wartosc w $t2
	
	li 	$v0, 4				# Print string
	la	$a0, write_fin
	syscall		
	
	li 	$v0, 1				# print int
	move	$a0, $t2
	syscall
	
	jr 	$ra				# Wroc do do_write
	
	
	
	
