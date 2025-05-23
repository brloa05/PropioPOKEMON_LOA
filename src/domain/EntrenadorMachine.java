package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import domain.Pokemon.Tipo;

public class EntrenadorMachine extends Entrenador {

	public EntrenadorMachine(String name, String tipo) throws PokemonException {
		super(name, tipo);
		
	}

	@Override
	public void escogerPokemonesTipo(Map<Tipo,List<Pokemon>> pokemonesMachine) throws PokemonException {
		
	}

	@Override
	public void escogerItemsTipo(ArrayList<Item> items) throws PokemonException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void agregarMovimientos(Pokemon pokemon, Map<Tipo, List<Movimiento>> movimientosPorTipo,List<Movimiento> movimientosIsPlayer) throws PokemonException {
		// TODO Auto-generated method stub
		
	}

}