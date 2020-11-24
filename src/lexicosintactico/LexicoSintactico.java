package lexicosintactico;
import analizadorlexico.AnalizadorLexico;
import datos.Gramatica;
import estructurasDeDatos.Nodo;
import estructurasDeDatos.Pila;
import java.io.EOFException;
public class LexicoSintactico {
  private final Gramatica gramatica;
  private final AnalizadorLexico analizadorLexico;
  private final Pila pila;
  private static final int predictiva [][] =  
                                {{1,0,0,0 ,0,0,0,0,0 ,0 ,0 ,0 ,0}, 
                                 {0,2,0,0 ,0,0,0,0,0 ,0 ,0 ,0 ,0},
                                 {0,0,3,4 ,0,0,0,0,0 ,0 ,0 ,0 ,0}, 
                                 {0,0,0,5 ,0,0,0,0,0 ,0 ,0 ,0 ,0},
                                 {0,0,0,6 ,0,0,6,0,6 ,6 ,0 ,0 ,0},
                                 {0,0,0,0 ,0,8,0,8,0 ,0 ,7 ,7 ,7},
                                 {0,0,0,10,0,0,9,0,11,12,0 ,0 ,0},
                                 {0,0,0,0 ,0,0,0,0,0 ,0 ,13,14,15}};
  public LexicoSintactico(Gramatica gramatica, Pila pila,
                          AnalizadorLexico analizadorLexico){
    this.gramatica = gramatica;
    this.analizadorLexico = analizadorLexico;
    this.pila = pila;
  }
  public void LlDriver() throws Exception{
    String x, a;
    try{
      System.out.println(">>Inicio del análisis");
      System.out.print("\tPila: ");
      pila.mostrarPila();
      pila.push(new Nodo("inicio"));
      System.out.println("Se ha agregado el símbolo inicial <inicio> a la pila");
      System.out.print("\tPila: ");
      pila.mostrarPila();
      x = (String) pila.getTope().getInfo();
      System.out.println("\tx: " + x + "\tÚltimo símbolo de la pila");
      a = analizadorLexico.getToken();
      System.out.println("\ta: " + a + "\tPrimer token del programa");
      System.out.println("\n-----Inicia el ciclo de análisis-----");
      System.out.print("\tPila: ");
      pila.mostrarPila();
      System.out.println("¿Está vacía la pila? " + pila.esVacia());
      while(!pila.esVacia()){
        System.out.println("\tx: " + x);
        System.out.println("\t¿x es noterminal? " + contains(x, gramatica.getNoTerminales()));
        if(contains(x, gramatica.getNoTerminales())){
          System.out.println("\t\t¿Predictiva de x: "+x+", a: "+a+" = "+ predict(a,x) +" es diferente de 0?");
          if(predict(a,x) != 0){
            System.out.println("\t\t\t\tx será el lado derecho de la producción " + predict(a,x));
            x = gramatica.getLadoDerecho()[predict(a,x) - 1];
            System.out.println("\t\t\t\tx: " + x);
            System.out.println("\t\t\t\tPop a la pila");
            pila.pop();
            System.out.println("\t\t\t\tAñadir el lado derecho de la producción");
            pila.cicloPush(x.split(" "));
            System.out.print("\t\t\t\tPila: ");
            pila.mostrarPila();
            x = (String) pila.getTope().getInfo();
          } else {
            System.out.println(">>>Error");
            throw new Exception();
//            break;
          }
        } else {
          System.out.println("\t\t¿Es x: "+x+" igual a a: "+a+" ?");
          if(x.equals(a)){
            System.out.println("\t\t\tPop a la pila");
            pila.pop();
            System.out.print("\t\t\tPila: ");
            pila.mostrarPila();
            a = analizadorLexico.getToken();
            x = (String) pila.getTope().getInfo();
            System.out.println("\t\t\ta: " + a + "\tSiguiente token del programa");
          } else {
            System.out.println(">>>Error");
            throw new Exception();
//            break;
          }
        }
      }
    }catch (EOFException e){
      System.out.println("\n>>>¡Se ha terminado de analizar el programa,"
              + " no se encontraron errores!");
    }
  }
  private boolean contains(String a, String[] r){
    for (String c : r)
      if(c.equals(a))
        return true;
    return false;
  }
  private int predict(String terminal, String noTerminal){
    int fila, columna;
    fila = indexNoTerminal(noTerminal);
    columna = indexTerminal(terminal);
    if(fila != -1 && columna != -1)
      return predictiva[fila][columna];
    else 
      return 0;
  }
  private int indexTerminal(String terminal){
    for (int i = 0; i < gramatica.getTerminales().length; i++)
      if(gramatica.getTerminales()[i].equals(terminal))
        return i;
    return -1;
  }
  private int indexNoTerminal(String noTerminal){
    for (int i = 0; i < gramatica.getNoTerminales().length; i++)
      if(gramatica.getNoTerminales()[i].equals(noTerminal))
        return i;
    return -1; 
  }
}
