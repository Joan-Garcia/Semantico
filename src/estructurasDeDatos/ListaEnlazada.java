package estructurasDeDatos;

public class ListaEnlazada {
  private Nodo inicio, fin;
  
  public ListaEnlazada(){
    inicio = fin = null;
  }
  
  public void add(Nodo n){
    if (isEmpty())
      fin = inicio = n;
    else {
      fin.setSiguiente(n);
      fin = n;
      fin.setSiguiente(null);
    }
  }
  
  public void print(){
    Nodo temp = inicio;
    
    while(temp != null){
      System.out.print(temp.getInfo() + "\t");
      temp = temp.getSiguiente();
    }
    System.out.println();
  }
  
  public boolean isEmpty(){
    return inicio == null;
  }
  
  public void removeFirst(){
    if (!isEmpty())
      inicio = inicio.getSiguiente();
    else
      System.out.println("no nodes to remove");
  }
  
  public void removeLast(){
    Nodo temp = inicio, rear = null;
    
    if (!isEmpty()){
      while(temp.getSiguiente() != null){
        rear = temp;
        temp = temp.getSiguiente();
      }
      if(temp == inicio)
        removeFirst();
      else {
        fin = rear;
        fin.setSiguiente(null);
      }
    } else System.out.println("no nodes to remove!");   
  }
  
  public Nodo first(){
    return inicio;
  }
  
  public Nodo last(){
    return fin;
  }
  
  public Nodo get(int indice){
    int c;
    
    c = 0;
    
    for(Nodo temp = inicio; temp !=null ; temp = temp.getSiguiente()){
      if (c == indice)
        return temp;
      c++;
    }
    
    return null;
  }
  
  //retorna un entero que representa el número de nodos en la lista.
  public int size(){
    Nodo temp = inicio;
    int size = 0;
    
    while(temp != null){
      size++;
      temp = temp.getSiguiente();
    }
    return size;
  }
  
  public boolean exist(Nodo a){
    for(Nodo temp = inicio; temp !=null ; temp = temp.getSiguiente())
      if (a.getInfo().equals(temp.getInfo()))
        return true;
    return false;
  }
  
  /**
   * Convierte los datos de la lista en un arreglo de String
   * 
   * @return String[]     array de la información de los nodos de la lista.
   */
  public String[] toArray(){
    Nodo temp = inicio;
    String[] arreglo;
    
    arreglo = new String[this.size()];
    
    for (int i = 0; i < this.size(); i++) {
      arreglo[i] = (String) temp.getInfo();
      temp = temp.getSiguiente();
    }
    
    return arreglo;
  }
  
}
