package analizadorsemantico;

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
    
    static String infixToPostfix(final String exp) {
        String result = "";
        final Stack<Character> stack = new Stack<Character>();
        for (int i = 0; i < exp.length(); ++i) {
            final char c = exp.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                result += c;
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result += stack.pop();
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
                    result += stack.pop();
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            if (stack.peek() == '(') {
                return "Invalid Expression";
            }
            result += stack.pop();
        }
        return result;
    }
    
    public static Pila toStack(final String exp) {
        final String result = infixToPostfix(exp);
        final Pila temp = new Pila();
        String cadena = "";
        final String[] arrayCaracteres = result.split("");
        for (int i = 0; i < arrayCaracteres.length; ++i) {
            if (Character.isLetter(arrayCaracteres[i].charAt(0))) {
                cadena += arrayCaracteres[i];
            }
            else if (Character.isDigit(arrayCaracteres[i].charAt(0))) {
                cadena += arrayCaracteres[i];
            }
        }
        return temp;
    }
    
    public static void main(final String[] args) {
        Pila p = new Pila();
        final String exp = "121*(Cuenta+12)";
        System.out.println(infixToPostfix(exp));
        p = toStack(exp);
        p.mostrarPila();
    }
}
