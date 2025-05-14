
package presentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import domain.Movimiento;
import domain.Pokemon;
import domain.PokemonAplication;
import domain.Entrenador;
import domain.EntrenadorMachine;
import domain.EntrenadorPlayer;
import domain.Item;

public class PokemonGUI extends JFrame {
	private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private GifPanel gifPanel;
    private MenuPanel menuPanel;
    private MenuVsPanel menuVsPanel;
    private MenuModoPanel menuModoPanel;
    private PvpPanel pvpPanel;
    private PvmPanel pvmPanel;
    private MvmPanel mvmPanel;
    private PvpPanelTwo pvpPanelTwo;
    private MvMPanelTwo mvmPanelTwo;
    private MenuMachinePvMPanel menuMachinePvMPanel;
    private MenuItemsPanel menuItemsPanel;
    private MenuItemsPanelTwo menuItemsPanelTwo;
    private Map<String, String> entrenadores;
    private String Entrenador1;
    private String Entrenador2;
    private Entrenador jugador;
    private Entrenador oponente;
    private MenuItemsPvMPanel menuItemsPvMPanel;
    private MovimientoSelectionPanel movimientoSelectionPanel;
    private MovimientoSelectionPanelTwo movimientoSelectionPanelTwo;
    private MovimientoSelectionPvMPanel movimientoSelectionPvMPanel;
    private PokemonSelectionPanel pokemonSelectionPanel;
    private PokemonSelectionPanelTwo pokemonSelectionPanelTwo;
    private PokemonSelectionPvMPanel pokemonSelectionPvMPanel;
    
    private BattlePanel battlePanel;
    private Map<Pokemon, List<Movimiento>> player1Movimientos;
    private Map<Pokemon, List<Movimiento>> player2Movimientos;
    private Map<Pokemon, List<Movimiento>> playerMovimientos;
    
    
    
    private static final String RUTA_INTRO_GIF = "/introPokemon.gif";
    
    private PokemonAplication pokemonAplication;
    private String modoJuego;
    
    private List<Pokemon> selectedPokemons = new ArrayList<>();
    private Map<Pokemon, List<Movimiento>> pokemonMovements;
    
    private Map<String, List<Pokemon>> playerPokemons;
    private Map<String, Map<Pokemon, List<Movimiento>>> playerMovements;
    private String currentPlayer;
    
    
    
    public PokemonGUI() {
    	setTitle("POOKemon");
        prepareEntrenador();
        iniciarNuevaAplicacion();
        pokemonMovements = new HashMap<>();
        playerPokemons = new HashMap<>();
        playerMovements = new HashMap<>();
        currentPlayer = null;  // Asegurarnos de que esto esté inicializado
        prepareElementosPantallaPrincipal();
        prepareAcciones();
        mostrarPantallaIntro();
        centrarVentana();
    }
    
    public void prepareEntrenador() {
    	this.entrenadores = new HashMap<String,String>();
    }


    private void prepareElementosPantallaPrincipal() {
        gifPanel = new GifPanel(RUTA_INTRO_GIF);
        JPanel introPanel = new JPanel(new BorderLayout());
        introPanel.add(gifPanel, BorderLayout.CENTER);
        
        menuPanel = new MenuPanel(this);
        menuModoPanel = new MenuModoPanel(this);
        menuVsPanel = new MenuVsPanel(this);
        pvpPanel = new PvpPanel(this);
        pvmPanel = new PvmPanel(this);
        mvmPanel = new MvmPanel(this);
        pvpPanelTwo = new PvpPanelTwo(this);
        menuMachinePvMPanel = new MenuMachinePvMPanel(this);
        mvmPanelTwo = new MvMPanelTwo(this);
        menuItemsPanel = new MenuItemsPanel(this);
        menuItemsPanelTwo = new MenuItemsPanelTwo(this);
        menuItemsPvMPanel = new MenuItemsPvMPanel(this);
        movimientoSelectionPanel = new MovimientoSelectionPanel(this, selectedPokemons);
        movimientoSelectionPanelTwo = new MovimientoSelectionPanelTwo(this, selectedPokemons);
        movimientoSelectionPvMPanel = new MovimientoSelectionPvMPanel(this, selectedPokemons);
        pokemonSelectionPanel = new PokemonSelectionPanel(this);
        pokemonSelectionPanelTwo = new PokemonSelectionPanelTwo(this);
        pokemonSelectionPvMPanel = new PokemonSelectionPvMPanel(this);
        
        battlePanel = new BattlePanel(this, null, null);
        
        cardPanel.add(introPanel, "INTRO");
        cardPanel.add(menuPanel, "MAIN");
        cardPanel.add(menuModoPanel, "MAIN MODO");
        cardPanel.add(menuVsPanel, "MAIN MODOVS");
        cardPanel.add(pvpPanel, "MAIN PvP");
        cardPanel.add(pvpPanelTwo, "MAIN PvP TWO");
        cardPanel.add(menuMachinePvMPanel, "MAIN MACHINE PvP");
        cardPanel.add(mvmPanelTwo, "MAIN MACHINE MvM TWO");
        cardPanel.add(mvmPanel, "MAIN MvM");
        cardPanel.add(pvmPanel, "MAIN PvM");
        cardPanel.add(menuItemsPanel, "MAIN ITEMS");
        cardPanel.add(menuItemsPanelTwo, "MAIN ITEMS TWO");
        cardPanel.add(menuItemsPvMPanel, "MAIN ITEMS PvM");
        cardPanel.add(movimientoSelectionPanel, "MAIN MOV PVP");
        cardPanel.add(movimientoSelectionPanelTwo, "MAIN MOV TWO");
        cardPanel.add(pokemonSelectionPanel, "MAIN POK PVP");
        cardPanel.add(pokemonSelectionPanelTwo, "MAIN POK TWO");
        cardPanel.add(pokemonSelectionPvMPanel, "MAIN POK PVM");
        cardPanel.add(battlePanel, "MAIN BATTLE");
        
        add(cardPanel);
    }
    
    

    private void prepareAcciones() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                mostrarMenuPrincipal();
            }
        });
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });
    }
    
    public void mostrarPantallaIntro() {
        cardLayout.show(cardPanel, "INTRO");
        setVisible(true);
    }

    public void mostrarMenuItemsPvMPanel() {
    	cardLayout.show(cardPanel, "MAIN ITEMS PvM");
    }
    
    
    public void mostrarMenuMvMTwo() {
    	cardLayout.show(cardPanel, "MAIN MACHINE MvM TWO");
    }
    public void mostrarMostrarMenuMachinePvM() {
        cardLayout.show(cardPanel, "MAIN MACHINE PvP");

    }
    public void mostrarMenuPvP() {
        cardLayout.show(cardPanel, "MAIN PvP");

    }
    
    public void mostrarMenuPvPTwo() {
    	
        cardLayout.show(cardPanel, "MAIN PvP TWO");

    }
    
    
    public void mostrarMenuMvM() {
        cardLayout.show(cardPanel, "MAIN MvM");

    }
    
    public void mostrarMenuPvM() {
        cardLayout.show(cardPanel, "MAIN PvM");

    }

    public void mostrarMenuPrincipal() {
        cardLayout.show(cardPanel, "MAIN");
    }

    public void mostrarMenuModo() {
        cardLayout.show(cardPanel, "MAIN MODO"); 
    }
    
    public void mostrarMenuModoVs() {
    	cardLayout.show(cardPanel, "MAIN MODOVS"); 
    }
    
    public void mostrarMenuItems() {
    	cardLayout.show(cardPanel, "MAIN ITEMS"); 
    }
    
    public void mostrarMenuItemsTwo() {
    	cardLayout.show(cardPanel, "MAIN ITEMS TWO"); 
    }
    
    public void mostrarMovimientoSelectionPanel(List<Pokemon> pokemones) {
        movimientoSelectionPanel = new MovimientoSelectionPanel(this, pokemones);
        cardPanel.add(movimientoSelectionPanel, "MAIN MOV PVP");
        cardLayout.show(cardPanel, "MAIN MOV PVP");
    }
    
    public void mostrarMovimientoSelectionPanelTwo(List<Pokemon> pokemones) {
        movimientoSelectionPanelTwo = new MovimientoSelectionPanelTwo(this, pokemones);
        cardPanel.add(movimientoSelectionPanelTwo, "MAIN MOV TWO");
        cardLayout.show(cardPanel, "MAIN MOV TWO");
    }
    
    public void mostrarMovimientoSelectionPvMPanel(List<Pokemon> pokemones) {
        movimientoSelectionPvMPanel = new MovimientoSelectionPvMPanel(this, pokemones);
        cardPanel.add(movimientoSelectionPvMPanel, "MAIN MOV PVM");
        cardLayout.show(cardPanel, "MAIN MOV PVM");
    }
    
    public void mostrarPokemonSelectionPanel() {
    	cardLayout.show(cardPanel, "MAIN POK PVP");
    }
    
    public void mostrarPokemonSelectionPanelTwo() {
    	cardLayout.show(cardPanel, "MAIN POK TWO");
    }
    
    public void mostrarPokemonSelectionPvMPanel() {
    	cardLayout.show(cardPanel, "MAIN POK PVM");
    }
    
    public void mostrarbattelPanel() {
        // Obtener los entrenadores actuales
        Entrenador jugador = new EntrenadorPlayer(Entrenador1, "Player");
        Entrenador oponente = new EntrenadorMachine(Entrenador2, "Machine");
        
        // Asignar los pokémones y movimientos al jugador desde los mapas existentes
        if (playerPokemons.containsKey(Entrenador1)) {
            jugador.setPokemones(playerPokemons.get(Entrenador1));
        }
        if (playerPokemons.containsKey(Entrenador2)) {
            oponente.setPokemones(playerPokemons.get(Entrenador2));
        }
        
        // Iniciar la batalla con los entrenadores configurados
        iniciarBatalla(jugador, oponente);
    }
    
    
    
    public void comienzoAgregarItems1(Map<String,Integer> itemsSeleccionados,String  numero) {
    	ArrayList<String> items = new ArrayList<>(); 
    	for (Map.Entry<String,Integer> i: itemsSeleccionados.entrySet()) {
    		if (i.getKey().equals("HyperPotion")) {
    			for(int t = 0; t < i.getValue(); t++ ) {
    				items.add("HyperPotion");
    			}
    		} else if(i.getKey().equals("SuperPotion")) {
    			for(int t = 0; t < i.getValue(); t++ ) {
    				items.add("SuperPotion");
    			}
    		} else if(i.getKey().equals("Potion")) {
    			for(int t = 0; t < i.getValue(); t++ ) {
    				items.add("Potion");
    			}
    		}else if(i.getKey().equals("Revive")) {
    			for(int t = 0; t < i.getValue(); t++ ) {
    				items.add("Revive");
    			}
    		}
    	}
    	
    	
    	Map<Entrenador, ArrayList<Item>> playerItems = new HashMap<>();
    	ArrayList<Item> itemsEscogidos = pokemonAplication.escojerItems(items);
    	ArrayList<Entrenador> entrenadores = pokemonAplication.getEntrenadores();
    	
    	
    	
    	String nombreBuscado = "player1".equals(numero) ? Entrenador1 : Entrenador2;

    	for (Entrenador e : entrenadores) {
    	    if (e.getName().equals(nombreBuscado)) {
    	        playerItems.put(e, itemsEscogidos);
    	        break;
    	    }
    	}
    	
    	pokemonAplication.agregarItemsLucha(playerItems);
    	
    }
    
    private void centrarVentana() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width / 2;
        int height = screen.height / 2;
        setBounds((screen.width - width)/2, (screen.height-height)/2, width, height);
    }
    
    public void comienzoAgregarEntrenadoresPorTipo1(String nombre, String tipo, String id) {
        this.currentPlayer = nombre;
        playerPokemons.put(nombre, new ArrayList<>());
        playerMovements.put(nombre, new HashMap<>());
        
        System.out.println("\n=== Nuevo Jugador Registrado ===");
        System.out.println("Nombre: " + nombre);
        System.out.println("Fecha: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("==============================\n");
        
    	
    	if (nombre == null || tipo == null || id == null) {
            throw new IllegalArgumentException("Ninguno de los argumentos puede ser null");
        }

        
        if (entrenadores == null) {
            entrenadores = new HashMap<>();
        }

        
        entrenadores.put(nombre, tipo);

        // Asignar al jugador correspondiente
        if ("player1".equals(id)) {
            this.Entrenador1 = nombre;
        } else if ("player2".equals(id)) {
            this.Entrenador2 = nombre;
        } else {
            throw new IllegalArgumentException("El valor de 'numero' debe ser 'player1' o 'player2'");
        }
    }
    
    

    public void agregarEntrenadores() {
    	pokemonAplication.crearEntrenadors(this.entrenadores); 
    	for (Map.Entry<String,String> e: entrenadores.entrySet()) {
    		System.out.println(e.getKey());
    		System.out.println(e.getValue());
    	}
    	
    }
    
    public void comienzoAgregarPokemones() {
    	
    }
    
    
    private void confirmarSalida() {
        int opcion = JOptionPane.showConfirmDialog(
            this, 
            "¿Estás seguro de que quieres salir?", 
            "Confirmar salida", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
    
    public void iniciarNuevaAplicacion() {
        this.pokemonAplication = new PokemonAplication();
    }
    
    public void resetearPokemonAplication() {
    	this.pokemonAplication = null;
    }
    
    public void volverAlMenuPrincipal() {
    	
        mostrarMenuPrincipal();
    }
    
    public void resetearEntrenador() {
        // Usar Iterator para evitar ConcurrentModificationException
        Iterator<Map.Entry<String, String>> iterator = entrenadores.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> e = iterator.next();
            String nombre = e.getKey();
            iterator.remove(); // Remueve de forma segura
            pokemonAplication.resetEntrenadore(nombre);
        }
        this.Entrenador1 = null;
    }

    
    public void resetearEntrenadorTwo() {
        if (entrenadores.containsKey(Entrenador2)) {
            pokemonAplication.resetEntrenadore(Entrenador2);
            entrenadores.remove(Entrenador2);
            this.Entrenador2 = null;
        }
    }
    
    
    public String getNameEntrenador1() {
    	String name = null;
    	for (Map.Entry<String,String> e: entrenadores.entrySet()) {
    		name = e.getKey();
    	}
    	return name;
	}
    
    
    public void agregarModalidad(String modalidad) {
    	pokemonAplication.elegirModalidad(modalidad);
    }
    
    public void resetearModalidad() {
    	pokemonAplication.resetModalidad();
    }
    
    
    

    public void prepareElementosPokemon() {
        // Obtener pokémones 
    	
        List<Pokemon> pokemonesFuego = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.FUEGO);
        List<Pokemon> pokemonesAgua = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.AGUA);
        List<Pokemon> pokemonesTierra = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.TIERRA);
        List<Pokemon> pokemonesLucha = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.LUCHA);
        List<Pokemon> pokemonesElectrico = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.ELECTRICO);
        List<Pokemon> pokemonesVolador = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.VOLADOR);
        List<Pokemon> pokemonesPlanta = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.PLANTA);
        List<Pokemon> pokemonesVeneno = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.VENENO);
        List<Pokemon> pokemonesDragon = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.DRAGON);
        List<Pokemon> pokemonesHada = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.HADA);
        List<Pokemon> pokemonesRoca = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.ROCA);
        List<Pokemon> pokemonesSiniestro = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.SINIESTRO);
        List<Pokemon> pokemonesAcero = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.ACERO);
        List<Pokemon> pokemonesPsiquico = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.PSIQUICO);
        List<Pokemon> pokemonesHilo = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.HIELO);
        List<Pokemon> pokemonesBicho = pokemonAplication.getPokemonesPorTipo(Pokemon.Tipo.BICHO);
        
        // Obtener movimientos 
        List<Movimiento> movimientosFuego = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.FUEGO);
        List<Movimiento> movimientosAgua = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.AGUA);
        List<Movimiento> movimientosTierra = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.TIERRA);
        List<Movimiento> movimientosLucha = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.LUCHA);
        List<Movimiento> movimientosElectrico = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.ELECTRICO);
        List<Movimiento> movimientosVolador = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.VOLADOR);
        List<Movimiento> movimientosPlanta = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.PLANTA);
        List<Movimiento> movimientosVeneno = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.VENENO);
        List<Movimiento> movimientosDragon = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.DRAGON);
        List<Movimiento> movimientosHada = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.HADA);
        List<Movimiento> movimientosRoca = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.ROCA);
        List<Movimiento> movimientosSiniestro = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.SINIESTRO);
        List<Movimiento> movimientosAcero = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.ACERO);
        List<Movimiento> movimientosPsiquico = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.PSIQUICO);
        List<Movimiento> movimientosHielo = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.HIELO);
        List<Movimiento> movimientosBicho = pokemonAplication.getMovimientosPorTipo(Pokemon.Tipo.BICHO);
    }
    
    

    // Métodos de acceso
    public PokemonAplication getPokemonAplication() {
        return pokemonAplication;
    }

    // Punto de entrada
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PokemonGUI());
    }
    
    public List<Pokemon> getSelectedPokemons() {
        return selectedPokemons;
    }

    public void setSelectedPokemons(List<Pokemon> pokemons) {
        if (currentPlayer != null) {
            playerPokemons.put(currentPlayer, new ArrayList<>(pokemons));
            System.out.println("\n=== Pokémon seleccionados por " + currentPlayer + " ===");
            for (Pokemon pokemon : pokemons) {
                System.out.println("- " + pokemon.getNombre());
            }
            System.out.println("=====================================\n");
        }
    }

    
    public void setPokemonMovements(Map<Pokemon, List<Movimiento>> movements) {
        if (currentPlayer != null) {
            playerMovements.put(currentPlayer, new HashMap<>(movements));
            System.out.println("\n=== Movimientos asignados por " + currentPlayer + " ===");
            for (Map.Entry<Pokemon, List<Movimiento>> entry : movements.entrySet()) {
                System.out.println("Pokémon: " + entry.getKey().getNombre());
                System.out.println("Movimientos:");
                for (Movimiento mov : entry.getValue()) {
                    System.out.println("  * " + mov.getName());
                }
            }
            System.out.println("=====================================\n");
        }
    }
    
    public void mostrarSeleccionMovimientos(List<Pokemon> pokemones) {
        MovimientoSelectionPanel movimientoPanel = new MovimientoSelectionPanel(this, pokemones);
        cardPanel.add(movimientoPanel, "MOVEMENT_SELECTION");
        cardLayout.show(cardPanel, "MOVEMENT_SELECTION");
    }

    public List<Movimiento> inicializarMovimientos() {
        List<Movimiento> allMovements = new ArrayList<>();
        // Obtener todos los movimientos definidos en la clase Movimiento
        for (Pokemon.Tipo tipo : Pokemon.Tipo.values()) {
            allMovements.addAll(Movimiento.getMovimientosPorTipo(tipo));
        }
        return allMovements;
    }
    
    public List<Pokemon> getCurrentPlayerPokemons() {
        return playerPokemons.get(currentPlayer);
    }

    public Map<Pokemon, List<Movimiento>> getPokemonMovements() {
        return playerMovements.get(currentPlayer);
    }
    
    private void comenzarBatalla() {
        Entrenador jugador = new EntrenadorPlayer("Jugador", "Player");
        Entrenador oponente = new EntrenadorMachine("Oponente", "Machine");
        
        // Asignar Pokémon y sus movimientos al jugador
        for (Pokemon pokemon : selectedPokemons) {
            jugador.getPokemones().add(pokemon);
            if (!pokemonMovements.containsKey(pokemon)) {
                pokemonMovements.put(pokemon, new ArrayList<>());
            }
        }

        // Mostrar información en consola
        System.out.println("\n=== Inicio de Batalla ===");
        System.out.println("Jugador: " + currentPlayer);
        System.out.println("Pokémon inicial: " + selectedPokemons.get(0).getNombre());
        System.out.println("Movimientos disponibles:");
        List<Movimiento> moves = pokemonMovements.get(selectedPokemons.get(0));
        if (moves != null) {
            for (Movimiento move : moves) {
                System.out.println("- " + move.getName());
            }
        }
        System.out.println("=====================\n");

        // Crear y mostrar el panel de batalla
        battlePanel = new BattlePanel(this, jugador, oponente);
        cardPanel.add(battlePanel, "BATTLE");
        cardLayout.show(cardPanel, "BATTLE");
    }
    
    
    
    public void iniciarBatalla(Entrenador jugador, Entrenador oponente) {
        if (jugador == null || oponente == null) {
            JOptionPane.showMessageDialog(this, 
                "Debes seleccionar ambos entrenadores primero", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostrar información en consola
        System.out.println("\n=== Inicio de Batalla ===");
        System.out.println("Jugador: " + jugador.getName());
        if (!jugador.getPokemones().isEmpty()) {
            Pokemon primerPokemon = jugador.getPokemones().get(0);
            System.out.println("Pokémon inicial: " + primerPokemon.getNombre());
            System.out.println("Movimientos disponibles:");
            List<Movimiento> moves = pokemonMovements.get(primerPokemon);
            if (moves != null) {
                for (Movimiento move : moves) {
                    System.out.println("- " + move.getName());
                }
            }
        }
        System.out.println("Oponente: " + oponente.getName());
        System.out.println("=====================\n");

        battlePanel = new BattlePanel(this, jugador, oponente);
        cardPanel.add(battlePanel, "BATTLE");
        cardLayout.show(cardPanel, "BATTLE");
    }
    
    public List<String> getPokemonNames() {
        if (pokemonAplication == null) {
            return new ArrayList<>(); 
        }
        return pokemonAplication.getPokemonNames();
    }
    public List<String> getMovimientoNames() {
        
        return pokemonAplication.getMovimientoNames();
    }

    public void setPokemonSeleccionadosPlayer1(List<String> seleccionados) {
        pokemonAplication.setPokemonSeleccionadosPlayer1(seleccionados);
    }

    public void setPokemonSeleccionadosPlayer2(List<String> seleccionados) {
        pokemonAplication.setPokemonSeleccionadosPlayer2(seleccionados);
    }

    public void setPokemonSeleccionadosPlayer(List<String> seleccionados) {
        pokemonAplication.setPokemonSeleccionadosPlayer(seleccionados);
    }

    public void setMovimientosSeleccionadosPlayer2(List<String> seleccionados) {
        pokemonAplication.setMovimientosSeleccionadosPlayer2(seleccionados);
    }

    public void setMovimientosSeleccionadosPlayer(List<String> seleccionados) {
        pokemonAplication.setMovimientosSeleccionadosPlayer(seleccionados);
    }
    
    public void displayCurrentPlayerData() {
        if (currentPlayer != null) {
            System.out.println("\n========================================");
            System.out.println("Datos del Jugador: " + currentPlayer);
            System.out.println("Fecha: " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            System.out.println("========================================");
            
            // Mostrar Pokémon y sus movimientos
            System.out.println("\nPokémon seleccionados:");
            List<Pokemon> pokemons = playerPokemons.get(currentPlayer);
            if (pokemons != null) {
                for (Pokemon pokemon : pokemons) {
                    System.out.println("- " + pokemon.getNombre());
                    // Mostrar movimientos de este Pokémon
                    Map<Pokemon, List<Movimiento>> movements = playerMovements.get(currentPlayer);
                    if (movements != null && movements.containsKey(pokemon)) {
                        System.out.println("  Movimientos:");
                        for (Movimiento mov : movements.get(pokemon)) {
                            System.out.println("    * " + mov.getName());
                        }
                    }
                }
            }
            System.out.println("========================================\n");
        }
    }
    
    
    

    
    
    
}