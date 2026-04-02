.data
n1:	.word 7
n2:	.word 8
n3:     .word 5
n4:     .word 0

.text
        # operacje przesy³ania danych
	lw   $t0, n1       # za³adowanie s³owa 32-bit z pamiêci do rejestru
	sll  $t0, $t0, 2   # przesuniêcie logiczne w lewo
	move $t1, $t0      # skopiowanie rejestru $t0 do $t1
	sw   $t1, n4       # zapis rejestru do pamiêci

        li   $t3, 0x30     # za³adowanie sta³ej do rejestru
        srl  $t3, $t3, 4   # przesuniêcie w prawo o 4 pozycje
        
        # adresowanie poœrednie
        la   $t2, n2       # za³adowanie adresu zmiennej n2 do rejestru $t2
        lw   $t5, ($t2)    # za³adowanie do $t5 spod adresu w rejestrze t2
        add  $t3, $t3, $t5
        addi $t2, $t2, 4   # dodanie 4 do adresu w t2 - terez mamy adres zmiennej n3
        lw   $t6, ($t2)    # za³adowanie do $t6 zmiennej n3 
        
        sub  $t3, $t3, $t6 # odejmowanie wartoœci w rejestrach
        subi $t3, $t3, 2   # odjêcie sta³ej 2 od $t3
        
        # operacje logiczne
        li  $t1, 1         # t1 maska
        li  $t2, 0x76      # wartoœæ do na³o¿enie maski
        and $t3, $t2, $t1  # t3 - wartoœæ po maskowaniu
        sll $t1, $t1, 1    # przesuniêcie maski o jedn¹ pozycjê
        and $t3, $t2, $t1  # t3 - wartoœæ po maskowaniu przesuniêt¹ mask¹
        
        andi $t3, $t2, 0x0f # t3 - wartoœæ po maskowaniu pozostaj¹ tylko cztery najmniej znacz¹ce bity

        
        
        # skoki
        beq  $t3, $t2, t3eqt2  # skocz jesli wartosci w rejestrach rowne
        li   $t1, 10
        j    cont1
t3eqt2: li   $t1, 20
cont1:

        bne  $t3, $t2, t3net2  # skocz jesli wartosci w rejestrach nierowne
        li   $t1, 10
        j    cont2
t3net2: li   $t1, 20
cont2: 

        bgez $t1, t1gez
        li   $t1, 30
t1gez:                   

        bnez $t1, t1nez   # skocz jeœli $t1 nie rowne 0
        li   $t1, 30
t1nez:

        beqz $t1, t1eqz   # skocz jeœli $t1 rowne 0
        li   $t1, 30
t1eqz:

        
