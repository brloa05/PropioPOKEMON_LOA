package presentation;

import domain.*;

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

public class BattlePvMPanel extends JPanel {
    private PokemonGUI pokemonGUI;
    private Entrenador jugador;
    private EntrenadorMachine maquina;
    private Pokemon pokemonJugador;
    private Pokemon pokemonMaquina;
    private BufferedImage backgroundImage;
    private BufferedImage playerPokemonImage;
    private BufferedImage enemyPokemonImage;
    private JPanel actionPanel;
    private JButton[] moveButtons;
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

 // En el constructor de BattlePvMPanel
    public BattlePvMPanel(PokemonGUI pokemonGUI, Entrenador jugador2, EntrenadorMachine maquina) {
        this.pokemonGUI = pokemonGUI;
        this.jugador = jugador2;
        this.maquina = maquina;
        prepareElements();
        prepareActions();
    }

    // En su lugar, agregar un método para inicializar los Pokémon cuando sea el momento
    public void initializePokemon(Pokemon pokemonJugador, Pokemon pokemonMaquina) {
        this.pokemonJugador = pokemonJugador;
        this.pokemonMaquina = pokemonMaquina;
        updatePokemonDisplay(); // Actualizar la interfaz con los Pokémon seleccionados
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
        moveButtons = new JButton[4];
        for (int i = 0; i < moveButtons.length; i++) {
            moveButtons[i] = new JButton();
            moveButtons[i].setPreferredSize(new Dimension(200, 50));
            moveButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            moveButtons[i].setOpaque(true);
            moveButtons[i].setBackground(new Color(51, 51, 51));
            moveButtons[i].setForeground(Color.WHITE);
            moveButtons[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
        
        setLayout(new BorderLayout());
        
        // Cargar imágenes
        try {
            URL backgroundUrl = getClass().getResource("/resource/Desktop - 4.gif");
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

        // Panel principal con fondo
        try {
            URL backgroundUrl = getClass().getResource("/resource/Desktop - 4.gif");
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

        // Panel de batalla
        JPanel battlePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        battlePanel.setOpaque(false);
        battlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Crear paneles de información vacíos inicialmente
        JPanel machineInfoPanel = createEmptyInfoPanel();
        JPanel playerInfoPanel = createEmptyInfoPanel();
        
        battlePanel.add(machineInfoPanel);
        battlePanel.add(new JPanel() {{setOpaque(false);}});
        battlePanel.add(new JPanel() {{setOpaque(false);}});
        battlePanel.add(playerInfoPanel);

        // Panel de acciones
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        actionCardLayout = new CardLayout();
        actionCards = new JPanel(actionCardLayout);
        actionCards.setOpaque(false);

        // Botones principales
        JPanel mainButtonsPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        mainButtonsPanel.setOpaque(false);
        
        JButton btnFight = createStyledButton("LUCHA", new Color(0, 122, 255));
        JButton btnItems = createStyledButton("ITEMS", new Color(255, 204, 0));
        JButton btnPokemon = createStyledButton("POKEMON", new Color(255, 149, 0));

        mainButtonsPanel.add(btnFight);
        mainButtonsPanel.add(btnItems);
        mainButtonsPanel.add(btnPokemon);

        // Configurar acciones de botones
        btnFight.addActionListener(e -> actionCardLayout.show(actionCards, "MOVES"));
        btnItems.addActionListener(e -> handleItems());
        btnPokemon.addActionListener(e -> handlePokemonChange());

        bottomPanel.add(actionCards, BorderLayout.CENTER);

        Container mainPanel = null;
		// Agregar todo al panel principal
        mainPanel.add(battlePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        mainPanel.setPreferredSize(new Dimension(800, 600));
    }

    private JPanel createEmptyInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        // Agregar placeholder para nombre y barra de salud
        JLabel nameLabel = new JLabel("...");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JProgressBar healthBar = new JProgressBar(0, 100);
        healthBar.setValue(0);
        healthBar.setString("0/0");
        healthBar.setStringPainted(true);
        
        JLabel stateLabel = new JLabel("...");
        stateLabel.setForeground(Color.WHITE);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        infoPanel.setOpaque(false);
        infoPanel.add(nameLabel);
        infoPanel.add(healthBar);
        infoPanel.add(stateLabel);

        panel.add(infoPanel);
        return panel;
    }

    
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
                default: return new Color(51, 51, 51);
            }
        }
        return new Color(51, 51, 51);
    }

    private BufferedImage loadPokemonImage(String pokemonName, boolean isPlayer) {
        if (pokemonName == null || pokemonName.isEmpty()) {
            return createPlaceholderImage();
        }

        String imagePath = POKEMON_IMAGE_MAP.get(pokemonName.toUpperCase());
        if (imagePath == null) {
            System.err.println("No se encontró ruta de imagen para: " + pokemonName);
            return createPlaceholderImage();
        }

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

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        
        try {
            BufferedImage pokemonImage = loadPokemonImage(pokemon.getNombre(), isPlayer);
            Image scaledImage = pokemonImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            BufferedImage placeholder = createPlaceholderImage();
            Image scaledPlaceholder = placeholder.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JLabel placeholderLabel = new JLabel(new ImageIcon(scaledPlaceholder));
            imagePanel.add(placeholderLabel, BorderLayout.CENTER);
        }

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
        // Acciones para los botones principales ya configuradas en prepareElements()
        
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
    	List<Item> items = jugador.getItems();
        if (items == null || items.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No tienes objetos disponibles", 
                "Objetos", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        System.out.println("Items disponibles: " + items.size());
        for (Item item : items) {
            System.out.println(" - " + item.getId());
        }

        String[] itemIds = items.stream()
                .map(Item::getId)
                .toArray(String[]::new);

        String selectedItemId = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona un objeto para usar:",
            "Usar Objeto",
            JOptionPane.QUESTION_MESSAGE,
            null,
            itemIds,
            itemIds[0]
        );

        if (selectedItemId != null) {
            Item item = items.stream()
                .filter(i -> i.getId().equals(selectedItemId))
                .findFirst()
                .orElse(null);

            if (item != null) {
                aplicarItem(item, pokemonJugador);
                updateHealthBars();
                // Después de usar el item, es el turno de la máquina
                realizarTurnoMaquina();
            }
        }
    }

    private void aplicarItem(Item item, Pokemon pokemon) {
        // Usar el método de efecto del item
        String resultado = item.efectoItem(pokemon);
        if (!resultado.equals("No efectuo")) {
            jugador.getItems().remove(item);
            updateHealthBars(); // Actualizar las barras de vida para mostrar el efecto de curación
        }
    }

    private void handlePokemonChange() {
        List<Pokemon> availablePokemon = jugador.getPokemones().stream()
            .filter(p -> p != pokemonJugador && p.getSalud() > 0)
            .collect(java.util.stream.Collectors.toList());

        if (availablePokemon.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No tienes más Pokémon disponibles para cambiar", 
                "Cambio de Pokémon", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] pokemonNames = availablePokemon.stream()
            .map(Pokemon::getNombre)
            .toArray(String[]::new);

        String selectedPokemon = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona un Pokémon:",
            "Cambiar Pokémon",
            JOptionPane.QUESTION_MESSAGE,
            null,
            pokemonNames,
            pokemonNames[0]
        );

        if (selectedPokemon != null) {
            Pokemon newPokemon = availablePokemon.stream()
                .filter(p -> p.getNombre().equals(selectedPokemon))
                .findFirst()
                .orElse(null);

            if (newPokemon != null) {
                pokemonJugador = newPokemon;
                updatePokemonDisplay();
                realizarTurnoMaquina();
            }
        }
    }

    private void executeMove(int moveIndex) {
        // Obtener los movimientos del pokémon actual desde el mapa de movimientos
        Map<Pokemon, List<Movimiento>> pokemonMoves = pokemonGUI.getPokemonMovements();
        List<Movimiento> movimientos = pokemonMoves.get(pokemonJugador);
        
        if (moveIndex < movimientos.size()) {
            Movimiento movimiento = movimientos.get(moveIndex);
            
            for (Pokemon.Tipo tipo : Pokemon.Tipo.values()) {
                if (pokemonGUI.getPokemonAplication().getMovimientosPorTipo(tipo).contains(movimiento)) {
                    break;
                }
            }
            
            // Realizar el ataque
            int damage = calculaDaño(movimiento, pokemonJugador, pokemonMaquina);
            pokemonMaquina.recibirDanio(damage);
            
            updateHealthBars();
            
            if (pokemonMaquina.estaDebilitado()) {
                handlePokemonFainted(pokemonMaquina);
            } else {
                realizarTurnoMaquina();
            }
        }
    }

    private int calculaDaño(Movimiento movimiento, Pokemon atacante, Pokemon defensor) {
        // Simplificar el cálculo de daño usando solo la potencia, ataque y defensa
        double baseDamage = movimiento.getPotencia() * 
            ((double)atacante.getAtaque() / defensor.getDefensa());
        
        // Factor aleatorio entre 0.85 y 1.0
        return (int)(baseDamage * (0.85 + Math.random() * 0.15));
    }

    private void realizarTurnoMaquina() {
    // Primero verificamos si debemos usar un item (cuando la vida está baja)
    if (pokemonMaquina.getSalud() < pokemonMaquina.getSaludMaxima() * 0.3) {
        // Obtener los items que la máquina tiene disponibles
        List<Item> itemsMaquina = maquina.getItems();
        
        // Intentar encontrar una poción (cualquier item que no sea Revive)
        Item itemParaUsar = itemsMaquina.stream()
            .filter(item -> !item.esRevive())
            .findFirst()
            .orElse(null);

        if (itemParaUsar != null) {
            // Mostrar mensaje sobre el uso del item
            JOptionPane.showMessageDialog(this, 
                "La máquina usa " + itemParaUsar.getId() + " en " + pokemonMaquina.getNombre(),
                "Uso de Item",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Aplicar el efecto del item y actualizar
            String resultado = itemParaUsar.efectoItem(pokemonMaquina);
            if (!resultado.equals("No efectuo")) {
                itemsMaquina.remove(itemParaUsar);
                updateHealthBars();
                return; // Terminar el turno después de usar el item
            }
        }
    }
    // Si el Pokémon está debilitado y tenemos un Revive, usarlo
    else if (pokemonMaquina.estaDebilitado()) {
        List<Item> itemsMaquina = maquina.getItems();
        Item revive = itemsMaquina.stream()
            .filter(Item::esRevive)
            .findFirst()
            .orElse(null);

        if (revive != null) {
            JOptionPane.showMessageDialog(this, 
                "La máquina usa " + revive.getId() + " en " + pokemonMaquina.getNombre(),
                "Uso de Revive",
                JOptionPane.INFORMATION_MESSAGE);
            
            String resultado = revive.efectoItem(pokemonMaquina);
            if (!resultado.equals("No efectuo")) {
                itemsMaquina.remove(revive);
                updateHealthBars();
                return;
            }
        }
    }

    // Si no usamos item o el Pokémon no necesita curación, realizar ataque
    Map<Pokemon, List<Movimiento>> pokemonMoves = pokemonGUI.getPokemonMovements();
    List<Movimiento> movimientosDisponibles = pokemonMoves.get(pokemonMaquina);
    
    if (movimientosDisponibles != null && !movimientosDisponibles.isEmpty()) {
        // Elegir un movimiento aleatorio
        int index = (int)(Math.random() * movimientosDisponibles.size());
        Movimiento movimiento = movimientosDisponibles.get(index);
        
        // Mostrar el movimiento usado
        JOptionPane.showMessageDialog(this,
            pokemonMaquina.getNombre() + " usa " + movimiento.getName() + "!",
            "Ataque de la Máquina",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Realizar el ataque
        int damage = calculaDaño(movimiento, pokemonMaquina, pokemonJugador);
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
            // Verificar si el jugador tiene más pokémon disponibles
            List<Pokemon> availablePokemon = jugador.getPokemones().stream()
                .filter(p -> p.getSalud() > 0)
                .collect(java.util.stream.Collectors.toList());

            if (availablePokemon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "¡Has perdido la batalla!");
                pokemonGUI.mostrarMenuPrincipal();
            } else {
                handlePokemonChange();
            }
        } else {
            // Verificar si la máquina tiene más pokémon disponibles
            List<Pokemon> availablePokemon = maquina.getPokemones().stream()
                .filter(p -> p.getSalud() > 0)
                .collect(java.util.stream.Collectors.toList());

            if (availablePokemon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "¡Has ganado la batalla!");
                pokemonGUI.mostrarMenuPrincipal();
            } else {
                // Seleccionar el siguiente pokémon disponible de la máquina
                pokemonMaquina = availablePokemon.get(0);
                updatePokemonDisplay();
            }
        }
    }

    private void updatePokemonDisplay() {
        // Actualizar imágenes
        playerPokemonImage = loadPokemonImage(pokemonJugador.getNombre(), true);
        enemyPokemonImage = loadPokemonImage(pokemonMaquina.getNombre(), false);
        
        // Actualizar información en la interfaz
        playerNameLabel.setText(pokemonJugador.getNombre());
        enemyNameLabel.setText(pokemonMaquina.getNombre());
        updateHealthBars();
        
        // Actualizar movimientos disponibles
        Map<Pokemon, List<Movimiento>> pokemonMoves = pokemonGUI.getPokemonMovements();
        List<Movimiento> movimientos = pokemonMoves.get(pokemonJugador);
        
        System.out.println("Actualizando movimientos para: " + pokemonJugador.getNombre());
        
        for (int i = 0; i < moveButtons.length; i++) {
            if (movimientos != null && i < movimientos.size()) {
                Movimiento mov = movimientos.get(i);
                moveButtons[i].setText(mov.getName());
                moveButtons[i].setEnabled(true);
                moveButtons[i].setBackground(getColorForMove(mov));
                System.out.println("Botón " + i + ": " + mov.getName());
            } else {
                moveButtons[i].setText("-");
                moveButtons[i].setEnabled(false);
                moveButtons[i].setBackground(new Color(51, 51, 51));
                System.out.println("Botón " + i + ": deshabilitado");
            }
        }
        
        revalidate();
        repaint();
    }

    private void updateHealthBars() {
        // Actualizar barra de salud del jugador
        playerHealthBar.setValue(pokemonJugador.getSalud());
        playerHealthBar.setString(pokemonJugador.getSalud() + "/" + pokemonJugador.getSaludMaxima());
        playerHealthBar.setForeground(getHealthColor(pokemonJugador));
        
        // Actualizar barra de salud de la máquina
        enemyHealthBar.setValue(pokemonMaquina.getSalud());
        enemyHealthBar.setString(pokemonMaquina.getSalud() + "/" + pokemonMaquina.getSaludMaxima());
        enemyHealthBar.setForeground(getHealthColor(pokemonMaquina));
        
        revalidate();
        repaint();
    }
    
    private void asegurarMainPanelInicializado() {
        JPanel mainPanel = null;
		if (mainPanel == null) {
            mainPanel = new javax.swing.JPanel();
            mainPanel.setLayout(new java.awt.BorderLayout()); // O el layout que uses normalmente
        }
    }
    
    public void prepararElementosSeguro() {
        asegurarMainPanelInicializado();
        prepareElements(); // Llama al método ya existente
    }
    
}