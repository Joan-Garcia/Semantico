package datos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EscribeEnArchivo {
  File f;
  FileWriter writer;
  
  public void crearArchivo(){
    f = new File("src/salidas/Arbol.txt");
    try {
      if (f.exists()){
        f.delete();
        f.createNewFile();
      }else
        f.createNewFile();
    } catch (IOException e) {
      System.out.println(">>>Error en la creación del Archivo.");
    }
  }
  
  public void escribeLinea(String linea){
      try {          
        f = new File("src/salidas/Arbol.txt");
        writer = new FileWriter(f, true);
        writer.write(linea + "\r\n");
        writer.close();
      } catch (Exception e) {
        System.out.println(">>>Error en la escritura del Archivo.");
      }
  }
}
