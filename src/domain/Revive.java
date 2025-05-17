package domain;

public class Revive extends Item {
	private static final String  name = "Revive";
	private static final int recuperacion = 400;
	public Revive(String id) {
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

