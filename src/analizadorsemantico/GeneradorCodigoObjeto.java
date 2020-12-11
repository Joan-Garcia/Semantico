package analizadorsemantico;

import datos.EscribeEnArchivo;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneradorCodigoObjeto {
  private ArrayList<String> lineas, variables, variablesFinales;
  private File cuartetosOptimizado;
  private Scanner sc;
  private EscribeEnArchivo archivoSalida;
  private final static String inicialesVar =    "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ",
                                codeHeader =    ".Const\n"
                                                + "\n"
                                                + ".Data?\n"
                                                + "\n"
                                                + ".Data\n"
                                                + "\tmessage DB \"Incio de ejecucion...\", 0\n"
                                                + "\tmessage2 DB \"Fin de ejecucion...\", 0\n"
                                                + "\tsalto DB 10, 13, 0	\n"
                                                + "\tinput DB 10 Dup(0)\n",
                                codeBody =      ".Code\n"
                                                + "\n"
                                                + "start:\n"
                                                + "\tInvoke StdOut, Addr message\n"
                                                + "\tInvoke StdOut, Addr salto\n",
                                codeFooter =    "\tInvoke StdOut, Addr message2\n"
                                                + "\tInvoke StdIn, Addr input, 10\n"
                                                + "\nEnd start";
  public GeneradorCodigoObjeto() {
    lineas = new ArrayList<>();
    variables = new ArrayList<>();
    variablesFinales = new ArrayList<>();
    archivoSalida = new EscribeEnArchivo();
    archivoSalida.crearArchivo("CodigoObjeto", ".asm");
  }
  private void leerCuartetosOptimizado(){
    try{
      cuartetosOptimizado = new File("src/salidas/CuartetosOptimizado.txt");
      sc = new Scanner(cuartetosOptimizado);
      while(sc.hasNext())
        lineas.add(sc.nextLine());                                              //Llenado del arraylist de las líneas.
    }catch(FileNotFoundException e){
      System.out.println(">>>Error abriendo archivo Cuartetos.txt");
    }
  }
  private void setVariables() {
    for (String linea : lineas) {
      String[] val = linea.toString().split(" ");
      for (int i = 0; i < val.length; i++) {
        if (inicialesVar.contains(Character.toString(val[i].charAt(0)).toUpperCase())) {
          if (variables.isEmpty()) {
            variables.add(val[i]);
          } else {
              if (!variables.contains(val[i]))
                variables.add(val[i]);
          }
        }
      }
    }
  }
  private void setVariablesFinales() {
    for (int i = 0; i < variables.size(); i++)
      if (!"T".contains(Character.toString(variables.get(i).toString().toUpperCase().charAt(0))))
        variablesFinales.add(variables.get(i));
  }
  private String reservaMemoria() {
    String code = "";
    for (int i = 0; i < variables.size(); i++)
      code = code + ("\t" + variables.get(i) + " DWord 0\n");
    for (int i = 0; i < variablesFinales.size(); i++)
      code = code + ("\tvarString" + (i + 1) + " DWord 0\n");
    return code;
  }
  private String realizaOperaciones() {
    String code = "";
    for (String linea : lineas){
      String[] val = linea.toString().split(" ");
      code = code + ("\tMov Eax, 0\n\tMov Eax, " + val[1] + "\n");
      if ("+".equals(val[0])){
        code = code + ("\tAdd Eax, " + val[2] + "\n");
      } else if ("-".equals(val[0])){
        code = code + ("\tSub Eax, " + val[2] + "\n");
      } else {
        code = code + ("\tMov Edx, 0\n\tMov Edx, " + val[2] + "\n\tMul Edx\n");
      }
      code = code + ("\tMov " + val[3] + ", Eax\n\n");
    }
    return code;
  }
  private String imprimeResultados() {
    String code = "";
    for (int i = 0; i < variablesFinales.size(); i++){
      code = code + ("\tInvoke dwtoa, " + variablesFinales.get(i) + ", Addr varString" + (i + 1) + "\n")
                  + "\tInvoke StdOut, Addr varString" + (i + 1) + "\n"
                  + "\tInvoke StdOut, Addr salto\n";
    }
    return code;
  }
  public void generaCodigoObjeto() {
      leerCuartetosOptimizado();
      setVariables();
      setVariablesFinales();
      String step2 = reservaMemoria(),
             step4 = realizaOperaciones(),
             step5 = imprimeResultados();
      System.out.print(codeHeader);
      System.out.println(step2);
      System.out.print(codeBody);
      System.out.println(step4);
      System.out.println(step5);
      System.out.println(codeFooter);
      archivoSalida.escribeLinea("CodigoObjeto", ".asm",  codeHeader
                                                      + step2
                                                      + codeBody
                                                      + step4
                                                      + step5
                                                      + codeFooter);
  }
    public static void main(String[] args) {
      new GeneradorCodigoObjeto().generaCodigoObjeto();
    }
}
