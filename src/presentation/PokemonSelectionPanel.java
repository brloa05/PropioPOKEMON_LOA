package presentation;

import domain.Especial;
import domain.Fisico;
import domain.Movimiento;
import domain.Pokemon;
import domain.Pokemon.Tipo;
import domain.PokemonAplication;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

public class PokemonSelectionPanel extends JPanel {
    private PokemonGUI pokemonGUI;
    private JLabel lblPokemonImage;
    private JTextArea txtStats;
    private JComboBox<String> cbTypes;
    private JComboBox<String> cbPokemons;
    private JButton btnAddPokemon;
    private JButton btnRemovePokemon;
    private JButton btnConfirmAndNext;
    private JButton btnReset;
    private JButton btnBack;
    private JList<String> selectedPokemonsList;
    private DefaultListModel<String> listModel;
    
    private Map<String, Pokemon> pokemonMap = new HashMap<>();
    private Map<Tipo, Integer> typeCount = new EnumMap<>(Tipo.class);
    private List<Pokemon> selectedPokemons = new ArrayList<>();
    
    private static final int MAX_POKEMONS = 6;
    private static final int MAX_SAME_TYPE = 2;

    private static final Map<String, String> POKEMON_IMAGE_MAP = createPokemonImageMap();
    
    private static Map<String, String> createPokemonImageMap() {
        Map<String, String> map = new HashMap<>();
        // Pokémon de un tipo
        map.put("BLASTOISE", "/blastoise-Objetivo.gif");
        map.put("SNORLAX", "/snorlax-Objetivo.gif");
        map.put("DONPHAN", "/donphan-Objetivo.gif");
        map.put("MACHAMP", "/machamp-Objetivo.gif");
        map.put("RAICHU", "/raichu-Objetivo.gif");
        
        // Pokémon de dos tipos
        map.put("CHARIZARD", "/charizard-Objetivo.gif");
        map.put("VENUSAUR", "/venusaur-Objetivo.gif");
        map.put("GENGAR", "/gengar-Objetivo.gif");
        map.put("DRAGONITE", "/dragonite-Objetivo.gif");
        map.put("TOGETIC", "/togetic-Objetivo.gif");
        map.put("TYRANITAR", "/tyranitar-Objetivo.gif");
        map.put("GARDEVOIR", "/gardevoir-Objetivo.gif");
        map.put("METAGROSS", "/metagross-Objetivo.gif");
        map.put("DELIBIRD", "/delibird-Objetivo.gif");
        
        return map;
    }

    public PokemonSelectionPanel(PokemonGUI pokemonGUI) {
        this.pokemonGUI = pokemonGUI;
        prepareElements();
        prepareActions();
        showPokemonDetails(null);
    }
    
    private void prepareElements() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel selectionPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel leftPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        
        cbTypes = new JComboBox<>();
        cbPokemons = new JComboBox<>();
        btnAddPokemon = new JButton("Añadir Pokémon");
        btnRemovePokemon = new JButton("Quitar Seleccionado");
        
        leftPanel.add(new JLabel("Tipo de Pokémon:"));
        leftPanel.add(cbTypes);
        leftPanel.add(new JLabel("Pokémon disponible:"));
        leftPanel.add(cbPokemons);

        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        rightPanel.add(btnAddPokemon);
        rightPanel.add(btnRemovePokemon);

        selectionPanel.add(leftPanel);
        selectionPanel.add(rightPanel);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        lblPokemonImage = new JLabel("Selecciona un Pokémon", SwingConstants.CENTER);
        lblPokemonImage.setPreferredSize(new Dimension(250, 250));
        lblPokemonImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        txtStats = new JTextArea("Selecciona un Pokémon para ver sus estadísticas");
        txtStats.setEditable(false);
        txtStats.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtStats.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        
        centerPanel.add(lblPokemonImage, BorderLayout.CENTER);
        centerPanel.add(new JScrollPane(txtStats), BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        
        listModel = new DefaultListModel<>();
        selectedPokemonsList = new JList<>(listModel);
        selectedPokemonsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Panel para los botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        btnBack = new JButton("Regresar");
        btnReset = new JButton("Resetear");  // Nuevo botón
        btnConfirmAndNext = new JButton("Confirmar y Siguiente (" + selectedPokemons.size() + "/" + MAX_POKEMONS + ")"); // Botón combinado
        
        btnConfirmAndNext.setEnabled(false);
        
        buttonPanel.add(btnBack);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnConfirmAndNext);
        
        bottomPanel.add(new JScrollPane(selectedPokemonsList), BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(selectionPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadPokemonTypes();
    }

    private void prepareActions() {
        cbTypes.addActionListener(e -> {
            if (cbTypes.getSelectedItem() != null) {
                loadPokemonsByType(Tipo.valueOf(((String) cbTypes.getSelectedItem()).toUpperCase()));
            }
        });

        cbPokemons.addActionListener(e -> {
            String pokemonName = (String) cbPokemons.getSelectedItem();
            if (pokemonName != null && !pokemonName.isEmpty()) {
                showPokemonDetails(pokemonMap.get(pokemonName));
            } else {
                showPokemonDetails(null);
            }
        });

        btnAddPokemon.addActionListener(e -> addSelectedPokemon());
        btnRemovePokemon.addActionListener(e -> removeSelectedPokemon());
        
        btnBack.addActionListener(e -> {
            if (!selectedPokemons.isEmpty()) {
                int response = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro que deseas regresar? Se perderán todas las selecciones.",
                    "Confirmar regreso",
                    JOptionPane.YES_NO_OPTION);
                
                if (response == JOptionPane.YES_OPTION) {
                    resetSelectedPokemons();
                    pokemonGUI.mostrarMenuItemsPvMPanel(); // Nueva función que debes crear en PokemonGUI
                }
            } else {
                pokemonGUI.mostrarMenuItems();
            }
        });

        btnReset.addActionListener(e -> {
            if (!selectedPokemons.isEmpty()) {
                int response = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro que deseas resetear todos los Pokémon seleccionados?",
                    "Confirmar reseteo",
                    JOptionPane.YES_NO_OPTION);
                
                if (response == JOptionPane.YES_OPTION) {
                    resetSelectedPokemons();
                }
            }
        });
        
        btnConfirmAndNext.addActionListener(e -> {
            if (selectedPokemons.size() < MAX_POKEMONS) {
                showError("Debes seleccionar exactamente " + MAX_POKEMONS + " Pokémon");
                return;
            }
            
            if (JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de tu selección de Pokémon?",
                "Confirmar selección",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                
                pokemonGUI.setSelectedPokemons(new ArrayList<>(selectedPokemons));
                pokemonGUI.displayCurrentPlayerData();
                pokemonGUI.mostrarMovimientoSelectionPanel(selectedPokemons);
            }
        });
    }
    
    private void resetSelectedPokemons() {
        selectedPokemons.clear();
        listModel.clear();
        typeCount.clear();
        loadPokemonTypes();
        updateConfirmAndNextButton();
    }
    
    private void loadPokemonTypes() {
        cbTypes.removeAllItems();
        for (Tipo tipo : Tipo.values()) {
            cbTypes.addItem(tipo.toString());
        }
    }

    private void loadPokemonsByType(Tipo tipo) {
        cbPokemons.removeAllItems();
        pokemonMap.clear();
        
        List<Pokemon> pokemons = pokemonGUI.getPokemonAplication().getPokemonesPorTipo(tipo);
        
        for (Pokemon pokemon : pokemons) {
            if (!selectedPokemons.contains(pokemon)) {
                String pokemonName = pokemon.getNombre();
                cbPokemons.addItem(pokemonName);
                pokemonMap.put(pokemonName, pokemon);
            }
        }
        
        if (cbPokemons.getItemCount() > 0) {
            cbPokemons.setSelectedIndex(0);
        } else {
            showPokemonDetails(null);
        }
    }

    private ImageIcon loadPokemonImage(String pokemonName) {
        if (pokemonName == null || pokemonName.isEmpty()) {
            return null;
        }

        String imagePath = POKEMON_IMAGE_MAP.get(pokemonName.toUpperCase());
        if (imagePath == null) {
            System.err.println("No se encontró ruta de imagen para: " + pokemonName);
            return null;
        }

        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                throw new FileNotFoundException("No se encontró el recurso: " + imagePath);
            }
            
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            
            if (originalIcon.getIconWidth() > 0 && originalIcon.getIconHeight() > 0) {
                double scale = Math.min(250.0 / originalIcon.getIconWidth(), 250.0 / originalIcon.getIconHeight());
                int width = (int)(originalIcon.getIconWidth() * scale);
                int height = (int)(originalIcon.getIconHeight() * scale);
                
                return new ImageIcon(originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
            }
            return originalIcon;
        } catch (Exception e) {
            System.err.println("Error cargando GIF animado para " + pokemonName + ": " + e.getMessage());

            try {
                InputStream is = getClass().getResourceAsStream(imagePath);
                if (is != null) {
                    BufferedImage image = ImageIO.read(is);
                    is.close();
                    if (image != null) {
                        return new ImageIcon(image.getScaledInstance(250, 250, Image.SCALE_SMOOTH));
                    }
                }
            } catch (IOException ioEx) {
                System.err.println("Error en fallback estático: " + ioEx.getMessage());
            }
        }
        
        return null;
    }

    private void showPokemonDetails(Pokemon pokemon) {
        if (pokemon == null) {
            lblPokemonImage.setIcon(null);
            lblPokemonImage.setText("Selecciona un Pokémon");
            txtStats.setText("Selecciona un Pokémon para ver sus estadísticas");
            return;
        }

        lblPokemonImage.setIcon(null);
        
        ImageIcon icon = loadPokemonImage(pokemon.getNombre());
        if (icon != null) {
            lblPokemonImage.setIcon(icon);
            lblPokemonImage.setText("");
        } else {
            lblPokemonImage.setIcon(null);
            lblPokemonImage.setText("Imagen no disponible");
            System.err.println("No se pudo cargar la imagen para: " + pokemon.getNombre());
        }

        StringBuilder stats = new StringBuilder();
        stats.append(String.format("Nombre: %s\n", pokemon.getNombre()));
        stats.append(String.format("Nivel: %d\n", pokemon.getNivel()));
        
        List<Tipo> tipos = pokemon.getTipos();
        stats.append("Tipo(s): ").append(tipos.get(0).toString());
        if (tipos.size() > 1) {
            stats.append("/").append(tipos.get(1).toString());
        }
        stats.append("\n");
        
        stats.append(String.format("PS: %d/%d\n", pokemon.getSalud(), pokemon.getSaludMaxima()));
        stats.append(String.format("Ataque: %d\n", pokemon.getAtaque()));
        stats.append(String.format("Defensa: %d\n", pokemon.getDefensa()));
        stats.append(String.format("Ataque Especial: %d\n", pokemon.getAtaqueEspecial()));
        stats.append(String.format("Defensa Especial: %d\n", pokemon.getDefensaEspecial()));
        stats.append(String.format("Velocidad: %d\n", pokemon.getVelocidad()));
        
        txtStats.setText(stats.toString());
    }

    private String normalizeName(String name) {
        return name.toLowerCase()
            .replace("á", "a").replace("é", "e").replace("í", "i")
            .replace("ó", "o").replace("ú", "u").replace(" ", "")
            .replace("'", "").replace(".", "");
    }

    private void addSelectedPokemon() {
        String pokemonName = (String) cbPokemons.getSelectedItem();
        if (pokemonName == null || pokemonName.isEmpty()) {
            showError("Selecciona un Pokémon primero");
            return;
        }

        Pokemon pokemon = pokemonMap.get(pokemonName);
        List<Tipo> tipos = pokemon.getTipos();
        
        if (selectedPokemons.size() >= MAX_POKEMONS) {
            showError("No puedes tener más de " + MAX_POKEMONS + " Pokémon");
            return;
        }

        for (Tipo tipo : tipos) {
            if (typeCount.getOrDefault(tipo, 0) >= MAX_SAME_TYPE) {
                showError("Límite alcanzado para el tipo " + tipo);
                return;
            }
        }

        selectedPokemons.add(pokemon);
        listModel.addElement(pokemonName);
        
        for (Tipo tipo : tipos) {
            typeCount.put(tipo, typeCount.getOrDefault(tipo, 0) + 1);
        }
        
        loadPokemonsByType(Tipo.valueOf(((String) cbTypes.getSelectedItem()).toUpperCase()));
        updateConfirmAndNextButton();
    }

    private void removeSelectedPokemon() {
        int selectedIndex = selectedPokemonsList.getSelectedIndex();
        if (selectedIndex == -1) {
            showError("Selecciona un Pokémon de la lista");
            return;
        }

        Pokemon pokemon = selectedPokemons.get(selectedIndex);
        
        selectedPokemons.remove(selectedIndex);
        listModel.remove(selectedIndex);
        
        for (Tipo tipo : pokemon.getTipos()) {
            typeCount.put(tipo, typeCount.get(tipo) - 1);
        }
        
        loadPokemonsByType(Tipo.valueOf(((String) cbTypes.getSelectedItem()).toUpperCase()));
        updateConfirmAndNextButton();
    }

    private void updateConfirmAndNextButton() {
        btnConfirmAndNext.setText("Confirmar y Siguiente (" + selectedPokemons.size() + "/" + MAX_POKEMONS + ")");
        btnConfirmAndNext.setEnabled(selectedPokemons.size() == MAX_POKEMONS);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
