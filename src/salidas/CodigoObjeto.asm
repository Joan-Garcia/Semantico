.Const

.Data?

.Data
	message DB "Incio de ejecucion...", 0
	message2 DB "Fin de ejecucion...", 0
	salto DB 10, 13, 0	
	input DB 10 Dup(0)
	temp3 DWord 0
	Cuenta DWord 0
	temp4 DWord 0
	Num DWord 0
	temp5 DWord 0
	Var_1 DWord 0
	Var DWord 0
	temp6 DWord 0
	Valor DWord 0
	varString1 DWord 0
	varString2 DWord 0
	varString3 DWord 0
	varString4 DWord 0
	varString5 DWord 0
.Code

start:
	Invoke StdOut, Addr message
	Invoke StdOut, Addr salto
	Mov Eax, 0
	Mov Eax, 45
	Mov Edx, 0
	Mov Edx, 5
	Mul Edx
	Mov temp3, Eax

	Mov Eax, 0
	Mov Eax, temp3
	Mov Edx, 0
	Mov Edx, _
	Mul Edx
	Mov Cuenta, Eax

	Mov Eax, 0
	Mov Eax, 121
	Mov Edx, 0
	Mov Edx, Cuenta
	Mul Edx
	Mov temp4, Eax

	Mov Eax, 0
	Mov Eax, temp4
	Mov Edx, 0
	Mov Edx, _
	Mul Edx
	Mov Num, Eax

	Mov Eax, 0
	Mov Eax, Num
	Add Eax, 12
	Mov temp5, Eax

	Mov Eax, 0
	Mov Eax, temp5
	Mov Edx, 0
	Mov Edx, _
	Mul Edx
	Mov Var_1, Eax

	Mov Eax, 0
	Mov Eax, Num
	Add Eax, Var
	Mov temp6, Eax

	Mov Eax, 0
	Mov Eax, temp6
	Mov Edx, 0
	Mov Edx, _
	Mul Edx
	Mov Valor, Eax

	Invoke dwtoa, Cuenta, Addr varString1
	Invoke StdOut, Addr varString1
	Invoke StdOut, Addr salto
	Invoke dwtoa, Num, Addr varString2
	Invoke StdOut, Addr varString2
	Invoke StdOut, Addr salto
	Invoke dwtoa, Var_1, Addr varString3
	Invoke StdOut, Addr varString3
	Invoke StdOut, Addr salto
	Invoke dwtoa, Var, Addr varString4
	Invoke StdOut, Addr varString4
	Invoke StdOut, Addr salto
	Invoke dwtoa, Valor, Addr varString5
	Invoke StdOut, Addr varString5
	Invoke StdOut, Addr salto
	Invoke StdOut, Addr message2
	Invoke StdIn, Addr input, 10

End start
