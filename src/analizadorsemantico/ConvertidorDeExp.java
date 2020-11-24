
package analizadorsemantico;
import estructurasDeDatos.ListaEnlazada;
import estructurasDeDatos.Nodo;
public class ConvertidorDeExp {
  String programa[];
  ListaEnlazada expPostfijas;
  public ConvertidorDeExp(String programa) {
    this.programa = programa.split("&");
    expPostfijas = new ListaEnlazada();
  }
  public void convierteExpresiones() {
    String linea;
    for (int i = 0; i < programa.length; i++) {                                 //Para cada línea del programa
      linea = programa[i];
      if(linea.contains(":=")){                                                 //Si encuentra una asignación
        String operaciones = linea.split(":=")[1];                              //Sacamos las parte derecha de la asignación
        String var = linea.split(":=")[0].replaceAll("\\s", "");
        // Prints de prueba. paa ver el funcionamiento
        System.out.println(operaciones.replaceAll("\\s", "").replace(";", "") + " ");
        System.out.println(InfijoAPostfijo.infixToPostfix(operaciones.replaceAll("\\s", "").replace(";", "") + " "));
        expPostfijas.add(new Nodo(InfijoAPostfijo.toStack(operaciones.replaceAll("\\s", "").replace(";", "") + " ", var)));
      }
    }
  }
  public ListaEnlazada getExpPostfijas() {
    return expPostfijas;
  }
}
