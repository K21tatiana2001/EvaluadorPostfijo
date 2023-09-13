/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad Ean (Bogotá - Colombia)
 * Departamento de Tecnologías de la Información y Comunicaciones
 * Licenciado bajo el esquema Academic Free License version 2.1
 * <p>
 * Proyecto Evaluador de Expresiones Postfijas
 * Fecha: Febrero 2021
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package universidadean.desarrollosw.postfijo;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.*;

/**
 * Esta clase representa una clase que evalúa expresiones en notación polaca o
 * postfija. Por ejemplo: 4 5 +
 */
public class EvaluadorPostfijo {

    /**
     * Permite saber si la expresión en la lista está balanceada
     * o no. Cada elemento de la lista es un elemento. DEBE OBlIGATORIAMENTE
     * USARSE EL ALGORITMO QUE ESTÁ EN EL ENUNCIADO.
     */
    static boolean estaBalanceada(List<String> expresion) {
        Stack<String> delimitadores = new Stack<>();

        // TODO: Escriba el algoritmo del enunciado aquí

        for (String elemento : expresion) {
            if (elemento.equals("(") || elemento.equals("[") || elemento.equals("{")) {
                delimitadores.push(elemento);
            } else if (elemento.equals(")") || elemento.equals("]") || elemento.equals("}")) {
                if (delimitadores.isEmpty()) {
                    return false;
                } else {
                    String apertura = delimitadores.pop();
                    if (!((apertura.equals("(") && elemento.equals(")")) ||
                            (apertura.equals("[") && elemento.equals("]")) ||
                            (apertura.equals("{") && elemento.equals("}")))) {
                        return false;
                    }
                }
            }
        }

        return delimitadores.isEmpty();
    }

    /**
     * Transforma la expresión, cambiando los símbolos de agrupación
     * de corchetes ([]) y llaves ({}) por paréntesis ()
     */
    static void reemplazarDelimitadores(List<String> expresion) {
        // TODO: Escriba el algoritmo aquí
        for (int i = 0; i < expresion.size(); i++) {
            String elemento = expresion.get(i);
            if (elemento.equals("[")) {
                expresion.set(i, "(");
            } else if (elemento.equals("]")) {
                expresion.set(i, ")");
            } else if (elemento.equals("{")) {
                expresion.set(i, "(");
            } else if (elemento.equals("}")) {
                expresion.set(i, ")");
            }
        }
    }

    /**
     * Realiza la conversión de la notación infija a postfija
     * @return la expresión convertida a postfija
     * OJO: Debe usarse el algoritmo que está en el enunciado OBLIGATORIAMENTE
     */
    static List<String> convertirAPostfijo(List<String> expresion) {
        Stack<String> pila = new Stack<>();
        List<String> salida = new ArrayList<>();

        // TODO: Escriba el algoritmo aquí

        Map<String, Integer> prioridades = new HashMap<>();
        prioridades.put("+", 1);
        prioridades.put("-", 1);
        prioridades.put("*", 2);
        prioridades.put("/", 2);
        prioridades.put("%", 2);
        prioridades.put("^", 3);

        for (String elemento : expresion) {
            if (esOperando(elemento)) {
                salida.add(elemento);
            } else if (esOperador(elemento)) {
                while (!pila.isEmpty() && prioridad(pila.peek()) >= prioridad(elemento)) {
                    salida.add(pila.pop());
                }
                pila.push(elemento);
            } else if (elemento.equals("(")) {
                pila.push(elemento);
            } else if (elemento.equals(")")) {
                while (!pila.isEmpty() && !pila.peek().equals("(")) {
                    salida.add(pila.pop());
                }
                pila.pop();
            }
        }

        while (!pila.isEmpty()) {
            salida.add(pila.pop());
        }

        return salida;
    }

    /**
     * Realiza la evaluación de la expresión postfijo utilizando una pila
     * @param expresion una lista de elementos con números u operadores
     * @return el resultado de la evaluación de la expresión.
     */
    static int evaluarPostFija(List<String> expresion) {
        Stack<Integer> pila = new Stack<>();

        // TODO: Realiza la evaluación de la expresión en formato postfijo

        for (String elemento : expresion) {
            if (esOperando(elemento)) {
                pila.push(Integer.parseInt(elemento));
            } else if (esOperador(elemento)) {
                int operand2 = pila.pop();
                int operand1 = pila.pop();
                int resultado = aplicarOperador(elemento, operand1, operand2);
                pila.push(resultado);
            }
        }

        return pila.pop();
    }
    static boolean esOperando(String elemento) {
        return elemento.matches("\\d+");
    }

    static boolean esOperador(String elemento) {
        return elemento.matches("[+\\-*/%^]");
    }

    static int prioridad(String operador) {
        Map<String, Integer> prioridades = new HashMap<>();
        prioridades.put("+", 1);
        prioridades.put("-", 1);
        prioridades.put("*", 2);
        prioridades.put("/", 2);
        prioridades.put("%", 2);
        prioridades.put("^", 3);

        return prioridades.getOrDefault(operador, 0);
    }
    static int aplicarOperador(String operador, int operand1, int operand2) {
        switch (operador) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                if (operand2 != 0) {
                    return operand1 / operand2;
                } else {
                    throw new ArithmeticException("División por cero");
                }
            case "%":
                if (operand2 != 0) {
                    return operand1 % operand2;
                } else {
                    throw new ArithmeticException("Módulo por cero");
                }
            case "^":
                return (int) Math.pow(operand1, operand2);
            default:
                throw new IllegalArgumentException("Operador no válido: " + operador);
        }
    }

    public static void main(String[] args) {
        String expresion = "((4 + 3) * 2)";
        List<String> expresionList = Arrays.asList(expresion.split("\\s+"));

        System.out.println("Expresión balanceada: " + estaBalanceada(expresionList));

        reemplazarDelimitadores(expresionList);
        System.out.println("Expresión con delimitadores reemplazados: " + expresionList);

        List<String> postfija = convertirAPostfijo(expresionList);
        System.out.println("Expresión en notación postfija: " + postfija);

        int resultado = evaluarPostFija(postfija);
        System.out.println("Resultado de la evaluación: " + resultado);
    }
}


