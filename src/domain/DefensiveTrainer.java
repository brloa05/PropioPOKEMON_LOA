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

	    // Ordenar por defensa total descendente
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

	
	@Override
	public void escogerItemsTipo() {
	    ArrayList<Item> potions = new ArrayList<>();
	    potions.add(new Potion("Potion0", 20));
	    potions.add(new SuperPotion("SuperPotion0", 50));
	    potions.add(new HyperPotion("HyperPotion0", 200));
	    potions.add(new Revive("Revive0", 200));

	    int pocionesElegidas = 0;
	    boolean reviveElegido = false;

	    // Ordenar por mayor recuperación primero (atacante = máxima recuperación en combate)
	    potions.sort((a, b) -> Integer.compare(b.getRecuperacion(), a.getRecuperacion()));

	    for (Item it : potions) {
	        if (it.esRevive()) continue;
	        if (pocionesElegidas < 2) {
	            this.items.add(it);
	            pocionesElegidas++;
	        }
	    }

	    for (Item it : potions) {
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
		        // Manejar caso cuando no hay más elementos disponibles
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
		        // Obtener tipo aleatorio no usado
		        Tipo tipoAleatorio = obtenerLlaveAleatoria(movimientosPorTipo, tiposUsados2);
		        tiposUsados2.add(tipoAleatorio);
		        
		        // Obtener movimiento aleatorio no usado
		        List<Movimiento> movimientos = movimientosPorTipo.get(tipoAleatorio);
		        Movimiento m = obtenerUnElementoAleatorio(movimientos, movimientosUsados2);
		        movimientosUsados2.add(m);
		        
		        if (m instanceof Especial || m instanceof Fisico) {
		            defensiveMovimiento.add(m);
		        }
		    } catch (NoSuchElementException e) {
		        // Manejar caso cuando no hay más elementos disponibles
		        break;
		    }
		}
		
		pokemon.setMovimientos(defensiveMovimiento);
	}




}


