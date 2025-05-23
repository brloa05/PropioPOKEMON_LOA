
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
import domain.PokemonDosTipos;
import domain.PokemonException;
import domain.PokemonUnTipo;
import domain.SuperPotion;
import domain.AttackingTrainer;
import domain.ChangingTrainer;
import domain.DefensiveTrainer;
import domain.Entrenador;
import domain.EntrenadorMachine;
import domain.EntrenadorPlayer;
import domain.ExpertTrainer;
import domain.HyperPotion;
import domain.Item;

public class PokemonGUI extends JFrame {
	private EntrenadorMachine maquina;
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
    private BattleSupreme1 battlePanel1;
    private BattlePanel battlePanel;
    private Map<Pokemon, List<Movimiento>> player1Movimientos;
    private Map<Pokemon, List<Movimiento>> player2Movimientos;
    private Map<Pokemon, List<Movimiento>> playerMovimientos;
    
    
    
    private static final String RUTA_INTRO_GIF = "/introPokemon.gif";
    
    private PokemonAplication pokemonAplication;
    
    private List<Pokemon> selectedPokemons = new ArrayList<>();
    private Map<Pokemon, List<Movimiento>> pokemonMovements;
    
    private Map<String, List<Pokemon>> playerPokemons;
    public Map<String, Map<Pokemon, List<Movimiento>>> playerMovements;
    private String currentPlayer;
    
    private String tipoMaquinaSeleccionada = "Normal";
    
    private ModoJuego modoJuego = ModoJuego.PVM; 
    
    
    
    public PokemonGUI() {
        setTitle("POOKemon");
        prepareEntrenador();
        iniciarNuevaAplicacion();
        
        // Inicializar todas las estructuras de datos
        pokemonMovements = new HashMap<>();
        playerPokemons = new HashMap<>();
        playerMovements = new HashMap<>();
        selectedPokemons = new ArrayList<>();
        
        currentPlayer = null;
        prepareElementosPantallaPrincipal();
        prepareAcciones();
        mostrarPantallaIntro();
        centrarVentana();
        this.tipoMaquinaSeleccionada = "Normal";
    }
    
    public enum ModoJuego {
        PVM,
        PVP
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
    
    public void setTipoMaquinaSeleccionada(String tipo) {
        this.tipoMaquinaSeleccionada = (tipo != null) ? tipo : "Normal";
    }
    
 // Método para mostrar batalla PvM
    public void mostrarBattlePvMPanel() {
    	debugMovements();
    	try {
        // Verifica si el jugador está inicializado
        if (jugador == null) {
            // Si no hay jugador, crea uno nuevo con los datos disponibles
            jugador = new EntrenadorPlayer(currentPlayer, "Player");
            
            // Si hay pokémon seleccionados para el jugador actual, asígnalos
            if (currentPlayer != null && playerPokemons.containsKey(currentPlayer)) {
                jugador.setPokemones(playerPokemons.get(currentPlayer));
            }
            
            if (!playerMovements.containsKey(currentPlayer)) {
                throw new IllegalStateException("No se han asignado movimientos para el jugador actual");
            }
            
        }

        // Crear la máquina según el tipo seleccionado
        if (maquina == null) {
            switch(tipoMaquinaSeleccionada) {
                case "ExpertTrainer":
                    maquina = new ExpertTrainer("Machine", "ExpertTrainer");
                    break;
                case "AttackingTrainer":
                    maquina = new AttackingTrainer("Machine", "AttackingTrainer");
                    break;
                case "DefensiveTrainer":
                    maquina = new DefensiveTrainer("Machine", "DefensiveTrainer");
                    break;
                case "ChangingTrainer":
                    maquina = new ChangingTrainer("Machine", "ChangingTrainer");
                    break;
                default:
                    maquina = new ExpertTrainer("Machine", "ExpertTrainer");
            }
        }

        // Verificar los Pokémon del jugador y la máquina
        if (jugador.getPokemones() == null || jugador.getPokemones().isEmpty()) {
            throw new IllegalStateException("El jugador debe tener al menos un Pokémon para iniciar la batalla");
        }
        
        if (maquina == null) {
        	
        }

        if (maquina.getPokemones() == null || maquina.getPokemones().isEmpty()) {
            cargarPokemonMaquina();
        }

        BattlePvMPanel battlePvMPanel = new BattlePvMPanel(this, jugador, maquina);
        cardPanel.add(battlePvMPanel, "BATTLE");
        cardLayout.show(cardPanel, "BATTLE");

	    } catch (IllegalStateException e) {
	        JOptionPane.showMessageDialog(
	            this,
	            "Error al iniciar la batalla: " + e.getMessage(),
	            "Error",
	            JOptionPane.ERROR_MESSAGE
	        );
	        volverAlMenuPrincipal();
	    } catch(PokemonException p) {
	    	
	    }
    }
    
    public void battleSupreme()   {
    		ArrayList<Entrenador> entr = pokemonAplication.getEntrenadores();
    		Entrenador ent1 = null;
    		Entrenador ent2 = null;
    		
    		for (Entrenador e: entr) {
		    	if (e.getName().equals(Entrenador1)) {
		    		ent1 = e;
		    	} else {
		    		ent2 = e;
		    	}
    		}
    		battlePanel1= new BattleSupreme1(this,ent1 ,ent2 );
	        cardPanel.add(battlePanel1, "BATTLE1");
	        cardLayout.show(cardPanel, "BATTLE1");
    	
    	
    }
    

    // Método para mostrar batalla PvP
    public void mostrarBattlePanel() {
        try {
            // Modo PvP - usar el BattlePanel original
            Entrenador jugador1 = new EntrenadorPlayer(Entrenador1, "PlayerTrainer");
            Entrenador jugador2 = new EntrenadorPlayer(Entrenador2, "PlayerTrainer");
            
            if (playerPokemons.containsKey(Entrenador1)) {
                jugador1.setPokemones(playerPokemons.get(Entrenador1));
            }
            if (playerPokemons.containsKey(Entrenador2)) {
                jugador2.setPokemones(playerPokemons.get(Entrenador2));
            }
            
            // Verificar que ambos jugadores tengan Pokémon
            if (jugador1.getPokemones() == null || jugador1.getPokemones().isEmpty()) {
                throw new IllegalStateException("El jugador 1 debe tener al menos un Pokémon para iniciar la batalla");
            }
            if (jugador2.getPokemones() == null || jugador2.getPokemones().isEmpty()) {
                throw new IllegalStateException("El jugador 2 debe tener al menos un Pokémon para iniciar la batalla");
            }
            
            battlePanel = new BattlePanel(this, jugador1, jugador2);
            cardPanel.add(battlePanel, "BATTLE");
            cardLayout.show(cardPanel, "BATTLE");

        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al iniciar la batalla: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            volverAlMenuPrincipal();
        } catch (PokemonException p) {
        	p.getStackTrace();
        }
    }



	// Añade los métodos getter y setter para modoJuego
	public ModoJuego getModoJuego() {
	    return modoJuego;
	}
	
	public void setModoJuego(ModoJuego modo) {
	    this.modoJuego = modo;
	}

    
    
	public void comienzoAgregarItems1(Map<String,Integer> itemsSeleccionados, String numero) {
		try {
		    ArrayList<String> items = new ArrayList<>();
		    
		    // Convertir el Map a lista de strings
		    for (Map.Entry<String,Integer> i: itemsSeleccionados.entrySet()) {
		        for(int t = 0; t < i.getValue(); t++ ) {
		            items.add(i.getKey());
		        }
		    }
		    
		    // Crear el Map para agregar los items al entrenador
		    Map<Entrenador, ArrayList<Item>> playerItems = new HashMap<>();
		    ArrayList<Item> itemsEscogidos = pokemonAplication.escojerItems(items);
		    ArrayList<Entrenador> entrenadores = pokemonAplication.getEntrenadores();
		    
		    // Buscar el entrenador correcto y asignarle los items
		    String nombreBuscado = "player1".equals(numero) ? Entrenador1 : Entrenador2;
		    for (Entrenador e : entrenadores) {
		        if (e.getName().equals(nombreBuscado)) {
		            playerItems.put(e, itemsEscogidos);
		            break;
		        }
		    }
		    
		    pokemonAplication.agregarItemsLucha(playerItems);
		    
		} catch(PokemonException p) {
			p.getStackTrace();
		}
	}

	
	public void comienzoAgregarObjetosSurvival() {
		pokemonAplication.agregarObjetosSupervivencia();
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
    	try {
    		pokemonAplication.crearEntrenadors(this.entrenadores); 
    	} catch(PokemonException p) {
    		p.getStackTrace();
    	}
    }
    
    public void aplicationModo(String modo) {
    	try {
    		pokemonAplication.setModo(modo);
    	} catch(PokemonException p) {
    		p.getStackTrace();
    	}
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
        try {
			this.pokemonAplication = new PokemonAplication();
		} catch (PokemonException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());		}
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
    	try {
			pokemonAplication.elegirModalidad(modalidad);
		} catch (PokemonException e) {
			e.printStackTrace();
		}
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
            if (playerMovements == null) {
                playerMovements = new HashMap<>();
            }
            Map<Pokemon, List<Movimiento>> pokemonMoves = new HashMap<>(movements);
            playerMovements.put(currentPlayer, pokemonMoves);
            
            System.out.println("\n=== Movimientos asignados por " + currentPlayer + " ===");
            for (Map.Entry<Pokemon, List<Movimiento>> entry : movements.entrySet()) {
                System.out.println("Pokémon: " + entry.getKey().getNombre());
                System.out.println("Movimientos:");
                for (Movimiento mov : entry.getValue()) {
                    System.out.println("  * " + mov.getName());
                }
            }
            System.out.println("=====================================\n");
            
            // También actualizar pokemonMovements para compatibilidad
            this.pokemonMovements = new HashMap<>(movements);
        } else {
            System.out.println("Error: No hay jugador actual seleccionado");
        }
        debugMovements();
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
        if (currentPlayer != null && playerMovements != null) {
            Map<Pokemon, List<Movimiento>> movements = playerMovements.get(currentPlayer);
            if (movements != null) {
                return movements;
            }
        }
        System.out.println("Warning: No se encontraron movimientos para el jugador actual");
        return new HashMap<>();
    }
    
    private void comenzarBatalla() {
        // Crear los entrenadores
    	try {
		    EntrenadorPlayer jugador = new EntrenadorPlayer(currentPlayer, "Player");
		    EntrenadorMachine maquina = new EntrenadorMachine("Machine1", "Machine");
	    	
	        // Asignar pokémon y movimientos al jugador
	        if (playerPokemons.containsKey(currentPlayer)) {
	            jugador.setPokemones(playerPokemons.get(currentPlayer));
	        }
	        
	        // Crear mapas para la máquina
	        Map<Pokemon.Tipo, List<Pokemon>> pokemonesPorTipo = new HashMap<>();
	        
	     // En la clase PokemonGUI
	        
	        // Agrupar pokémon de un tipo
	        for (PokemonUnTipo.PokemonBase base : PokemonUnTipo.PokemonBase.values()) {
	            if (!pokemonesPorTipo.containsKey(base.tipo)) {
	                pokemonesPorTipo.put(base.tipo, new ArrayList<>());
	            }
	            Pokemon pokemon = new PokemonUnTipo(base.name(), base.stats[0], new ArrayList<>());
	            pokemonesPorTipo.get(base.tipo).add(pokemon);
	        }
	        
	        // Agrupar pokémon de dos tipos
	        for (PokemonDosTipos.PokemonBase base : PokemonDosTipos.PokemonBase.values()) {
	            if (!pokemonesPorTipo.containsKey(base.tipo1)) {
	                pokemonesPorTipo.put(base.tipo1, new ArrayList<>());
	            }
	            Pokemon pokemon = new PokemonDosTipos(base.name(), base.stats[0], new ArrayList<>());
	            pokemonesPorTipo.get(base.tipo1).add(pokemon);
	        }
	        
	        // Crear mapa de movimientos por tipo
	        Map<Pokemon.Tipo, List<Movimiento>> movimientosPorTipo = new HashMap<>();
	        for (Pokemon.Tipo tipo : Pokemon.Tipo.values()) {
	            movimientosPorTipo.put(tipo, Movimiento.getMovimientosPorTipo(tipo));
	        }
	        
	        // Que la máquina escoja sus pokémon según su estrategia
	        maquina.escogerPokemonesTipo(pokemonesPorTipo);
	        
	        // Asignar movimientos a cada pokémon de la máquina
	        for (Pokemon pokemon : maquina.getPokemones()) {
	            maquina.agregarMovimientos(pokemon, movimientosPorTipo, new ArrayList<>());
	        }
	        
	        // Asignar items según la estrategia de la máquina
	        maquina.escogerItemsTipo(pokemonAplication.getItems()); // poner como parametro pokemonAplication.getItems()
	        
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
	        System.out.println("Oponente: " + maquina.getName());
	        System.out.println("=====================\n");
	
	        // Crear y mostrar el panel de batalla
	        battlePanel = new BattlePanel(this, jugador, maquina);
	        cardPanel.add(battlePanel, "BATTLE");
	        cardLayout.show(cardPanel, "BATTLE");
    	} catch(PokemonException p) {
    		p.getStackTrace();

    	}
    }
    
    private void verificarDatosBatalla(Entrenador jugador, Entrenador oponente) {
	    // Verificar que los entrenadores existan
	    if (jugador == null || oponente == null) {
	        throw new IllegalStateException("Debes seleccionar ambos entrenadores primero");
	    }
	    
	    // Verificar que tengan pokémones asignados
	    if (jugador.getPokemones() == null || jugador.getPokemones().isEmpty()) {
	        throw new IllegalStateException("El jugador " + jugador.getName() + " no tiene pokémones asignados");
	    }
	    if (oponente.getPokemones() == null || oponente.getPokemones().isEmpty()) {
	        throw new IllegalStateException("El oponente " + oponente.getName() + " no tiene pokémones asignados");
	    }
	    
	    // Verificar que tengan movimientos asignados
	    if (!pokemonMovements.containsKey(jugador.getPokemones().get(0))) {
	        throw new IllegalStateException("No se han asignado movimientos al pokémon inicial del jugador");
	    }
	    if (!pokemonMovements.containsKey(oponente.getPokemones().get(0))) {
	        throw new IllegalStateException("No se han asignado movimientos al pokémon inicial del oponente");
	    }
    }
    
    private void asignarPokemonesYMovimientosMaquina(EntrenadorMachine maquina) {
    	try {
	        // Crear el mapa de pokémon por tipo
	        Map<Pokemon.Tipo, List<Pokemon>> pokemonesPorTipo = new HashMap<>();
	        
	        // Agrupar pokémon de un tipo
	        for (PokemonUnTipo.PokemonBase base : PokemonUnTipo.PokemonBase.values()) {
	            if (!pokemonesPorTipo.containsKey(base.tipo)) {
	                pokemonesPorTipo.put(base.tipo, new ArrayList<>());
	            }
	            Pokemon pokemon = new PokemonUnTipo(base.name(), base.stats[0], new ArrayList<>());
	            pokemonesPorTipo.get(base.tipo).add(pokemon);
	        }
	        
	        // Agrupar pokémon de dos tipos
	        for (PokemonDosTipos.PokemonBase base : PokemonDosTipos.PokemonBase.values()) {
	            if (!pokemonesPorTipo.containsKey(base.tipo1)) {
	                pokemonesPorTipo.put(base.tipo1, new ArrayList<>());
	            }
	            Pokemon pokemon = new PokemonDosTipos(base.name(), base.stats[0], new ArrayList<>());
	            pokemonesPorTipo.get(base.tipo1).add(pokemon);
	        }
	        
	        // Crear mapa de movimientos por tipo
	        Map<Pokemon.Tipo, List<Movimiento>> movimientosPorTipo = new HashMap<>();
	        for (Pokemon.Tipo tipo : Pokemon.Tipo.values()) {
	            movimientosPorTipo.put(tipo, Movimiento.getMovimientosPorTipo(tipo));
	        }
	        
	        // Que la máquina escoja sus pokémon según su estrategia
	        maquina.escogerPokemonesTipo(pokemonesPorTipo);
	        
	        // Asignar movimientos a cada pokémon de la máquina
	        for (Pokemon pokemon : maquina.getPokemones()) {
	            maquina.agregarMovimientos(pokemon, movimientosPorTipo, new ArrayList<>());
	        }
	        
	        // Asignar items según la estrategia de la máquina
	        maquina.escogerItemsTipo(pokemonAplication.getItems());
    	} catch(PokemonException p) {
    		p.getStackTrace();

    	}
    	
    }
    
    
    
    public void iniciarBatalla(Entrenador jugador, Entrenador oponente) {
    try {
        verificarDatosBatalla(jugador, oponente);
        
        // Mostrar información en consola
        System.out.println("\n=== Inicio de Batalla ===");
        System.out.println("Jugador: " + jugador.getName());
        Pokemon primerPokemon = jugador.getPokemones().get(0);
        System.out.println("Pokémon inicial: " + primerPokemon.getNombre());
        System.out.println("Movimientos disponibles:");
        List<Movimiento> moves = pokemonMovements.get(primerPokemon);
        if (moves != null) {
            for (Movimiento move : moves) {
                System.out.println("- " + move.getName());
            }
        }
        System.out.println("Oponente: " + oponente.getName());
        System.out.println("=====================\n");

        battlePanel = new BattlePanel(this, jugador, oponente);
        cardPanel.add(battlePanel, "BATTLE");
        cardLayout.show(cardPanel, "BATTLE");
    } catch (IllegalStateException e) {
        JOptionPane.showMessageDialog(this, 
            e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}	
   
    
    public void iniciarBatallaPvM(Map<String, Integer> itemsSeleccionados) {
    	try {
	        // 1. Convertir el Map de selecciones a lista de strings como lo hace comienzoAgregarItems1
	        ArrayList<String> items = new ArrayList<>();
	        for (Map.Entry<String,Integer> i: itemsSeleccionados.entrySet()) {
	            for(int t = 0; t < i.getValue(); t++ ) {
	                items.add(i.getKey());
	            }
	        }
	        
	        // 2. Usar el método existente para crear los items
	        ArrayList<Item> itemsEscogidos = pokemonAplication.escojerItems(items);
	        
	        // 3. Crear el map de entrenador-items
	        Map<Entrenador,ArrayList<Item>> playerItems = new HashMap<>();
	        ArrayList<Entrenador> entrenadores = pokemonAplication.getEntrenadores();
	        
	        // 4. Buscar el entrenador del jugador y asignarle los items
	        for (Entrenador e : entrenadores) {
	            if (e.getName().equals(Entrenador1)) { // Usando Entrenador1 que ya existe en PokemonGUI
	                playerItems.put(e, itemsEscogidos);
	                break;
	            }
	        }
	        
	        // 5. Agregar los items usando el método existente
	        pokemonAplication.agregarItemsLucha(playerItems);
	        
	        // 6. Mostrar el panel de batalla
	        mostrarBattlePvMPanel();
    	} catch(PokemonException p) {
    		p.getStackTrace();

    	}
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
    
    private void cargarPokemonMaquina() {
    	try {
	        // Crear el mapa de pokémon por tipo para la máquina
	        Map<Pokemon.Tipo, List<Pokemon>> pokemonesPorTipo = new HashMap<>();
	        
	        // Inicializar las listas para cada tipo
	        for (Pokemon.Tipo tipo : Pokemon.Tipo.values()) {
	            pokemonesPorTipo.put(tipo, new ArrayList<>());
	        }
	        
	        
	        // Agregar pokémon de un tipo
	        for (PokemonUnTipo.PokemonBase base : PokemonUnTipo.PokemonBase.values()) {
	            Pokemon pokemon = new PokemonUnTipo(base.name(), base.stats[0], new ArrayList<>());
	            pokemonesPorTipo.get(base.tipo).add(pokemon);
	        }
	        
	        // Agregar pokémon de dos tipos
	        for (PokemonDosTipos.PokemonBase base : PokemonDosTipos.PokemonBase.values()) {
	            Pokemon pokemon = new PokemonDosTipos(base.name(), base.stats[0], new ArrayList<>());
	            pokemonesPorTipo.get(base.tipo1).add(pokemon);
	        }
	        
	        // Dejar que la máquina escoja sus pokémon
	        maquina.escogerPokemonesTipo(pokemonesPorTipo);
	
	        // Agregar movimientos a los pokémon de la máquina
	        Map<Pokemon.Tipo, List<Movimiento>> movimientosPorTipo = new HashMap<>();
	        for (Pokemon.Tipo tipo : Pokemon.Tipo.values()) {
	            movimientosPorTipo.put(tipo, Movimiento.getMovimientosPorTipo(tipo));
	        }
	        
	        for (Pokemon pokemon : maquina.getPokemones()) {
	            maquina.agregarMovimientos(pokemon, movimientosPorTipo, new ArrayList<>());
	        }
    	} catch(PokemonException p) {
    		p.getStackTrace();

    	}
    }
    
    private void debugMovements() {
        System.out.println("\n=== Debug de Movimientos ===");
        System.out.println("Jugador actual: " + currentPlayer);
        System.out.println("playerMovements null? " + (playerMovements == null));
        if (playerMovements != null) {
            System.out.println("Número de jugadores con movimientos: " + playerMovements.size());
            for (Map.Entry<String, Map<Pokemon, List<Movimiento>>> entry : playerMovements.entrySet()) {
                System.out.println("\nJugador: " + entry.getKey());
                Map<Pokemon, List<Movimiento>> pokemonMoves = entry.getValue();
                for (Map.Entry<Pokemon, List<Movimiento>> pokemonEntry : pokemonMoves.entrySet()) {
                    System.out.println("  Pokémon: " + pokemonEntry.getKey().getNombre());
                    System.out.println("  Movimientos: " + pokemonEntry.getValue().size());
                    for (Movimiento mov : pokemonEntry.getValue()) {
                        System.out.println("    - " + mov.getName());
                    }
                }
            }
        }
        System.out.println("===========================\n");
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