package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import domain.Pokemon.Tipo;

public class DefensiveTrainer extends EntrenadorMachine{

	public DefensiveTrainer(String name, String tipo) {
		super(name, tipo);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void escogerPokemonesTipo(Map<Tipo, List<Pokemon>> pokemonesMachine) {
	    ArrayList<Pokemon> todos = new ArrayList<>();
	    for (List<Pokemon> lista : pokemonesMachine.values()) {
	        todos.addAll(lista);
	    }

	    
	    todos.sort((a, b) -> Integer.compare(
	        (b.getDefensa() + b.getDefensaEspecial()),
	        (a.getDefensa() + a.getDefensaEspecial())
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
	public void escogerItemsTipo(ArrayList<Item> items) {
	    

	    int pocionesElegidas = 0;
	    boolean reviveElegido = false;

	    
	    items.sort((a, b) -> Integer.compare(b.getRecuperacion(), a.getRecuperacion()));

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
	public void agregarMovimientos(Pokemon pokemon, Map<Tipo, List<Movimiento>> movimientosPorTipo, List<Movimiento> movimientosIsPlayer) {
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
		        
		        if (m instanceof DeEstado) {
		            defensiveMovimiento.add(m);
		        }
		    } catch (NoSuchElementException e) {
		       
		        break;
		    }
		}

		if (defensiveMovimiento.size() < 3) {
		    System.out.println("Advertencia: Solo se encontraron " + defensiveMovimiento.size() + 
		                     " movimientos de estado únicos");
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
		        
		        if (m instanceof Especial || m instanceof Fisico) {
		            defensiveMovimiento.add(m);
		        }
		    } catch (NoSuchElementException e) {
		        
		        break;
		    }
		}
		
		pokemon.setMovimientos(defensiveMovimiento);
	}




}


