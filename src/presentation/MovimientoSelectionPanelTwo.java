package presentation;

import domain.Movimiento;
import domain.Pokemon;
import domain.Pokemon.Tipo;
import presentation.PokemonGUI.ModoJuego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovimientoSelectionPanelTwo extends JPanel {
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
    private JButton btnConfirm;
    private JButton btnSiguiente;
    private List<Pokemon> selectedPokemons = new ArrayList<>();
    private JButton btnConfirmAndProceed_NEW;

    public MovimientoSelectionPanelTwo(PokemonGUI pokemonGUI, List<Pokemon> pokemones) {
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
        btnConfirm = new JButton("Confirmar Movimientos");
        btnSiguiente = new JButton("Siguiente");
        btnConfirmAndProceed_NEW = new JButton("Avanzar (Nueva Lógica)");
        bottomPanel.add(btnConfirm);
        bottomPanel.add(btnSiguiente);
        bottomPanel.add(btnConfirmAndProceed_NEW);
        add(bottomPanel, BorderLayout.SOUTH);


        loadPokemonMovements((Pokemon) cbPokemons.getSelectedItem());
    }

    private void prepareActions() {
        cbPokemons.addActionListener(e -> {
            Pokemon selected = (Pokemon) cbPokemons.getSelectedItem();
            loadPokemonMovements(selected);
        });

        btnAddMovement.addActionListener(e -> {
            Movimiento selected = availableMovementsList.getSelectedValue();
            if (selected != null && selectedModel.size() < 4) {
                selectedModel.addElement(selected);
                availableModel.removeElement(selected);
            }
        });

        btnRemoveMovement.addActionListener(e -> {
            Movimiento selected = selectedMovementsList.getSelectedValue();
            if (selected != null) {
                availableModel.addElement(selected);
                selectedModel.removeElement(selected);
            }
        });

     // En prepareActions():

        btnConfirm.addActionListener(e -> {
            Pokemon current = (Pokemon) cbPokemons.getSelectedItem();
            saveCurrentPokemonMovements(current);
            
            if (validateAllMovementsComplete()) {
                pokemonGUI.setPokemonMovements(new HashMap<>(movimientosAsignados));
                pokemonGUI.syncMovementsWithTrainers(); // Llamada clave para sincronizar
                
                JOptionPane.showMessageDialog(this, 
                    "Movimientos confirmados correctamente", 
                    "Confirmación", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                showError("Todos los Pokémon deben tener 4 movimientos asignados");
            }
        });

        btnSiguiente.addActionListener(e -> {
            // 1. Guardar movimientos del Pokémon actual
            Pokemon current = (Pokemon) cbPokemons.getSelectedItem();
            saveCurrentPokemonMovements(current);
            
            // 2. Validar completitud
            if (!validateAllMovementsComplete()) {
                showError("Debes completar todos los movimientos primero");
                return;
            }
            
            // 3. Solo navegar a batalla
            pokemonGUI.mostrarBattlePanel();
        });
        
        btnConfirmAndProceed_NEW.addActionListener(e -> {
            Pokemon currentPokemonInComboBox = (Pokemon) cbPokemons.getSelectedItem();
            if (currentPokemonInComboBox != null) {
                saveCurrentPokemonMovements(currentPokemonInComboBox); // Asegura que los movimientos del Pokémon actualmente visible en el ComboBox se guarden
            }

            // Lógica de Validación Correcta: Verifica si todos los Pokémon en el equipo del jugador tienen 4 movimientos.
            // La lista 'pokemones' contiene todos los Pokémon seleccionados por el jugador actual para su equipo.
            boolean allPokemonHaveFourMoves = true;
            if (pokemones == null || pokemones.isEmpty()) {
                allPokemonHaveFourMoves = false; // No debería ocurrir si la selección se hizo correctamente.
            } else {
                for (Pokemon p : pokemones) {
                    // 'movimientosAsignados' almacena los movimientos asignados a cada Pokémon por el jugador en este panel.
                    if (movimientosAsignados.getOrDefault(p, new ArrayList<>()).size() != 4) {
                        allPokemonHaveFourMoves = false;
                        break;
                    }
                }
            }

            if (allPokemonHaveFourMoves) {
                // Pasa todos los movimientos recopilados para el jugador actual al controlador principal de la GUI.
                // 'pokemonGUI.setPokemonMovements' debería manejar la actualización del estado para el jugador actual.
                pokemonGUI.setPokemonMovements(new HashMap<>(movimientosAsignados));

                // Asegura que estos movimientos también se sincronicen con los objetos Entrenador.
                pokemonGUI.syncMovementsWithTrainers();

                // Procede al panel de batalla.
                pokemonGUI.mostrarBattlePanel();
            } else {
                // Muestra un mensaje de error indicando que no todos los Pokémon tienen 4 movimientos.
                showError("Error de Nueva Lógica: Cada Pokémon debe tener exactamente 4 movimientos asignados para proceder.");
            }
        });
        //
    }
    

        private boolean validateAllMovementsComplete() {
            return pokemones.stream()
                .allMatch(p -> movimientosAsignados.getOrDefault(p, List.of()).size() == 1);
        }

    private void setModoJuego(ModoJuego pvp) {
		// TODO Auto-generated method stub
		
	}

	private void loadPokemonMovements(Pokemon pokemon) {
    	
    	if (pokemon == null) {
            availableModel.clear();
            selectedModel.clear();
            return;
        }
    	
    	if (cbPokemons.getSelectedIndex() > 0) {
            saveCurrentPokemonMovements((Pokemon) cbPokemons.getItemAt(cbPokemons.getSelectedIndex()-1));
        }


        availableModel.clear();
        selectedModel.clear();


        List<Movimiento> allPossibleMovements = new ArrayList<>();
        for (Tipo tipo : pokemon.getTipos()) {
            allPossibleMovements.addAll(Movimiento.getMovimientosPorTipo(tipo));
        }
        

        List<Movimiento> assignedToOthers = new ArrayList<>();
        movimientosAsignados.entrySet().stream()
            .filter(entry -> !entry.getKey().equals(pokemon))
            .forEach(entry -> assignedToOthers.addAll(entry.getValue()));
        

        List<Movimiento> alreadyAssigned = movimientosAsignados.getOrDefault(pokemon, List.of());
        
        allPossibleMovements.stream()
        .filter(m -> !assignedToOthers.contains(m) || alreadyAssigned.contains(m))
        .forEach(availableModel::addElement);
            
        alreadyAssigned.forEach(selectedModel::addElement);
    }

    private void saveCurrentPokemonMovements(Pokemon pokemon) {
        List<Movimiento> movements = new ArrayList<>();
        for (int i = 0; i < selectedModel.size(); i++) {
            movements.add(selectedModel.getElementAt(i));
        }
        movimientosAsignados.put(pokemon, movements);
    }
    
    public void showError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void confirmSelection() {
        if (selectedPokemons.size() < 1) {
            showError("Debes seleccionar al menos 1 Pokémon");
            return;
        }
        pokemonGUI.setSelectedPokemons(new ArrayList<>(selectedPokemons));
        pokemonGUI.setPokemonMovements(new HashMap<>(movimientosAsignados));
    }
}