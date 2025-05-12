package domain;

public class Potion extends Item{
	public Potion(String id, int recuperacion) {
		super(id, recuperacion);
		// TODO Auto-generated constructor stub
	}

	private static final String  name = "Potion";

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
					pokemon.setSalud(pokemon.getSalud() + 20);
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
