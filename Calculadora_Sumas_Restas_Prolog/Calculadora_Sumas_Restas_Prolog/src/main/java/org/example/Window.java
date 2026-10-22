package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class Window extends JFrame {
    private static final String[] EJEMPLOS = {
            "5 + 2", "10 - 3", "5 + 2 - 3 + 10", "(10 - 4) + 2", "-5 + 12"
    };

    private final TextField txtEntrada = new TextField("Ej: (10 - 4) + 2");
    private final PanelResult panelResultado = new PanelResult();
    private final JLabel lblEstado = new JLabel("Listo para calcular");

    public Window(Connection motor) {
        super("Calculadora de sumas y restas - Prolog");

        txtEntrada.addActionListener(e -> ejecutar(motor));

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        agregar(contenido, etiqueta("Operacion aritmetica"), 6);
        agregar(contenido, txtEntrada, 10);
        agregar(contenido, crearEjemplos(), 20);
        agregar(contenido, crearBotones(motor), 22);
        agregar(contenido, panelResultado, 12);
        agregar(contenido, crearPie(), 0);

        JPanel raiz = new JPanel(new BorderLayout(0, 20));
        raiz.setBackground(Theme.BG);
        raiz.setBorder(new EmptyBorder(24, 28, 24, 28));
        raiz.add(crearEncabezado(), BorderLayout.NORTH);
        raiz.add(contenido, BorderLayout.CENTER);

        setContentPane(raiz);
        setMinimumSize(new Dimension(680, 520));
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel crearEncabezado() {
        JLabel titulo = new JLabel("Calculadora aritmetica");
        titulo.setFont(Theme.sans(Font.BOLD, 25));
        titulo.setForeground(Theme.TEXT);

        JLabel subtitulo = new JLabel("Sumas y restas evaluadas con reglas en Prolog");
        subtitulo.setFont(Theme.sans(Font.PLAIN, 13));
        subtitulo.setForeground(Theme.MUTED);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.add(titulo);
        p.add(Box.createVerticalStrut(5));
        p.add(subtitulo);
        return p;
    }

    private JPanel crearEjemplos() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        p.setOpaque(false);

        JLabel lbl = new JLabel("Ejemplos:");
        lbl.setForeground(Theme.MUTED);
        lbl.setFont(Theme.sans(Font.PLAIN, 12));
        p.add(lbl);

        for (String ej : EJEMPLOS) {
            Buttons chip = new Buttons(ej, Theme.CARD, Theme.TEXT, 11, 6, 11);
            chip.addActionListener(e -> {
                txtEntrada.setText(ej);
                txtEntrada.requestFocusInWindow();
                txtEntrada.setCaretPosition(txtEntrada.getText().length());
            });
            p.add(chip);
        }
        return p;
    }

    private JPanel crearBotones(Connection motor) {
        Buttons btnCalcular = new Buttons("Calcular", Theme.BLUE, Theme.BG, 28, 11, 14);
        Buttons btnLimpiar = new Buttons("Limpiar", Theme.CARD, Theme.TEXT, 20, 11, 14);
        btnCalcular.addActionListener(e -> ejecutar(motor));
        btnLimpiar.addActionListener(e -> {
            txtEntrada.setText("");
            panelResultado.setTexto("");
            panelResultado.setTitulo("Resultado");
            estado("Listo para calcular", Theme.MUTED);
            txtEntrada.requestFocusInWindow();
        });

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        p.add(btnCalcular);
        p.add(btnLimpiar);
        return p;
    }

    private JPanel crearPie() {
        lblEstado.setFont(Theme.sans(Font.PLAIN, 12));
        lblEstado.setForeground(Theme.MUTED);

        Buttons btnCopiar = new Buttons("Copiar resultado", Theme.CARD, Theme.TEXT, 16, 7, 12);
        btnCopiar.addActionListener(e -> copiarResultado());

        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(lblEstado, BorderLayout.WEST);
        p.add(btnCopiar, BorderLayout.EAST);
        return p;
    }

    private void ejecutar(Connection motor) {
        String entrada = txtEntrada.getText().trim();
        if (entrada.isEmpty()) {
            panelResultado.setTexto("");
            estado("Escribe una operacion primero.", Theme.AMBER);
            return;
        }

        try {
            String resultado = motor.calcular(entrada);
            panelResultado.setTitulo("Resultado de: " + entrada);
            panelResultado.setTexto(resultado);
            estado("Operacion calculada correctamente", Theme.GREEN);
        } catch (Exception ex) {
            panelResultado.setTexto("");
            estado(ex.getMessage() == null ? "No se pudo calcular la operacion." : ex.getMessage(),
                    Theme.RED);
        }
    }

    private void copiarResultado() {
        String t = panelResultado.getTexto();
        if (t.isEmpty()) {
            estado("No hay resultado para copiar.", Theme.AMBER);
            return;
        }
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(t), null);
        estado("Resultado copiado al portapapeles", Theme.BLUE);
    }

    private void estado(String texto, Color color) {
        lblEstado.setText(texto);
        lblEstado.setForeground(color);
    }

    private static void agregar(JPanel panel, JComponent c, int espacioDespues) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(c);
        if (espacioDespues > 0) {
            panel.add(Box.createVerticalStrut(espacioDespues));
        }
    }

    private static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Theme.sans(Font.BOLD, 12));
        l.setForeground(Theme.MUTED);
        return l;
    }
}
