
package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import domain.Pokemon.Tipo;

public class ChangingTrainer extends EntrenadorMachine {

	public ChangingTrainer(String name, String tipo) {
		super(name, tipo);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void escogerPokemonesTipo(Map<Tipo, List<Pokemon>> pokemonesMachine) {
	    //se queman los datos 
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

	    Item menor = null;
	    Item mayor = null;

	    for (Item it : potions) {
	        if (it.esRevive()) continue; // lo elegimos después
	        if (menor == null || it.getRecuperacion() < menor.getRecuperacion()) {
	            menor = it;
	        }
	        if (mayor == null || it.getRecuperacion() > mayor.getRecuperacion()) {
	            mayor = it;
	        }
	    }

	    if (menor != null && pocionesElegidas < 2) {
	        this.items.add(menor);
	        pocionesElegidas++;
	    }
	    if (mayor != null && !mayor.equals(menor) && pocionesElegidas < 2) {
	        this.items.add(mayor);
	        pocionesElegidas++;
	    }

	    // Agrega revive si no está ya y si no fue elegido
	    for (Item it : potions) {
	        if (it.esRevive() && !reviveElegido) {
	            this.items.add(it);
	            reviveElegido = true;
	        }
	    }
	}
	
	@Override
	public void agregarMovimientos(Pokemon pokemon, Map<Tipo, List<Movimiento>> movimientosPorTipo, List<Movimiento> movimientosIsPlayer) {
		List<Movimiento> defensiveMovimiento = new ArrayList<>();
		Set<Tipo> tiposUsados = new HashSet<>();
		Set<Movimiento> movimientosUsados = new HashSet<>();
		String ultimoTipo = "";

		while (defensiveMovimiento.size() < 4) {
		    try {
		        Tipo tipoAleatorio = obtenerLlaveAleatoria(movimientosPorTipo, tiposUsados);
		        tiposUsados.add(tipoAleatorio);
		        
		        
		        List<Movimiento> movimientos = movimientosPorTipo.get(tipoAleatorio);
		        Movimiento m = obtenerUnElementoAleatorio(movimientos, movimientosUsados);
		        movimientosUsados.add(m);
		        
		        if (m instanceof DeEstado && !ultimoTipo.equals("DeEstado") ) {
		            defensiveMovimiento.add(m);
		            ultimoTipo = "DeEstado";
		        } else if(m instanceof Especial && !ultimoTipo.equals("Especial")) {
		        	defensiveMovimiento.add(m);
		            ultimoTipo = "Especial";
		        } else if (m instanceof Fisico && !ultimoTipo.equals("Fisico")) {
		        	defensiveMovimiento.add(m);
		            ultimoTipo = "Fisico";
		        }
		        
		    } catch (NoSuchElementException e) {
		        // Manejar caso cuando no hay más elementos disponibles
		        break;
		    }
		}
		
		
		pokemon.setMovimientos(defensiveMovimiento);
	}


}
