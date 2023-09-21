import java.util.ArrayList;
import java.util.Scanner;

import Lexico.AnalizadorLexico;
import Sintactico.AnalizadorSintactico;

public class Compilador {

    public static String programToString(ArrayList<ArrayList<Character>> p) {
        String out = "";
        int linea = 1;

        for (ArrayList<Character> l : p) {
            out += "[" + linea + "]: ";
            for (Character c : l) {
                out += c;
            }
            out += "\n";
            linea++;
        }

        return out;
    }

    public static void main(String[] args) {
        System.out.print("Ingrese el nombre del archivo: ");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        ArrayList<ArrayList<Character>> program = BinaryFileReader.read(input);

        if (program == null)
            return;

        AnalizadorLexico aLexico = new AnalizadorLexico(program);
        AnalizadorSintactico aSintactico = new AnalizadorSintactico();
        ArrayList<Error> errores = null;

        // while (!aLexico.hasFinishedTokenizer()) {
        // aSintactico.getNextToken(aLexico);
        // }

        System.out.println(programToString(program));

    }
}
