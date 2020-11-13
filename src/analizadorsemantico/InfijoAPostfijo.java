package analizadorsemantico;

import estructurasDeDatos.Nodo;
import estructurasDeDatos.Pila;
import java.util.Stack;

class InfijoAPostfijo {
    
    static int Prec(final char ch) {
        switch (ch) {
            case '+':
            case '-': {
                return 1;
            }
            case '*':
            case '/': {
                return 2;
            }
            case '^': {
                return 3;
            }
            default: {
                return -1;
            }
        }
    }
    
    public static String infixToPostfix(final String exp) {
        String result = "";
        String tempresult = "";       //Para guardar números de varios dígitos
        final Stack<Character> stack = new Stack<Character>();
        for (int i = 0; i < exp.length(); ++i) {
            final char c = exp.charAt(i);
            if (Character.isLetterOrDigit(c) || c == '.') { //Guardar los números hasta que no encuentre
                tempresult += c;
                if(!Character.isLetterOrDigit(exp.charAt(i + 1)) && exp.charAt(i + 1) != '.'){
                    result += tempresult + " ";
                    tempresult = "";
                }
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result += stack.pop() + " ";
                }
                if (!stack.isEmpty() && stack.peek() != '(') {
                    return "Expresi\u00f3n inv\u00e1lida";
                }
                stack.pop();
            } else {
                while (!stack.isEmpty() && Prec(c) <= Prec(stack.peek())) {
                    if (stack.peek() == '(') {
                        return "Invalid Expression";
                    }
                    result += stack.pop() + " ";
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            if (stack.peek() == '(') {
                return "Invalid Expression";
            }
            result += stack.pop()+ " ";
        }
        return result;
    }
    
    public static Pila toStack(final String exp, final String variable) {
        final String result = infixToPostfix(exp);
        final Pila temp = new Pila();
        
        temp.push(new Nodo(variable));
        temp.push(new Nodo("="));
        
        final String[] arrayExpresiones = result.split(" ");
        
//        for (String expresion : arrayExpresiones)
//            temp.push(new Nodo(expresion));
        
        /*Si el orden de entrada a la pila es inverso:
        */
        
        for (int i = arrayExpresiones.length; i > 0 ;i--)
            temp.push(new Nodo(arrayExpresiones[i-1]));
        
        return temp;
    }
    
    public static void main(final String[] args) {
        final String exp = "3+12.5 ";
        System.out.println(infixToPostfix(exp));
        toStack(exp, "Num_1").mostrarPila();
    }
}
