package estructurasDeDatos;

public class Nodo <T> {
  private T info;
  private Nodo<T> siguiente;
  
  public Nodo(T x){
    info = x;
    siguiente = null;
  }
  
  public Nodo getSiguiente(){
    return siguiente;
  }
  
  public void setSiguiente(Nodo n){
    siguiente = n;
  }
  
  public T getInfo(){
    return info;
  }
  
  public void setInfo(T i){
    info = i;
  }
  
}
