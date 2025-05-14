package presentation;

import domain.*;
import domain.Movimiento.ObjetivoEfecto;
import domain.Movimiento.TipoEfecto;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class BattlePanel extends JPanel {
    private PokemonGUI pokemonGUI;
    private Entrenador jugador;
    private Entrenador oponente;
    private Pokemon pokemonJugador;
    private Pokemon pokemonOponente;
    private BufferedImage backgroundImage;
    private BufferedImage playerPokemonImage;
    private BufferedImage enemyPokemonImage;
    private JPanel actionPanel;
    private JButton[] moveButtons = new JButton[4];
    private JProgressBar playerHealthBar;
    private JProgressBar enemyHealthBar;
    private JLabel playerNameLabel;
    private JLabel enemyNameLabel;
    private JLabel playerStateLabel;
    private JLabel enemyStateLabel;
    private JTextArea battleLog;
    private CardLayout actionCardLayout;
    private JPanel actionCards;

    public BattlePanel(PokemonGUI pokemonGUI, Entrenador jugador, Entrenador oponente) {
        this.pokemonGUI = pokemonGUI;
        
        if (jugador == null || oponente == null) {
            cargarDatosDePrueba();
        } else {
            this.jugador = jugador;
            this.oponente = oponente;
        }

        // Inicializar los Pokémon activos
        this.pokemonJugador = this.jugador.getPokemones().get(0);
        this.pokemonOponente = this.oponente.getPokemones().get(0);

        prepareElements();
        prepareActions();
    }

    private void cargarDatosDePrueba() {
        this.jugador = new EntrenadorPlayer("Ash", "player");
        this.oponente = new ExpertTrainer("Gary", "expert");
        
        List<Movimiento> movimientosCharizard = new ArrayList<>();
        movimientosCharizard.add(new Fisico("Lanzallamas", 90, 100, 15, 
            "Puede quemar (10%)", TipoEfecto.QUEMAR, ObjetivoEfecto.OBJETIVO, 0));
        movimientosCharizard.add(new Especial("Giro Fuego", 35, 85, 15, 
            "Puede quemar (10%)", TipoEfecto.QUEMAR, ObjetivoEfecto.OBJETIVO, 0));
        
        List<Movimiento> movimientosBlastoise = new ArrayList<>();
        movimientosBlastoise.add(new Especial("Hidrobomba", 110, 80, 5, 
            "Potente ataque de agua", TipoEfecto.NINGUNO, ObjetivoEfecto.OBJETIVO, 0));
        movimientosBlastoise.add(new Fisico("Cabezazo", 80, 90, 15, 
            "Ataque físico contundente", TipoEfecto.NINGUNO, ObjetivoEfecto.OBJETIVO, 0));
        
        List<Pokemon> equipoJugador = new ArrayList<>();
        equipoJugador.add(new PokemonUnTipo("CHARIZARD", 50, movimientosCharizard));
        
        List<Pokemon> equipoOponente = new ArrayList<>();
        equipoOponente.add(new PokemonUnTipo("BLASTOISE", 50, movimientosBlastoise));
        
        this.jugador.setPokemones(equipoJugador);
        this.oponente.setPokemones(equipoOponente);
    }

    private void prepareElements() {
        setLayout(new BorderLayout());
        
        // Cargar imágenes
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/resource/battle_background.gif"));
            playerPokemonImage = loadPokemonImage(pokemonJugador.getNombre(), true);
            enemyPokemonImage = loadPokemonImage(pokemonOponente.getNombre(), false);
        } catch (IOException e) {
            e.printStackTrace();
            createBackupImages();
        }

        // Panel principal con fondo
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

        // Panel de batalla (área roja en la imagen)
        JPanel battlePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        battlePanel.setOpaque(false);
        battlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel del enemigo (área superior)
        JPanel enemyInfoPanel = createPokemonInfoPanel(pokemonOponente, false);
        battlePanel.add(enemyInfoPanel);
        battlePanel.add(new JPanel() {
            {setOpaque(false);}
        }); // Espacio en blanco transparente

        // Panel del jugador (área inferior)
        JPanel playerInfoPanel = createPokemonInfoPanel(pokemonJugador, true);
        battlePanel.add(new JPanel() {
            {setOpaque(false);}
        }); // Espacio en blanco transparente
        battlePanel.add(playerInfoPanel);

        // Panel de acciones (área negra en la imagen)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sistema de cartas para alternar entre menús
        actionCardLayout = new CardLayout();
        actionCards = new JPanel(actionCardLayout);
        actionCards.setOpaque(false);

        // Panel de botones principales
        JPanel mainButtonsPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        mainButtonsPanel.setOpaque(false);
        
        // Botones con sus colores específicos
        JButton btnFight = createStyledButton("LUCHA", new Color(0, 122, 255));
        JButton btnItems = createStyledButton("ITEMS", new Color(255, 204, 0));
        JButton btnPokemon = createStyledButton("POKEMON", new Color(255, 149, 0));

        mainButtonsPanel.add(btnFight);
        mainButtonsPanel.add(btnItems);
        mainButtonsPanel.add(btnPokemon);

        // Panel de movimientos
        JPanel movesPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        movesPanel.setOpaque(false);
        
        List<Movimiento> movimientos = new ArrayList<>();
        // Obtener los movimientos del Pokémon si están disponibles
        Map<Pokemon, List<Movimiento>> pokemonMoves = pokemonGUI.getPokemonMovements();
        if (pokemonMoves != null && pokemonMoves.containsKey(pokemonJugador)) {
            movimientos = pokemonMoves.get(pokemonJugador);
        }

        // Crear los botones de movimiento
        for (int i = 0; i < 4; i++) {
            if (i < movimientos.size()) {
                moveButtons[i] = createStyledButton(movimientos.get(i).getName(), new Color(70, 130, 180));
                moveButtons[i].setActionCommand("MOVE_" + i);
            } else {
                moveButtons[i] = createStyledButton("-", Color.GRAY);
                moveButtons[i].setEnabled(false);
            }
            movesPanel.add(moveButtons[i]);
        }

        // Botón de retorno en el panel de movimientos
        JButton btnReturn = createStyledButton("Volver", new Color(200, 0, 0));
        btnReturn.addActionListener(e -> actionCardLayout.show(actionCards, "MAIN_BUTTONS"));
        movesPanel.add(btnReturn);
        movesPanel.add(new JPanel() {
            {setOpaque(false);}
        }); // Panel vacío para mantener la simetría

        // Agregar los paneles de cartas
        actionCards.add(mainButtonsPanel, "MAIN_BUTTONS");
        actionCards.add(movesPanel, "MOVES");

        // Configurar acciones de los botones principales
        btnFight.addActionListener(e -> actionCardLayout.show(actionCards, "MOVES"));
        btnItems.addActionListener(e -> handleItems());
        btnPokemon.addActionListener(e -> handlePokemonChange());

        bottomPanel.add(actionCards, BorderLayout.CENTER);

        // Agregar todo al panel principal
        mainPanel.add(battlePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Asegurar que el panel principal ocupe todo el espacio disponible
        mainPanel.setPreferredSize(new Dimension(800, 600));
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

    private JPanel createPokemonInfoPanel(Pokemon pokemon, boolean isPlayer) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // Panel de información (nombre, HP, estado)
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        infoPanel.setOpaque(false);
        
        // Nombre del Pokémon
        JLabel nameLabel = new JLabel(pokemon.getNombre());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Barra de salud con color variable según el porcentaje
        JProgressBar healthBar = new JProgressBar(0, pokemon.getSaludMaxima());
        healthBar.setValue(pokemon.getSalud());
        healthBar.setForeground(getHealthColor(pokemon));
        healthBar.setString(pokemon.getSalud() + "/" + pokemon.getSaludMaxima());
        healthBar.setStringPainted(true);
        
        // Estado
        JLabel stateLabel = new JLabel("Normal");
        stateLabel.setForeground(Color.WHITE);

        if (isPlayer) {
            playerNameLabel = nameLabel;
            playerHealthBar = healthBar;
            playerStateLabel = stateLabel;
        } else {
            enemyNameLabel = nameLabel;
            enemyHealthBar = healthBar;
            enemyStateLabel = stateLabel;
        }

        infoPanel.add(nameLabel);
        infoPanel.add(healthBar);
        infoPanel.add(stateLabel);

        // Panel de la imagen del Pokémon
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        
        // Cargar y escalar la imagen del Pokémon
        BufferedImage pokemonImage = isPlayer ? playerPokemonImage : enemyPokemonImage;
        ImageIcon pokemonIcon = new ImageIcon(pokemonImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(pokemonIcon);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        panel.add(infoPanel);
        panel.add(imagePanel);

        return panel;
    }

    private Color getHealthColor(Pokemon pokemon) {
        double percentage = (double) pokemon.getSalud() / pokemon.getSaludMaxima();
        if (percentage > 0.5) return Color.GREEN;
        if (percentage > 0.25) return Color.YELLOW;
        return Color.RED;
    }

    private void prepareActions() {
        // Acciones para los botones principales
        for (Component comp : ((JPanel)actionCards.getComponent(0)).getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton)comp;
                switch (button.getText()) {
                    case "LUCHA":
                        button.addActionListener(e -> actionCardLayout.show(actionCards, "MOVES"));
                        break;
                    case "ITEMS":
                        button.addActionListener(e -> handleItems());
                        break;
                    case "POKEMON":
                        button.addActionListener(e -> handlePokemonChange());
                        break;
                }
            }
        }

        // Acción para el botón de retorno en el panel de movimientos
        for (Component comp : ((JPanel)actionCards.getComponent(1)).getComponents()) {
            if (comp instanceof JButton && ((JButton)comp).getText().equals("Volver")) {
                ((JButton)comp).addActionListener(e -> actionCardLayout.show(actionCards, "MAIN_BUTTONS"));
            }
        }

        // Acciones para los botones de movimiento
        for (int i = 0; i < moveButtons.length; i++) {
            if (moveButtons[i].isEnabled()) {
                final int moveIndex = i;
                moveButtons[i].addActionListener(e -> {
                    executeMove(moveIndex);
                    actionCardLayout.show(actionCards, "MAIN_BUTTONS");
                });
            }
        }
    }

    private void handleItems() {
        // Implementar manejo de items
        JOptionPane.showMessageDialog(this, "Función de items en desarrollo");
    }

    private void handlePokemonChange() {
        // Implementar cambio de Pokémon
        JOptionPane.showMessageDialog(this, "Función de cambio de Pokémon en desarrollo");
    }

    private void executeMove(int moveIndex) {
        Movimiento movimiento = pokemonGUI.getPokemonMovements().get(pokemonJugador).get(moveIndex);
        
        // Calcular y aplicar el daño
        int damage = calculateDamage(movimiento, pokemonJugador, pokemonOponente);
        pokemonOponente.recibirDanio(damage);
        
        // Actualizar la interfaz
        updateHealthBars();
        
        // Verificar si el oponente fue derrotado
        if (pokemonOponente.estaDebilitado()) {
            handlePokemonFainted(pokemonOponente);
        } else {
            // Turno del oponente
            enemyTurn();
        }
    }

    private int calculateDamage(Movimiento movimiento, Pokemon attacker, Pokemon defender) {
        // Implementar cálculo de daño según las reglas de Pokémon
        double baseDamage = movimiento.getPotencia() * 
            ((double)attacker.getAtaque() / defender.getDefensa());
        return (int)(baseDamage * (0.85 + Math.random() * 0.15));
    }

    private void enemyTurn() {
        List<Movimiento> enemyMoves = pokemonGUI.getPokemonMovements().get(pokemonOponente);
        if (!enemyMoves.isEmpty()) {
            // Seleccionar un movimiento aleatorio
            int moveIndex = (int)(Math.random() * enemyMoves.size());
            Movimiento selectedMove = enemyMoves.get(moveIndex);
            
            // Calcular y aplicar el daño
            int damage = calculateDamage(selectedMove, pokemonOponente, pokemonJugador);
            pokemonJugador.recibirDanio(damage);
            
            // Actualizar la interfaz
            updateHealthBars();
            
            // Verificar si el jugador fue derrotado
            if (pokemonJugador.estaDebilitado()) {
                handlePokemonFainted(pokemonJugador);
            }
        }
    }

    private void handlePokemonFainted(Pokemon pokemon) {
        String message = pokemon.getNombre() + " se ha debilitado!";
        JOptionPane.showMessageDialog(this, message);
        
        if (pokemon == pokemonJugador) {
            // El jugador perdió
            JOptionPane.showMessageDialog(this, "Has perdido la batalla!");
            pokemonGUI.mostrarMenuPrincipal();
        } else {
            // El jugador ganó
            JOptionPane.showMessageDialog(this, "¡Has ganado la batalla!");
            pokemonGUI.mostrarMenuPrincipal();
        }
    }

    private void updateHealthBars() {
        // Actualizar barra de salud del jugador
        playerHealthBar.setValue(pokemonJugador.getSalud());
        playerHealthBar.setString(pokemonJugador.getSalud() + "/" + pokemonJugador.getSaludMaxima());
        playerHealthBar.setForeground(getHealthColor(pokemonJugador));
        
        // Actualizar barra de salud del enemigo
        enemyHealthBar.setValue(pokemonOponente.getSalud());
        enemyHealthBar.setString(pokemonOponente.getSalud() + "/" + pokemonOponente.getSaludMaxima());
        enemyHealthBar.setForeground(getHealthColor(pokemonOponente));
        
        revalidate();
        repaint();
    }

    private BufferedImage loadPokemonImage(String pokemonName, boolean isPlayer) throws IOException {
        String suffix = isPlayer ? "Usuario" : "Objetivo";
        String imagePath = "/" + pokemonName.toLowerCase() + "-" + suffix + ".gif";
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl == null) {
            throw new IOException("No se encontró la imagen: " + imagePath);
        }
        return ImageIO.read(imageUrl);
    }

    private void createBackupImages() {
        backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = backgroundImage.createGraphics();
        g2d.setColor(new Color(100, 150, 200));
        g2d.fillRect(0, 0, 800, 600);
        g2d.dispose();

        playerPokemonImage = createPlaceholderImage();
        enemyPokemonImage = createPlaceholderImage();
    }

    private BufferedImage createPlaceholderImage() {
        BufferedImage img = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, 120, 120);
        g2d.setColor(Color.BLACK);
        g2d.drawString("Pokémon", 30, 60);
        g2d.dispose();
        return img;
    }
}