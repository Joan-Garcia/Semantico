package estructurasDeDatos;

/**
 * Pila de nodos que almacenan como información < String >
 * 
 */
public class Pila{
    private Nodo tope, fondo;
    
    public Pila(){
      tope = fondo = null;
    }
    
    public void push(Nodo n){
      Nodo temp;
      
      if(!esVacia()){
        temp = tope;
        tope = n;
        tope.setSiguiente(temp);
      } else {
        tope = fondo = n;
      }
    }
    
    public void pop(){
      Nodo temp;
      
      if(!esVacia()){
        if(tope == fondo){
          tope = null;
          fondo = null;
        } else {
          temp = tope;
          tope = temp.getSiguiente();
          temp.setSiguiente(null);
        }
      }
    }
    
    public void cicloPush(String[] a){
//      for(String info : a)
//        push(new Nodo(info));
      for(int i = a.length-1; i>=0; i--)
        if(a[i] != " " && a[i].length() > 0)
          push(new Nodo(a[i]));
    }
    
    public void mostrarPila(){
      if(!esVacia()){
        if(tope == fondo){
          System.out.println("[" + tope.getInfo() + "]\n");
        } else {
        for(Nodo temp = tope; temp != null; temp = temp.getSiguiente())
          System.out.print("[" + temp.getInfo() + "]");
          System.out.print("\n");
        }
      }
    }
    
    public boolean esVacia(){
      return tope == null && fondo == null;
    }
    
    public Nodo getTope(){
      return tope;
    }
    
    public Nodo getFondo(){
      return fondo;
    }
}
