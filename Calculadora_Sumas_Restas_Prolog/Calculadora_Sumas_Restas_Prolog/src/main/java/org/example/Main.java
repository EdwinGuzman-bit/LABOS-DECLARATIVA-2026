package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection motor = new Connection();
                new Window(motor).setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "No se pudo iniciar la calculadora. Verifica que Maven haya descargado tuProlog.\n"
                                + ex.getMessage(),
                        "Error al iniciar",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
