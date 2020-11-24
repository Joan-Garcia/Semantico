package datos;
import estructurasDeDatos.ListaEnlazada;
import estructurasDeDatos.Nodo;
public class Gramatica {
  private String[] gramatica, ladoDerecho, noTerminales, terminales;
  private Archivo archivo;
  public Gramatica(Archivo archivo){
    this.archivo = archivo;
  }
  /**
   * Abre el archivo de texto que contiene la gramática y se encarga de llenar
   * las estructuras de datos.
   */
  public void prepararGramatica(){
    boolean boolNoTerminal = false;
    String linea;
    ListaEnlazada gramatica, ladoDerecho, noTerminales, terminales;             //Utilizamos estructuras dinámicas como puente.
    gramatica = new ListaEnlazada();
    ladoDerecho = new ListaEnlazada();
    noTerminales = new ListaEnlazada();
    terminales = new ListaEnlazada();
    archivo.abrirArchivo();
    while((linea = archivo.leerLineaSiguiente()) != null){
      String[] lineaDividida = linea.split("->");
      // Añadimos cada producción
      gramatica.add(new Nodo(linea));
      // Añadimos el lado derecho de la producción
      ladoDerecho.add(new Nodo(lineaDividida[1]));
      // Los símbolos no terminales se encuentran en el lado izquierdo, después
      // nos encargaremos de eliminar duplicados.
      noTerminales.add(new Nodo(lineaDividida[0]));
    }
    //Paso de las estructuras de datos a los parámetros de la clase.
    this.gramatica = gramatica.toArray();
    this.ladoDerecho = ladoDerecho.toArray();
    this.noTerminales = eliminaDuplicadosYEspacios(noTerminales.toArray());
    // Los símbolos terminales se encuentran del lado derecho de la producción, 
    // al igual que también algunos no terminales, por ello, comparamos ambas
    // listas.
    for (String cadena : this.ladoDerecho) {                                    //Para cada lado derecho de las producciones
      String[] cadenaDividida = cadena.split(" ");                              //Separa los símbolos
      for (String simbolo : cadenaDividida){                                     //Para cada símbolo
        boolNoTerminal = false;
        if(!simbolo.equals(" ") && !simbolo.equals("")){
          simbolo = simbolo.replace(" ", "");
          for (String noTerminal : this.noTerminales)
            if(simbolo.equals(noTerminal))                                       //Compara si es un terminal
              boolNoTerminal = true;                                              // ... Si es un terminal, enciende la bandera.
          if(!boolNoTerminal)
            terminales.add(new Nodo(simbolo));
        }
      }
    }
    this.terminales = eliminaDuplicadosYEspacios(terminales.toArray());
    archivo.cerrarArchivo();
  }
  private String[] eliminaDuplicadosYEspacios(String[] arreglo){
    ListaEnlazada arregloFinal;
    arregloFinal = new ListaEnlazada();
    for (int i = 0; i < arreglo.length; i++) 
      for (int j = 0; j < arreglo.length; j++) 
        if(i != j)
          if(arreglo[i].equals(arreglo[j]))
            arreglo[j] = "°";
    for(String cadena : arreglo)
      if(!cadena.equals("°"))
        arregloFinal.add(new Nodo(cadena.replace(" ", "")));
    return arregloFinal.toArray();
  }
  public String[] getGramatica() {
    return gramatica;
  }
  public void setGramatica(String[] gramatica) {
    this.gramatica = gramatica;
  }
  public String[] getLadoDerecho() {
    return ladoDerecho;
  }
  public void setLadoDerecho(String[] ladoDerecho) {
    this.ladoDerecho = ladoDerecho;
  }
  public String[] getNoTerminales() {
    return noTerminales;
  }
  public void setNoTerminales(String[] noTerminales) {
    this.noTerminales = noTerminales;
  }
  public String[] getTerminales() {
    return terminales;
  }
  public void setTerminales(String[] terminales) {
    this.terminales = terminales;
  }
  public Archivo getArchivo() {
    return archivo;
  }
  public void setArchivo(Archivo archivo) {
    this.archivo = archivo;
  }
}
