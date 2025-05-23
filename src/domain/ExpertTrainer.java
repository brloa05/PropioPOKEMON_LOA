package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import domain.Pokemon.Tipo;

public class ExpertTrainer extends EntrenadorMachine{

	public ExpertTrainer(String name, String tipo ) throws PokemonException {
		super(name, tipo);
	}
	//items
		//
	@Override
	public void escogerPokemonesTipo(Map<Tipo, List<Pokemon>> pokemonesMachine) throws PokemonException {
		if (items == null) {
			throw new PokemonException(PokemonException.LISTA_ITEMS_VACIA);
		}
	    ArrayList<Pokemon> todos = new ArrayList<>();
	    for (List<Pokemon> lista : pokemonesMachine.values()) {
	        todos.addAll(lista);
	    }

	    // Criterio de puntaje compuesto (puedes ajustar pesos)
	    todos.sort((a, b) -> {
	        int scoreA = (a.getAtaque() + a.getAtaqueEspecial()) * 2 + a.getVelocidad()
	                   + a.getDefensa() + a.getDefensaEspecial() + a.getSaludMaxima();
	        int scoreB = (b.getAtaque() + b.getAtaqueEspecial()) * 2 + b.getVelocidad()
	                   + b.getDefensa() + b.getDefensaEspecial() + b.getSaludMaxima();
	        return Integer.compare(scoreB, scoreA);
	    });

	    ArrayList<Pokemon> pokemonesElegidos = new ArrayList<>();
	    for (int i = 0; i < 6 && i < todos.size(); i++) {
	        pokemonesElegidos.add(todos.get(i));
	    }

	    this.pokemones = pokemonesElegidos;
	}

	
	@Override
	public void escogerItemsTipo(ArrayList<Item> items) throws PokemonException {
		if (items == null) {
			throw new PokemonException(PokemonException.LISTA_ITEMS_VACIA);
		}

	    int pocionesElegidas = 0;
	    boolean reviveElegido = false;

	    // Ordenar por mayor recuperación para elegir las 2 mejores pociones
	    items.sort((a, b) -> Integer.compare( b.getRecuperacion(), a.getRecuperacion()));

	    for (Item it : items) {
	        if (it.esRevive()) continue;
	        if (pocionesElegidas < 2) {
	            this.items.add(it);
	            pocionesElegidas++;
	        }
	    }

	    for (Item it : items) {
	        if (it.esRevive() && !reviveElegido) {
	            this.items.add(it);
	            reviveElegido = true;
	            break;
	        }
	    }
	}
	
	@Override
	public void agregarMovimientos(Pokemon pokemon, Map<Tipo, List<Movimiento>> movimientosPorTipo, List<Movimiento> movimientosIsPlayer) throws PokemonException {
		if (pokemon == null  ) {
			throw new PokemonException(PokemonException.POKEMON_IS_NULL);
		}
		
		if (movimientosPorTipo == null  ) {
			throw new PokemonException(PokemonException.MOVIMIENTOSPORTIPO_IS_NULL);
		}
		
		if (movimientosIsPlayer == null  ) {
			throw new PokemonException(PokemonException.MOVIMIENTOSISPLAYER_IS_NULL);
		}
		ArrayList<Movimiento> todosMovimientos = new ArrayList<>();

	    // Agregar todos los movimientos de todos los tipos
	    for (List<Movimiento> lista : movimientosPorTipo.values()) {
	        todosMovimientos.addAll(lista);
	    }

	    // Ordenar los movimientos por un criterio de puntaje
	    todosMovimientos.sort((m1, m2) -> {
	        int score1 = m1.getPotencia() * m1.getPresicion(); // Ejemplo: peso por poder y precisión
	        int score2 = m2.getPotencia() * m2.getPresicion();
	        return Integer.compare(score2, score1); // Mayor puntaje primero
	    });

	    // Seleccionar los 4 mejores movimientos
	    ArrayList<Movimiento> mejoresMovimientos = new ArrayList<>();
	    for (int i = 0; i < 4 && i < todosMovimientos.size(); i++) {
	        mejoresMovimientos.add(todosMovimientos.get(i));
	    }

	    // Asignar los movimientos al Pokémon
	    pokemon.setMovimientos(mejoresMovimientos);
	}

}