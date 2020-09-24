package analizadorlexico;

import estructurasDeDatos.ListaEnlazada;
import estructurasDeDatos.Nodo;
import java.io.EOFException;

public class AnalizadorLexico {
  private final String[][] listaPalabrasReservadas;
  private final ListaEnlazada simbolos, tokens;
  private final String programa;
  private int inicio;
  private static final String tipoToken [][] = {{"programa", "Inicio de archivo"},
                                                {"begin",    "Inicio bloque de código"},
                                                {"end",      "Fin bloque de código"},
                                                {"id",       "Int"},
                                                {":=",       "Asignación"},
                                                {";",        "Punto y coma"},
                                                {"+",        "Operación Suma"},
                                                {"-",        "Operación Resta"},
                                                {"*",        "Operación Multiplicación"},
                                                {"(",        "Parentesis que abre"},
                                                {")",        "Parentesis que cierra"}};
  
  public AnalizadorLexico(String programa){
    listaPalabrasReservadas = new String[4][2];
    simbolos = new ListaEnlazada();
    tokens = new ListaEnlazada();
    // Llenar la lista de palabras reservadas:
    listaPalabrasReservadas[0][0] = "Palabra reservada";
    listaPalabrasReservadas[1][0] = "programa";
    listaPalabrasReservadas[2][0] = "begin";
    listaPalabrasReservadas[3][0] = "end";
    
    listaPalabrasReservadas[0][1] = "token";
    listaPalabrasReservadas[1][1] = "400";
    listaPalabrasReservadas[2][1] = "401";
    listaPalabrasReservadas[3][1] = "402";
    
    for (int i = 0; i < 2; i++)
      simbolos.add(new Nodo(new ListaEnlazada()));
    
    this.programa = programa;
    this.inicio = 0;
    
    for (int i = 0; i < 5; i++) 
      tokens.add(new Nodo(new ListaEnlazada()));
  }
  
  public String getToken() throws EOFException{
    String palabra;
    int estado;
    boolean error  = false;
    palabra = "";
    estado = 0;
    
    if(inicio == programa.length())
      throw new EOFException();
    else
    while(inicio != programa.length())
    switch(estado){
      case 0:
        if (esSimbolo(programa.charAt(inicio))){ 
          estado = 2;                                   
          break;
        } else if(error){
          palabra += programa.charAt(inicio);
          if(esEspacio(programa.charAt(inicio + 1))){
            inicio++;
            return null;
          }
          inicio++;
          estado = 0;
          break;
        } else if(esNumero(programa.charAt(inicio))){
          estado = 5;
          break;
        } else if(esMayuscula(programa.charAt(inicio))){
          palabra += programa.charAt(inicio);
          inicio++;
          estado = 3;
          break;
        } else if(esMinuscula(programa.charAt(inicio))){
          estado = 1;
          break;
        } else if(esEspacio(programa.charAt(inicio))){
          inicio++;
          estado = 0;
          break;
        } else {                                      //Es caracter no permitido
          palabra += programa.charAt(inicio);
          if(esPunto(programa.charAt(inicio))){
            while(esNumero(programa.charAt(inicio + 1))){
              palabra += programa.charAt(inicio + 1);
              inicio++;
            }
          }
          if(esEspacio(programa.charAt(inicio + 1))|| 
             esSimbolo(programa.charAt(inicio + 1))  ||
             esNumero(programa.charAt(inicio + 1))   ||
             esMinuscula(programa.charAt(inicio + 1))||
             esMayuscula(programa.charAt(inicio + 1))  
             ){
            inicio++;
            return null;
          }
          inicio++;
          estado = 0;
          break;
        }
      case 1:                                               //PALABRASRESERVADAS
        if(esMinuscula(programa.charAt(inicio))){
          palabra += programa.charAt(inicio);
          inicio++;
          estado = 1;
          break;
        } else {
          if(esPalabraReservada(palabra)){
            añadeFilaATokens(palabra, "Palabra reservada", getTipoToken(palabra)
                             ,String.valueOf(tokenPalabraReservada(palabra)));
            inicio++;
            return palabra;
          } else {
            return null;
          }
        }
      case 2:                                               // SÍMBOLO
        if(esSimbolo(programa.charAt(inicio))){
          palabra += programa.charAt(inicio);
          if(programa.charAt(inicio) == ':' && programa.charAt(inicio + 1) == '='){
            // Caso de símbolo de asignación.
            añadeFilaATokens(":=", "Símbolo especial", getTipoToken(":="), "403");
            inicio++;
            palabra += programa.charAt(inicio);
            inicio++;
            
            return palabra;
          } else {
            añadeFilaATokens(String.valueOf(programa.charAt(inicio)), 
                           "Caracter simple", getTipoToken(palabra)
                           ,String.valueOf(valorASCII(programa.charAt(inicio))));
            inicio++;
            return palabra;
          }
        } else {
          estado = 0;
          break;
        }
      case 3:                                               //IDENTIFICADOR         
        if(esMinuscula(programa.charAt(inicio))){
          palabra += programa.charAt(inicio);
          inicio++;
          estado = 3;
          break;
        } else if(esGuionBajo(programa.charAt(inicio))){
          if(esEspacio(programa.charAt(inicio + 1))){
            estado = 0;
            error = true;
            break;
          }
          palabra += programa.charAt(inicio);
          inicio++;
          estado = 3;
          break;
        } else if(esNumero(programa.charAt(inicio))){
          palabra += programa.charAt(inicio);
          inicio++;
          estado = 3;
          break;
        } else{
          if(esMayuscula(programa.charAt(inicio)) && !esSimbolo(programa.charAt(inicio)) && 
                  !esEspacio(programa.charAt(inicio))){
            estado = 0;
            error = true;
            break;
          }
          //  Añade a la tabla de símbolos.
          if(!simboloRegistrado(palabra))                                       //Si no está registrado el identificador.
            añadeFilaASimbolos(palabra, "Identificador");                         //Regístralo.
          
          ListaEnlazada temp = (ListaEnlazada) simbolos.get(0).getInfo();       //Para obtener la longitud de la lista y con ello el valor de id
          añadeFilaATokens(palabra, "Identificador", getTipoToken("id"),String.valueOf(500 + temp.size()));
          return "id";
        }
      case 5:
        if(esNumero(programa.charAt(inicio))){              // NÚMERO ENTERO
          palabra += programa.charAt(inicio);             //Guardamos el número
          inicio++;
          estado = 5;
          break;
        } else if (esPunto(programa.charAt(inicio))){  // sigue un punto.
          palabra += programa.charAt(inicio);          //Guardamos el punto
          if (!esNumero(programa.charAt(inicio + 1))){
            inicio++;
            return null;
          } else {
            inicio++;
            estado = 6;                                     //Es flotante
            break;
          }
        } else {                                       //Termina el número entero
          if(esEspacio(programa.charAt(inicio))){
            añadeFilaATokens(palabra, "Número", "Int", palabra);
            return "intliteral";
          }else if(esMayuscula(programa.charAt(inicio)) || 
                   !esSimbolo(programa.charAt(inicio)) ||
                   esMinuscula(programa.charAt(inicio))){
            estado = 0;
            error = true;
            break;
          } else{
            añadeFilaATokens(palabra, "Número", "Int", palabra);
            return "intliteral";
          }
        }
      case 6:                                             // NÚMERO FLOTANTE
        if(esNumero(programa.charAt(inicio))){
          palabra += programa.charAt(inicio); 
          inicio++;
          estado = 6;
          break;
        } else {                                      //Termina el número float
          añadeFilaATokens(palabra, "Número", "Float", "500");
          return "realliteral";
        }
    }
    throw new EOFException();
  }
  
  private void añadeFilaASimbolos(String lexema, String clasificacion){
    ListaEnlazada temp = new ListaEnlazada();
            
    temp = (ListaEnlazada) simbolos.get(0).getInfo();
    temp.add(new Nodo(lexema));
            
    temp = (ListaEnlazada) simbolos.get(1).getInfo();
    temp.add(new Nodo(clasificacion));
  }
  
  // Comprueba si un símbolo ya existe en la tabla de símbolos.
  private boolean simboloRegistrado(String lexema){
    ListaEnlazada temp = new ListaEnlazada();
            
    temp = (ListaEnlazada) simbolos.get(0).getInfo();
    return temp.exist(new Nodo(lexema));
  }
  
  private boolean esPunto(char c){
    return c == '.';
  }
  
  private boolean esSimbolo(char c){
    return c == ';' || c == '=' || c == '+' || c == '-' || c == '*' || 
           c == '(' || c == ')' || c == ':'; 
  }
  
  private boolean esMinuscula(char c){
    return Character.isLowerCase(c);
  }
  
  private boolean esMayuscula(char c){
    return Character.isUpperCase(c);
  }
  
  private boolean esEspacio(char c){
    return Character.isWhitespace(c);
  }
  
  private boolean esGuionBajo(char c){
    return new Character(c).equals('_');
  }
  
  private boolean esNumero(char c){
    return Character.isDigit(c);
  }
  
  private int valorASCII(char c){
    return (int) c;
  }
  
  // Comprueba si una palabra forma parte de las palabras reservadas.
  public boolean esPalabraReservada(String palabra){
    for (int i = 0; i <= 3; i++)
      if(palabra.equals(listaPalabrasReservadas[i][0]))
        return true;
    return false;
  }
  
  public ListaEnlazada getTablaSimbolos(){
    return simbolos;
  }
  
  public String[][] getListaPalabrasReservadas(){
    return listaPalabrasReservadas;
  }
  
  // Devuelve el token correspondiente a la palabra reservada.
  private int tokenPalabraReservada(String palabra){
    for (int i = 0; i <= 3; i++)
      if(palabra.equals(listaPalabrasReservadas[i][0]))
        return Integer.parseInt(listaPalabrasReservadas[i][1]);
    return -1;
  }
  
  private void añadeFilaATokens(String lexema, String token, String tipo,
                                String atributo){
    ListaEnlazada temp = new ListaEnlazada();
    
    temp = (ListaEnlazada) tokens.get(0).getInfo();
    if (temp.exist(new Nodo<String>(lexema))){
      int i = temp.indexOf(lexema);
      temp = (ListaEnlazada) tokens.get(4).getInfo();
      int v = 1 + Integer.parseInt((String) temp.get(i).getInfo());
      temp.get(i).setInfo(String.valueOf(v));
    } else {
    temp.add(new Nodo(lexema));
            
    temp = (ListaEnlazada) tokens.get(1).getInfo();
    temp.add(new Nodo(token));
    
    temp = (ListaEnlazada) tokens.get(2).getInfo();
    temp.add(new Nodo(tipo));
    
    temp = (ListaEnlazada) tokens.get(3).getInfo();
    temp.add(new Nodo(atributo));
    
    temp = (ListaEnlazada) tokens.get(4).getInfo();
    temp.add(new Nodo("1"));
    }
  }
  
  public ListaEnlazada getTablaTokens(){
    return tokens;
  }
  
  private String getTipoToken(String lexema) {
    for (int i = 0; i < 11; i++)
      if (tipoToken[i][0].equals(lexema))
        return tipoToken[i][1];
    return "x";
  }
  
  private boolean lexemaRepetido(String lexema) {
    ListaEnlazada temp = new ListaEnlazada();
    temp = (ListaEnlazada) tokens.get(0).getInfo();
    if (temp.exist(new Nodo<String>(lexema)))
      return true;
    return false;
  }
}
