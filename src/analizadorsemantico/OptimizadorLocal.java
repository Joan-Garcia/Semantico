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

import datos.EscribeEnArchivo;
import estructurasDeDatos.ListaEnlazada;
import estructurasDeDatos.Nodo;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class OptimizadorLocal {
  private File archivoEntrada;
  private Scanner sc;
  private final ArrayList<String> lineas;                                       //Guarda cada cadena de las líneas del archivo
  private final EscribeEnArchivo archivoSalida;
  
  public OptimizadorLocal(){
    lineas = new ArrayList<>();
    archivoSalida = new EscribeEnArchivo();
    archivoSalida.crearArchivo("CuartetosOptimizado");
  }
  
  private void captura(){
    try{
      archivoEntrada = new File("src/salidas/Cuartetos.txt");
      sc = new Scanner(archivoEntrada);
      while(sc.hasNext())
        lineas.add(sc.nextLine());                                              //Llenado del arraylist de las líneas.
    }catch(FileNotFoundException e){
      System.out.println(">>>Error abriendo archivo Cuartetos.txt");
    }
  }
  
  private void procesa(){
    boolean registrado = false;
    String[] lineaSegmentada = new String[3];
    ListaEnlazada contadores = new ListaEnlazada();                             //Lista enlazada de contadores. Contiene el id seguido del
                                                                                //número de repeticiones
    
    for(int i = 0; i < lineas.size(); i++){                                     //Para cada línea en el archivo.
      
      if(lineas.get(i).charAt(0) == '='){                                         //Si se trata de una instrucción de asignación
        lineaSegmentada = lineas.get(i).split(" ");                                 //Obten las cuatro partes de la instrucción
        
        for (int j = 0; j < contadores.size(); j++){                                //Verifica si ya está registrado:
          if(contadores.get(j).getInfo().equals(lineaSegmentada[3])){
            int numeroActual = (int) contadores.get(j).getSiguiente().getInfo();      //Si está registrado, obten su contador
            contadores.get(j).getSiguiente().setInfo(numeroActual + 1);               //Aumentalo en uno
            registrado = true;                                                        //Señala a la bandera que ya está registrado 
            break;                                                                    //Procede a la siguiente línea del archivo
          }
        }
        
        if(!registrado){                                                            //NO está registrado 
          contadores.add(new Nodo(lineaSegmentada[3]));                               //Añádelo a la lista de contadores.
          contadores.add(new Nodo(1));                                                //Añade su contador en 1
        }
        
      }
      
    }
    
    //En este punto, tenemos completa la lista de contadores para optimizar
    
    for(int i = lineas.size()-1; i >= 0; i--){                                    //Para cada línea en el archivo. Recorre de última a primera
      
      if(lineas.get(i).charAt(0) == '='){                                         //Si se trata de una instrucción de asignación
        lineaSegmentada = lineas.get(i).split(" ");                                 //Obten las cuatro partes de la instrucción
        
        //Si obtenemos una instrucción de asignación cuyo id ya se ha repetido,
        //debemos eliminarla. Para saber si ya pasamos la línea que queremos
        //conservar, modificaremos su contador a 0.

        if(contadores.exist(new Nodo(lineaSegmentada[3]))){                         //¿El id está en la lista de contadores?
          int indexContador = (int) contadores.indexOf(lineaSegmentada[3]) + 1;       //Obten el index de su contador
          if((int) contadores.get(indexContador).getInfo() == 0){                      //¿Ya se presentó la asignación a guardar?
            //Eliminar la asignación redundante
//            System.out.println(lineas.get(i));
//            System.out.println(lineas.get(i-1));
            lineas.remove(i);
            lineas.remove(i - 1);
            i--;
          }else{                                                                      //Estamos en la asignación a guardar
            contadores.get(indexContador).setInfo(0);
          }
        }
        
      }
    
    }
  }
  
  private void escribe(){
    String[] lineaSegmentada = new String[3];
    System.out.println("OPERADOR\tOPERANDO1\tOPERANDO2\tRESULTADO");
    
    for(int i = 0; i < lineas.size(); i++){
      lineaSegmentada = lineas.get(i).split(" ");
      for (String parte: lineaSegmentada) 
        System.out.print(parte + "\t\t");
      System.out.println();
      archivoSalida.escribeLinea("CuartetosOptimizado", lineas.get(i));
    }
    
  }
  
  public void optimiza(){
    captura();
    procesa();
    escribe();
  }
  
  public static void main(String[] args) {
    OptimizadorLocal ol = new OptimizadorLocal();
    ol.optimiza();
  }
}
