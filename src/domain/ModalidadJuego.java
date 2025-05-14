package domain;

import java.util.ArrayList;
import java.util.Map;

abstract public class ModalidadJuego {
	private String name ;
	
	public ModalidadJuego(String name) {
		this.name = name;
	}
	abstract public ArrayList<Entrenador> crearEntrenadores(Map<String, String> nombresAndTipo);
	
	
	public String getName() {
		return name;
	}
	
}
