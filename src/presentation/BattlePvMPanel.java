package presentation;

import domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener; // Asegúrate que esté importado
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BattlePvMPanel extends JPanel {
    private PokemonGUI pokemonGUI;
    private Entrenador jugador;
    private EntrenadorMachine maquina;
    private Pokemon pokemonJugador;
    private Pokemon pokemonMaquina;

    private ImageIcon backgroundImageIcon;
    private BufferedImage staticBackgroundImageFallback;

    private JPanel mainPanel; // Panel principal que contendrá todo
    private JPanel battleAreaPanel; // Panel para los sprites y barras de vida
    private JPanel actionPanel; // Panel inferior para log y botones de acción del jugador
    
    private JButton[] moveButtons;
    private JProgressBar playerHealthBar;
    private JProgressBar enemyHealthBar;
    private JLabel playerNameLabel;
    private JLabel enemyNameLabel;
    private JTextArea battleLog;
    private CardLayout actionCardLayout; // Para cambiar entre panel de botones y panel de movimientos
    private JPanel actionCards;        // El panel que usa el CardLayout

    public BattlePvMPanel(PokemonGUI pokemonGUI, Entrenador jugador, EntrenadorMachine maquina) {
        this.pokemonGUI = pokemonGUI;
        this.jugador = jugador;
        this.maquina = maquina;

        if (jugador != null && jugador.getPokemones() != null && !jugador.getPokemones().isEmpty()) {
            this.pokemonJugador = jugador.getPokemones().get(0);
        } else {
            // Manejar error o asignar un Pokémon por defecto si es necesario
            // Por ahora, si no hay Pokémon, la batalla no debería iniciar correctamente.
            // Esto debería ser validado antes de crear BattlePvMPanel.
            // throw new IllegalStateException("El jugador no tiene Pokémon para la batalla.");
            // Para evitar un NullPointerException inmediato, podemos crear un placeholder,
            // pero lo ideal es que PokemonGUI asegure que el jugador tiene equipo.
        }

        if (maquina != null && maquina.getPokemones() != null && !maquina.getPokemones().isEmpty()) {
            this.pokemonMaquina = maquina.getPokemones().get(0);
        } else {
            // Similar al jugador, la máquina debería tener Pokémon.
            // throw new IllegalStateException("La máquina no tiene Pokémon para la batalla.");
        }
        
        // Asegurar que Pokémon no sean null para evitar NullPointerExceptions en prepareElements
        if (this.pokemonJugador == null && this.jugador != null) this.jugador.setPokemones(new ArrayList<>()); // Evitar NPE
        if (this.pokemonMaquina == null && this.maquina != null) this.maquina.setPokemones(new ArrayList<>()); // Evitar NPE


        prepareElements();
        prepareActions(); // Configura listeners para botones de acción ya creados
        updatePokemonDisplay(); // Para asegurar que todo se muestre correctamente al inicio
    }

    private ImageIcon loadPokemonImage(String pokemonName, boolean isPlayer) {
        if (pokemonName == null || pokemonName.isEmpty()) {
            return new ImageIcon(createPlaceholderImage());
        }
        String suffix = isPlayer ? "-Usuario.gif" : "-Objetivo.gif";
        String[] possiblePaths = {
            "/" + pokemonName.toLowerCase() + suffix,
            "/resources/" + pokemonName.toLowerCase() + suffix
        };
        for (String path : possiblePaths) {
            URL imageUrl = getClass().getResource(path);
            if (imageUrl != null) {
                try {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                        return icon;
                    }
                } catch (Exception e) {
                    // Continuar
                }
            }
        }
        return new ImageIcon(createPlaceholderImage());
    }

    private BufferedImage createPlaceholderImage() {
        BufferedImage img = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, 120, 120);
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "?";
        int x = (120 - fm.stringWidth(text)) / 2;
        int y = ((120 - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(text, x, y);
        g2d.dispose();
        return img;
    }

    private void createBackupImages() {
        staticBackgroundImageFallback = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = staticBackgroundImageFallback.createGraphics();
        g2d.setColor(new Color(70, 130, 180));
        g2d.fillRect(0, 0, 800, 600);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        String text = "Fondo de Batalla No Disponible";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (800 - fm.stringWidth(text)) / 2;
        int y = ((600 - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(text, x, y);
        g2d.dispose();
    }
    
    private void prepareElements() {
        setLayout(new BorderLayout());
        this.backgroundImageIcon = null;
        try {
            URL backgroundUrl = getClass().getResource("/battle_background.gif");
            if (backgroundUrl != null) {
                this.backgroundImageIcon = new ImageIcon(backgroundUrl);
                if (this.backgroundImageIcon.getIconWidth() <= 0) this.backgroundImageIcon = null;
            }
            if (this.backgroundImageIcon == null) {
                backgroundUrl = getClass().getResource("/resources/battle_background.gif");
                if (backgroundUrl != null) {
                    this.backgroundImageIcon = new ImageIcon(backgroundUrl);
                    if (this.backgroundImageIcon.getIconWidth() <= 0) this.backgroundImageIcon = null;
                }
            }
            if (this.backgroundImageIcon == null) {
                if (this.staticBackgroundImageFallback == null) createBackupImages();
            } else {
                this.staticBackgroundImageFallback = null;
            }
        } catch (Exception e) {
            if (this.staticBackgroundImageFallback == null) createBackupImages();
        }

        mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImageIcon != null && backgroundImageIcon.getImage() != null) {
                    g.drawImage(backgroundImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else if (staticBackgroundImageFallback != null) {
                    g.drawImage(staticBackgroundImageFallback, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(30,30,70));
                    g.fillRect(0,0,getWidth(),getHeight());
                    g.setColor(Color.WHITE);
                    g.drawString("Error de fondo", 50,50);
                }
            }
        };
        mainPanel.setOpaque(false);

        battleAreaPanel = new JPanel(new GridBagLayout());
        battleAreaPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        // Validar que pokemonMaquina y pokemonJugador no sean null antes de crear sus paneles
        if (pokemonMaquina == null) { // Fallback si la máquina no tiene Pokémon (no debería ocurrir)
             maquina.setPokemones(new ArrayList<>()); // Evitar NPE
             maquina.getPokemones().add(new PokemonUnTipo("MISSINGNO",100, new ArrayList<>())); // Placeholder
             pokemonMaquina = maquina.getPokemones().get(0);
        }
        JPanel enemyPanel = createPokemonInfoPanel(pokemonMaquina, false);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5; gbc.weighty = 0.5; 
        gbc.anchor = GridBagConstraints.NORTHEAST; gbc.insets = new Insets(5,5,5,5);
        battleAreaPanel.add(enemyPanel, gbc);

        if (pokemonJugador == null) { // Fallback si el jugador no tiene Pokémon
            jugador.setPokemones(new ArrayList<>());
            jugador.getPokemones().add(new PokemonUnTipo("JUGADOR_SIN_PKM",100, new ArrayList<>()));
            pokemonJugador = jugador.getPokemones().get(0);
        }
        JPanel playerPanel = createPokemonInfoPanel(pokemonJugador, true);
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.SOUTHWEST;
        battleAreaPanel.add(playerPanel, gbc);
        
        actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.BLACK);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        actionCardLayout = new CardLayout();
        actionCards = new JPanel(actionCardLayout);
        actionCards.setOpaque(false);

        JPanel mainButtonsPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // Espacio horizontal entre botones
        mainButtonsPanel.setOpaque(false);
        JButton btnFight = createStyledButton("LUCHA", new Color(220,50,50)); // Rojo para Lucha
        JButton btnItems = createStyledButton("ITEMS", new Color(50,150,220)); // Azul para Items
        JButton btnPokemon = createStyledButton("POKEMON", new Color(50,200,100)); // Verde para Pokémon
        mainButtonsPanel.add(btnFight);
        mainButtonsPanel.add(btnItems);
        mainButtonsPanel.add(btnPokemon);

        JPanel movesPanelContainer = new JPanel(new BorderLayout());
        movesPanelContainer.setOpaque(false);
        JPanel movesDisplayPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        movesDisplayPanel.setOpaque(false);
        
        moveButtons = new JButton[4];
        // La actualización de los botones de movimiento se hará en updatePokemonDisplay y prepareActions
        for (int i = 0; i < 4; i++) {
            moveButtons[i] = createStyledButton("-", Color.GRAY); // Placeholder inicial
            moveButtons[i].setEnabled(false);
            movesDisplayPanel.add(moveButtons[i]);
        }
        movesPanelContainer.add(movesDisplayPanel, BorderLayout.CENTER);
        JButton btnReturnMoves = createStyledButton("Volver", new Color(100,100,100));
        movesPanelContainer.add(btnReturnMoves, BorderLayout.SOUTH);

        actionCards.add(mainButtonsPanel, "MAIN_BUTTONS");
        actionCards.add(movesPanelContainer, "MOVES");
        actionCardLayout.show(actionCards, "MAIN_BUTTONS");
        
        battleLog = new JTextArea(5, 30);
        battleLog.setEditable(false);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        battleLog.setForeground(Color.WHITE);
        battleLog.setBackground(new Color(20,20,20));
        JScrollPane scrollLog = new JScrollPane(battleLog);
        scrollLog.setPreferredSize(new Dimension(0,120)); // Un poco más de altura para el log

        actionPanel.add(actionCards, BorderLayout.CENTER);
        actionPanel.add(scrollLog, BorderLayout.SOUTH);
        
        mainPanel.add(battleAreaPanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setPreferredSize(new Dimension(800, 600));

        if(jugador != null && maquina != null) { // Solo si ambos están definidos
            updateBattleLog("Batalla PvM: " + jugador.getName() + " vs " + maquina.getName());
            updateBattleLog("Turno de " + jugador.getName() + ".");
        }
    }
    
    private void prepareActions() {
        // Los listeners para btnFight, btnItems, btnPokemon se configuran en updatePokemonDisplay
        // o se pueden configurar aquí una vez si los botones son campos de clase globales del panel de botones.
        // Por simplicidad, la lógica de actualización de movimientos también se hace en updatePokemonDisplay.
        // Asegurémonos que los botones de acción principales (Lucha, Items, Pokemon) tengan sus listeners aquí.
        
        // Obtenemos los botones del mainButtonsPanel (el primer card de actionCards)
        // Esto es un poco indirecto; sería mejor si btnFight, etc., fueran campos de clase.
        // Por ahora, asumimos que updatePokemonDisplay los reconfigura si es necesario.
        // La lógica de updatePokemonDisplay ya re-crea los action listeners para los botones de movimiento.
        // Para LUCHA, ITEMS, POKEMON (botones del mainButtonsPanel)
        // Si los extraemos como campos de clase:
        // btnFight.addActionListener(e -> actionCardLayout.show(actionCards, "MOVES"));
        // btnItems.addActionListener(e -> handleItems());
        // btnPokemon.addActionListener(e -> handlePokemonChange());
        // El método updatePokemonDisplay() ha sido modificado para añadir estos listeners.
    }

    private JPanel createPokemonInfoPanel(Pokemon pokemon, boolean isPlayer) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));


        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.setPreferredSize(new Dimension(160, 160)); 

        ImageIcon pokemonIcon = loadPokemonImage(pokemon.getNombre(), isPlayer);
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        if (pokemonIcon != null && pokemonIcon.getIconWidth() > 0) {
            imageLabel.setIcon(pokemonIcon);
        } else {
            imageLabel.setText("No Img");
            imageLabel.setForeground(Color.WHITE);
            imageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        }
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        JPanel infoTextPanel = new JPanel(new GridLayout(3, 1, 3, 3));
        infoTextPanel.setOpaque(false);
        infoTextPanel.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,80),1));
        infoTextPanel.setBackground(new Color(0,0,0,50)); // Fondo oscuro semitransparente
        infoTextPanel.setPreferredSize(new Dimension(160, 70));


        JLabel nameLabel = new JLabel(pokemon.getNombre(), SwingConstants.CENTER);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JProgressBar healthBar = new JProgressBar(0, pokemon.getSaludMaxima() > 0 ? pokemon.getSaludMaxima() : 100);
        healthBar.setValue(pokemon.getSalud());
        healthBar.setForeground(getHealthColor(pokemon));
        healthBar.setString(pokemon.getSalud() + "/" + pokemon.getSaludMaxima());
        healthBar.setStringPainted(true);
        healthBar.setOpaque(false);
        healthBar.setPreferredSize(new Dimension(150, 18));

        JLabel stateLabel = new JLabel("Normal", SwingConstants.CENTER); 
        stateLabel.setForeground(Color.WHITE);
        stateLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        if (isPlayer) {
            this.playerNameLabel = nameLabel;
            this.playerHealthBar = healthBar;
        } else {
            this.enemyNameLabel = nameLabel;
            this.enemyHealthBar = healthBar;
        }

        infoTextPanel.add(nameLabel);
        infoTextPanel.add(healthBar);
        infoTextPanel.add(stateLabel);
        
        if(isPlayer){ 
            panel.add(imagePanel); 
            panel.add(Box.createRigidArea(new Dimension(0,5)));
            panel.add(infoTextPanel);
        } else { 
            panel.add(infoTextPanel);
            panel.add(Box.createRigidArea(new Dimension(0,5)));
            panel.add(imagePanel);
        }
        return panel;
    }

    private Color getHealthColor(Pokemon pokemon) {
        if (pokemon == null || pokemon.getSaludMaxima() == 0) return Color.GRAY;
        double percentage = (double) pokemon.getSalud() / pokemon.getSaludMaxima();
        if (percentage > 0.5) return Color.GREEN;
        if (percentage > 0.2) return Color.ORANGE; // Cambiado de YELLOW para mejor contraste
        return Color.RED;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1), // Borde exterior sutil
            BorderFactory.createEmptyBorder(5, 15, 5, 15) // Padding interno
        ));
        button.setOpaque(true); 
        return button;
    }
    
    private Color getColorForMove(Movimiento movimiento) {
        Pokemon.Tipo tipoMovimiento = null;
        if (pokemonGUI != null && pokemonGUI.getPokemonAplication() != null) {
            for (Pokemon.Tipo tipo : Pokemon.Tipo.values()) {
                List<Movimiento> movimientosDelTipo = pokemonGUI.getPokemonAplication().getMovimientosPorTipo(tipo);
                if (movimientosDelTipo != null && movimientosDelTipo.contains(movimiento)) {
                    tipoMovimiento = tipo;
                    break;
                }
            }
        }
        if (tipoMovimiento != null) {
            switch (tipoMovimiento) {
                case FUEGO: return new Color(240, 128, 48);  // Naranja Fuego
                case AGUA: return new Color(104, 144, 240); // Azul Agua
                case PLANTA: return new Color(120, 200, 80); // Verde Planta
                case ELECTRICO: return new Color(248, 208, 48); // Amarillo Eléctrico
                case HIELO: return new Color(152, 216, 216); // Cian Hielo
                case LUCHA: return new Color(192, 48, 40);   // Rojo Lucha
                case VENENO: return new Color(160, 64, 160);  // Púrpura Veneno
                case TIERRA: return new Color(224, 192, 104); // Arena Tierra
                case VOLADOR: return new Color(168, 144, 240); // Lavanda Volador
                case PSIQUICO: return new Color(248, 88, 136);  // Rosa Psíquico
                case BICHO: return new Color(168, 184, 32);  // Verde Lima Bicho
                case ROCA: return new Color(184, 160, 56);   // Marrón Roca
                case FANTASMA: return new Color(112, 88, 152);  // Morado Fantasma
                case DRAGON: return new Color(112, 56, 248);   // Índigo Dragón
                case SINIESTRO: return new Color(112, 88, 72);   // Marrón Oscuro Siniestro
                case ACERO: return new Color(184, 184, 208); // Plata Acero
                case HADA: return new Color(238, 153, 172);  // Rosa Claro Hada
                default: return new Color(168, 168, 120);    // Grisáceo Normal/Default
            }
        }
        return new Color(168, 168, 120);
    }

    private void updateBattleLog(String message) {
        if (battleLog != null) {
            battleLog.append(message + "\n");
            battleLog.setCaretPosition(battleLog.getDocument().getLength());
        }
    }

    private void handleItems() {
        if (jugador == null || jugador.getItems() == null || jugador.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tienes objetos disponibles.", "Objetos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        List<Item> playerItems = jugador.getItems();
        String[] itemDisplayNames = playerItems.stream()
            .map(item -> {
                if (item instanceof Potion) return ((Potion)item).getNameItem();
                if (item instanceof SuperPotion) return ((SuperPotion)item).getNameItem();
                if (item instanceof HyperPotion) return ((HyperPotion)item).getNameItem();
                if (item instanceof Revive) return ((Revive)item).getNameItem();
                return item.getId();
            })
            .distinct() // Para no mostrar el mismo nombre de ítem múltiples veces si hay varios iguales (ej. 2 Potions)
            .toArray(String[]::new);

        if (itemDisplayNames.length == 0) {
            JOptionPane.showMessageDialog(this, "No tienes objetos seleccionables.", "Objetos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String chosenItemDisplayName = (String) JOptionPane.showInputDialog(
            this, "Selecciona un objeto para usar:", "Usar Objeto",
            JOptionPane.QUESTION_MESSAGE, null, itemDisplayNames, itemDisplayNames[0]);

        if (chosenItemDisplayName != null) {
            Item selectedItemInstance = null;
            // Encontrar la primera instancia del ítem con ese nombre/ID para consumir
            for (Item item : playerItems) {
                String currentItemDisplayName = "";
                if (item instanceof Potion) currentItemDisplayName = ((Potion)item).getNameItem();
                else if (item instanceof SuperPotion) currentItemDisplayName = ((SuperPotion)item).getNameItem();
                else if (item instanceof HyperPotion) currentItemDisplayName = ((HyperPotion)item).getNameItem();
                else if (item instanceof Revive) currentItemDisplayName = ((Revive)item).getNameItem();
                else currentItemDisplayName = item.getId();
                if (currentItemDisplayName.equals(chosenItemDisplayName)) {
                    selectedItemInstance = item; // Este es el objeto Item específico que se usará
                    break;
                }
            }

            if (selectedItemInstance == null) return;

            List<Pokemon> team = jugador.getPokemones();
            if (team == null || team.isEmpty()) {
                updateBattleLog("No hay Pokémon en el equipo para usar " + chosenItemDisplayName + ".");
                return;
            }

            List<Pokemon> validTargets = new ArrayList<>();
            if (selectedItemInstance.esRevive()) {
                for (Pokemon p : team) { if (p.estaDebilitado()) validTargets.add(p); }
            } else {
                for (Pokemon p : team) { if (!p.estaDebilitado() && p.getSalud() < p.getSaludMaxima()) validTargets.add(p); }
            }

            if (validTargets.isEmpty()) {
                String message = selectedItemInstance.esRevive() ? "No hay Pokémon debilitados para usar " + chosenItemDisplayName + "."
                                                               : "No hay Pokémon que necesiten " + chosenItemDisplayName + ".";
                updateBattleLog(message);
                JOptionPane.showMessageDialog(this, message, "No hay objetivos válidos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String[] pokemonNamesForDialog = validTargets.stream()
                                                .map(p -> p.getNombre() + " (HP: " + p.getSalud() + "/" + p.getSaludMaxima() + ")")
                                                .toArray(String[]::new);
            String chosenPokemonNameAndHp = (String) JOptionPane.showInputDialog(
                this, "Selecciona un Pokémon para usar " + chosenItemDisplayName + ":",
                "Usar Ítem en Pokémon", JOptionPane.QUESTION_MESSAGE, null,
                pokemonNamesForDialog, pokemonNamesForDialog[0]);

            if (chosenPokemonNameAndHp != null) {
                String chosenPokemonName = chosenPokemonNameAndHp.split(" \\(HP:")[0];
                Pokemon targetPokemon = validTargets.stream().filter(p -> p.getNombre().equals(chosenPokemonName)).findFirst().orElse(null);

                if (targetPokemon != null) {
                    boolean itemUsed = pokemonGUI.getPokemonAplication().usarItem(targetPokemon, selectedItemInstance, jugador);
                    if (itemUsed) {
                        updateBattleLog(jugador.getName() + " usó " + chosenItemDisplayName + " en " + targetPokemon.getNombre() + ".");
                        updateHealthBars();
                        realizarTurnoMaquina();
                    } else {
                        updateBattleLog(chosenItemDisplayName + " no tuvo efecto en " + targetPokemon.getNombre() + ".");
                        JOptionPane.showMessageDialog(this, chosenItemDisplayName + " no tuvo efecto en " + targetPokemon.getNombre() + ".",
                                                      "Ítem sin efecto", JOptionPane.WARNING_MESSAGE);
                        realizarTurnoMaquina(); // El intento de usar un ítem (aunque falle el efecto) consume el turno.
                    }
                } else {
                    // No se seleccionó un Pokémon válido, el turno podría pasar o no, según reglas.
                    // Para PvM, es justo que la máquina actúe si el jugador cancela/falla la selección.
                    realizarTurnoMaquina();
                }
            } else { // Jugador canceló la selección de Pokémon objetivo
                realizarTurnoMaquina();
            }
        }
        // Si el jugador cancela la selección del ítem, su turno no se consume explícitamente aquí,
        // se queda en el panel de acción principal.
    }

    private void handlePokemonChange() {
        if (jugador == null || jugador.getPokemones() == null) return;

        List<Pokemon> availablePokemon = jugador.getPokemones().stream()
            .filter(p -> p != pokemonJugador && p != null && !p.estaDebilitado())
            .collect(java.util.stream.Collectors.toList());
            
        if (availablePokemon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tienes más Pokémon disponibles para cambiar.", "Cambio de Pokémon", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] pokemonNames = availablePokemon.stream().map(Pokemon::getNombre).toArray(String[]::new);
        String selectedPokemonName = (String) JOptionPane.showInputDialog(
            this, "Selecciona un Pokémon:", "Cambiar Pokémon",
            JOptionPane.QUESTION_MESSAGE, null, pokemonNames, pokemonNames[0]);

        if (selectedPokemonName != null) {
            Pokemon newPokemon = availablePokemon.stream().filter(p -> p.getNombre().equals(selectedPokemonName)).findFirst().orElse(null);
            if (newPokemon != null) {
                updateBattleLog(jugador.getName() + " retira a " + pokemonJugador.getNombre() + ".");
                pokemonJugador = newPokemon;
                updateBattleLog(jugador.getName() + " envía a " + pokemonJugador.getNombre() + "!");
                updatePokemonDisplay();
                realizarTurnoMaquina();
            }
        }
    }

    private void executeMove(int moveIndex) {
        if (pokemonJugador == null || pokemonJugador.getMovimientos() == null || moveIndex >= pokemonJugador.getMovimientos().size() || pokemonJugador.estaDebilitado()){
             realizarTurnoMaquina(); // Si no puede atacar, pasa el turno
             return;
        }
        
        List<Movimiento> movimientosJugador;
        Map<String, Map<Pokemon, List<Movimiento>>> allPlayerMovements = pokemonGUI.playerMovements;
         if(allPlayerMovements != null && jugador != null && allPlayerMovements.containsKey(jugador.getName())){
             Map<Pokemon, List<Movimiento>> currentPlayerMoves = allPlayerMovements.get(jugador.getName());
             if(currentPlayerMoves != null && pokemonJugador != null && currentPlayerMoves.containsKey(pokemonJugador)){
                movimientosJugador = currentPlayerMoves.get(pokemonJugador);
             } else {
                movimientosJugador = pokemonJugador.getMovimientos();
             }
         } else {
            movimientosJugador = pokemonJugador.getMovimientos();
         }
         if (movimientosJugador == null || moveIndex >= movimientosJugador.size()) {
             realizarTurnoMaquina(); return;
         }


        Movimiento movimiento = movimientosJugador.get(moveIndex);
        
        updateBattleLog(pokemonJugador.getNombre() + " usó " + movimiento.getName() + "!");
        int damage = calculaDaño(movimiento, pokemonJugador, pokemonMaquina);
        pokemonMaquina.recibirDanio(damage);
        updateBattleLog(pokemonJugador.getNombre() + " causó " + damage + " puntos de daño a " + pokemonMaquina.getNombre() + ".");
        updateHealthBars();
            
        if (pokemonMaquina.estaDebilitado()) {
            handlePokemonFainted(pokemonMaquina, false); // false porque es el Pokémon de la máquina (no es el jugador)
        } else {
            realizarTurnoMaquina();
        }
    }

    private int calculaDaño(Movimiento movimiento, Pokemon atacante, Pokemon defensor) {
        if (movimiento == null || atacante == null || defensor == null) return 0;

        Pokemon.Tipo tipoMovimiento = null;
        if (pokemonGUI != null && pokemonGUI.getPokemonAplication() != null) {
            for (Pokemon.Tipo tipo : Pokemon.Tipo.values()) {
                List<Movimiento> movimientosDelTipo = pokemonGUI.getPokemonAplication().getMovimientosPorTipo(tipo);
                if (movimientosDelTipo != null && movimientosDelTipo.contains(movimiento)) {
                    tipoMovimiento = tipo;
                    break;
                }
            }
        }

        double efectividad = 1.0;
        if (tipoMovimiento != null) {
             efectividad = defensor.getEfectividad(tipoMovimiento);
        }

        double stab = 1.0;
        if (tipoMovimiento != null && atacante.getTipos().contains(tipoMovimiento)) {
            stab = 1.5;
        }
        
        double variacion = 0.85 + (Math.random() * 0.15);
        int danoBase = movimiento.getPotencia();
        double potenciaAtaque = (movimiento instanceof Fisico) ? atacante.getAtaque() : atacante.getAtaqueEspecial();
        double defensaObjetivo = (movimiento instanceof Fisico) ? defensor.getDefensa() : defensor.getDefensaEspecial();

        if (defensaObjetivo == 0) defensaObjetivo = 1;

        double calculoDano = ((((2.0 * atacante.getNivel() / 5.0) + 2.0) * danoBase * (potenciaAtaque / defensaObjetivo)) / 50.0) + 2.0;
        calculoDano *= stab * efectividad * variacion;
        
        if (danoBase == 0 && movimiento instanceof DeEstado) { // Movimientos de estado no hacen daño directo así
             calculoDano = 0;
        } else if (calculoDano < 1 && danoBase > 0) {
             calculoDano = 1; // Mínimo 1 de daño si el movimiento tiene potencia
        }
        return (int) calculoDano;
    }

    private void realizarTurnoMaquina() {
        updateBattleLog("Turno de " + maquina.getName() + "...");
        if (pokemonMaquina == null || maquina == null) return;

        if (pokemonMaquina.estaDebilitado()) {
            List<Item> itemsMaquina = maquina.getItems();
            Item reviveParaUsar = null;
            if(itemsMaquina != null) reviveParaUsar = itemsMaquina.stream().filter(Item::esRevive).findFirst().orElse(null);

            if (reviveParaUsar != null) {
                String resultado = reviveParaUsar.efectoItem(pokemonMaquina);
                if (!resultado.equals("No efectuo")) {
                    itemsMaquina.remove(reviveParaUsar);
                    updateBattleLog(maquina.getName() + " usó " + reviveParaUsar.getId() + " en " + pokemonMaquina.getNombre() + ".");
                    updateHealthBars();
                    // El Pokémon revivido puede actuar o el turno pasa. Aquí, el turno de la máquina termina tras usar el ítem.
                    updateBattleLog("Turno de " + jugador.getName() + "."); // Devuelve el turno al jugador
                    return;
                }
            }
            List<Pokemon> pokemonesDispMaquina = maquina.getPokemones().stream().filter(p -> p != null && !p.estaDebilitado()).collect(java.util.stream.Collectors.toList());
            if (!pokemonesDispMaquina.isEmpty()) {
                updateBattleLog(maquina.getName() + " retira a " + pokemonMaquina.getNombre() + ".");
                pokemonMaquina = pokemonesDispMaquina.get(0);
                updateBattleLog(maquina.getName() + " envía a " + pokemonMaquina.getNombre() + "!");
                updatePokemonDisplay(); // Actualizar UI con el nuevo Pokémon de la máquina
                // Continuar con la lógica de acción del nuevo Pokémon
            } else {
                handlePokemonFainted(pokemonMaquina, false); // false porque es el de la máquina
                return; // Fin de batalla
            }
        }
        
        double umbralSaludBaja = 0.40;
        if (!pokemonMaquina.estaDebilitado() && pokemonMaquina.getSalud() < (pokemonMaquina.getSaludMaxima() * umbralSaludBaja)) {
            List<Item> itemsMaquina = maquina.getItems();
            Item pocionParaUsar = null;
            if(itemsMaquina != null) {
                pocionParaUsar = itemsMaquina.stream()
                    .filter(item -> !item.esRevive() && item.getRecuperacion() > 0)
                    .max(java.util.Comparator.comparingInt(Item::getRecuperacion))
                    .orElse(null);
            }
            if (pocionParaUsar != null) {
                String resultado = pocionParaUsar.efectoItem(pokemonMaquina);
                if (!resultado.equals("No efectuo")) {
                    itemsMaquina.remove(pocionParaUsar);
                    updateBattleLog(maquina.getName() + " usó " + pocionParaUsar.getId() + " en " + pokemonMaquina.getNombre() + ".");
                    updateHealthBars();
                    updateBattleLog("Turno de " + jugador.getName() + "."); // Devuelve el turno al jugador
                    return;
                }
            }
        }

        List<Movimiento> movimientosDisponibles = (pokemonMaquina != null) ? pokemonMaquina.getMovimientos() : new ArrayList<>();
        if (movimientosDisponibles != null && !movimientosDisponibles.isEmpty()) {
            int index = (int) (Math.random() * movimientosDisponibles.size());
            Movimiento movimientoSeleccionado = movimientosDisponibles.get(index);
            
            updateBattleLog(pokemonMaquina.getNombre() + " usó " + movimientoSeleccionado.getName() + "!");
            int damage = calculaDaño(movimientoSeleccionado, pokemonMaquina, pokemonJugador);
            pokemonJugador.recibirDanio(damage);
            updateBattleLog(pokemonMaquina.getNombre() + " causó " + damage + " puntos de daño a " + pokemonJugador.getNombre() + ".");
            updateHealthBars();

            if (pokemonJugador.estaDebilitado()) {
                handlePokemonFainted(pokemonJugador, true); // true porque es el Pokémon del jugador
            } else {
                 updateBattleLog("Turno de " + jugador.getName() + "."); // Devuelve el turno al jugador
            }
        } else {
            updateBattleLog(pokemonMaquina.getNombre() + " no tiene movimientos y no puede atacar.");
            updateBattleLog("Turno de " + jugador.getName() + "."); // Devuelve el turno al jugador
        }
    }
    
    private void handlePokemonFainted(Pokemon faintedPokemon, boolean isPlayerPokemon) {
        updateBattleLog(faintedPokemon.getNombre() + " se ha debilitado!");

        if (isPlayerPokemon) { // Pokémon del jugador humano se debilitó
            List<Pokemon> availablePlayerPokemon = jugador.getPokemones().stream()
                .filter(p -> p != null && !p.estaDebilitado())
                .collect(java.util.stream.Collectors.toList());
            if (availablePlayerPokemon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "¡" + maquina.getName() + " ha ganado la batalla! No tienes más Pokémon.", "Fin de Batalla", JOptionPane.INFORMATION_MESSAGE);
                pokemonGUI.mostrarMenuPrincipal();
            } else {
                updateBattleLog(jugador.getName() + " debe cambiar de Pokémon.");
                handlePokemonChange(); // Jugador humano elige su siguiente Pokémon
            }
        } else { // Pokémon de la máquina se debilitó
            List<Pokemon> availableMachinePokemon = maquina.getPokemones().stream()
                .filter(p -> p != null && !p.estaDebilitado())
                .collect(java.util.stream.Collectors.toList());
            if (availableMachinePokemon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "¡" + jugador.getName() + " ha ganado la batalla! La Máquina no tiene más Pokémon.", "Fin de Batalla", JOptionPane.INFORMATION_MESSAGE);
                pokemonGUI.mostrarMenuPrincipal();
            } else {
                updateBattleLog(maquina.getName() + " debe cambiar de Pokémon.");
                pokemonMaquina = availableMachinePokemon.get(0); // Máquina elige el siguiente
                updateBattleLog(maquina.getName() + " envía a " + pokemonMaquina.getNombre() + "!");
                updatePokemonDisplay();
                updateBattleLog("Turno de " + jugador.getName() + "."); // Turno del jugador después del cambio de la máquina
            }
        }
    }

    public void updatePokemonDisplay() {
        // Actualizar panel del jugador
        if (jugador != null && pokemonJugador != null && mainPanel != null) {
            // Recrear o actualizar el panel del jugador
            // Esta parte es un poco compleja sin referencias directas a los JLabels dentro de los paneles
            // Lo más sencillo es forzar una reconstrucción parcial si es necesario, o asegurar que los campos globales se actualicen.
             if (battleAreaPanel != null) { // battleAreaPanel es el JPanel con GridBagLayout
                Component[] components = battleAreaPanel.getComponents();
                for (Component comp : components) { // Quitar los paneles de info viejos
                    if (comp instanceof JPanel) { // Asumiendo que los info panels son los únicos JPanels directos en battleAreaPanel
                        // Podríamos necesitar una forma más específica de identificar los paneles de info
                        // o simplemente limpiar y re-añadir.
                        // battleAreaPanel.remove(comp); // Cuidado con ConcurrentModificationException si se hace mal
                    }
                }
                battleAreaPanel.removeAll(); // Más simple pero puede ser un parpadeo

                GridBagConstraints gbc = new GridBagConstraints();
                if (pokemonMaquina != null) {
                    JPanel enemyPanel = createPokemonInfoPanel(pokemonMaquina, false);
                    gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5; gbc.weighty = 0.5; 
                    gbc.anchor = GridBagConstraints.NORTHEAST; gbc.insets = new Insets(5,5,5,5);
                    battleAreaPanel.add(enemyPanel, gbc);
                }
                if (pokemonJugador != null) {
                    JPanel playerPanel = createPokemonInfoPanel(pokemonJugador, true);
                    gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.SOUTHWEST;
                    battleAreaPanel.add(playerPanel, gbc);
                }
                battleAreaPanel.revalidate();
                battleAreaPanel.repaint();
            }
        }
        updateHealthBars(); // Siempre actualiza barras de vida

        // Actualizar botones de movimiento del jugador
        if (actionCards != null && actionCardLayout != null && moveButtons != null && jugador != null && pokemonJugador != null) {
            // Limpiar y re-añadir listeners a los botones de acción principales del jugador
            JPanel mainButtonsPanel = (JPanel) actionCards.getComponent(0); // Asumiendo que es el primero
            JButton btnFight = (JButton) mainButtonsPanel.getComponent(0);
            JButton btnItems = (JButton) mainButtonsPanel.getComponent(1);
            JButton btnPokemon = (JButton) mainButtonsPanel.getComponent(2);

            for(ActionListener al : btnFight.getActionListeners()) btnFight.removeActionListener(al);
            for(ActionListener al : btnItems.getActionListeners()) btnItems.removeActionListener(al);
            for(ActionListener al : btnPokemon.getActionListeners()) btnPokemon.removeActionListener(al);

            btnFight.addActionListener(e -> actionCardLayout.show(actionCards, "MOVES"));
            btnItems.addActionListener(e -> handleItems());
            btnPokemon.addActionListener(e -> handlePokemonChange());


            // Actualizar panel de movimientos
            JPanel movesPanelContainer = (JPanel) actionCards.getComponent(1); // Asumiendo que es el segundo
            JPanel movesDisplayPanel = (JPanel) movesPanelContainer.getComponent(0); // El GridLayout
            movesDisplayPanel.removeAll(); // Limpiar botones de movimiento antiguos

            List<Movimiento> movimientosJugador;
             Map<String, Map<Pokemon, List<Movimiento>>> allPlayerMovements = pokemonGUI.playerMovements;
             if(allPlayerMovements != null && jugador != null && allPlayerMovements.containsKey(jugador.getName())){
                 Map<Pokemon, List<Movimiento>> currentPlayerMoves = allPlayerMovements.get(jugador.getName());
                 if(currentPlayerMoves != null && pokemonJugador != null && currentPlayerMoves.containsKey(pokemonJugador)){
                    movimientosJugador = currentPlayerMoves.get(pokemonJugador);
                 } else {
                    movimientosJugador = pokemonJugador.getMovimientos(); // Fallback a los movimientos del objeto Pokémon
                 }
             } else {
                movimientosJugador = pokemonJugador.getMovimientos(); // Fallback
             }
             if (movimientosJugador == null) movimientosJugador = new ArrayList<>();


            for (int i = 0; i < 4; i++) {
                if (moveButtons[i] == null) moveButtons[i] = createStyledButton("-", Color.GRAY);
                for(ActionListener al : moveButtons[i].getActionListeners()) moveButtons[i].removeActionListener(al);

                if (i < movimientosJugador.size()) {
                    Movimiento mov = movimientosJugador.get(i);
                    moveButtons[i].setText(mov.getName());
                    moveButtons[i].setBackground(getColorForMove(mov));
                    moveButtons[i].setEnabled(true);
                    final int moveIndex = i;
                    moveButtons[i].addActionListener(e -> {
                        executeMove(moveIndex);
                        // executeMove ahora llama a realizarTurnoMaquina o maneja fin, no se necesita cambiar card aquí
                    });
                } else {
                    moveButtons[i].setText("-");
                    moveButtons[i].setBackground(Color.GRAY);
                    moveButtons[i].setEnabled(false);
                }
                movesDisplayPanel.add(moveButtons[i]);
            }
            movesDisplayPanel.revalidate();
            movesDisplayPanel.repaint();
        }
        revalidate(); // Revalidar todo el BattlePvMPanel
        repaint();
    }
    
    private void updateHealthBars() {
        if (playerHealthBar != null && pokemonJugador != null) {
            playerHealthBar.setMaximum(pokemonJugador.getSaludMaxima() > 0 ? pokemonJugador.getSaludMaxima() : 100);
            playerHealthBar.setValue(pokemonJugador.getSalud());
            playerHealthBar.setString(pokemonJugador.getSalud() + "/" + pokemonJugador.getSaludMaxima());
            playerHealthBar.setForeground(getHealthColor(pokemonJugador));
        }
         if (playerNameLabel!= null && pokemonJugador != null){
            playerNameLabel.setText(pokemonJugador.getNombre());
        }


        if (enemyHealthBar != null && pokemonMaquina != null) {
            enemyHealthBar.setMaximum(pokemonMaquina.getSaludMaxima() > 0 ? pokemonMaquina.getSaludMaxima() : 100);
            enemyHealthBar.setValue(pokemonMaquina.getSalud());
            enemyHealthBar.setString(pokemonMaquina.getSalud() + "/" + pokemonMaquina.getSaludMaxima());
            enemyHealthBar.setForeground(getHealthColor(pokemonMaquina));
        }
        if (enemyNameLabel != null && pokemonMaquina != null){
            enemyNameLabel.setText(pokemonMaquina.getNombre());
        }
    }
}