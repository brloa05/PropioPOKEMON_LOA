package domain;

import java.util.ArrayList;
import java.util.Map;

abstract public class ModalidadJuego {
	private String name ;
	
	public ModalidadJuego(String name) throws PokemonException {
		if (!(name.equals("PvP") || name.equals("PvM") || name.equals("MvM") )){
			throw new PokemonException(PokemonException.MODALIDAD_JUEGO_INVALIDAD);
		}
		this.name = name;
	}
	abstract public ArrayList<Entrenador> crearEntrenadores(Map<String, String> nombresAndTipo) throws PokemonException;
	
	
	public String getName() {
		return name;
	}
	
}
