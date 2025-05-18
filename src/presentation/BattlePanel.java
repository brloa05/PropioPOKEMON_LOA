package presentation;

import domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

public class BattlePanel extends JPanel {
    private PokemonGUI pokemonGUI;
    private Entrenador jugador1;
    private Entrenador jugador2;
    private Pokemon pokemonJugador1;
    private Pokemon pokemonJugador2;
    private BufferedImage backgroundImage;
    private BufferedImage player1PokemonImage;
    private BufferedImage player2PokemonImage;
    private JButton[] moveButtonsPlayer1 = new JButton[4];
    private JButton[] moveButtonsPlayer2 = new JButton[4];
    private JProgressBar player1HealthBar;
    private JProgressBar player2HealthBar;
    private JLabel player1NameLabel;
    private JLabel player2NameLabel;
    private JLabel player1StateLabel;
    private JLabel player2StateLabel;
    private JTextArea battleLog;
    private CardLayout actionCardLayout;
    private JPanel actionCards;
    private boolean isPlayer1Turn = true;
    private static final Map<String, String> POKEMON_IMAGE_MAP = createPokemonImageMap();

    public BattlePanel(PokemonGUI pokemonGUI, Entrenador jugador1, Entrenador jugador2) {
    this.pokemonGUI = pokemonGUI;
    this.jugador1 = jugador1;
    this.jugador2 = jugador2;

    try {
        // Verificar que los jugadores existan
        if (jugador1 == null || jugador2 == null) {
            throw new IllegalStateException("Ambos jugadores deben estar definidos");
        }

        // Verificar y obtener los Pokémon del jugador 1
        if (jugador1.getPokemones() == null || jugador1.getPokemones().isEmpty()) {
            throw new IllegalStateException("El jugador 1 (" + jugador1.getName() + ") no tiene Pokémon");
        }
        this.pokemonJugador1 = jugador1.getPokemones().get(0);

        // Verificar y obtener los Pokémon del jugador 2
        if (jugador2.getPokemones() == null || jugador2.getPokemones().isEmpty()) {
            throw new IllegalStateException("El jugador 2 (" + jugador2.getName() + ") no tiene Pokémon");
        }
        this.pokemonJugador2 = jugador2.getPokemones().get(0);

        // Verificar los movimientos del jugador 1
        Map<Pokemon, List<Movimiento>> player1Moves = pokemonGUI.getPokemonMovements();
        if (player1Moves == null || !player1Moves.containsKey(pokemonJugador1)) {
            throw new IllegalStateException("No hay movimientos asignados para el Pokémon " + 
                pokemonJugador1.getNombre() + " del jugador " + jugador1.getName());
        }

        // Verificar los movimientos del jugador 2
        Map<Pokemon, List<Movimiento>> player2Moves = pokemonGUI.getPokemonMovements();
        if (player2Moves == null || !player2Moves.containsKey(pokemonJugador2)) {
            throw new IllegalStateException("No hay movimientos asignados para el Pokémon " + 
                pokemonJugador2.getNombre() + " del jugador " + jugador2.getName());
        }

        prepareElements();
        prepareActions();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Erroal inicializar la batalla: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        throw new RuntimeException("Erroral inicializar la batalla", e);
    }
}
    
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

    private void prepareElements() {
        setLayout(new BorderLayout());
        
        // Inicializar el panel principal con fondo
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        mainPanel.setOpaque(false);
        
        // Cargar imagen de fondo
        try {
            URL backgroundUrl = getClass().getResource("/resource/battle_background.gif");
            if (backgroundUrl != null) {
                backgroundImage = ImageIO.read(backgroundUrl);
            } else {
                System.out.println("No se pudo encontrar la imagen de fondo");
                createBackupImages();
            }
        } catch (IOException e) {
            System.out.println("Error al cargar imagen: " + e.getMessage());
            createBackupImages();
        }

        // Panel de batalla usando GridBagLayout
        JPanel battlePanel = new JPanel(new GridBagLayout());
        battlePanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        // Panel del Jugador 2 (arriba a la derecha)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(20, 0, 0, 20);
        JPanel player2Panel = createPokemonInfoPanel(pokemonJugador2, false);
        battlePanel.add(player2Panel, gbc);

        // Panel del Jugador 1 (abajo a la izquierda)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.insets = new Insets(0, 20, 20, 0);
        JPanel player1Panel = createPokemonInfoPanel(pokemonJugador1, true);
        battlePanel.add(player1Panel, gbc);

        // Panel de acciones (área inferior negra)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sistema de cartas para los menús
        actionCardLayout = new CardLayout();
        actionCards = new JPanel(actionCardLayout);
        actionCards.setOpaque(false);

        // Crear paneles de acciones para ambos jugadores
        createActionPanels();

        bottomPanel.add(actionCards, BorderLayout.CENTER);
        mainPanel.add(battlePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setPreferredSize(new Dimension(800, 600));
    }

    private void createActionPanels() {
        // Panel de botones principales para Jugador 1
        JPanel player1ButtonsPanel = createPlayerButtonsPanel(1);
        
        // Panel de botones principales para Jugador 2
        JPanel player2ButtonsPanel = createPlayerButtonsPanel(2);
        
        // Panel de movimientos para Jugador 1
        JPanel player1MovesPanel = createPlayerMovesPanel(1);
        
        // Panel de movimientos para Jugador 2
        JPanel player2MovesPanel = createPlayerMovesPanel(2);

        // Agregar todos los paneles al sistema de cartas
        actionCards.add(player1ButtonsPanel, "PLAYER1_BUTTONS");
        actionCards.add(player2ButtonsPanel, "PLAYER2_BUTTONS");
        actionCards.add(player1MovesPanel, "PLAYER1_MOVES");
        actionCards.add(player2MovesPanel, "PLAYER2_MOVES");

        // Mostrar el panel del Jugador 1 inicialmente
        actionCardLayout.show(actionCards, "PLAYER1_BUTTONS");
    }

    private JPanel createPlayerButtonsPanel(int playerNum) {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnFight = createStyledButton("LUCHA", new Color(0, 122, 255));
        JButton btnItems = createStyledButton("ITEMS", new Color(255, 204, 0));
        JButton btnPokemon = createStyledButton("POKEMON", new Color(255, 149, 0));

        btnFight.addActionListener(e -> actionCardLayout.show(actionCards, "PLAYER" + playerNum + "_MOVES"));
        btnItems.addActionListener(e -> handleItems(playerNum));
        btnPokemon.addActionListener(e -> handlePokemonChange(playerNum));

        buttonPanel.add(btnFight);
        buttonPanel.add(btnItems);
        buttonPanel.add(btnPokemon);

        return buttonPanel;
    }
    
    private void handlePokemonChange(int playerNum) {
        Entrenador currentPlayer = (playerNum == 1) ? jugador1 : jugador2;
        List<Pokemon> availablePokemons = currentPlayer.getPokemones();
        
        if (availablePokemons == null || availablePokemons.isEmpty()) {
            showError("No hay más Pokémon disponibles");
            return;
        }

        // Crear un array con los nombres de los Pokémon disponibles
        String[] pokemonNames = availablePokemons.stream()
                .map(Pokemon::getNombre)
                .toArray(String[]::new);

        // Mostrar diálogo de selección
        String selectedName = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona un Pokémon:",
            "Cambiar Pokémon",
            JOptionPane.QUESTION_MESSAGE,
            null,
            pokemonNames,
            pokemonNames[0]
        );

        if (selectedName != null) {
            // Encontrar el Pokémon seleccionado
            Pokemon selectedPokemon = availablePokemons.stream()
                .filter(p -> p.getNombre().equals(selectedName))
                .findFirst()
                .orElse(null);

            if (selectedPokemon != null) {
                // Actualizar el Pokémon actual
                if (playerNum == 1) {
                    pokemonJugador1 = selectedPokemon;
                } else {
                    pokemonJugador2 = selectedPokemon;
                }
                
                // Actualizar la visualización
                updatePokemonDisplay(playerNum);
                updateBattleLog(currentPlayer.getName() + " cambió a " + selectedName);
                switchTurns();
            }
        }
    }
    
    private void prepareActions() {
    }

    private JPanel createPlayerMovesPanel(int playerNum) {
        JPanel movesPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        movesPanel.setOpaque(false);
        
        Pokemon currentPokemon = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2;
        Map<Pokemon, List<Movimiento>> movements = pokemonGUI.getPokemonMovements();
        List<Movimiento> movimientos = movements.get(currentPokemon);

        JButton[] buttons = (playerNum == 1) ? moveButtonsPlayer1 : moveButtonsPlayer2;
        
        for (int i = 0; i < 4; i++) {
            if (movimientos != null && i < movimientos.size()) {
                Movimiento mov = movimientos.get(i);
                buttons[i] = createStyledButton(mov.getName(), getColorForMove(mov));
                final int moveIndex = i;
                buttons[i].addActionListener(e -> {
                    executeMove(moveIndex, playerNum);
                    switchTurns();
                });
            } else {
                buttons[i] = createStyledButton("-", Color.GRAY);
                buttons[i].setEnabled(false);
            }
            movesPanel.add(buttons[i]);
        }

        return movesPanel;
    }

    private void switchTurns() {
        isPlayer1Turn = !isPlayer1Turn;
        actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS");
        updateBattleLog(isPlayer1Turn ? "Turno del Jugador 1" : "Turno del Jugador 2");
    }

    private void executeMove(int moveIndex, int playerNum) {
        Pokemon attacker = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2;
        Pokemon defender = (playerNum == 1) ? pokemonJugador2 : pokemonJugador1;
        
        Map<Pokemon, List<Movimiento>> movements = pokemonGUI.getPokemonMovements();
        List<Movimiento> pokemonMoves = movements.get(attacker);
        
        if (pokemonMoves != null && moveIndex < pokemonMoves.size()) {
            Movimiento movimiento = pokemonMoves.get(moveIndex);
            int damage = calculateDamage(movimiento, attacker, defender);
            defender.recibirDanio(damage);
            
            updateBattleLog(attacker.getNombre() + " usó " + movimiento.getName() + 
                          " y causó " + damage + " de daño!");
            updateHealthBars();
            
            if (defender.estaDebilitado()) {
                handlePokemonFainted(defender);
            }
        }
    }

    private int calculateDamage(Movimiento movimiento, Pokemon attacker, Pokemon defender) {
        double baseDamage = movimiento.getPotencia() * 
            ((double)attacker.getAtaque() / defender.getDefensa());
        return (int)(baseDamage * (0.85 + Math.random() * 0.15));
    }

    private void handlePokemonFainted(Pokemon pokemon) {
        String message = pokemon.getNombre() + " se ha debilitado!";
        updateBattleLog(message);
        
        if (pokemon == pokemonJugador1) {
            handleVictory(jugador2);
        } else {
            handleVictory(jugador1);
        }
    }

    private void handleVictory(Entrenador winner) {
        String message = "¡" + winner.getName() + " ha ganado la batalla!";
        updateBattleLog(message);
        JOptionPane.showMessageDialog(this, message);
        pokemonGUI.mostrarMenuPrincipal();
    }

    private void handleItems(int playerNum) {
        Entrenador currentPlayer = (playerNum == 1) ? jugador1 : jugador2;
        Pokemon currentPokemon = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2;
        
        List<Item> items = currentPlayer.getItems();
        if (items == null || items.isEmpty()) {
            showError("No tienes objetos disponibles");
            return;
        }

        String[] itemNames = items.stream()
                .map(Item::getId)
                .toArray(String[]::new);

        String selectedItem = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona un objeto:",
            "Usar Objeto",
            JOptionPane.QUESTION_MESSAGE,
            null,
            itemNames,
            itemNames[0]
        );

        if (selectedItem != null) {
            Item item = items.stream()
                .filter(i -> i.getId().equals(selectedItem))
                .findFirst()
                .orElse(null);

            if (item != null) {
                String resultado = item.efectoItem(currentPokemon);
                if (!resultado.equals("No efectuo")) {
                    items.remove(item);
                    updateHealthBars();
                    updateBattleLog(currentPlayer.getName() + " usó " + item.getId());
                    switchTurns();
                }
            }
        }
    }



    private void updatePokemonDisplay(int playerNum) {
        if (playerNum == 1) {
            player1PokemonImage = loadPokemonImage(pokemonJugador1.getNombre(), true);
            player1NameLabel.setText(pokemonJugador1.getNombre());
            updateMovements(1);
        } else {
            player2PokemonImage = loadPokemonImage(pokemonJugador2.getNombre(), false);
            player2NameLabel.setText(pokemonJugador2.getNombre());
            updateMovements(2);
        }
        updateHealthBars();
    }

    private void updateMovements(int playerNum) {
        Pokemon currentPokemon = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2;
        JButton[] buttons = (playerNum == 1) ? moveButtonsPlayer1 : moveButtonsPlayer2;
        
        Map<Pokemon, List<Movimiento>> movements = pokemonGUI.getPokemonMovements();
        List<Movimiento> pokemonMoves = movements.get(currentPokemon);
        
        if (pokemonMoves != null) {
            for (int i = 0; i < buttons.length; i++) {
                if (i < pokemonMoves.size()) {
                    Movimiento mov = pokemonMoves.get(i);
                    buttons[i].setText(mov.getName());
                    buttons[i].setEnabled(true);
                    buttons[i].setBackground(getColorForMove(mov));
                } else {
                    buttons[i].setText("-");
                    buttons[i].setEnabled(false);
                    buttons[i].setBackground(Color.GRAY);
                }
            }
        }
    }

    private void updateHealthBars() {
        player1HealthBar.setValue(pokemonJugador1.getSalud());
        player1HealthBar.setString(pokemonJugador1.getSalud() + "/" + pokemonJugador1.getSaludMaxima());
        player1HealthBar.setForeground(getHealthColor(pokemonJugador1));
        
        player2HealthBar.setValue(pokemonJugador2.getSalud());
        player2HealthBar.setString(pokemonJugador2.getSalud() + "/" + pokemonJugador2.getSaludMaxima());
        player2HealthBar.setForeground(getHealthColor(pokemonJugador2));
        
        revalidate();
        repaint();
    }

    private void updateBattleLog(String message) {
        if (battleLog != null) {
            battleLog.append(message + "\n");
            battleLog.setCaretPosition(battleLog.getDocument().getLength());
        }
    }

    private Color getHealthColor(Pokemon pokemon) {
        double percentage = (double) pokemon.getSalud() / pokemon.getSaludMaxima();
        if (percentage > 0.5) return Color.GREEN;
        if (percentage > 0.25) return Color.YELLOW;
        return Color.RED;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        return button;
    }

    private Color getColorForMove(Movimiento movimiento) {
        // Implementar la lógica para determinar el color basado en el tipo de movimiento
        return new Color(70, 130, 180); // Color por defecto
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private BufferedImage loadPokemonImage(String pokemonName, boolean isPlayer) {
        if (pokemonName == null || pokemonName.isEmpty()) {
            return createPlaceholderImage();
        }

        String suffix = isPlayer ? "-Usuario.gif" : "-Objetivo.gif";
        String imagePath = "/resource/" + pokemonName.toLowerCase() + suffix;
        
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                throw new FileNotFoundException("No se encontró el recurso: " + imagePath);
            }
            return ImageIO.read(imageUrl);
        } catch (Exception e) {
            System.err.println("Error cargando imagen para " + pokemonName + ": " + e.getMessage());
            return createPlaceholderImage();
        }
    }

    private BufferedImage createPlaceholderImage() {
        BufferedImage img = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, 120, 120);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Pokémon", 30, 60);
        g2d.dispose();
        return img;
    }

    private void createBackupImages() {
        backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = backgroundImage.createGraphics();
        g2d.setColor(new Color(100, 150, 200));
        g2d.fillRect(0, 0, 800, 600);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Batalla Pokémon", 300, 300);
        g2d.dispose();

        player1PokemonImage = createPlaceholderImage();
        player2PokemonImage = createPlaceholderImage();
    }

    private JPanel createPokemonInfoPanel(Pokemon pokemon, boolean isPlayer) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(pokemon.getNombre());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JProgressBar healthBar = new JProgressBar(0, pokemon.getSaludMaxima());
        healthBar.setValue(pokemon.getSalud());
        healthBar.setForeground(getHealthColor(pokemon));
        healthBar.setString(pokemon.getSalud() + "/" + pokemon.getSaludMaxima());
        healthBar.setStringPainted(true);
        
        JLabel stateLabel = new JLabel("Normal");
        stateLabel.setForeground(Color.WHITE);

        if (isPlayer) {
            player1NameLabel = nameLabel;
            player1HealthBar = healthBar;
            player1StateLabel = stateLabel;
        } else {
            player2NameLabel = nameLabel;
            player2HealthBar = healthBar;
            player2StateLabel = stateLabel;
        }

        infoPanel.add(nameLabel);
        infoPanel.add(healthBar);
        infoPanel.add(stateLabel);

        panel.add(infoPanel);

        return panel;
    }
}