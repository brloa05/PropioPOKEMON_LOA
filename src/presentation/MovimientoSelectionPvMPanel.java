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
    private JButton btnSiguiente;           // Nuevo botón
    private JButton btnConfirmarMovimientos; // Nuevo botón
    private Map<Pokemon, Boolean> pokemonConfirmado;
    private List<Pokemon> selectedPokemons = new ArrayList<>();
    private String tipoDeMaquinaSeleccionado = "Normal";
    
    public MovimientoSelectionPvMPanel(PokemonGUI pokemonGUI, List<Pokemon> pokemones) {
        this.pokemonGUI = pokemonGUI;
        this.pokemones = pokemones;
        this.movimientosAsignados = new HashMap<>();
        this.pokemonConfirmado = new HashMap<>();
        
        // Inicializar el estado de confirmación para los Pokémon que ya tienen 4 movimientos
        for (Pokemon pokemon : pokemones) {
            List<Movimiento> moves = movimientosAsignados.getOrDefault(pokemon, new ArrayList<>());
            if (moves.size() == 4) {
                pokemonConfirmado.put(pokemon, true);
            }
        }
        
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


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        tnConfirmarMovimientos = new JButton("Confirmar Movimientos");
        btnSiguiente = new JButton("Siguiente");
        
        // Configuración inicial de los botones
        bottomPanel.add(btnConfirmarMovimientos);
        bottomPanel.add(btnSiguiente);
        add(bottomPanel, BorderLayout.SOUTH);

        // Cargar los movimientos iniciales y verificar el estado
        loadPokemonMovements((Pokemon) cbPokemons.getSelectedItem());
        verificarMovimientosCompletos();
        verificarTodosLosPokemon();
        
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
        
        btnConfirmarMovimientos.addActionListener(e -> {
            Pokemon currentPokemon = (Pokemon) cbPokemons.getSelectedItem();
            if (currentPokemon != null && selectedModel.size() == 4) {
                saveCurrentPokemonMovements(currentPokemon);
                pokemonConfirmado.put(currentPokemon, true);
                btnConfirmarMovimientos.setEnabled(false);
                verificarTodosLosPokemon();
            }
        });
        
        btnAddMovement.addActionListener(e -> {
            Movimiento selected = availableMovementsList.getSelectedValue();
            Pokemon currentPokemon = (Pokemon) cbPokemons.getSelectedItem();
            
            if (selected != null && selectedModel.size() < 4) {
                System.out.println("Añadiendo movimiento: " + selected.getName() + 
                                 " a " + currentPokemon.getNombre());
                selectedModel.addElement(selected);
                availableModel.removeElement(selected);
                
                saveCurrentPokemonMovements(currentPokemon);
                actualizarEstadoBotones(currentPokemon);
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
                
                saveCurrentPokemonMovements(currentPokemon);
                pokemonConfirmado.put(currentPokemon, false); // Desconfirmar al remover movimientos
                actualizarEstadoBotones(currentPokemon);
            }
        });
        
        cbPokemons.addActionListener(e -> {
            Pokemon selected = (Pokemon) cbPokemons.getSelectedItem();
            loadPokemonMovements(selected);
            actualizarEstadoBotones(selected);
        });

        btnSiguiente.addActionListener(e -> {
            Pokemon current = (Pokemon) cbPokemons.getSelectedItem();
            
            System.out.println("\n=== Verificación Final ===");
            boolean allComplete = true;
            StringBuilder errorMessage = new StringBuilder("Estado de los Pokémon:\n");
            
            for (Pokemon p : pokemones) {
                List<Movimiento> moves = movimientosAsignados.get(p);
                Boolean isConfirmed = pokemonConfirmado.getOrDefault(p, false);
                int moveCount = moves != null ? moves.size() : 0;
                
                errorMessage.append(p.getNombre())
                           .append(": ")
                           .append(moveCount)
                           .append(" movimientos - ")
                           .append(isConfirmed ? "Confirmado" : "No confirmado")
                           .append("\n");
                
                allComplete &= isConfirmed && moveCount == 4;
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
                    "Debes confirmar 4 movimientos para cada Pokémon:\n" + errorMessage.toString(),
                    "Movimientos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
    }

    private void setModoJuego(ModoJuego pvm) {
		// TODO Auto-generated method stub
		
	}
    
    private void actualizarEstadoBotones(Pokemon pokemon) {
        if (pokemon != null) {
            List<Movimiento> moves = movimientosAsignados.getOrDefault(pokemon, new ArrayList<>());
            boolean tieneCuatroMovimientos = moves.size() == 4;
            boolean estaConfirmado = pokemonConfirmado.getOrDefault(pokemon, false);
            
            // El botón de confirmar se habilita solo si tiene 4 movimientos y no está confirmado
            btnConfirmarMovimientos.setEnabled(tieneCuatroMovimientos && !estaConfirmado);
            
            // Verificar si todos los Pokémon están confirmados
            verificarTodosLosPokemon();
        }
    }
    
    private void verificarTodosLosPokemon() {
        boolean todosConfirmados = true;
        for (Pokemon p : pokemones) {
            List<Movimiento> moves = movimientosAsignados.getOrDefault(p, new ArrayList<>());
            boolean estaConfirmado = pokemonConfirmado.getOrDefault(p, false);
            todosConfirmados &= (moves.size() == 4 && estaConfirmado);
        }
        
        btnSiguiente.setEnabled(todosConfirmados);
        System.out.println("Estado de botón Siguiente: " + todosConfirmados);
        System.out.println("Pokémon confirmados: " + pokemonConfirmado);
    }


    
    private void verificarMovimientosCompletos() {
        boolean allHaveMovements = true;
        
        for (Pokemon p : pokemones) {
            List<Movimiento> movements = movimientosAsignados.get(p);
            int numMovimientos = (movements != null ? movements.size() : 0);
            boolean hasMovements = numMovimientos == 4; // Cambiar a exactamente 4 movimientos
            
            if (hasMovements && !pokemonConfirmado.containsKey(p)) {
                pokemonConfirmado.put(p, true);
            }
            
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

        actualizarEstadoBotones((Pokemon) cbPokemons.getSelectedItem());
        verificarTodosLosPokemon();
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

        List<Movimiento> currentMoves = movimientosAsignados.getOrDefault(pokemon, new ArrayList<>());
        if (currentMoves.size() == 4 && !pokemonConfirmado.containsKey(pokemon)) {
            pokemonConfirmado.put(pokemon, true);
        }
        
        actualizarEstadoBotones(pokemon);
        verificarTodosLosPokemon();

        verificarMovimientosCompletos();
    }

    private void saveCurrentPokemonMovements(Pokemon pokemon) {
        if (pokemon == null) return;

        List<Movimiento> movements = new ArrayList<>();
        for (int i = 0; i < selectedModel.size(); i++) {
            movements.add(selectedModel.getElementAt(i));
        }

        System.out.println("\n=== Guardando movimientos para " + pokemon.getNombre() + " ===");
        System.out.println("Número de movimientos a guardar: " + movements.size());
        movements.forEach(m -> System.out.println("- " + m.getName()));

        movimientosAsignados.put(pokemon, new ArrayList<>(movements));
        
        // Si tiene exactamente 4 movimientos, marcarlo como confirmado automáticamente
        if (movements.size() == 4) {
            pokemonConfirmado.put(pokemon, true);
        } else {
            pokemonConfirmado.put(pokemon, false);
        }
        
        actualizarEstadoBotones(pokemon);
    }
    
    public void showError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
}