package lexicosintactico;
import analizadorlexico.AnalizadorLexico;
import analizadorsemantico.ConvertidorDeExp;
import analizadorsemantico.GeneradorArbol;
import analizadorsemantico.GeneradorCuartetos;
import analizadorsemantico.OptimizadorLocal;
import datos.Archivo;
import datos.Gramatica;
import estructurasDeDatos.ListaEnlazada;
import estructurasDeDatos.Pila;
public class Main {
  Gramatica gramatica;
  AnalizadorLexico analizadorLexico;
  GeneradorArbol arboles;
  ConvertidorDeExp infijoAPostfijo;
  GeneradorCuartetos generadorDeCuartetos;
  OptimizadorLocal optimizadorCuartetos;
  Pila pila;
  LexicoSintactico analizadorLexicoSintactico;
  ListaEnlazada temp;
  String programa;
  public Main(){
    gramatica = new Gramatica(new Archivo());
    pila = new Pila();
  }
  private void procesa() throws Exception{
    analizadorLexicoSintactico = new LexicoSintactico(gramatica, pila,
                                                      analizadorLexico);
    analizadorLexicoSintactico.LlDriver();
    arboles = new GeneradorArbol(programa, analizadorLexico.getTablaSimbolos(), 
                                 analizadorLexico.getTablaTokens());
    infijoAPostfijo = new ConvertidorDeExp(programa);
  }
  private void captura(){
    System.out.println("Selecciona archivo de gramática");
    gramatica.prepararGramatica();
    System.out.println("Selecciona archivo de programa");
    programa = new Archivo().leerArchivo();
    analizadorLexico = new AnalizadorLexico(programa);
  }
  private void resultados(){
    //Impresión de la tabla de símbolos:
    temp = (ListaEnlazada) analizadorLexico.getTablaSimbolos().get(0).getInfo();
    String[] s1 = temp.toArray();
    temp = (ListaEnlazada) analizadorLexico.getTablaSimbolos().get(1).getInfo();
    String[] s2 = temp.toArray();
    temp = (ListaEnlazada) analizadorLexico.getTablaSimbolos().get(2).getInfo();
    String[] s3 = temp.toArray();
    temp = (ListaEnlazada) analizadorLexico.getTablaSimbolos().get(3).getInfo();
    String[] s4 = temp.toArray();
    temp = (ListaEnlazada) analizadorLexico.getTablaSimbolos().get(4).getInfo();
    String[] s5 = temp.toArray();
    temp = (ListaEnlazada) analizadorLexico.getTablaSimbolos().get(5).getInfo();
    String[] s6 = temp.toArray();
    System.out.println("\nTabla de Simbolos");
    System.out.printf("%-20s%-20s%-20s%-20s%-20s%-20s\n", "Nombre", "Tipo", "Valor de Id", "Repeticiones", "Linea", "Valor Atributo");
    System.out.println("-------------------------------------------------------"
            + "--------------------------------------------------------------------");
    for(int i = 0; i < s1.length; i++)
      System.out.printf("%-20s%-20s%-20s%-20s%-20s%-20s\n", s1[i], s2[i], s3[i], s4[i], s5[i], s6[i]);
    System.out.println("");
    //Impresión de la tabla de tokens:
    temp = (ListaEnlazada) analizadorLexico.getTablaTokens().get(0).getInfo();
    String[] t1 = temp.toArray();
    temp = (ListaEnlazada) analizadorLexico.getTablaTokens().get(1).getInfo();
    String[] t2 = temp.toArray();
    temp = (ListaEnlazada) analizadorLexico.getTablaTokens().get(2).getInfo();
    String[] t3 = temp.toArray();
    temp = (ListaEnlazada) analizadorLexico.getTablaTokens().get(3).getInfo();
    String[] t4 = temp.toArray();
    temp = (ListaEnlazada) analizadorLexico.getTablaTokens().get(4).getInfo();
    String[] t5 = temp.toArray();
    System.out.println("\nTabla de Tokens");
    System.out.printf("%-20s%-30s%-40s%-20s%-20s\n", "Lexema", "Token", "Tipo", "Valor", "Repetición");
    System.out.println("-------------------------------------------------------"
            + "-------------------------------------------------------------------");
    for (int i = 0; i < t1.length; i++)
      System.out.printf("%-20s%-30s%-40s%-20s%-20s\n", t1[i], t2[i], t3[i], t4[i], t5[i]);
    System.out.println("");
    arboles.generaArboles();
    System.out.println("\nConversión de Infijo a Postfijo");
    infijoAPostfijo.convierteExpresiones();
    // Se muestran las pilas para comprobar que se guradaron correctamente
    ListaEnlazada test = infijoAPostfijo.getExpPostfijas();
    for (int i = 0; i < test.size(); i++){
      Pila p = (Pila) test.get(i).getInfo();
      p.mostrarPila();
    }
    System.out.println("\nGeneración de código intermedio");
    generadorDeCuartetos = new GeneradorCuartetos(infijoAPostfijo.getExpPostfijas());
    generadorDeCuartetos.mostrarCuarteto();
    System.out.println("\nOptimizador local");
    optimizadorCuartetos = new OptimizadorLocal();
    optimizadorCuartetos.optimiza();
  }
  public static void main(String[] args) {
    Main m = new Main();
    long tInicio, tFin, tiempo;
    try{
      m.captura();
      tInicio = System.currentTimeMillis();
      m.procesa();
      m.resultados();
      tFin = System.currentTimeMillis();
      tiempo = tFin - tInicio;
      System.out.println("TIEMPO DE EJECUCIÓN: "+tiempo+"ms");
    }catch(Exception e){
      System.out.println(">>>Analisis terminado por error.");
      e.printStackTrace();
    }
  }
}
