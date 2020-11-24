package datos;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
public class Archivo {
  File archivo;
  JFileChooser chooser;
  FileReader fr;
  BufferedReader br;
  String f;
  public Archivo() {
    chooser = new JFileChooser();
  }
  private String seleccionaArchivo() {
    chooser.setFileFilter(new FileNameExtensionFilter(".txt", "txt"));
    chooser.setDialogTitle("Selecciona un archivo.");
    if (chooser.showDialog(null, "Ok") == JFileChooser.CANCEL_OPTION)
      return null;
    archivo = chooser.getSelectedFile();
    return archivo.getAbsolutePath();
  }
  public void abrirArchivo(){
    try{
      fr = new FileReader (seleccionaArchivo());
      br = new BufferedReader(fr);
      // Ponemos un apuntador en el inicio del archivo
      br.mark(0); 
    } catch(FileNotFoundException e){
      System.out.println("Error al abrir el archivo");
    } catch(IOException e2){
      System.out.println("Error al inicializar el apuntador");
    }
  }
  public String leerArchivo() {
    String cadena = "", linea;
    try {
      f = seleccionaArchivo();
      fr = new FileReader (f);
      br = new BufferedReader(fr);
      while ((linea=br.readLine())!=null) {
        cadena = cadena + linea + " & ";
      }
      }catch (IOException e) {
        e.printStackTrace();
      }finally {
        try{
          if( null != fr )
            fr.close();               
        }catch (Exception e2){ 
          e2.printStackTrace();
        }
      }
    return cadena;
  }
  public void cerrarArchivo(){
    try{
    fr.close();
    } catch(IOException e){
      System.out.println("Error al cerrar el archivo");
    }
  }
  /**
   * Lee la línea del archivo y mueve el apuntador de línea.
   * 
   * @return String línea donde se encuentra el apuntador. Retorna null en caso
   *          de haber llegado al final del archivo.
   */
  public String leerLineaSiguiente(){  
    try {
      return br.readLine();
    }catch (IOException e) {
      System.out.println("Error al leer la siguiente línea del archivo");
    }
    return null;
  }
  /**
   * Mueve el apuntador de línea al inicio del archivo.
   */
  public void reiniciarApuntadorDeLinea(){
    try{
      br.reset();
    }catch(IOException e){
      System.out.println("Error al mover el apuntador de línea.");
    }
  }
  public String getRutaArchivo(){
    return f;
  }
}
