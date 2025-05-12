package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import domain.Pokemon.Tipo;

public class ModalidadPvP extends ModalidadJuego implements ModoSupervivencia,ModoNormal {
	
	public ModalidadPvP(String name) {
		super(name);
	}

	@Override
	public ArrayList<Entrenador> crearEntrenadores(Map<String, String> nombresAndTipo) {
	    if (nombresAndTipo == null) {
	        throw new IllegalArgumentException("El mapa de nombres y tipos no puede ser null");
	    }

	    ArrayList<Entrenador> entrenadores = new ArrayList<>();
	    for (Map.Entry<String, String> entry : nombresAndTipo.entrySet()) {
	        entrenadores.add(new EntrenadorPlayer(entry.getKey(), entry.getValue()));
	        System.out.println(entry.getKey());
	    }
	    

	    return entrenadores;
	}
	
	@Override
	public void agregarPokemones(Map<Entrenador, ArrayList<Pokemon>> playerPokemones, Map<Tipo,List<Pokemon>> pokemonesMachine) {
		for (Map.Entry<Entrenador, ArrayList<Pokemon>> e: playerPokemones.entrySet()) {
			e.getKey().setPokemones(e.getValue());
		}	
	}

	@Override
	public void agregarItems(Map<Entrenador, ArrayList<Item>> playerItems) {
		for(Map.Entry<Entrenador,ArrayList<Item>> player: playerItems.entrySet()) {
			player.getKey().setItems(player.getValue());
		}
	}

	@Override
	public void agregarMoimientosPlayer(Map<Map<Entrenador, Pokemon>, List<Movimiento>> playerMovimientos, Map<Tipo, List<Movimiento>> movimientosPorTipo) {
		for (Map.Entry<Map<Entrenador, Pokemon>,  List<Movimiento>>  pm : playerMovimientos.entrySet()) {
			for (Map.Entry<Entrenador, Pokemon>  ep:  pm.getKey().entrySet()) {
				ep.getKey().agregarMovimientos(ep.getValue(),movimientosPorTipo, pm.getValue());
			}
		}
	}
	

	
	
	
	
}