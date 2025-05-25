package presentation;

import domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class BattlePanel extends JPanel {
    private PokemonGUI pokemonGUI;
    private Entrenador jugador1;
    private Entrenador jugador2;
    private Pokemon pokemonJugador1;
    private Pokemon pokemonJugador2;

    private ImageIcon backgroundImageIcon;
    private BufferedImage backgroundImage; // Necesario para createBackupImages y el fallback en paintComponent

    private JButton[] moveButtonsPlayer1 = new JButton[4];
    private JButton[] moveButtonsPlayer2 = new JButton[4];
    private JProgressBar player1HealthBar;
    private JProgressBar player2HealthBar;
    private JLabel player1NameLabel;
    private JLabel player2NameLabel;

    private JLabel lblPokemonImagePlayer1;
    private JLabel lblPokemonImagePlayer2;
    private ImageIcon player1PokemonIcon;
    private ImageIcon player2PokemonIcon;

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
            if (jugador1 == null || jugador2 == null) {
                throw new IllegalStateException("Ambos jugadores deben estar definidos");
            }
            if (jugador1.getPokemones() == null || jugador1.getPokemones().isEmpty()) {
                throw new IllegalStateException("El jugador 1 (" + jugador1.getName() + ") no tiene Pokémon");
            }
            this.pokemonJugador1 = jugador1.getPokemones().get(0);

            if (jugador2.getPokemones() == null || jugador2.getPokemones().isEmpty()) {
                throw new IllegalStateException("El jugador 2 (" + jugador2.getName() + ") no tiene Pokémon");
            }
            this.pokemonJugador2 = jugador2.getPokemones().get(0);

            Map<String, Map<Pokemon, List<Movimiento>>> playerMovements = pokemonGUI.playerMovements;
            if (playerMovements == null || !playerMovements.containsKey(jugador1.getName())) {
                throw new IllegalStateException("No hay movimientos asignados para el jugador " + jugador1.getName());
            }
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

        System.out.println("--- INICIO DEPURACIÓN CARGA FONDO BATTLEPANEL ---");
        this.backgroundImageIcon = null; 
        String[] pathsToTry = {
            "/battle_background.gif", 
            "battle_background.gif",           
            "/resources/battle_background.gif", 
            "resources/battle_background.gif"   
        };

        for (String path : pathsToTry) {
            System.out.println("Intentando cargar fondo desde la ruta: \"" + path + "\"");
            URL backgroundUrl = null;
            try {
                backgroundUrl = getClass().getResource(path);
                System.out.println("  URL obtenida para \"" + path + "\": " + backgroundUrl);

                if (backgroundUrl != null) {
                    ImageIcon tempIcon = new ImageIcon(backgroundUrl);
                    if (tempIcon.getIconWidth() > 0 && tempIcon.getIconHeight() > 0) {
                        this.backgroundImageIcon = tempIcon;
                        System.out.println("  ÉXITO: ImageIcon de fondo cargado y válido desde: " + path);
                        this.backgroundImage = null;
                        break; 
                    } else {
                        System.out.println("  ADVERTENCIA: ImageIcon desde \"" + path + "\" (URL: " + backgroundUrl + ") parece inválido (ancho/alto es 0 o negativo).");
                    }
                }
            } catch (Exception e) {
                System.out.println("  ERROR: Excepción al intentar cargar/crear ImageIcon desde URL '" + backgroundUrl + "' para la ruta '" + path + "'. Mensaje: " + e.getMessage());
                // e.printStackTrace(); // Descomentar para ver el stack trace completo de la excepción
            }
            if (this.backgroundImageIcon != null) break; // Salir si ya se cargó
        }

        if (this.backgroundImageIcon == null) {
            System.out.println("FALLO TOTAL: No se pudo cargar backgroundImageIcon desde ninguna ruta intentada. Se procederá con createBackupImages().");
            if (this.backgroundImage == null) { // Solo crea el backup si aún no existe
                 createBackupImages();
            }
        }
        System.out.println("--- FIN DEPURACIÓN CARGA FONDO BATTLEPANEL ---");
        // --- FIN DE SECCIÓN DE CARGA DE IMAGEN DE FONDO ---

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImageIcon != null && backgroundImageIcon.getImage() != null) {
                    g.drawImage(backgroundImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(70, 130, 180));
                    g.fillRect(0,0, getWidth(), getHeight());
                    g.setColor(Color.WHITE);
                    FontMetrics fm = g.getFontMetrics();
                    String textoError = "Fondo no cargado";
                    int x = (getWidth() - fm.stringWidth(textoError)) / 2;
                    int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                    g.drawString(textoError, x, y);
                }
            }
        };
        mainPanel.setOpaque(false);

        JPanel battlePanel = new JPanel(new GridBagLayout());
        battlePanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        lblPokemonImagePlayer2 = new JLabel();
        lblPokemonImagePlayer2.setPreferredSize(new Dimension(150, 150));
        player2PokemonIcon = loadPokemonImage(pokemonJugador2.getNombre(), false);
        if (player2PokemonIcon != null && player2PokemonIcon.getIconWidth() > 0) {
            lblPokemonImagePlayer2.setIcon(player2PokemonIcon);
        } else {
            lblPokemonImagePlayer2.setText("Img J2 N/A");
        }
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        battlePanel.add(lblPokemonImagePlayer2, gbc);

        JPanel player2Panel = createPokemonInfoPanel(pokemonJugador2, false);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(20, 0, 0, 20);
        battlePanel.add(player2Panel, gbc);

        lblPokemonImagePlayer1 = new JLabel();
        lblPokemonImagePlayer1.setPreferredSize(new Dimension(150, 150));
        player1PokemonIcon = loadPokemonImage(pokemonJugador1.getNombre(), true);
        if (player1PokemonIcon != null && player1PokemonIcon.getIconWidth() > 0) {
            lblPokemonImagePlayer1.setIcon(player1PokemonIcon);
        } else {
            lblPokemonImagePlayer1.setText("Img J1 N/A");
        }
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        battlePanel.add(lblPokemonImagePlayer1, gbc);

        JPanel player1Panel = createPokemonInfoPanel(pokemonJugador1, true);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.insets = new Insets(0, 20, 20, 0);
        battlePanel.add(player1Panel, gbc);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        actionCardLayout = new CardLayout();
        actionCards = new JPanel(actionCardLayout);
        actionCards.setOpaque(false);

        createActionPanels();

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

        updateBattleLog("¡Comienza la batalla entre " + jugador1.getName() + " y " + jugador2.getName() + "!");
        updateBattleLog("Turno de " + jugador1.getName());
    }

    private void createActionPanels() {
        JPanel player1ButtonsPanel = createPlayerButtonsPanel(1);
        JPanel player2ButtonsPanel = createPlayerButtonsPanel(2);
        JPanel player1MovesPanel = createPlayerMovesPanel(1);
        JPanel player2MovesPanel = createPlayerMovesPanel(2);
        
        player1ItemsPanel_NEW = createPlayerItemsPanel_NEW(1);
        player2ItemsPanel_NEW = createPlayerItemsPanel_NEW(2);
        
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
        JButton btnItemsPanel_NEW = createStyledButton("ITEMS", new Color(255, 180, 0)); 
        JButton btnPokemon = createStyledButton("POKEMON", new Color(255, 149, 0));
        
        btnItemsPanel_NEW.addActionListener(e -> {
            updatePlayerItemsPanel_NEW(playerNum); 
            actionCardLayout.show(actionCards, "PLAYER" + playerNum + "_ITEMS_NEW");
        });
        
        btnFight.addActionListener(e -> actionCardLayout.show(actionCards, "PLAYER" + playerNum + "_MOVES"));
        btnPokemon.addActionListener(e -> handlePokemonChange(playerNum));

        buttonPanel.add(btnFight);
        buttonPanel.add(btnItemsPanel_NEW);
        buttonPanel.add(btnPokemon);

        return buttonPanel;
    }
    
    private void handlePokemonChange(int playerNum) {
        Entrenador currentPlayer = (playerNum == 1) ? jugador1 : jugador2;
        List<Pokemon> availablePokemons = currentPlayer.getPokemones();
        
        if (availablePokemons == null || availablePokemons.isEmpty() || availablePokemons.stream().allMatch(Pokemon::estaDebilitado)) {
            showError("No hay más Pokémon disponibles para cambiar.");
            return;
        }

        List<Pokemon> choosablePokemons = new ArrayList<>();
        for(Pokemon p : availablePokemons) {
            if(!p.estaDebilitado() && p != ((playerNum == 1) ? pokemonJugador1 : pokemonJugador2)) {
                 choosablePokemons.add(p);
            }
        }
        if(choosablePokemons.isEmpty()){
            showError("No hay otros Pokémon a los que cambiar o el único disponible ya está en batalla.");
            return;
        }


        String[] pokemonNames = choosablePokemons.stream()
                .map(Pokemon::getNombre)
                .toArray(String[]::new);
        
        if (pokemonNames.length == 0) {
            showError("No hay otros Pokémon disponibles para cambiar en este momento.");
            return;
        }

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
            Pokemon selectedPokemon = null;
            for(Pokemon p : choosablePokemons){
                if(p.getNombre().equals(selectedName)){
                    selectedPokemon = p;
                    break;
                }
            }

            if (selectedPokemon != null) {
                if (playerNum == 1) {
                    pokemonJugador1 = selectedPokemon;
                } else {
                    pokemonJugador2 = selectedPokemon;
                }
                updatePokemonDisplay(playerNum);
                updateBattleLog(currentPlayer.getName() + " cambió a " + selectedName + ".");
                switchTurns();
            }
        }
    }
    
    private JPanel createPlayerMovesPanel(int playerNum) {
        JPanel movesPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        movesPanel.setOpaque(false);

        Pokemon currentPokemon = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2;
        String playerName = (playerNum == 1) ? jugador1.getName() : jugador2.getName();
    
        List<Movimiento> movimientos = new ArrayList<>();
        if (pokemonGUI.playerMovements != null && pokemonGUI.playerMovements.containsKey(playerName)) {
            Map<Pokemon, List<Movimiento>> pokemonMovesMap = pokemonGUI.playerMovements.get(playerName);
            if (pokemonMovesMap != null && pokemonMovesMap.containsKey(currentPokemon)) {
                movimientos = pokemonMovesMap.get(currentPokemon);
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
                });
            } else {
                buttons[i] = createStyledButton("-", Color.GRAY);
                buttons[i].setEnabled(false);
            }
            movesPanel.add(buttons[i]);
        }

        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);
        
        JButton btnReturn = createStyledButton("Volver", new Color(200, 0, 0));
        btnReturn.addActionListener(e -> 
            actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS")
        );
        
        containerPanel.add(movesPanel, BorderLayout.CENTER);
        containerPanel.add(btnReturn, BorderLayout.SOUTH);
    
        return containerPanel;
    }

    private void switchTurns() {
        isPlayer1Turn = !isPlayer1Turn;
        actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS");
        updateBattleLog("Turno de " + (isPlayer1Turn ? jugador1.getName() : jugador2.getName()) + ".");
    }

    private void executeMove(int moveIndex, int playerNum) {
	    Pokemon attacker = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2;
	    Pokemon defender = (playerNum == 1) ? pokemonJugador2 : pokemonJugador1;
	    
	    Map<String, Map<Pokemon, List<Movimiento>>> playerMovements = pokemonGUI.playerMovements;
	    String playerName = (playerNum == 1) ? jugador1.getName() : jugador2.getName();
	    
	    List<Movimiento> pokemonMoves = playerMovements.get(playerName).get(attacker);
	    
	    if (pokemonMoves != null && moveIndex < pokemonMoves.size()) {
	        Movimiento movimiento = pokemonMoves.get(moveIndex);
    
            updateBattleLog(attacker.getNombre() + " usó " + movimiento.getName() + "!");
            int damage = calculateDamage(movimiento, attacker, defender);
            defender.recibirDanio(damage);
            updateBattleLog(attacker.getNombre() + " causó " + damage + " puntos de daño a " + defender.getNombre() + ".");
            updateHealthBars();
            
            if (defender.estaDebilitado()) {
                handlePokemonFainted(defender, (playerNum == 1) ? 2 : 1); 
            } else {
                switchTurns();
            }
        } else {
             switchTurns();
        }
    }

    /**
     * Calcula el daño que un movimiento causará.
     * Incluye lógica para STAB, efectividad de tipos, variación y golpes críticos (con posible Garra Afilada).
     *
     * @param movimiento El movimiento que se está utilizando.
     * @param atacante El Pokémon que realiza el ataque.
     * @param defensor El Pokémon que recibe el ataque.
     * @return El daño calculado como un entero.
     */
    private int calculateDamage(Movimiento movimiento, Pokemon atacante, Pokemon defensor) {
        // 1. OBTENER EL TIPO DEL MOVIMIENTO
        // Necesitamos determinar el tipo del movimiento para calcular STAB y efectividad.
        Pokemon.Tipo tipoMovimiento = null;
        // Se busca en la lista de movimientos de PokemonAplication para encontrar el tipo del movimiento.
        // Esta es una forma de hacerlo si los movimientos no almacenan su propio tipo directamente.
        if (pokemonGUI.getPokemonAplication() != null && pokemonGUI.getPokemonAplication().getMovimientosPorTipo(Pokemon.Tipo.AGUA) != null ) { // Ejemplo de tipo para verificación [cite: 773]
             for (Pokemon.Tipo tipo : Pokemon.Tipo.values()) { // Itera sobre todos los tipos de Pokémon existentes
                // Revisa si la lista de movimientos para el 'tipo' actual contiene el 'movimiento' usado.
                if (pokemonGUI.getPokemonAplication().getMovimientosPorTipo(tipo).contains(movimiento)) { // [cite: 773]
                    tipoMovimiento = tipo; // Asigna el tipo encontrado
                    break; // Termina el bucle una vez que se encuentra el tipo
                }
            }
        }

        // 2. CALCULAR EFECTIVIDAD DEL TIPO
        // Determina cuán efectivo es el tipo del movimiento contra los tipos del Pokémon defensor.
        double efectividad = 1.0; // Valor por defecto si no hay modificación de efectividad
        if (tipoMovimiento != null && defensor != null) { // Solo si tenemos un tipo de movimiento y un defensor
             efectividad = defensor.getEfectividad(tipoMovimiento); // Llama al método del Pokémon defensor [cite: 302, 607, 639, 775]
        }
        // Mostrar mensaje de efectividad en el log de batalla
        if (efectividad > 1.0) {
            updateBattleLog("¡Es súper efectivo!");
        } else if (efectividad < 1.0 && efectividad > 0.0) {
            updateBattleLog("No es muy efectivo...");
        } else if (efectividad == 0.0) {
            updateBattleLog(defensor.getNombre() + " es inmune al ataque!");
        }


        // 3. CALCULAR STAB (Same Type Attack Bonus)
        // Bonificación si el tipo del movimiento coincide con alguno de los tipos del Pokémon atacante.
        double stab = 1.0; // Valor por defecto
        if (tipoMovimiento != null && atacante.getTipos().contains(tipoMovimiento)) { // Comprueba si el atacante comparte tipo con el movimiento [cite: 777]
            stab = 1.5; // Bonificación STAB común
        }
        
        // 4. CALCULAR GOLPE CRÍTICO (incluyendo Garra Afilada)
        double probabilidadCriticoBase = 0.0625; // Probabilidad estándar de crítico (ej. 1/16 o 6.25%)

        // ---- INICIO LÓGICA GARRA AFILADA ----
        // Esta sección asume que existe una forma de acceder al ítem equipado por el Pokémon.
        // Idealmente, Pokemon tendría un método como `getObjetoEquipado()`.
        // Como no podemos modificar `Pokemon.java` según las reglas, esta es una simulación
        // de cómo se integraría si dicha funcionalidad existiera.
        /*
        Item itemEquipado = null;
        // Lógica para obtener el ítem equipado del 'atacante'.
        // Esto es conceptual y requeriría cambios en Entrenador o Pokemon para implementarse plenamente.
        // Por ejemplo, si Entrenador tuviera un Map<Pokemon, Item> para los ítems equipados:
        // if (jugador1.getPokemones().contains(atacante)) { // Si el atacante es del jugador 1
        //     itemEquipado = jugador1.getItemEquipadoPor(atacante); // Método hipotético
        // } else if (jugador2.getPokemones().contains(atacante)) { // Si el atacante es del jugador 2
        //     itemEquipado = jugador2.getItemEquipadoPor(atacante); // Método hipotético
        // }


        if (itemEquipado instanceof GarraAfilada) { // Si el ítem equipado es una GarraAfilada
            probabilidadCriticoBase += ((GarraAfilada) itemEquipado).getModificadorCritico(); // Añade el bono de la GarraAfilada
            // System.out.println(atacante.getNombre() + " tiene Garra Afilada, prob. crítico aumentada a: " + probabilidadCriticoBase);
        }
        */
        // ---- FIN LÓGICA GARRA AFILADA ----

        boolean esCritico = Math.random() < probabilidadCriticoBase; // Determina si el golpe es crítico
        double multiplicadorCritico = 1.0; // Daño normal por defecto

        if (esCritico) {
            multiplicadorCritico = 1.5; // Multiplicador estándar para golpes críticos
            updateBattleLog("¡Un golpe crítico!"); // Informa en el log de batalla
        }
        
        // 5. VARIACIÓN ALEATORIA DEL DAÑO
        // Introduce una pequeña aleatoriedad para que el daño no sea siempre el mismo.
        double variacion = 0.85 + (Math.random() * 0.15); // Factor de daño entre 0.85 y 1.0 [cite: 779]
        
        // 6. CÁLCULO BASE DEL DAÑO
        // Fórmula estándar de daño en Pokémon.
        int danoBaseMovimiento = movimiento.getPotencia(); // Potencia base del movimiento [cite: 780]
        
        // Determina si el movimiento es físico o especial para usar las estadísticas correspondientes.
        double potenciaAtaqueDelAtacante;
        double defensaDelDefensor;

        if (movimiento instanceof Fisico) { // Si el movimiento es de clase Fisico [cite: 781]
            potenciaAtaqueDelAtacante = atacante.getAtaque(); // Ataque físico del atacante [cite: 315]
            defensaDelDefensor = defensor.getDefensa();     // Defensa física del defensor [cite: 316]
        } else if (movimiento instanceof Especial) { // Si el movimiento es de clase Especial
            potenciaAtaqueDelAtacante = atacante.getAtaqueEspecial(); // Ataque especial del atacante [cite: 317]
            defensaDelDefensor = defensor.getDefensaEspecial();     // Defensa especial del defensor [cite: 318]
        } else { // Movimientos de estado u otros que no causan daño directo
            return 0; // Los movimientos de estado puros no infligen daño directo con esta fórmula
        }

        // Prevenir división por cero si la defensa es 0 (aunque no debería ocurrir en Pokémon estándar)
        if (defensaDelDefensor == 0) { // [cite: 781]
            defensaDelDefensor = 1;
        }

        // Fórmula principal de daño
        double calculoDano = ((((2.0 * atacante.getNivel() / 5.0) + 2.0) * danoBaseMovimiento * (potenciaAtaqueDelAtacante / defensaDelDefensor)) / 50.0) + 2.0; // [cite: 782]
        
        // 7. APLICAR TODOS LOS MODIFICADORES
        calculoDano *= stab;                // Aplica STAB
        calculoDano *= efectividad;         // Aplica efectividad de tipo
        calculoDano *= variacion;           // Aplica variación aleatoria
        calculoDano *= multiplicadorCritico; // Aplica daño por golpe crítico

        // Asegurar un daño mínimo de 1 si el movimiento tiene potencia y el cálculo resulta en menos.
        if (calculoDano < 1 && danoBaseMovimiento > 0) { // [cite: 783]
            calculoDano = 1;
        }
        
        // Si el movimiento no tiene potencia (ej. movimientos de estado que erróneamente llegan aquí), el daño es 0.
        if (danoBaseMovimiento == 0) {
            calculoDano = 0;
        }

        return (int) calculoDano; // Devuelve el daño final como entero
    }

    private void handlePokemonFainted(Pokemon faintedPokemon, int faintedPlayerNum) {
        updateBattleLog(faintedPokemon.getNombre() + " se ha debilitado!");

        Entrenador trainerOfFainted = (faintedPlayerNum == 1) ? jugador1 : jugador2;
        Entrenador opponentTrainer = (faintedPlayerNum == 1) ? jugador2 : jugador1;

        boolean canSwitch = false;
        if (trainerOfFainted.getPokemones() != null) {
            for (Pokemon p : trainerOfFainted.getPokemones()) {
                if (p != null && !p.estaDebilitado()) {
                    canSwitch = true;
                    break;
                }
            }
        }

        if (canSwitch) {
            updateBattleLog(trainerOfFainted.getName() + " debe cambiar de Pokémon.");
            // Forzar el cambio para el jugador cuyo Pokémon se debilitó
            // El handlePokemonChange ya tiene la lógica de diálogo.
            // Se debe llamar DESPUÉS de que el turno actual haya concluido visualmente si es necesario,
            // o el switchTurns se encargará.
            handlePokemonChange(faintedPlayerNum); // El jugador que perdió el pokémon debe cambiar
                                                   // handlePokemonChange internamente llama a switchTurns,
                                                   // lo que pasará el turno al otro jugador después del cambio.
        } else {
            handleVictory(opponentTrainer);
        }
    }

    private void handleVictory(Entrenador winner) {
        String message = "¡" + winner.getName() + " ha ganado la batalla!";
        updateBattleLog(message);
        JOptionPane.showMessageDialog(this, message, "Fin de Batalla", JOptionPane.INFORMATION_MESSAGE);
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

        String[] itemNames = items.stream().map(Item::getId).toArray(String[]::new);
        String selectedItemId = (String) JOptionPane.showInputDialog( this, "Selecciona un objeto:",
            "Usar Objeto", JOptionPane.QUESTION_MESSAGE, null, itemNames, itemNames[0]);

        if (selectedItemId != null) {
            Item selectedItem = items.stream().filter(i -> i.getId().equals(selectedItemId)).findFirst().orElse(null);
            if (selectedItem != null) {
                String resultado = selectedItem.efectoItem(currentPokemon);
                if (!resultado.equals("No efectuo")) {
                    pokemonGUI.getPokemonAplication().usarItem(currentPokemon, selectedItem, currentPlayer); // Para removerlo
                    updateHealthBars();
                    updateBattleLog(currentPlayer.getName() + " usó " + selectedItem.getId() + " en " + currentPokemon.getNombre() + ".");
                    updatePlayerItemsPanel_NEW(playerNum);
                    switchTurns();
                } else {
                     updateBattleLog(selectedItem.getId() + " no tuvo efecto.");
                }
            }
        }
    }

    private void updatePokemonDisplay(int playerNum) {
        Pokemon currentPokemonToDisplay = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2;
        JLabel currentLabel = (playerNum == 1) ? lblPokemonImagePlayer1 : lblPokemonImagePlayer2;
        ImageIcon currentIconField = (playerNum == 1) ? player1PokemonIcon : player2PokemonIcon;
        JLabel currentNameLabel = (playerNum == 1) ? player1NameLabel : player2NameLabel;

        currentIconField = loadPokemonImage(currentPokemonToDisplay.getNombre(), playerNum == 1);
        if (currentIconField != null && currentLabel != null && currentIconField.getIconWidth() > 0) {
            currentLabel.setIcon(currentIconField);
            currentLabel.setText("");
        } else if (currentLabel != null) {
            currentLabel.setIcon(null);
            currentLabel.setText("Img J" + playerNum + " N/A");
        }
        
        if(playerNum == 1) player1PokemonIcon = currentIconField; else player2PokemonIcon = currentIconField;

        if (currentNameLabel != null) currentNameLabel.setText(currentPokemonToDisplay.getNombre());
        updateMovements(playerNum);
        updateHealthBars();
    }

    private void updateMovements(int playerNum) {
        Pokemon currentPokemon = (playerNum == 1) ? pokemonJugador1 : pokemonJugador2;
        JButton[] buttons = (playerNum == 1) ? moveButtonsPlayer1 : moveButtonsPlayer2;
        
        Map<String, Map<Pokemon, List<Movimiento>>> playerMovements = pokemonGUI.playerMovements;
        String playerName = (playerNum == 1) ? jugador1.getName() : jugador2.getName();
        
        List<Movimiento> pokemonMoves = null;
        if (playerMovements != null && playerMovements.containsKey(playerName)) {
            Map<Pokemon, List<Movimiento>> specificPlayerMoves = playerMovements.get(playerName);
            if (specificPlayerMoves != null) {
                pokemonMoves = specificPlayerMoves.get(currentPokemon);
            }
        }
        if (pokemonMoves == null) pokemonMoves = new ArrayList<>();


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
        revalidate();
        repaint();
    }

    private void updateHealthBars() {
        if (player1HealthBar != null && pokemonJugador1 != null) {
            player1HealthBar.setMaximum(pokemonJugador1.getSaludMaxima());
            player1HealthBar.setValue(pokemonJugador1.getSalud());
            player1HealthBar.setString(pokemonJugador1.getSalud() + "/" + pokemonJugador1.getSaludMaxima());
            player1HealthBar.setForeground(getHealthColor(pokemonJugador1));
        }
        if (player2HealthBar != null && pokemonJugador2 != null) {
            player2HealthBar.setMaximum(pokemonJugador2.getSaludMaxima());
            player2HealthBar.setValue(pokemonJugador2.getSalud());
            player2HealthBar.setString(pokemonJugador2.getSalud() + "/" + pokemonJugador2.getSaludMaxima());
            player2HealthBar.setForeground(getHealthColor(pokemonJugador2));
        }
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
        if (pokemon == null || pokemon.getSaludMaxima() == 0) return Color.GRAY;
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
        Pokemon.Tipo tipoMovimiento = null;
        if (pokemonGUI.getPokemonAplication() != null) {
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
                default: return new Color(120, 120, 120); 
            }
        }
        return new Color(120, 120, 120);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private BufferedImage tryLoadImageCorrectly_NEW(String correctedPath) {
        try {
            URL imageUrl = getClass().getResource(correctedPath);
            if (imageUrl != null) {
                return ImageIO.read(imageUrl);
            }
        } catch (IOException e) {
            // No es necesario imprimir error aquí si es un intento y puede fallar
        }
        return null; 
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
                    // Continuar al siguiente path si hay error
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
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "?";
        int x = (120 - fm.stringWidth(text)) / 2;
        int y = ((120 - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(text, x, y);
        g2d.dispose();
        return img;
    }

    private void createBackupImages() {
        backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = backgroundImage.createGraphics();
        g2d.setColor(new Color(70, 130, 180)); // Un azul para el fondo de respaldo
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

    private JPanel createPokemonInfoPanel(Pokemon pokemon, boolean isPlayer) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(pokemon.getNombre());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JProgressBar healthBar = new JProgressBar(0, pokemon.getSaludMaxima() > 0 ? pokemon.getSaludMaxima() : 100);
        healthBar.setValue(pokemon.getSalud());
        healthBar.setForeground(getHealthColor(pokemon));
        healthBar.setString(pokemon.getSalud() + "/" + pokemon.getSaludMaxima());
        healthBar.setStringPainted(true);
        
        JLabel stateLabel = new JLabel("Normal"); // Esto podría necesitar actualizarse con el estado real del Pokémon
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
        JPanel itemsDisplayPanel = new JPanel(new GridLayout(1, 3, 5, 5)); 
        itemsDisplayPanel.setOpaque(false);

        Entrenador currentPlayer = (playerNum == 1) ? jugador1 : jugador2;
        JButton[] currentItemButtons = (playerNum == 1) ? itemButtonsPlayer1_NEW : itemButtonsPlayer2_NEW;
        
        updatePlayerItemsPanelContent(playerNum, itemsDisplayPanel, currentPlayer, currentItemButtons);

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

    private void updatePlayerItemsPanelContent(int playerNum, JPanel itemsDisplayPanel, Entrenador currentPlayer, JButton[] currentItemButtons) {
        itemsDisplayPanel.removeAll(); // Limpiar botones antiguos antes de añadir/actualizar
        List<Item> playerItems = (currentPlayer != null) ? currentPlayer.getItems() : new ArrayList<>();

        for (int i = 0; i < currentItemButtons.length; i++) {
            if (currentItemButtons[i] == null) { // Asegurar que el botón exista en el array
                 currentItemButtons[i] = createStyledButton("-", Color.GRAY); // Crear si no existe
            }
            for (ActionListener al : currentItemButtons[i].getActionListeners()) {
                currentItemButtons[i].removeActionListener(al);
            }

            if (playerItems != null && i < playerItems.size()) {
                Item item = playerItems.get(i);
                String itemName = "";
                if (item instanceof Potion) itemName = ((Potion)item).getNameItem();
                else if (item instanceof SuperPotion) itemName = ((SuperPotion)item).getNameItem();
                else if (item instanceof HyperPotion) itemName = ((HyperPotion)item).getNameItem();
                else if (item instanceof Revive) itemName = ((Revive)item).getNameItem();
                else itemName = item.getId();

                currentItemButtons[i].setText(itemName);
                currentItemButtons[i].setBackground(new Color(100, 100, 255));
                currentItemButtons[i].setEnabled(true);
                
                final int itemIndexInLoop = i;
                final Entrenador finalCurrentPlayerForListener = currentPlayer;
                final int finalPlayerNumForListener = playerNum;

                currentItemButtons[i].addActionListener(e -> {
                    if (itemIndexInLoop < finalCurrentPlayerForListener.getItems().size()) {
                        Item selectedItem = finalCurrentPlayerForListener.getItems().get(itemIndexInLoop);
                        List<Pokemon> team = finalCurrentPlayerForListener.getPokemones();

                        if (team == null || team.isEmpty()) {
                            updateBattleLog("No hay Pokémon en el equipo para usar " + selectedItem.getId() + ".");
                            actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS");
                            return;
                        }

                        List<Pokemon> validTargets = new ArrayList<>();
                        if (selectedItem.esRevive()) {
                            for (Pokemon p : team) { if (p.estaDebilitado()) validTargets.add(p); }
                        } else {
                            for (Pokemon p : team) { if (!p.estaDebilitado() && p.getSalud() < p.getSaludMaxima()) validTargets.add(p); }
                        }

                        if (validTargets.isEmpty()) {
                            String message = selectedItem.esRevive() ? "No hay Pokémon debilitados para usar " + selectedItem.getId() + "."
                                                                   : "No hay Pokémon que necesiten " + selectedItem.getId() + ".";
                            updateBattleLog(message);
                            JOptionPane.showMessageDialog(BattlePanel.this, message, "No hay objetivos válidos", JOptionPane.INFORMATION_MESSAGE);
                            actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS");
                            return;
                        }

                        String[] pokemonNamesForDialog = validTargets.stream()
                                                            .map(p -> p.getNombre() + " (HP: " + p.getSalud() + "/" + p.getSaludMaxima() + ")")
                                                            .toArray(String[]::new);
                        String chosenPokemonNameAndHp = (String) JOptionPane.showInputDialog( BattlePanel.this,
                            "Selecciona un Pokémon para usar " + selectedItem.getId() + ":", "Usar Ítem en Pokémon",
                            JOptionPane.QUESTION_MESSAGE, null, pokemonNamesForDialog, pokemonNamesForDialog[0]);

                        if (chosenPokemonNameAndHp != null) {
                            String chosenPokemonName = chosenPokemonNameAndHp.split(" \\(HP:")[0];
                            Pokemon targetPokemon = validTargets.stream().filter(p -> p.getNombre().equals(chosenPokemonName)).findFirst().orElse(null);

                            if (targetPokemon != null) {
                                boolean itemUsed = pokemonGUI.getPokemonAplication().usarItem(targetPokemon, selectedItem, finalCurrentPlayerForListener);
                                if (itemUsed) {
                                    updateBattleLog(finalCurrentPlayerForListener.getName() + " usó " + selectedItem.getId() + " en " + targetPokemon.getNombre() + ".");
                                    updateHealthBars();
                                    updatePlayerItemsPanel_NEW(finalPlayerNumForListener);
                                    switchTurns();
                                } else {
                                    updateBattleLog(selectedItem.getId() + " no tuvo efecto en " + targetPokemon.getNombre() + ".");
                                    JOptionPane.showMessageDialog(BattlePanel.this, selectedItem.getId() + " no tuvo efecto en " + targetPokemon.getNombre() + ".",
                                                                  "Ítem sin efecto", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        }
                        actionCardLayout.show(actionCards, isPlayer1Turn ? "PLAYER1_BUTTONS" : "PLAYER2_BUTTONS");
                    }
                });
            } else {
                currentItemButtons[i].setText("-");
                currentItemButtons[i].setBackground(Color.GRAY);
                currentItemButtons[i].setEnabled(false);
            }
            itemsDisplayPanel.add(currentItemButtons[i]); // Añadir el botón (nuevo o actualizado) al panel
        }
        itemsDisplayPanel.revalidate();
        itemsDisplayPanel.repaint();
    }


    private void updatePlayerItemsPanel_NEW(int playerNum) {
        JPanel targetPanel = null;
        Entrenador currentPlayer = null;
        JButton[] currentItemButtons = null;

        if (playerNum == 1) {
            targetPanel = player1ItemsPanel_NEW;
            currentPlayer = jugador1;
            currentItemButtons = itemButtonsPlayer1_NEW;
        } else {
            targetPanel = player2ItemsPanel_NEW;
            currentPlayer = jugador2;
            currentItemButtons = itemButtonsPlayer2_NEW;
        }

        if (targetPanel == null) { // Si el panel de items aún no se ha creado (en createActionPanels)
             if(playerNum == 1) {
                player1ItemsPanel_NEW = createPlayerItemsPanel_NEW(1); // Crear si es la primera vez
                targetPanel = player1ItemsPanel_NEW;
             } else {
                player2ItemsPanel_NEW = createPlayerItemsPanel_NEW(2);
                targetPanel = player2ItemsPanel_NEW;
             }
             // No es necesario llamar a updatePlayerItemsPanelContent aquí porque createPlayerItemsPanel_NEW ya lo hace.
        } else {
            // El panel ya existe, solo actualizar su contenido.
            // Necesitamos acceder al GridLayout dentro del containerPanel.
            if (targetPanel.getComponentCount() > 0 && targetPanel.getComponent(0) instanceof JPanel) {
                 JPanel itemsDisplayPanel = (JPanel) targetPanel.getComponent(0); // Asumiendo que es el primer componente (GridLayout)
                 updatePlayerItemsPanelContent(playerNum, itemsDisplayPanel, currentPlayer, currentItemButtons);
            }
        }
        if (targetPanel != null) {
            targetPanel.revalidate();
            targetPanel.repaint();
        }
    }
    
    private void prepareActions() {
    }
}