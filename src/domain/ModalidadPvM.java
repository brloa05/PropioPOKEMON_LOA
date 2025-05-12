package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import domain.Pokemon.Tipo;

public class ModalidadPvM extends ModalidadJuego implements ModoNormal{
	
	public ModalidadPvM(String name) {
		super(name);
		
	}

	@Override
	public ArrayList<Entrenador> crearEntrenadores(Map<String, String> nombresAndTipo) {
		ArrayList<Entrenador> entrenadors = new ArrayList<>();
		for (Map.Entry<String, String> entry: nombresAndTipo.entrySet()) {
			if (entry.getValue().equals("ExpertTrainer")) {
				entrenadors.add(new ExpertTrainer(entry.getKey(), entry.getValue()));
			} else if(entry.getValue().equals("ChangingTrainer")) {
				entrenadors.add(new ChangingTrainer(entry.getKey(), entry.getValue()));
			} else if(entry.getValue().equals("AttackingTrainer")) {
				entrenadors.add(new AttackingTrainer(entry.getKey(), entry.getValue()));
				
			} else if (entry.getValue().equals("DefensiveTrainer")) {
				entrenadors.add(new DefensiveTrainer(entry.getKey(), entry.getValue()));
			} else if (entry.getValue().equals("PlayerTrainer")){
				entrenadors.add(new EntrenadorPlayer(entry.getKey(), entry.getValue()));
			}	
			
		}
		return entrenadors;
	}
	// playerTrainer
	@Override
	public void agregarPokemones(Map<Entrenador, ArrayList<Pokemon>> playerPokemones, Map<Tipo,List<Pokemon>> pokemonesMachine) {
		for (Map.Entry<Entrenador, ArrayList<Pokemon>> e: playerPokemones.entrySet()) {
			if (e.getKey().getTipo().equals("PlayerTrainer")) {
				e.getKey().setPokemones(e.getValue());
			} else {
				e.getKey().escogerPokemonesTipo(pokemonesMachine);
			}
	
		}
		
	}

	@Override
	public void agregarItems(Map<Entrenador, ArrayList<Item>> playerItems) {
		for (Map.Entry<Entrenador, ArrayList<Item>> player: playerItems.entrySet()) {
			if (player.getKey().getTipo().equals("PlayerTrainer")) {
				player.getKey().setItems(player.getValue());
			} else {
				player.getKey().escogerItemsTipo();
			}
	
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
	
	


