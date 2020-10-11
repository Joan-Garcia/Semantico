package analizadorsemantico;

import analizadorlexico.AnalizadorLexico;
import datos.EscribeEnArchivo;
import estructurasDeDatos.ListaEnlazada;
import estructurasDeDatos.Nodo;
import java.io.EOFException;

public class GeneradorArbol {
  String programa[];
  ListaEnlazada tablaDeTokens;
  ListaEnlazada simbolos;
  //Lista de las listas generadas por cada expresión que encuentre:
  ListaEnlazada arboles;
  EscribeEnArchivo archivo;
  
  
  //Recibe la cadena de programa formateada con '&' en cada salto de línea
  public GeneradorArbol(String programa, ListaEnlazada simbolos, ListaEnlazada tablaDeTokens){
    this.programa = programa.split("&");
    this.simbolos = simbolos;
    this.tablaDeTokens = tablaDeTokens;
    arboles = new ListaEnlazada();
    archivo = new EscribeEnArchivo();
    
    for (int i = 0; i < 6; i++)
      arboles.add(new Nodo(new ListaEnlazada()));
    
    archivo.crearArchivo();
  }
  
  public void generaArboles(){
    String linea;
    String tipoExpresion;
    
    for (int i = 0; i < programa.length; i++) {                                 //Para cada línea del programa
      linea = programa[i];
    
      if(linea.contains(":=")){                                                 //Si encuentra una asignación
        String operaciones = linea.split(":=")[1];                              //Sacamos las parte derecha de la asignación
        
        tipoExpresion = evaluaExpresion(operaciones);
        cambiaTipoEnSimbolos(linea.split(":=")[0].replaceAll("\\s", ""), tipoExpresion);
        printRegistro(getIndexSimbolo(linea.split(":=")[0].replaceAll("\\s", "")));
      }
    }
  }
  
  private String evaluaExpresion(String expresion){
    AnalizadorLexico tokenizer = new AnalizadorLexico(expresion);
    ListaEnlazada lexemasEnExpresion = new ListaEnlazada();
    String[] tiposEnExpresion;
    String resultado = "";
    //Extraer los lexemas de la expresion:
    try{
      while (true){
        tokenizer.getToken();
      }
    } catch(EOFException e){
      //Se terminaron de extraer los tokens
    }
    
    //Pasar la tabla de simbolos a nuestra lista de lexemas en expresión
    lexemasEnExpresion = (ListaEnlazada) tokenizer.getTablaSimbolos().get(0).getInfo();
       
    //Extraer los tipos de cada lexema:
    tiposEnExpresion = new String[lexemasEnExpresion.size()];
    for (int i = 0; i < tiposEnExpresion.length; i++){
      tiposEnExpresion[i] = getTipo((String) lexemasEnExpresion.get(i).getInfo()); 
    }
   
    //Evaluar:
    if(tiposEnExpresion.length == 1)
      resultado = tiposEnExpresion[0];
    else{
      resultado = tipoResultante(tiposEnExpresion[0], tiposEnExpresion[1]);
      for (int i = 2; i < tiposEnExpresion.length; i++) 
        resultado = tipoResultante(resultado, tiposEnExpresion[i]);
    }
    imprimeArbol(lexemasEnExpresion, resultado);
    return resultado;
  }
  
  private void imprimeArbol(ListaEnlazada listaDeTokens, String resultado){
    System.out.println("\nÁrboles de derivación");
    System.out.printf("%-20s%-20s%-20s%-20s%-20s%-20s\n", "Nombre", "Tipo", "Valor de Id", "Repeticiones", "Linea", "Valor Atributo");
    System.out.println("-------------------------------------------------------"
            + "--------------------------------------------------------------------");
    for (int i = 0; i < listaDeTokens.size(); i++)
      printRegistro(getIndexSimbolo((String) listaDeTokens.get(i).getInfo()));
    
    System.out.printf("%-20s%-20s\n", "Expresión", resultado);
    archivo.escribeLinea("Expresion; "+resultado);
    
    System.out.println("\n");
  }
   
  private String tipoResultante(String tipo1, String tipo2){
    if(tipo1.equals("Int") && tipo2.equals("Int"))
      return "Int";
    else if(tipo1.equals("Int") && tipo2.equals("Float"))
      return "Float";
    else if(tipo1.equals("Float") && tipo2.equals("Int")) 
      return "Float";
    else if(tipo1.equals("Float") && tipo2.equals("Float"))
      return "Float";
    else{
      System.out.println("ERROR SEMÁNTICO");
      System.exit(0);
    }
    return null;
  }
  
  //Devuelve el tipo de un lexema que se encuentra en la tabla de tokens
  private String getTipo(String lexema){
    ListaEnlazada temp = new ListaEnlazada();
    
    temp = (ListaEnlazada) tablaDeTokens.get(0).getInfo();
    
    if (temp.exist(new Nodo<String>(lexema))){
      int i = temp.indexOf(lexema);                                             //Index de fila donde está el lexema
      temp = (ListaEnlazada) tablaDeTokens.get(2).getInfo();                    //Columna de tipo
      return (String) temp.get(i).getInfo();
    } else 
      return null;
  }
  
  private int getIndexSimbolo(String simbolo){
    ListaEnlazada temp = new ListaEnlazada();
    
    temp = (ListaEnlazada) simbolos.get(0).getInfo();
    
    if (temp.exist(new Nodo<String>(simbolo)))
      return temp.indexOf(simbolo);  
    
    return -1;
  }
  
  private void printRegistro(int index){
    String nombre;
    String tipo;
    String id;
    String repeticiones;
    String linea;
    String atributo;
    ListaEnlazada temp;
    
    temp = (ListaEnlazada) simbolos.get(0).getInfo();
    nombre = (String) temp.get(index).getInfo();
    temp = (ListaEnlazada) simbolos.get(1).getInfo();
    tipo = (String) temp.get(index).getInfo();
    temp = (ListaEnlazada) simbolos.get(2).getInfo();
    id = (String) temp.get(index).getInfo();
    temp = (ListaEnlazada) simbolos.get(3).getInfo();
    repeticiones = (String) temp.get(index).getInfo();
    temp = (ListaEnlazada) simbolos.get(4).getInfo();
    linea = (String) temp.get(index).getInfo();
    temp = (ListaEnlazada) simbolos.get(5).getInfo();
    atributo = (String) temp.get(index).getInfo();
    
    System.out.printf("%-20s%-20s%-20s%-20s%-20s%-20s\n", nombre, tipo, id, 
                       repeticiones, linea, atributo);
    archivo.escribeLinea(nombre + "; " + tipo + "; " + id + "; " + repeticiones + "; " + linea + "; " + atributo);
  }
  
  private void cambiaTipoEnSimbolos(String lexema, String tipo) {
    ListaEnlazada temp = new ListaEnlazada();
    
    temp = (ListaEnlazada) simbolos.get(0).getInfo();
    if (temp.exist(new Nodo(lexema))){
      int i = temp.indexOf(lexema);
      temp = (ListaEnlazada) simbolos.get(1).getInfo();
      temp.get(i).setInfo(String.valueOf(tipo));
    }
  }
  
}
