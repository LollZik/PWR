#=============================================
.eqv STACK_SIZE 2048
#=============================================
.data
# obszar na zapamietanie adresu stosu systemowego
sys_stack_addr: .word 0

# deklaracja wlasnego obszaru stosu
stack: 		 .space STACK_SIZE

global_array:	 .word	1,2,3,4,5,6,7,8,9,10
# ============================================
.text
# czynności inicjalizacyjne
	sw 	$sp, sys_stack_addr 		# zachowanie adresu stosu systemowego
	la 	$sp, stack+STACK_SIZE 		# zainicjowanie obszaru stosu

main:
        subi 	$sp, $sp, 4 # Rezerwujemy miejsce na zmienna lokalna s (4 bajty)
        
        #sum(x, y) od lewej do prawej -> najpierw dodajemy x(global_array) a potem y (array_size)

        # Dodaj global_array do stosu
        subi 	$sp, $sp, 4         		# rezerwujemy 4 bajty
        la   	$t0, global_array
        sw   	$t0, 0($sp)         		# [ $sp ] = adres global_array[0]
        
        # Dodaj do stosu liczbę 10 (rozmiar tablicy)
        subi 	$sp, $sp, 4         		# rezerwujemy kolejne 4 bajty
        li   	$t1, 10
        sw   	$t1, 0($sp)         		# [ $sp ] = 10
        
        jal 	sum
        
        # Po powrocie od sum na szczycie stosu jest wartosc return
        lw	$t2, 0($sp)			# $t2 = suma
        
        # Zapisanie sumy do wartości s
        
        addi	$sp, $sp, 12			# "zdejmujemy" ze stosu wartosc return (4 bajty) oraz argumenty sum (8 bajtow)
        					# Teraz $sp wskazuje na s
        sw	$t2, ($sp)			# $t2 = s
        
        # Print s
        lw	$a0, ($sp) 			# $a0 = s
        li	$v0, 1				# Print int
        syscall
        
        lw   $sp, sys_stack_addr	     	# Odtworzenie wskaznika stosu
        #exit
	li   $v0, 10
	syscall
  
# ============================================
# Podprogram int sum( int *array, int array_size)           
sum:
	subi 	$sp, $sp, 8			# Miejsce na return, $ra (2*4 bajty)
	sw	$ra, ($sp)			# Zapisz $ra na stosie
	subi 	$sp, $sp, 8			# Miejsce na 2 zmienne lokalne i oraz s
	# s = 0
	li	$t0, 0				
	sw	$t0, ($sp)			# s = 0
		
	# i = array_size - 1
	lw	$t1, 16($sp)			# $t1 = array_size
	addi	$t1, $t1, -1			# $t1 -= 1
	sw	$t1, 4($sp)			# Zapisz do stosu na miejscu i
	
	# Pętla
sum_loop:
	lw	$t2, 4($sp)			# $t2 = i
	blt	$t2, $zero, sum_end		# Jesli i < 0, skocz do sum_end
	# else:
	# adres array[i]
	lw	$t3, 20($sp)			# $t3 = &array[0]
	sll	$t4, $t2, 2			# offset i (jako sll poniewaz nie mamy stosowac **zadnych** optymalizacji)
	add	$t5, $t3, $t4			# adres array[i]
	lw	$t6, ($t5)			# wartosc array[i]
	
	# suma s + array[i]
	lw	$t7, ($sp)			# $t7 = s
	add	$t7, $t7, $t6			# $t7 = s + array[i]
	sw	$t7, ($sp)			# s = $t7
	
	# decrement
	addi	$t2, $t2, -1			# i -= 1
	sw	$t2, 4($sp)			# zapisz nowe i na stosie
	j sum_loop
	
sum_end:
	# wartosc s jest w 0($sp) jeszcze jako zmienna lokalna
	lw	$t8, ($sp)			# t8 = s
	# Zapisz wartosc tej zmiennej w return
	sw	$t8, 12($sp)			# return = s
	
	addi	$sp, $sp, 8			# Zdejmujemy zmienne lokalne (8 bitow) ze stosu
	lw	$ra, ($sp)			# Odczytujemy $ra
	addi	$sp, $sp, 4			# Zdejmujemy $ra ze stosu
	jr	$ra				# powrot do main
