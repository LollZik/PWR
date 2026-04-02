.data
msg1_in: 	.asciiz "Podaj pierwsza liczbe: "
msg2_in: 	.asciiz "Podaj druga liczbe: "
msg_status:	.asciiz "\nStatus nadmiaru (1 = nadmiar ; 0 = brak): "
msg_out:	.asciiz "Wynik mnozenia: "

licz1:		.word 0
licz2:		.word 0

wyn:		.word 0
status:		.word 0
	
.text
	# Wypisz pierwszy komunikat
	li $v0, 4 # Print string
	la $a0, msg1_in
	syscall
	
	# Zapisz pierwszy argument w licz1
	li $v0, 5 # Int input
	syscall
	move $t0, $v0
	sw $t0, licz1
	
	# Wypisz drugi komunikat
	li $v0, 4 # Print string
	la $a0, msg2_in
	syscall
	
	# Zapisz drugi argument w licz2
	li $v0, 5 # Int input
	syscall
	move $t1, $v0
	sw $t1, licz2
	
	# Przygotowanie do mnozenia
    	lw $s0, licz1            # $s0 = licz1 (mnozna)
    	lw $s1, licz2             # $s1 = licz2 (mnoznik)
    	lw $s2, wyn                 # $s2 = wynik
    	lw $t4, status                 # $t4 = status (overflow)
    	
    	
    	# Rozpoczynamy proces mnozenia
start:
	beqz $s1, end  # Sprawdz czy s1 == 0, jesli tak: skocz do end
	#jesli nie:
	andi $t2, $s1, 1 # Przypisz do t2 wynik operacji t1 ANDI 1 (równy wartosci najmlodszego bitu t1)
	beqz $t2, skip # Jesli najmlodszy bit == 0 to przeskocz do skip, bo nie wplynie on na wynik
	
	addu $t3, $s2, $s0 # t3 = dotychczasowy wynik plus mnozna
	sltu $t5, $t3, $s2 # Sprawdź, czy wystapil nadmiar
	or   $t4, $t4, $t5   # Ustaw status na 1 jesli wystapil nadmiar (nie zmieni statusu z 1 na 0 w zadnym przypadku)
	move $s2, $t3 # Zaaktualizuj wartosc wyniku
	
skip:
	sll $s0, $s0, 1 # Przesun s0 o 1 bit w lewo (pomnoz mnozna przez 2)
	srl $s1, $s1, 1 # Przesun s1 o 1 bit w prawo (podziel mnoznik przez 2)
	j   start # wroc na poczatek petli
		
	
end: # Zakoncz działanie programu

	# Zapisz wyniki do pamieci statycznej
	sw $s2, wyn
	sw $t4, status

	#Wypisz wynik mnożenia
	li $v0, 4
	la $a0, msg_out
	syscall
	
	li $v0, 1
	move $a0, $s2
	syscall 
	
	# Wypisz komunikat o nadmiarze 
	li $v0, 4 # Print string
	la $a0, msg_status
	syscall
	
	# Wypisz stan nadmiaru
	li $v0, 1 # Print int
	move $a0, $t4
	syscall
	
	li $v0, 10 # exit
	syscall
