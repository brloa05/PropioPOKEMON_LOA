package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import domain.Pokemon.Tipo;

public class AttackingTrainer extends EntrenadorMachine{

	public AttackingTrainer(String name, String tipo) throws PokemonException {
		super(name, tipo);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void escogerPokemonesTipo(Map<Tipo, List<Pokemon>> pokemonesMachine) throws PokemonException {
		
		if (pokemonesMachine == null) {
			throw new PokemonException(PokemonException.POKEMONESMACHINE_IS_NULL);
		}
		
	    ArrayList<Pokemon> todos = new ArrayList<>();
	    for (List<Pokemon> lista : pokemonesMachine.values()) {
	        todos.addAll(lista);
	    }

	  
	    todos.sort((a, b) -> Integer.compare(
	        (b.getAtaque() + b.getAtaqueEspecial()),
	        (a.getAtaque() + a.getAtaqueEspecial())
	    ));

	    ArrayList<Pokemon> pokemonesElegidos = new ArrayList<>();
	    for (int i = 0; i < 6 && i < todos.size(); i++) {
	        pokemonesElegidos.add(todos.get(i));
	    }

	    this.pokemones = pokemonesElegidos;
	}
	
	//items
	//
	@Override
	public void escogerItemsTipo(ArrayList<Item> items) throws PokemonException {
	    
		if (items == null) {
			throw new PokemonException(PokemonException.LISTA_ITEMS_VACIA);
		}
	    int pocionesElegidas = 0;
	    boolean reviveElegido = false;

	    
	    items.sort(Comparator.comparingInt(Item::getRecuperacion));

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
		
		List<Movimiento> defensiveMovimiento = new ArrayList<>();
		Set<Tipo> tiposUsados = new HashSet<>();
		Set<Movimiento> movimientosUsados = new HashSet<>();

		while (defensiveMovimiento.size() < 3) {
		    try {
		        
		        Tipo tipoAleatorio = obtenerLlaveAleatoria(movimientosPorTipo, tiposUsados);
		        tiposUsados.add(tipoAleatorio);
		        
		        
		        List<Movimiento> movimientos = movimientosPorTipo.get(tipoAleatorio);
		        Movimiento m = obtenerUnElementoAleatorio(movimientos, movimientosUsados);
		        movimientosUsados.add(m);
		        
		        if (m instanceof Especial || m instanceof Fisico ) {
		            defensiveMovimiento.add(m);
		        }
		    } catch (NoSuchElementException e) {
		    
		        break;
		    }
		}

		
		Set<Tipo> tiposUsados2 = new HashSet<>();
		Set<Movimiento> movimientosUsados2 = new HashSet<>();
		
		while (defensiveMovimiento.size() < 4) {
		    try {
		       
		        Tipo tipoAleatorio = obtenerLlaveAleatoria(movimientosPorTipo, tiposUsados2);
		        tiposUsados2.add(tipoAleatorio);
		        
		        
		        List<Movimiento> movimientos = movimientosPorTipo.get(tipoAleatorio);
		        Movimiento m = obtenerUnElementoAleatorio(movimientos, movimientosUsados2);
		        movimientosUsados2.add(m);
		        
		        if (m instanceof DeEstado) {
		            defensiveMovimiento.add(m);
		        }
		    } catch (NoSuchElementException e) {
		     
		        break;
		    }
		}
		
		pokemon.setMovimientos(defensiveMovimiento);
	}





}


