.data
msg_in:    .asciiz  "Podaj liczbe:"
msg_out:   .asciiz "Wynik: "

.text
     # Wypisz komunikat
     li $v0, 4
     la $a0, msg_in
     syscall
     
     # wprowadz liczbe int
     li $v0, 5
     syscall
     move $a1, $v0
     
     # dodaj 10 do wprowdzonej liczby - wynik w $t0
     addi $t0, $a1, 10
           
     # wypisz komunikat      
     li $v0, 4
     la $a0, msg_out
     syscall

     # wyprowadz wynik sumowania
     li $v0, 1
     move $a0, $t0
     syscall
             
     # zakoñcz poprawnie program                                               
     li $v0, 10
     syscall
	
