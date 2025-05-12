package domain;

public class Revive extends Item {
	public Revive(String id, int recuperacion) {
		super(id, recuperacion);
		// TODO Auto-generated constructor stub
	}

	private static final String  name = "Revive";

	@Override
	public String efectoItem(Pokemon pokemon) {
		if (pokemon.getSalud() == 0) {
			pokemon.setSalud(pokemon.getSaludMaxima() / 2);
			return id;
		} else {
			return "No efectuo";
		}
	}
	
	public String getNameItem() {
		return name;
	}	
	
	@Override
	public boolean esRevive() {
        return true;
    }

}

