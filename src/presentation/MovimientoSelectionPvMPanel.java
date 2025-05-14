package presentation;

import domain.Movimiento;
import domain.Pokemon;
import domain.Pokemon.Tipo;
import presentation.PokemonGUI.ModoJuego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MovimientoSelectionPvMPanel extends JPanel {
    private PokemonGUI pokemonGUI;
    private List<Pokemon> pokemones;
    private Map<Pokemon, List<Movimiento>> movimientosAsignados;
    private JComboBox<Pokemon> cbPokemons;
    private JList<Movimiento> availableMovementsList;
    private JList<Movimiento> selectedMovementsList;
    private DefaultListModel<Movimiento> availableModel;
    private DefaultListModel<Movimiento> selectedModel;
    private JButton btnAddMovement;
    private JButton btnRemoveMovement;
    private JButton btnConfirmarYSiguiente;
    private List<Pokemon> selectedPokemons = new ArrayList<>();
    private String tipoDeMaquinaSeleccionado = "Normal";
    
    public MovimientoSelectionPvMPanel(PokemonGUI pokemonGUI, List<Pokemon> pokemones) {
        this.pokemonGUI = pokemonGUI;
        this.pokemones = pokemones;
        this.movimientosAsignados = new HashMap<>();
        prepareElements();
        prepareActions();
    }

    private void prepareElements() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cbPokemons = new JComboBox<>();
        pokemones.forEach(p -> cbPokemons.addItem(p));
        topPanel.add(new JLabel("Selecciona un Pokémon:"));
        topPanel.add(cbPokemons);
        add(topPanel, BorderLayout.NORTH);


        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        availableModel = new DefaultListModel<>();
        availableMovementsList = new JList<>(availableModel);
        availableMovementsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectedModel = new DefaultListModel<>();
        selectedMovementsList = new JList<>(selectedModel);
        selectedMovementsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        btnAddMovement = new JButton(">");
        btnRemoveMovement = new JButton("<");
        buttonPanel.add(btnAddMovement);
        buttonPanel.add(btnRemoveMovement);

        centerPanel.add(new JScrollPane(availableMovementsList));
        centerPanel.add(buttonPanel);
        centerPanel.add(new JScrollPane(selectedMovementsList));

        add(centerPanel, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnConfirmarYSiguiente = new JButton("Confirmar y Continuar");
        btnConfirmarYSiguiente.setEnabled(false); // Inicialmente deshabilitado
        bottomPanel.add(btnConfirmarYSiguiente);
        add(bottomPanel, BorderLayout.SOUTH);
        
        if (movimientosAsignados == null) {
            movimientosAsignados = new HashMap<>();
        }

        // Cargar los movimientos iniciales
        loadPokemonMovements((Pokemon) cbPokemons.getSelectedItem());
        
        // Verificar el estado inicial
        System.out.println("=== Inicialización del Panel ===");
        System.out.println("Número de Pokémon: " + pokemones.size());
        verificarMovimientosCompletos();
        
        loadPokemonMovements((Pokemon) cbPokemons.getSelectedItem());
        verificarMovimientosCompletos(); // Verificar estado inicial
    }

    private void prepareActions() {
        cbPokemons.addActionListener(e -> {
            Pokemon selected = (Pokemon) cbPokemons.getSelectedItem();
            loadPokemonMovements(selected);
        });

        btnAddMovement.addActionListener(e -> {
            Movimiento selected = availableMovementsList.getSelectedValue();
            Pokemon currentPokemon = (Pokemon) cbPokemons.getSelectedItem();
            
            if (selected != null && selectedModel.size() < 4) {
                System.out.println("Añadiendo movimiento: " + selected.getName() + 
                                 " a " + currentPokemon.getNombre());
                selectedModel.addElement(selected);
                availableModel.removeElement(selected);
                
                // Guardar inmediatamente después de añadir
                saveCurrentPokemonMovements(currentPokemon);
            }
        });

        btnRemoveMovement.addActionListener(e -> {
            Movimiento selected = selectedMovementsList.getSelectedValue();
            Pokemon currentPokemon = (Pokemon) cbPokemons.getSelectedItem();
            
            if (selected != null) {
                System.out.println("Removiendo movimiento: " + selected.getName() + 
                                 " de " + currentPokemon.getNombre());
                availableModel.addElement(selected);
                selectedModel.removeElement(selected);
                
                // Guardar inmediatamente después de remover
                saveCurrentPokemonMovements(currentPokemon);
            }
        });

        btnConfirmarYSiguiente.addActionListener(e -> {
            Pokemon current = (Pokemon) cbPokemons.getSelectedItem();
            saveCurrentPokemonMovements(current);

            System.out.println("\n=== Verificación Final ===");
            boolean allComplete = true;
            StringBuilder errorMessage = new StringBuilder("Estado de los Pokémon:\n");
            
            for (Pokemon p : pokemones) {
                List<Movimiento> moves = movimientosAsignados.get(p);
                int moveCount = moves != null ? moves.size() : 0;
                boolean hasEnoughMoves = moveCount >= 1;
                
                errorMessage.append(p.getNombre())
                           .append(": ")
                           .append(moveCount)
                           .append(" movimientos\n");
                
                allComplete &= hasEnoughMoves;
            }
            
            System.out.println(errorMessage.toString());
            
            if (allComplete) {
                pokemonGUI.setPokemonMovements(new HashMap<>(movimientosAsignados));
                pokemonGUI.displayCurrentPlayerData();
                setModoJuego(ModoJuego.PVM);
                pokemonGUI.mostrarBattlePvMPanel();
                pokemonGUI.setTipoMaquinaSeleccionada(tipoDeMaquinaSeleccionado);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Falta asignar movimientos:\n" + errorMessage.toString(),
                    "Movimientos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
    }

    private void setModoJuego(ModoJuego pvm) {
		// TODO Auto-generated method stub
		
	}
    
    private void verificarMovimientosCompletos() {
        System.out.println("\n=== Verificación de Movimientos ===");
        boolean allHaveMovements = true;
        Map<String, Integer> movimientosPorPokemon = new HashMap<>();

        for (Pokemon p : pokemones) {
            List<Movimiento> movements = movimientosAsignados.get(p);
            int numMovimientos = (movements != null ? movements.size() : 0);
            boolean hasMovements = numMovimientos >= 1;
            
            movimientosPorPokemon.put(p.getNombre(), numMovimientos);
            
            System.out.println(String.format(
                "Pokémon: %-10s - Movimientos: %d - Tiene suficientes: %s",
                p.getNombre(),
                numMovimientos,
                hasMovements
            ));
            
            allHaveMovements &= hasMovements;
        }

        System.out.println("Estado del mapa de movimientos:");
        movimientosAsignados.forEach((pokemon, moves) -> 
            System.out.println(pokemon.getNombre() + ": " + moves.size() + " movimientos"));

        btnConfirmarYSiguiente.setEnabled(allHaveMovements);
        System.out.println("Botón habilitado: " + allHaveMovements);
    }

    private void loadPokemonMovements(Pokemon pokemon) {
        if (pokemon == null) {
            availableModel.clear();
            selectedModel.clear();
            verificarMovimientosCompletos();
            return;
        }

        // Guardar los movimientos del pokemon actual antes de cargar el nuevo
        Pokemon previousPokemon = (Pokemon) cbPokemons.getSelectedItem();
        if (previousPokemon != null) {
            saveCurrentPokemonMovements(previousPokemon);
        }

        availableModel.clear();
        selectedModel.clear();

        // Obtener todos los movimientos posibles para el pokemon
        Set<Movimiento> allPossibleMovements = new HashSet<>(); // Usar Set para evitar duplicados
        for (Tipo tipo : pokemon.getTipos()) {
            List<Movimiento> movimientosTipo = Movimiento.getMovimientosPorTipo(tipo);
            if (movimientosTipo != null) {
                allPossibleMovements.addAll(movimientosTipo);
            }
        }

        // Debugging: Imprimir tipos y movimientos disponibles
        System.out.println("\n=== Cargando movimientos para " + pokemon.getNombre() + " ===");
        System.out.println("Tipos del Pokémon: " + Arrays.toString(pokemon.getTipos().toArray()));
        System.out.println("Movimientos posibles encontrados: " + allPossibleMovements.size());

        // Obtener movimientos ya asignados a otros pokemon
        Set<Movimiento> assignedToOthers = new HashSet<>();
        movimientosAsignados.entrySet().stream()
            .filter(entry -> !entry.getKey().equals(pokemon))
            .forEach(entry -> assignedToOthers.addAll(entry.getValue()));

        // Obtener movimientos ya asignados a este pokemon
        List<Movimiento> alreadyAssigned = movimientosAsignados.getOrDefault(pokemon, new ArrayList<>());
        
        // Agregar primero los movimientos ya asignados
        alreadyAssigned.forEach(mov -> {
            selectedModel.addElement(mov);
            System.out.println("Movimiento ya asignado: " + mov.getName());
        });

        // Agregar los movimientos disponibles que no están asignados a otros
        allPossibleMovements.stream()
            .filter(m -> !assignedToOthers.contains(m) || alreadyAssigned.contains(m))
            .forEach(m -> {
                if (!alreadyAssigned.contains(m)) {
                    availableModel.addElement(m);
                    System.out.println("Movimiento disponible añadido: " + m.getName());
                }
            });

        verificarMovimientosCompletos();
    }

	private void saveCurrentPokemonMovements(Pokemon pokemon) {
	    if (pokemon == null) {
	        System.out.println("ERROR: Intentando guardar movimientos para un Pokémon null");
	        return;
	    }

	    List<Movimiento> movements = new ArrayList<>();
	    for (int i = 0; i < selectedModel.size(); i++) {
	        movements.add(selectedModel.getElementAt(i));
	    }

	    System.out.println("\n=== Guardando movimientos para " + pokemon.getNombre() + " ===");
	    System.out.println("Número de movimientos a guardar: " + movements.size());
	    movements.forEach(m -> System.out.println("- " + m.getName()));

	    movimientosAsignados.put(pokemon, new ArrayList<>(movements));

	    // Verificar que los movimientos se guardaron correctamente
	    List<Movimiento> savedMovements = movimientosAsignados.get(pokemon);
	    System.out.println("Movimientos guardados efectivamente: " + 
	        (savedMovements != null ? savedMovements.size() : 0));

	    verificarMovimientosCompletos();
	}
    
    public void showError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
}