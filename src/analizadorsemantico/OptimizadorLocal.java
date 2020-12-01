/*
  Optimización local de tipo:
  1. Instrucciones que se repiten sin haber tenido modificación alguna en uno de
     sus valores.
  Del archivo de cuartetos, cuando se encuentra una línea de asignación, 
  es decir, que ésta inicia directamente con "=", se debe de asignar un contador
  por cada id de asignación, por ejemplo:
    
                              = temp1 - Cuenta 

  se debe tener un contador para "Cuenta", donde, si se repite una enésima vez,
  en el archivo optimizado de cuartetos debe aparecer sólo la última asignación.

  Entrada:                                      Contadores de asignaciones:
  + 1356 15 temp1 
  = temp1 - Cuenta                              <- Cuenta = 1
  - 75 5 temp2 
  = temp2 - Valor                               <- Valor = 1
  * 45 5 temp3 
  = temp3 - Cuenta                              <- Cuenta++
  * 121 Cuenta temp4 
  = temp4 - Num                                 <- Num = 1
  + Num 12 temp5 
  = temp5 - Var_1                               <- Var_1 = 1
  + Num Var temp6 
  = temp6 - Valor                               <- Valor++
    
  Salida:       *Como "Cuenta" y "Valor" tienen asignaciones sin utilizar
  * 45 5 temp3 
  = temp3 - Cuenta                              
  * 121 Cuenta temp4 
  = temp4 - Num                                 
  + Num 12 temp5 
  = temp5 - Var_1                               
  + Num Var temp6 
  = temp6 - Valor     
 */
package analizadorsemantico;

import estructurasDeDatos.ListaEnlazada;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class OptimizadorLocal {
  File archivo;
  Scanner sc;
  ArrayList<String> lineas;
  ListaEnlazada tablaSimbolos;
  public OptimizadorLocal(ListaEnlazada tablaSimbolos){
    lineas = new ArrayList<String>();
    this.tablaSimbolos = tablaSimbolos;
  }
  
  public void openFile(){
    try{
      archivo = new File("src/salidas/Cuartetos.txt");
      sc = new Scanner(archivo);
      while(sc.hasNext())
        lineas.add(sc.nextLine());
    }catch(FileNotFoundException e){
      System.out.println(">>>Error abriendo archivo Cuartetos.txt");
    }
  }
  public void optimiza(){
    //int[] contador = new int[lineas.size()];
    for(int i = 0; i < lineas.size(); i++){
      if(lineas.get(i).charAt(0) == '='){
        
      }
    }
  }
  
//  public static void main(String[] args) {
//    OptimizadorLocal ol = new OptimizadorLocal();
//    ol.openFile();
//    ol.optimiza();
//  }
}
