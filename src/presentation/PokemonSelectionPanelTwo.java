package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class PokemonSelectionPanelTwo extends JPanel {
    private PokemonGUI pokemonGUI;
    private Map<String, JCheckBox> pokemonCheckboxes;
    private JButton btnRegresar, btnSiguiente;
    private BufferedImage imagenFondo;
    private JLabel contadorLabel;
    private int pokemonesSeleccionados;
    private final int MAX_POKEMONES = 6;

    public PokemonSelectionPanelTwo(PokemonGUI pokemonGUI) {
        this.pokemonGUI = pokemonGUI;
        this.pokemonCheckboxes = new HashMap<>();
        this.pokemonesSeleccionados = 0;
        prepareElementos();
        prepareAcciones();
    }

    private void prepareElementos() {
        setLayout(new BorderLayout());
        
        try {
            imagenFondo = ImageIO.read(getClass().getResource("/modoMenu.png"));
        } catch (IOException e) {
            imagenFondo = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = imagenFondo.createGraphics();
            g2d.setColor(new Color(50, 50, 70));
            g2d.fillRect(0, 0, 800, 600);
            g2d.dispose();
        }

        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelTitulo = new JPanel();
        panelTitulo.setOpaque(false);
        JLabel titulo = new JLabel("SELECCIÓN DE POKÉMON - PLAYER 2", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        panelTitulo.add(titulo);
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);

        JPanel panelPokemon = new JPanel(new GridLayout(0, 3, 20, 20));
        panelPokemon.setOpaque(false);
        
        List<String> pokemonNames = pokemonGUI.getPokemonNames();
        for (String name : pokemonNames) {
            JCheckBox checkBox = new JCheckBox(name);
            checkBox.setOpaque(false);
            checkBox.setForeground(Color.WHITE);
            checkBox.setFont(new Font("Arial", Font.BOLD, 14));
            pokemonCheckboxes.put(name, checkBox);
            panelPokemon.add(checkBox);
        }

        JScrollPane scrollPokemon = new JScrollPane(panelPokemon);
        scrollPokemon.setOpaque(false);
        scrollPokemon.getViewport().setOpaque(false);
        scrollPokemon.setBorder(BorderFactory.createEmptyBorder());

        contadorLabel = new JLabel("Pokémon seleccionados: 0/6", SwingConstants.CENTER);
        contadorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contadorLabel.setForeground(Color.WHITE);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);
        btnRegresar = new JButton("Regresar");
        btnSiguiente = new JButton("Siguiente");
        
        for (JButton btn : new JButton[]{btnRegresar, btnSiguiente}) {
            btn.setPreferredSize(new Dimension(120, 40));
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setBackground(new Color(70, 70, 90));
            btn.setForeground(Color.WHITE);
        }
        
        panelBotones.add(btnRegresar);
        panelBotones.add(btnSiguiente);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.add(contadorLabel, BorderLayout.NORTH);
        panelSur.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.add(scrollPokemon, BorderLayout.CENTER);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void prepareAcciones() {
        ItemListener checkboxListener = e -> {
            JCheckBox source = (JCheckBox) e.getSource();
            if (source.isSelected()) {
                if (pokemonesSeleccionados < MAX_POKEMONES) {
                    pokemonesSeleccionados++;
                } else {
                    source.setSelected(false);
                }
            } else {
                pokemonesSeleccionados--;
            }
            contadorLabel.setText("Pokémon seleccionados: " + pokemonesSeleccionados + "/6");
            btnSiguiente.setEnabled(pokemonesSeleccionados > 0);
        };

        for (JCheckBox cb : pokemonCheckboxes.values()) {
            cb.addItemListener(checkboxListener);
        }

        btnRegresar.addActionListener(e -> {
            pokemonGUI.mostrarMovimientoSelectionPanel(null);
        });

        btnSiguiente.addActionListener(e -> {
            List<String> seleccionados = new ArrayList<>();
            for (Map.Entry<String, JCheckBox> entry : pokemonCheckboxes.entrySet()) {
                if (entry.getValue().isSelected()) {
                    seleccionados.add(entry.getKey());
                }
            }
            pokemonGUI.setPokemonSeleccionadosPlayer2(seleccionados);
            pokemonGUI.mostrarMovimientoSelectionPanelTwo(null);
        });
    }
}