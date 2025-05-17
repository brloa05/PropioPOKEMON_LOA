package domain;

public class Potion extends Item{
	
	private static final String  name = "Potion";
	private static final int recuperacion =  20;
	
	public Potion(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public int getRecuperacion() {
		return recuperacion;
	}

	@Override
	public String efectoItem(Pokemon pokemon) {
		if (pokemon.getSalud() == 0) {
			if (pokemon.getSalud() == pokemon.getSaludMaxima()) {
				return "No efectuo";
			} else if (pokemon.getSalud() < pokemon.getSaludMaxima()) {
				if (Math.abs(pokemon.getSalud() - pokemon.getSaludMaxima()) <= recuperacion) {
					pokemon.setSalud(pokemon.getSalud() + (recuperacion - Math.abs(recuperacion-Math.abs(pokemon.getSalud() - pokemon.getSaludMaxima()))));
					return id;
				} else {
					pokemon.setSalud(pokemon.getSalud() + recuperacion);
					return id;
				}
			}
		}
		return "No efectuo";
	}
	
	public String getNameItem() {
		return name;
	}	
	
}
