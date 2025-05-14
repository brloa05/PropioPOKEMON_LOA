package presentation;

import domain.*;
import domain.Movimiento.ObjetivoEfecto;
import domain.Movimiento.TipoEfecto;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static final Map<String, String> POKEMON_IMAGE_MAP = createPokemonImageMap();

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
            // Cambiar la ruta para coincidir con la ubicación real del archivo
            URL backgroundUrl = getClass().getResource("/resource/Desktop - 4.gif");
            if (backgroundUrl != null) {
                backgroundImage = ImageIO.read(backgroundUrl);
            } else {
                System.out.println("No se pudo encontrar la imagen de fondo");
                createBackupImages();
            }
            
            playerPokemonImage = loadPokemonImage(pokemonJugador.getNombre(), true);
            enemyPokemonImage = loadPokemonImage(pokemonOponente.getNombre(), false);
        } catch (IOException e) {
            System.out.println("Error al cargar imagen: " + e.getMessage());
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

        // Panel de batalla (área superior)
        JPanel battlePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        battlePanel.setOpaque(false);
        battlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel del enemigo
        JPanel enemyInfoPanel = createPokemonInfoPanel(pokemonOponente, false);
        battlePanel.add(enemyInfoPanel);
        battlePanel.add(new JPanel() {
            {setOpaque(false);}
        });

        // Panel del jugador
        JPanel playerInfoPanel = createPokemonInfoPanel(pokemonJugador, true);
        battlePanel.add(new JPanel() {
            {setOpaque(false);}
        });
        battlePanel.add(playerInfoPanel);

        // Panel de acciones (área inferior negra)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sistema de cartas para los menús
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

        // Botón de retorno
        JButton btnReturn = createStyledButton("Volver", new Color(200, 0, 0));
        btnReturn.addActionListener(e -> actionCardLayout.show(actionCards, "MAIN_BUTTONS"));
        movesPanel.add(btnReturn);
        movesPanel.add(new JPanel() {{setOpaque(false);}});

        // Agregar los paneles al sistema de cartas
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
        mainPanel.setPreferredSize(new Dimension(800, 600));
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

    private ImageIcon loadPokemonImage(String pokemonName, boolean isPlayer) {
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
                double scale = Math.min(250.0 / originalIcon.getIconWidth(), 
                                     250.0 / originalIcon.getIconHeight());
                int width = (int)(originalIcon.getIconWidth() * scale);
                int height = (int)(originalIcon.getIconHeight() * scale);
                
                return new ImageIcon(originalIcon.getImage().getScaledInstance(
                    width, height, Image.SCALE_DEFAULT));
            }
            return originalIcon;
        } catch (Exception e) {
            System.err.println("Error cargando GIF animado para " + pokemonName + 
                             ": " + e.getMessage());
            return null;
        }
    }
    
    public void updateMovements(Pokemon pokemon) {
        if (pokemon != null && pokemonGUI != null) {
            Map<Pokemon, List<Movimiento>> movements = pokemonGUI.getPokemonMovements();
            if (movements != null && movements.containsKey(pokemon)) {
                List<Movimiento> pokemonMoves = movements.get(pokemon);
                // Actualizar los botones de movimiento
                for (int i = 0; i < moveButtons.length; i++) {
                    if (i < pokemonMoves.size()) {
                        Movimiento move = pokemonMoves.get(i);
                        moveButtons[i].setText(move.getName());
                        moveButtons[i].setEnabled(true);
                    } else {
                        moveButtons[i].setText("-");
                        moveButtons[i].setEnabled(false);
                    }
                }
            }
        }
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

        playerPokemonImage = createPlaceholderImage();
        enemyPokemonImage = createPlaceholderImage();
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
        
        // Barra de salud
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
        JOptionPane.showMessageDialog(this, "Función de items en desarrollo");
    }

    private void handlePokemonChange() {
        JOptionPane.showMessageDialog(this, "Función de cambio de Pokémon en desarrollo");
    }

    private void executeMove(int moveIndex) {
        Movimiento movimiento = pokemonGUI.getPokemonMovements().get(pokemonJugador).get(moveIndex);
        
        int damage = calculateDamage(movimiento, pokemonJugador, pokemonOponente);
        pokemonOponente.recibirDanio(damage);
        
        updateHealthBars();
        
        if (pokemonOponente.estaDebilitado()) {
            handlePokemonFainted(pokemonOponente);
        } else {
            enemyTurn();
        }
    }

    private int calculateDamage(Movimiento movimiento, Pokemon attacker, Pokemon defender) {
        double baseDamage = movimiento.getPotencia() * 
            ((double)attacker.getAtaque() / defender.getDefensa());
        return (int)(baseDamage * (0.85 + Math.random() * 0.15));
    }

    private void enemyTurn() {
        List<Movimiento> enemyMoves = pokemonGUI.getPokemonMovements().get(pokemonOponente);
        if (!enemyMoves.isEmpty()) {
            int moveIndex = (int)(Math.random() * enemyMoves.size());
            Movimiento selectedMove = enemyMoves.get(moveIndex);
            
            int damage = calculateDamage(selectedMove, pokemonOponente, pokemonJugador);
            pokemonJugador.recibirDanio(damage);
            
            updateHealthBars();
            
            if (pokemonJugador.estaDebilitado()) {
                handlePokemonFainted(pokemonJugador);
            }
        }
    }

    private void handlePokemonFainted(Pokemon pokemon) {
        String message = pokemon.getNombre() + " se ha debilitado!";
        JOptionPane.showMessageDialog(this, message);
        
        if (pokemon == pokemonJugador) {
            JOptionPane.showMessageDialog(this, "Has perdido la batalla!");
            pokemonGUI.mostrarMenuPrincipal();
        } else {
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
}