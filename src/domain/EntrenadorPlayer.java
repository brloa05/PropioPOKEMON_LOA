package domain;

import java.util.List;
import java.util.Map;

import domain.Pokemon.Tipo;

public class EntrenadorPlayer extends Entrenador {

	public EntrenadorPlayer(String name, String tipo ) {
		super(name, tipo);
		
	}

	@Override
	public void escogerPokemonesTipo(Map<Tipo,List<Pokemon>> pokemonesMachine) {
	}

	@Override
	public void escogerItemsTipo() {
		
	}

	@Override
	public void agregarMovimientos(Pokemon pokemon, Map<Tipo, List<Movimiento>> movimientosPorTipo, List<Movimiento> movimientosIsPlayer) {
		pokemon.setMovimientos(movimientosIsPlayer);
	}

}