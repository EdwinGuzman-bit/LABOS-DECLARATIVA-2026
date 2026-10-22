package org.example;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;

import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * Conecta la interfaz Java con las reglas de calculo escritas en Prolog.
 */
public class Connection {
    private final Prolog engine = new Prolog();

    // Lista blanca: evita enviar al motor caracteres que no sean de una operacion aritmetica permitida.
    private static final Pattern EXPRESION_PERMITIDA =
            Pattern.compile("[0-9+().\\s-]+");

    public Connection() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/calculo.pl")) {
            if (in == null) {
                throw new IllegalStateException(
                        "No se encontro calculo.pl en src/main/resources.");
            }
            engine.setTheory(new Theory(in));
        }
    }

    public String calcular(String expresion) throws Exception {
        if (expresion == null || expresion.isBlank()) {
            throw new IllegalArgumentException("Escribe una operacion primero.");
        }

        String limpia = expresion
                .replace('−', '-')
                .replace(',', '.')
                .replaceAll("\\s+", "");

        if (!EXPRESION_PERMITIDA.matcher(limpia).matches()) {
            throw new IllegalArgumentException(
                    "Usa solamente numeros, +, -, parentesis y decimales.");
        }

        // Evita expresiones incompletas antes de enviarlas a Prolog.
        if (limpia.matches(".*[+.-]$") || limpia.contains("..")
                || limpia.contains("()") || limpia.contains("++")
                || limpia.contains("--") && !limpia.startsWith("--")) {
            // Los signos dobles pueden tener significado unario, por eso el analizador
            // de Prolog tambien valida la estructura completa.
            if (limpia.matches(".*[+.]$") || limpia.contains("..") || limpia.contains("()")) {
                throw new IllegalArgumentException("La operacion esta incompleta o tiene un formato incorrecto.");
            }
        }

        SolveInfo info = engine.solve("evaluar((" + limpia + "), R).");
        if (!info.isSuccess()) {
            throw new IllegalArgumentException("No se pudo interpretar la operacion. Revisa los parentesis y los signos.");
        }

        String resultado = info.getTerm("R").toString();
        return resultado;
    }
}
