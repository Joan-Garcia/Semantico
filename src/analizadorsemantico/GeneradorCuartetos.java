package analizadorsemantico;

import datos.EscribeEnArchivo;
import estructurasDeDatos.ListaEnlazada;
import estructurasDeDatos.Nodo;
import estructurasDeDatos.Pila;

public class GeneradorCuartetos {
  //Arreglo que guardará los cuartetos generados
  private final String [][] cuarteto;
  //Lista que guardará la expresión en postfijo a evaluar
  private final ListaEnlazada operandos;
  EscribeEnArchivo archivo;
  
  //Recibe la lista enlazada de pilas que resulta de ConvertidorDeExp.java al 
  //terminar de analizar el programa.
  public GeneradorCuartetos(ListaEnlazada expEnPostfix){
    cuarteto = new String[20][4];
    operandos = new ListaEnlazada();
    
    //Tenemos que pasar las pilas de la lista enlazada al arreglo *operandos*
    for (int i = 0; i < expEnPostfix.size(); i++) {                             //Para cada pila en la lista
      Pila p = (Pila) expEnPostfix.get(i).getInfo();                            //  Tomar la pila
      while(!p.esVacia())                                                       //  Para cada elemento de la pila
        operandos.add(new Nodo(p.popElemento().getInfo()));                     //    Mételo en la lista de operandos
    }
    archivo = new EscribeEnArchivo();
    archivo.crearArchivo("Cuartetos");
  }
  
  //Genera los cuartetos a partir de los operandos.
  private void GeneraCuartetos(){
    int o1=0;  //índice del arreglo cuartetos
    int resuelve=0;  //bandera que indica si se encontró un signo de "="
                     //para resolverse la operación aritmética
    int longoper2=operandos.size();  //longitud del tamaño del arreglo que contiene la notación
    int temporal=1; //contador de las variables temporales
    int bandera=0;  //bandera para los operandos, si bandera=0 el valor del operando se escribirá en operando1
                    // en caso contrario el operando se escribirá en operando2
        
    for(int i=0; i<longoper2; i++){
      switch((String) operandos.get(i).getInfo()){
        case "+": 
          cuarteto[o1][0]=(String) operandos.get(i).getInfo();
          cuarteto[o1][3]="temp"+temporal;
          temporal++;
          o1++;
          break;
        case "-": 
          cuarteto[o1][0]=(String) operandos.get(i).getInfo();
          cuarteto[o1][3]="temp"+temporal;
          temporal++;
          o1++;
          break;
        case "*": 
          cuarteto[o1][0]=(String) operandos.get(i).getInfo();
          cuarteto[o1][3]="temp"+temporal;
          temporal++;
          o1++;
          break;
        case "=":
          cuarteto[o1][0]=(String) operandos.get(i).getInfo();
          cuarteto[o1][1]=cuarteto[o1-1][3];
          bandera=1;
          resuelve=1;                   
          break;
        default:
          if(resuelve==0){  //si todavía no se ha leído en esa expresión un signo de "="
            if (bandera==0){   //si es el operando1
              cuarteto[o1][1]=(String) operandos.get(i).getInfo();
              bandera=1;
              break;
            }else{       // si es el operando2
              cuarteto[o1][2]=(String) operandos.get(i).getInfo();  //si es el operando
              resuelve=1;
              bandera=0;
              break;
            }
          }else{    // si ya se encontró un signo "="
            if(bandera==1){  //si es el operando2
              cuarteto[o1][2]="-";
              cuarteto[o1][3]=(String) operandos.get(i).getInfo();
              bandera=0;
              resuelve=0;
              o1++;
              break;
            }
          }  
      }
    }
  }
    
  public void mostrarCuarteto() {
    GeneraCuartetos();
    String line = "";
    System.out.println("OPERADOR\tOPERANDO1\tOPERANDO2\tRESULTADO");
    for(int i=0,linea=0; i<cuarteto.length;i++){
      for(int j=0; j<cuarteto[i].length;j++){
        if(cuarteto[i][j]!="" && cuarteto[i][j] != null){
          System.out.print(cuarteto[i][j]+"\t\t");
          line = line + cuarteto[i][j] + " ";
        }else
          break;
        linea++;
        if(linea==4){
          System.out.println("");
          archivo.escribeLinea("Cuartetos", line);
          line = "";
          linea=0;
        }
      }
    }
    System.out.println("");
  }
}
