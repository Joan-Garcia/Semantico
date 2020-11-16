package datos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EscribeEnArchivo {
  File f;
  FileWriter writer;
  
  public void crearArchivo(String name){
    f = new File("src/salidas/"+name+".txt");
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
  
  public void escribeLinea(String file, String linea){
      try {          
        f = new File("src/salidas/"+file+".txt");
        writer = new FileWriter(f, true);
        writer.write(linea + "\r\n");
        writer.close();
      } catch (Exception e) {
        System.out.println(">>>Error en la escritura del Archivo.");
      }
  }
}
