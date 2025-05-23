package presentation;

import domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
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
    private JButton[] moveButtonsPlayer1 = new JButton[4];
    private JButton[] moveButtonsPlayer2 = new JButton[4];
    private JProgressBar player1HealthBar;
    private JProgressBar player2HealthBar;
    private JLabel player1NameLabel;
    private JLabel player2NameLabel;
    private JLabel lblPokemonImagePlayer1;
    private JLabel lblPokemonImagePlayer2;
    private BufferedImage player1PokemonImageBuffer; 
    private BufferedImage player2PokemonImageBuffer;
    private JTextArea battleLog;
    private CardLayout actionCardLayout;
    private JPanel actionCards;
    private boolean isPlayer1Turn = true;
    private JPanel player1ItemsPanel_NEW;
    private JPanel player2ItemsPanel_NEW; 
    private JButton[] itemButtonsPlayer1_NEW = new JButton[3]; 
    private JButton[] itemButtonsPlayer2_NEW = new JButton[3];

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
            Map<String, Map<Pokemon, List<Movimiento>>> playerMovements = pokemonGUI.playerMovements;
            if (playerMovements == null || !playerMovements.containsKey(jugador1.getName())) {
                throw new IllegalStateException("No hay movimientos asignados para el jugador " + jugador1.getName());
            }

            // Verificar los movimientos del jugador 2
            if (!playerMovements.containsKey(jugador2.getName())) {
                throw new IllegalStateException("No hay movimientos asignados para el jugador " + jugador2.getName());
            }

            prepareElements();
            prepareActions();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al inicializar la batalla: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Error al inicializar la batalla", e);
        }
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
        	String correctedBackgroundPath_NEW = "/battle_background.gif"; // Esta es la ruta corregida esperada
            backgroundImage = tryLoadImageCorrectly_NEW(correctedBackgroundPath_NEW);
            
            if (backgroundImage != null) {
                System.out.println("NUEVO_INTENTO_CARGA: Imagen de fondo cargada exitosamente con: " + correctedBackgroundPath_NEW);
            } else {
	            URL backgroundUrl = getClass().getResource("/resources/battle_background.gif");
	            if (backgroundUrl != null) {
	                backgroundImage = ImageIO.read(backgroundUrl);
	            } else {
	                System.out.println("No se pudo encontrar la imagen de fondo");
	                if (backgroundImage == null) createBackupImages();
	            }
            }
        }	catch (IOException e) {
            System.out.println("Error al cargar imagen: " + e.getMessage());
            if (backgroundImage == null) createBackupImages();
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
        
        lblPokemonImagePlayer2 = new JLabel();
        lblPokemonImagePlayer2.setPreferredSize(new Dimension(150, 150)); // Ajusta tamaño según necesites
        player2PokemonImageBuffer = loadPokemonImage(pokemonJugador2.getNombre(), false);
        if (player2PokemonImageBuffer != null) {
            lblPokemonImagePlayer2.setIcon(new ImageIcon(player2PokemonImageBuffer.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        } else {
            lblPokemonImagePlayer2.setText("Img J2 N/A"); // Placeholder si no carga
        }
        
        // Panel del Jugador 1 (abajo a la izquierda)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.insets = new Insets(0, 20, 20, 0);
        JPanel player1Panel = createPokemonInfoPanel(pokemonJugador1, true);
        battlePanel.add(player1Panel, gbc);
        
        lblPokemonImagePlayer1 = new JLabel();
        lblPokemonImagePlayer1.setPreferredSize(new Dimension(150, 150)); // Ajusta tamaño
        player1PokemonImageBuffer = loadPokemonImage(pokemonJugador1.getNombre(), true);
        if (player1PokemonImageBuffer != null) {
            lblPokemonImagePlayer1.setIcon(new ImageIcon(player1PokemonImageBuffer.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        } else {
            lblPokemonImagePlayer1.setText("Img J1 N/A"); // Placeholder
        }
        
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

        // Área de registro de batalla
        battleLog = new JTextArea();
        battleLog.setEditable(false);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setFont(new Font("Arial", Font.PLAIN, 12));
        battleLog.setForeground(Color.WHITE);
        battleLog.setBackground(new Color(50, 50, 50));
        JScrollPane scrollLog = new JScrollPane(battleLog);
        scrollLog.setPreferredSize(new Dimension(0, 100));

        bottomPanel.add(actionCards, BorderLayout.CENTER);
        bottomPanel.add(scrollLog, BorderLayout.SOUTH);
        
        mainPanel.add(battlePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setPreferredSize(new Dimension(800, 600));
        
        loadPokemonImage(pokemonJugador1.getNombre(), true);
        loadPokemonImage(pokemonJugador2.getNombre(), false);
        
        // Mostrar mensaje inicial
        updateBattleLog("¡Comienza la batalla entre " + jugador1.getName() + " y " + jugador2.getName() + "!");
        updateBattleLog("Turno de " + jugador1.getName());
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
        
        player1ItemsPanel_NEW = createPlayerItemsPanel_NEW(1);
        player2ItemsPanel_NEW = createPlayerItemsPanel_NEW(2);
        
        

        // Agregar todos los paneles al sistema de cartas
        actionCards.add(player1ButtonsPanel, "PLAYER1_BUTTONS");
        actionCards.add(player2ButtonsPanel, "PLAYER2_BUTTONS");
        actionCards.add(player1MovesPanel, "PLAYER1_MOVES");
        actionCards.add(player2MovesPanel, "PLAYER2_MOVES");
        
        actionCards.add(player1ItemsPanel_NEW, "PLAYER1_ITEMS_NEW");
        actionCards.add(player2ItemsPanel_NEW, "PLAYER2_ITEMS_NEW");

        actionCardLayout.show(actionCards, "PLAYER1_BUTTONS");
    }

    private JPanel createPlayerButtonsPanel(int playerNum) {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnFight = createStyledButton("LUCHA", new Color(0, 122, 255));
        JButton btnItems = createStyledButton("ITEMS", new Color(255, 204, 0));
        JButton btnPokemon = createStyledButton("POKEMON", new Color(255, 149, 0));
        
        JButton btnItemsPanel_NEW = createStyledButton("ITEMS", new Color(255, 180, 0)); 
        btnItemsPanel_NEW.addActionListener(e -> {
            updatePlayerItemsPanel_NEW(playerNum); 
            actionCardLayout.show(actionCards, "PLAYER" + playerNum + "_ITEMS_NEW");
        });
        
        btnFight.addActionListener(e -> actionCardLayout.show(actionCards, "PLAYER" + playerNum + "_MOVES"));
        btnItems.addActionListener(e -> handleItems(playerNum));
        btnPokemon.addActionListener(e -> handlePokemonChange(playerNum));

        buttonPanel.add(btnFight);
        buttonPanel.add(btnItemsPanel_NEW);
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
    
    private JPanel createPlayerMovesPanel(int playerNum) {
    JPanel movesPanel = new JPanel(new GridLayout(2, 2, 5, 5));
    movesPanel.setOpaque(false);

    Pokemon currentPokemon = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2;
    String playerName = (playerNum == 1) ? jugador1.getName() : jugador2.getName();
    
    // Obtener movimientos del mapa playerMovements en lugar del Pokémon directamente
    List<Movimiento> movimientos = new ArrayList<>();
    if (pokemonGUI.playerMovements != null && pokemonGUI.playerMovements.containsKey(playerName)) {
        Map<Pokemon, List<Movimiento>> pokemonMoves = pokemonGUI.playerMovements.get(playerName);
        if (pokemonMoves != null && pokemonMoves.containsKey(currentPokemon)) {
            movimientos = pokemonMoves.get(currentPokemon);
        }
    }

    JButton[] buttons = (playerNum == 1) ? moveButtonsPlayer1 : moveButtonsPlayer2;
    
    for (int i = 0; i < 4; i++) {
        if (movimientos != null && i < movimientos.size()) {
            Movimiento mov = movimientos.get(i);
            buttons[i] = createStyledButton(mov.getName(), getColorForMove(mov));
            final int moveIndex = i;
            buttons[i].addActionListener(e -> {
                executeMove(moveIndex, playerNum);
                actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS");
            });
        } else {
            buttons[i] = createStyledButton("-", Color.GRAY);
            buttons[i].setEnabled(false);
        }
        movesPanel.add(buttons[i]);
    }

    JPanel containerPanel = new JPanel(new BorderLayout());
    containerPanel.setOpaque(false);
    
    JPanel gridPanel = new JPanel(new GridLayout(2, 2, 5, 5));
    gridPanel.setOpaque(false);
    for (int i = 0; i < 4; i++) {
        gridPanel.add(buttons[i]);
    }
    
    JButton btnReturn = createStyledButton("Volver", new Color(200, 0, 0));
    btnReturn.addActionListener(e -> 
        actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS")
    );
    
    containerPanel.add(gridPanel, BorderLayout.CENTER);
    containerPanel.add(btnReturn, BorderLayout.SOUTH);
    
    return containerPanel;
}

    private void switchTurns() {
        isPlayer1Turn = !isPlayer1Turn;
        actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS");
        updateBattleLog(isPlayer1Turn ? "Turno de " + jugador1.getName() : "Turno de " + jugador2.getName());
    }

    // Reemplazar el método executeMove existente con esta versión mejorada
    private void executeMove(int moveIndex, int playerNum) {
	    Pokemon attacker = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2;
	    Pokemon defender = (playerNum == 1) ? pokemonJugador2 : pokemonJugador1;
	    
	    Map<String, Map<Pokemon, List<Movimiento>>> playerMovements = pokemonGUI.playerMovements;
	    String playerName = (playerNum == 1) ? jugador1.getName() : jugador2.getName();
	    
	    List<Movimiento> pokemonMoves = playerMovements.get(playerName).get(attacker);
	    
	    if (pokemonMoves != null && moveIndex < pokemonMoves.size()) {
	        Movimiento movimiento = pokemonMoves.get(moveIndex);
        
        // Mostrar información del movimiento en el log
        updateBattleLog(attacker.getNombre() + " usó " + movimiento.getName() + "!");
        
        // Calcular y aplicar daño
        int damage = calculateDamage(movimiento, attacker, defender);
        defender.recibirDanio(damage);
        
        updateBattleLog("Causó " + damage + " puntos de daño!");
        updateHealthBars();
        
        if (defender.estaDebilitado()) {
            handlePokemonFainted(defender, playerNum);
        }else {
        	switchTurns();
        }
    }
}

 // Agregar este método en la clase BattlePanel
    private int calculateDamage(Movimiento movimiento, Pokemon atacante, Pokemon defensor) {
        // Obtener el tipo del movimiento
        Pokemon.Tipo tipoMovimiento = null;
        for (Pokemon.Tipo tipo : Pokemon.Tipo.values()) {
            if (pokemonGUI.getPokemonAplication().getMovimientosPorTipo(tipo).contains(movimiento)) {
                tipoMovimiento = tipo;
                break;
            }
        }
        
        // Calcular daño base
        double baseDamage = movimiento.getPotencia() * 
            ((double)atacante.getAtaque() / defensor.getDefensa());
        
        // Aplicar bonificación por tipo (STAB - Same Type Attack Bonus)
        if (atacante.getTipos().contains(tipoMovimiento)) {
            baseDamage *= 1.5;
        }
        
        // Factor aleatorio entre 0.85 y 1.0
        double randomFactor = 0.85 + Math.random() * 0.15;
        
        return (int)(baseDamage * randomFactor);
    }

    private void handlePokemonFainted(Pokemon pokemon, int playerNum) {
        String message = pokemon.getNombre() + " se ha debilitado!";
        updateBattleLog(message);
        
        Entrenador currentPlayer = (playerNum == 1) ? jugador1 : jugador2;
        Entrenador opponent = (playerNum == 1) ? jugador2 : jugador1;
        
        // Verificar si el jugador tiene más Pokémon
        boolean hasMorePokemon = currentPlayer.getPokemones().stream()
            .anyMatch(p -> !p.estaDebilitado());
            
        if (!hasMorePokemon) {
            handleVictory(opponent);
        } else {
            // Forzar cambio de Pokémon
            handlePokemonChange(playerNum);
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
            player1PokemonImageBuffer = loadPokemonImage(pokemonJugador1.getNombre(), true); //
            if (player1PokemonImageBuffer != null && lblPokemonImagePlayer1 != null) {
                lblPokemonImagePlayer1.setIcon(new ImageIcon(player1PokemonImageBuffer.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
                lblPokemonImagePlayer1.setText(""); // Limpiar texto si la imagen carga
            } else if (lblPokemonImagePlayer1 != null) {
                lblPokemonImagePlayer1.setIcon(null);
                lblPokemonImagePlayer1.setText("Img J1 N/A");
            }
            if (player1NameLabel != null) player1NameLabel.setText(pokemonJugador1.getNombre()); //
            updateMovements(1); //
        } else {
            player2PokemonImageBuffer = loadPokemonImage(pokemonJugador2.getNombre(), false); //
            if (player2PokemonImageBuffer != null && lblPokemonImagePlayer2 != null) {
                lblPokemonImagePlayer2.setIcon(new ImageIcon(player2PokemonImageBuffer.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
                lblPokemonImagePlayer2.setText(""); // Limpiar texto si la imagen carga
            } else if (lblPokemonImagePlayer2 != null) {
                lblPokemonImagePlayer2.setIcon(null);
                lblPokemonImagePlayer2.setText("Img J2 N/A");
            }
            if (player2NameLabel != null) player2NameLabel.setText(pokemonJugador2.getNombre()); //
            updateMovements(2); 
        }
        updateHealthBars(); 
    }

    // Reemplazar el método updateMovements existente con esta versión mejorada
    private void updateMovements(int playerNum) {
        Pokemon currentPokemon = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2;
        JButton[] buttons = (playerNum == 1) ? moveButtonsPlayer1 : moveButtonsPlayer2;
        
        Map<String, Map<Pokemon, List<Movimiento>>> playerMovements = pokemonGUI.playerMovements;
        String playerName = (playerNum == 1) ? jugador1.getName() : jugador2.getName();
        
        List<Movimiento> pokemonMoves = playerMovements.get(playerName).get(currentPokemon);
        
        if (pokemonMoves != null) {
            for (int i = 0; i < buttons.length; i++) {
                if (i < pokemonMoves.size()) {
                    Movimiento mov = pokemonMoves.get(i);
                    buttons[i].setText(mov.getName());
                    buttons[i].setEnabled(true);
                    buttons[i].setBackground(getColorForMove(mov));
                    buttons[i].setToolTipText("Potencia: " + mov.getPotencia());
                } else {
                    buttons[i].setText("-");
                    buttons[i].setEnabled(false);
                    buttons[i].setBackground(Color.GRAY);
                    buttons[i].setToolTipText(null);
                }
            }
        }
        revalidate();
        repaint();
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

 // Reemplazar el método getColorForMove existente con esta versión mejorada
    private Color getColorForMove(Movimiento movimiento) {
        // Obtener el tipo del movimiento
        Pokemon.Tipo tipoMovimiento = null;
        for (Pokemon.Tipo tipo : Pokemon.Tipo.values()) {
            if (pokemonGUI.getPokemonAplication().getMovimientosPorTipo(tipo).contains(movimiento)) {
                tipoMovimiento = tipo;
                break;
            }
        }
        
        // Asignar color según el tipo
        if (tipoMovimiento != null) {
            switch (tipoMovimiento) {
                case FUEGO: return new Color(255, 68, 0);
                case AGUA: return new Color(0, 128, 255);
                case PLANTA: return new Color(0, 204, 0);
                case ELECTRICO: return new Color(255, 204, 0);
                case HIELO: return new Color(102, 204, 255);
                case LUCHA: return new Color(153, 51, 0);
                case VENENO: return new Color(153, 0, 153);
                case TIERRA: return new Color(153, 102, 51);
                case VOLADOR: return new Color(153, 204, 255);
                case PSIQUICO: return new Color(255, 102, 255);
                case BICHO: return new Color(102, 153, 0);
                case ROCA: return new Color(153, 153, 102);
                case FANTASMA: return new Color(102, 51, 153);
                case DRAGON: return new Color(102, 0, 204);
                case SINIESTRO: return new Color(51, 51, 51);
                case ACERO: return new Color(153, 153, 153);
                case HADA: return new Color(255, 153, 255);
                default: return new Color(70, 130, 180);
            }
        }
        return new Color(70, 130, 180); // Color por defecto si no se encuentra el tipo
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Intenta cargar una imagen desde el classpath usando la ruta proporcionada.
     * @param correctedPath La ruta corregida al recurso, comenzando con "/".
     * @return La BufferedImage si se carga correctamente, o null si no se encuentra o hay un error.
     */
    private BufferedImage tryLoadImageCorrectly_NEW(String correctedPath) {
        try {
            URL imageUrl = getClass().getResource(correctedPath);
            if (imageUrl != null) {
                return ImageIO.read(imageUrl);
            } else {
                System.err.println("NUEVO_INTENTO_CARGA (INFO): No se encontró el recurso con la ruta corregida: " + correctedPath);
            }
        } catch (IOException e) {
            System.err.println("NUEVO_INTENTO_CARGA (ERROR): Falló al cargar la imagen con la ruta corregida '" + correctedPath + "': " + e.getMessage());
        }
        return null; 
    }
    
    private BufferedImage loadPokemonImage(String pokemonName, boolean isPlayer) {
        if (pokemonName == null || pokemonName.isEmpty()) {
            return createPlaceholderImage();
        }

        String suffix = isPlayer ? "-Usuario.gif" : "-Objetivo.gif";
        String correctedImagePath_NEW = "/" + pokemonName.toLowerCase() + suffix;
        BufferedImage loadedImage_NEW = tryLoadImageCorrectly_NEW(correctedImagePath_NEW);
        
        if (loadedImage_NEW != null) {
            System.out.println("NUEVO_INTENTO_CARGA: Imagen de Pokémon '" + pokemonName + "' cargada exitosamente con: " + correctedImagePath_NEW);
            return loadedImage_NEW;
        }
        
        String imagePath = "/resources/" + pokemonName.toLowerCase() + suffix;
        
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

        createPlaceholderImage();
        createPlaceholderImage();
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
        } else {
            player2NameLabel = nameLabel;
            player2HealthBar = healthBar;
        }

        infoPanel.add(nameLabel);
        infoPanel.add(healthBar);
        infoPanel.add(stateLabel);

        panel.add(infoPanel);

        return panel;
    }
    
    private JPanel createPlayerItemsPanel_NEW(int playerNum) {
        JPanel itemsDisplayPanel = new JPanel(new GridLayout(2, 2, 5, 5)); // Similar al panel de movimientos
        itemsDisplayPanel.setOpaque(false);

        Entrenador currentPlayer = (playerNum == 1) ? jugador1 : jugador2;
        Pokemon currentPokemon = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2;
        List<Item> playerItems = currentPlayer.getItems(); // [cite: 75]

        JButton[] currentItemButtons = (playerNum == 1) ? itemButtonsPlayer1_NEW : itemButtonsPlayer2_NEW;

        for (int i = 0; i < currentItemButtons.length; i++) {
            if (playerItems != null && i < playerItems.size()) {
                Item item = playerItems.get(i);
                String itemName = "";

                if (item instanceof Potion) itemName = ((Potion)item).getNameItem();
                else if (item instanceof SuperPotion) itemName = ((SuperPotion)item).getNameItem(); 
                else if (item instanceof HyperPotion) itemName = ((HyperPotion)item).getNameItem(); 
                else if (item instanceof Revive) itemName = ((Revive)item).getNameItem();
                else itemName = item.getId();

                currentItemButtons[i] = createStyledButton(itemName, new Color(100, 100, 255)); 
                final int itemIndex = i; 

                currentItemButtons[i].addActionListener(e -> {
                    if (itemIndex < currentPlayer.getItems().size()) { 
                        Item selectedItem = currentPlayer.getItems().get(itemIndex);
                        boolean itemUsed = pokemonGUI.getPokemonAplication().usarItem(currentPokemon, selectedItem, currentPlayer); 

                        if (itemUsed) {
                            updateBattleLog(currentPlayer.getName() + " usó " + selectedItem.getId() + " en " + currentPokemon.getNombre() + ".");
                            updateHealthBars(); 
                            updatePlayerItemsPanel_NEW(playerNum); 
                            switchTurns();
                        } else {
                            updateBattleLog(selectedItem.getId() + " no tuvo efecto.");
                        }
                        actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS");
                    }
                });
            } else {
                currentItemButtons[i] = createStyledButton("-", Color.GRAY);
                currentItemButtons[i].setEnabled(false);
            }
            itemsDisplayPanel.add(currentItemButtons[i]);
        }

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);
        containerPanel.add(itemsDisplayPanel, BorderLayout.CENTER);

        JButton btnReturn = createStyledButton("Volver", new Color(200, 0, 0));
        btnReturn.addActionListener(e ->
            actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS")
        );
        containerPanel.add(btnReturn, BorderLayout.SOUTH);

        return containerPanel;
    }

    /**
     * Actualiza el panel de ítems para un jugador específico.
     * Esto es útil si la lista de ítems cambia (ej. después de usar uno).
     */
    private void updatePlayerItemsPanel_NEW(int playerNum) {
        Entrenador currentPlayer = (playerNum == 1) ? jugador1 : jugador2; //
        // Pokemon currentPokemon = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2; // Pokémon activo, podría no ser el objetivo del ítem
        List<Item> playerItems = currentPlayer.getItems(); //
        JButton[] currentItemButtons = (playerNum == 1) ? itemButtonsPlayer1_NEW : itemButtonsPlayer2_NEW; //

        for (int i = 0; i < currentItemButtons.length; i++) {
            // Limpiar listeners antiguos para evitar duplicados si este método se llama múltiples veces para el mismo panel
            for (ActionListener al : currentItemButtons[i].getActionListeners()) { //
                currentItemButtons[i].removeActionListener(al); //
            }

            if (playerItems != null && i < playerItems.size()) {
                Item item = playerItems.get(i); //
                String itemName = "";
                if (item instanceof Potion) itemName = ((Potion)item).getNameItem(); //
                else if (item instanceof SuperPotion) itemName = ((SuperPotion)item).getNameItem(); //
                else if (item instanceof HyperPotion) itemName = ((HyperPotion)item).getNameItem(); //
                else if (item instanceof Revive) itemName = ((Revive)item).getNameItem(); //
                else itemName = item.getId(); //

                currentItemButtons[i].setText(itemName); //
                currentItemButtons[i].setBackground(new Color(100, 100, 255)); //
                currentItemButtons[i].setEnabled(true); //

                final int itemIndexInLoop = i; // Usar un nombre de variable final diferente para el lambda
                final Entrenador finalCurrentPlayerForListener = currentPlayer; // Capturar currentPlayer para el listener
                final int finalPlayerNumForListener = playerNum; // Capturar playerNum para el listener

                currentItemButtons[i].addActionListener(e -> {
                    if (itemIndexInLoop < finalCurrentPlayerForListener.getItems().size()) {
                        Item selectedItem = finalCurrentPlayerForListener.getItems().get(itemIndexInLoop);
                        List<Pokemon> team = finalCurrentPlayerForListener.getPokemones(); //

                        if (team == null || team.isEmpty()) {
                            updateBattleLog("No hay Pokémon en el equipo para usar " + selectedItem.getId() + ".");
                            actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS");
                            return;
                        }

                        List<Pokemon> validTargets = new ArrayList<>();
                        if (selectedItem.esRevive()) { //
                            for (Pokemon p : team) {
                                if (p.estaDebilitado()) { //
                                    validTargets.add(p);
                                }
                            }
                        } else { // Para pociones u otros ítems
                            for (Pokemon p : team) {
                                if (!p.estaDebilitado() && p.getSalud() < p.getSaludMaxima()) { //
                                    validTargets.add(p);
                                }
                            }
                        }

                        if (validTargets.isEmpty()) {
                            String message;
                            if (selectedItem.esRevive()) { //
                                message = "No hay Pokémon debilitados en el equipo para usar " + selectedItem.getId() + ".";
                            } else {
                                message = "No hay Pokémon en el equipo que necesiten " + selectedItem.getId() +
                                          " (o todos tienen salud máxima/están debilitados).";
                            }
                            updateBattleLog(message);
                            JOptionPane.showMessageDialog(BattlePanel.this, message, "No hay objetivos válidos", JOptionPane.INFORMATION_MESSAGE);
                            actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS");
                            return;
                        }

                        String[] pokemonNamesForDialog = validTargets.stream()
                                                            .map(p -> p.getNombre() + " (HP: " + p.getSalud() + "/" + p.getSaludMaxima() + ")")
                                                            .toArray(String[]::new);

                        String chosenPokemonNameAndHp = (String) JOptionPane.showInputDialog(
                            BattlePanel.this,
                            "Selecciona un Pokémon para usar " + selectedItem.getId() + ":",
                            "Usar Ítem en Pokémon",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            pokemonNamesForDialog,
                            pokemonNamesForDialog[0]
                        );

                        if (chosenPokemonNameAndHp != null) {
                            String chosenPokemonName = chosenPokemonNameAndHp.split(" \\(HP:")[0];
                            Pokemon targetPokemon = null;
                            for (Pokemon p : validTargets) {
                                if (p.getNombre().equals(chosenPokemonName)) {
                                    targetPokemon = p;
                                    break;
                                }
                            }

                            if (targetPokemon != null) {
                                boolean itemUsed = pokemonGUI.getPokemonAplication().usarItem(targetPokemon, selectedItem, finalCurrentPlayerForListener); //

                                if (itemUsed) { //
                                    updateBattleLog(finalCurrentPlayerForListener.getName() + " usó " + selectedItem.getId() + " en " + targetPokemon.getNombre() + "."); //
                                    updateHealthBars(); //
                                    updatePlayerItemsPanel_NEW(finalPlayerNumForListener); //
                                    switchTurns(); //
                                } else {
                                    updateBattleLog(selectedItem.getId() + " no tuvo efecto en " + targetPokemon.getNombre() + "."); //
                                    JOptionPane.showMessageDialog(BattlePanel.this, selectedItem.getId() + " no tuvo efecto en " + targetPokemon.getNombre() + ". " +
                                                                  "Verifica las condiciones del Pokémon (ej. HP no máximo para pociones, debilitado para Revivir).",
                                                                  "Ítem sin efecto", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        }
                        // Volver a los botones de acción principales independientemente de si se usó el ítem o se canceló el diálogo
                        actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS");
                    }
                });
            } else {
                currentItemButtons[i].setText("-"); //
                currentItemButtons[i].setBackground(Color.GRAY); //
                currentItemButtons[i].setEnabled(false); //
            }
        } // Fin del bucle for

        // Revalidar y repintar el panel de ítems específico del jugador para reflejar los cambios en los botones
        if (playerNum == 1 && player1ItemsPanel_NEW != null) { //
            player1ItemsPanel_NEW.revalidate(); //
            player1ItemsPanel_NEW.repaint(); //
        } else if (playerNum == 2 && player2ItemsPanel_NEW != null) { //
            player2ItemsPanel_NEW.revalidate(); //
            player2ItemsPanel_NEW.repaint(); //
        }
    }
    
    
    private void prepareActions() {
        // This method sets up any additional action listeners or event handlers
        // that weren't already set up in prepareElements()
        
        // Currently all action listeners are set up in:
        // - createPlayerButtonsPanel()
        // - createPlayerMovesPanel()
        // So this method can be empty, but we'll keep it for future expansion
    }
}