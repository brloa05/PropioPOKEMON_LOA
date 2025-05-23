package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import domain.Pokemon.Tipo;

public class ModalidadPvP extends ModalidadJuego implements ModoNormal {
	
	public ModalidadPvP(String name) throws PokemonException {
		super(name);
	}

	@Override
	public ArrayList<Entrenador> crearEntrenadores(Map<String, String> nombresAndTipo) throws PokemonException {
	    if (nombresAndTipo == null) {
	        throw new PokemonException(PokemonException.MAPA_ENTRENADORES_VACIO);
	    }

	    ArrayList<Entrenador> entrenadores = new ArrayList<>();
	    for (Map.Entry<String, String> entry : nombresAndTipo.entrySet()) {
	        entrenadores.add(new EntrenadorPlayer(entry.getKey(), entry.getValue()));
	        System.out.println(entry.getKey());
	    }
	    

	    return entrenadores;
	}
	
	@Override
	public void agregarPokemones(Map<Entrenador, ArrayList<Pokemon>> playerPokemones, Map<Tipo,List<Pokemon>> pokemonesMachine) throws PokemonException {
		if (playerPokemones == null) {
			throw new PokemonException(PokemonException.PLAYERPOKEMONES_IS_NULL);
		}
		for (Map.Entry<Entrenador, ArrayList<Pokemon>> e: playerPokemones.entrySet()) {
			e.getKey().setPokemones(e.getValue());
		}	
	}

	@Override
	public void agregarItems(Map<Entrenador, ArrayList<Item>> playerItems,  ArrayList<Item> itemsDefault) {
		for(Map.Entry<Entrenador,ArrayList<Item>> player: playerItems.entrySet()) {
			player.getKey().setItems(player.getValue());
		}
	}

	@Override
	public void agregarMoimientosPlayer(Map<Map<Entrenador, Pokemon>, List<Movimiento>> playerMovimientos, Map<Tipo, List<Movimiento>> movimientosPorTipo) throws PokemonException {
		for (Map.Entry<Map<Entrenador, Pokemon>,  List<Movimiento>>  pm : playerMovimientos.entrySet()) {
			for (Map.Entry<Entrenador, Pokemon>  ep:  pm.getKey().entrySet()) {
				ep.getKey().agregarMovimientos(ep.getValue(),movimientosPorTipo, pm.getValue());
			}
		}
	}

	

	
	
	
	
}