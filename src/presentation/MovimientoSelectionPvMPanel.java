package presentation;

import domain.Movimiento;
import domain.Pokemon;
import domain.Pokemon.Tipo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private JButton btnConfirm;
    private JButton btnSiguiente;
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
        btnConfirm = new JButton("Confirmar Movimientos");
        btnSiguiente = new JButton("Siguiente");
        bottomPanel.add(btnConfirm);
        bottomPanel.add(btnSiguiente);
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

        btnConfirm.addActionListener(e -> {
            Pokemon current = (Pokemon) cbPokemons.getSelectedItem();
            saveCurrentPokemonMovements(current);

            boolean allComplete = pokemones.stream()
                .allMatch(p -> movimientosAsignados.getOrDefault(p, List.of()).size() == 4);

            if (allComplete) {
                pokemonGUI.setPokemonMovements(new HashMap<>(movimientosAsignados));
                pokemonGUI.displayCurrentPlayerData(); 
            } else {
                JOptionPane.showMessageDialog(this,
                    "Debes asignar 4 movimientos a cada Pokémon",
                    "Movimientos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        btnSiguiente.addActionListener(e -> {
            pokemonGUI.setModoJuego("PvM");  
            pokemonGUI.mostrarbattelPanel();  
            pokemonGUI.setTipoMaquinaSeleccionada(tipoDeMaquinaSeleccionado);
        });
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