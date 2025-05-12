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
    private JButton btnItem;
    private JButton btnCambiar;
    private JLabel lblPlayerPokemon;
    private JLabel lblEnemyPokemon;
    private JLabel lblPlayerHealth;
    private JLabel lblEnemyHealth;
    private JTextArea txtBattleLog;

    public BattlePanel(PokemonGUI pokemonGUI, Entrenador jugador, Entrenador oponente) {
        this.pokemonGUI = pokemonGUI;
        
        if (jugador == null || oponente == null) {
            cargarDatosDePrueba();
        } else {
            this.jugador = jugador;
            this.oponente = oponente;
        }
        
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
        
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/battle_background.gif"));
            playerPokemonImage = loadPokemonImage(pokemonJugador.getNombre(), true);
            enemyPokemonImage = loadPokemonImage(pokemonOponente.getNombre(), false);
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = backgroundImage.createGraphics();
            g2d.setColor(new Color(100, 150, 200));
            g2d.fillRect(0, 0, 800, 600);
            g2d.dispose();
            
            playerPokemonImage = createPlaceholderImage();
            enemyPokemonImage = createPlaceholderImage();
        }
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        JPanel enemyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        enemyPanel.setOpaque(false);
        lblEnemyPokemon = new JLabel(new ImageIcon(enemyPokemonImage));
        lblEnemyHealth = createHealthBar(pokemonOponente);
        enemyPanel.add(lblEnemyHealth);
        enemyPanel.add(lblEnemyPokemon);
        
        JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playerPanel.setOpaque(false);
        lblPlayerPokemon = new JLabel(new ImageIcon(playerPokemonImage));
        lblPlayerHealth = createHealthBar(pokemonJugador);
        playerPanel.add(lblPlayerPokemon);
        playerPanel.add(lblPlayerHealth);
        
        actionPanel = new JPanel(new GridLayout(2, 2));
        actionPanel.setOpaque(false);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        

        List<Movimiento> movimientos = pokemonGUI.getPokemonMovements().get(pokemonJugador);
        for (int i = 0; i < 4; i++) {
            if (i < movimientos.size()) {
                Movimiento movimiento = movimientos.get(i);
                moveButtons[i] = new JButton(movimiento.getName());
                moveButtons[i].setActionCommand("MOVE_" + i);
            } else {
                moveButtons[i] = new JButton("-");
                moveButtons[i].setEnabled(false);
            }
            actionPanel.add(moveButtons[i]);
        }
        

        JPanel optionsPanel = new JPanel(new GridLayout(3, 1));
        optionsPanel.setOpaque(false);
        btnItem = new JButton("Objeto");
        btnCambiar = new JButton("Cambiar");
        optionsPanel.add(btnItem);
        optionsPanel.add(btnCambiar);
        

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(actionPanel, BorderLayout.CENTER);
        bottomPanel.add(optionsPanel, BorderLayout.EAST);
        

        txtBattleLog = new JTextArea(5, 30);
        txtBattleLog.setEditable(false);
        txtBattleLog.setOpaque(false);
        txtBattleLog.setForeground(Color.WHITE);
        txtBattleLog.setText("¡Comienza la batalla entre " + jugador.getName() + " y " + oponente.getName() + "!\n");
        

        mainPanel.add(enemyPanel, BorderLayout.NORTH);
        mainPanel.add(playerPanel, BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(txtBattleLog), BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.WEST);
        
        add(mainPanel);
    }

    private BufferedImage loadPokemonImage(String pokemonName, boolean isPlayer) throws IOException {
        String suffix = isPlayer ? "Usuario" : "Objetivo";
        String imagePath = "/" + pokemonName.toLowerCase() + "-" + suffix + ".gif";
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl == null) {
            return createPlaceholderImage();
        }
        return ImageIO.read(imageUrl);
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

    private JLabel createHealthBar(Pokemon pokemon) {
        JPanel healthPanel = new JPanel(new BorderLayout());
        healthPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(pokemon.getNombre());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JProgressBar healthBar = new JProgressBar(0, pokemon.getSaludMaxima());
        healthBar.setValue(pokemon.getSalud());
        healthBar.setString(pokemon.getSalud() + "/" + pokemon.getSaludMaxima());
        healthBar.setStringPainted(true);
        healthBar.setForeground(getHealthColor(pokemon));
        
        healthPanel.add(nameLabel, BorderLayout.NORTH);
        healthPanel.add(healthBar, BorderLayout.CENTER);
        
        JLabel container = new JLabel();
        container.setLayout(new BorderLayout());
        container.add(healthPanel, BorderLayout.CENTER);
        return container;
    }

    private Color getHealthColor(Pokemon pokemon) {
        double percentage = (double) pokemon.getSalud() / pokemon.getSaludMaxima();
        if (percentage > 0.6) return Color.GREEN;
        if (percentage > 0.3) return Color.YELLOW;
        return Color.RED;
    }

    private void prepareActions() {

        for (int i = 0; i < moveButtons.length; i++) {
            if (moveButtons[i] != null && moveButtons[i].isEnabled()) {
                final int moveIndex = i;
                moveButtons[i].addActionListener(e -> {
                    Movimiento movimiento = pokemonGUI.getPokemonMovements().get(pokemonJugador).get(moveIndex);
                    executeMove(movimiento);
                });
            }
        }
        

        btnItem.addActionListener(e -> {
            txtBattleLog.append(jugador.getName() + " usó un objeto!\n");

        });
        

        btnCambiar.addActionListener(e -> {
            txtBattleLog.append(jugador.getName() + " quiere cambiar de Pokémon!\n");

        });
        
    }

    private void executeMove(Movimiento movimiento) {

        txtBattleLog.append(pokemonJugador.getNombre() + " usó " + movimiento.getName() + "!\n");

        int damage = (int)(movimiento.getPotencia() * 0.8); // Daño simulado
        
        pokemonOponente.recibirDanio(damage);
        updateHealthBars();
        
        txtBattleLog.append("¡Le hizo " + damage + " puntos de daño a " + pokemonOponente.getNombre() + "!\n");
        
        // Verificar si el oponente fue derrotado
        if (pokemonOponente.estaDebilitado()) {
            txtBattleLog.append("¡" + pokemonOponente.getNombre() + " fue derrotado!\n");
            disableMoveButtons();
        }
        
        enemyTurn();
    }

    private void enemyTurn() {
        if (pokemonOponente.estaDebilitado()) return;
        
        List<Movimiento> enemyMoves = pokemonGUI.getPokemonMovements().get(pokemonOponente);
        if (!enemyMoves.isEmpty()) {
            Movimiento enemyMove = enemyMoves.get(0);
            txtBattleLog.append(pokemonOponente.getNombre() + " usó " + enemyMove.getName() + "!\n");
            
            int damage = (int)(enemyMove.getPotencia() * 0.8); // Daño simulado
            pokemonJugador.recibirDanio(damage);
            updateHealthBars();
            
            txtBattleLog.append("¡Le hizo " + damage + " puntos de daño a " + pokemonJugador.getNombre() + "!\n");
            
            // Verificar si el jugador fue derrotado
            if (pokemonJugador.estaDebilitado()) {
                txtBattleLog.append("¡" + pokemonJugador.getNombre() + " fue derrotado!\n");
                disableMoveButtons();
                JOptionPane.showMessageDialog(this, "¡Has perdido la batalla!", "Derrota", JOptionPane.INFORMATION_MESSAGE);
                pokemonGUI.mostrarMenuPrincipal();
            }
        }
    }

    private void updateHealthBars() {
        lblPlayerHealth.removeAll();
        lblPlayerHealth.add(createHealthBar(pokemonJugador));
        lblEnemyHealth.removeAll();
        lblEnemyHealth.add(createHealthBar(pokemonOponente));
        revalidate();
        repaint();
    }

    private void disableMoveButtons() {
        for (JButton btn : moveButtons) {
            if (btn != null) {
                btn.setEnabled(false);
            }
        }
    }
}
