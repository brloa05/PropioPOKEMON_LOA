package domain;

public class SuperPotion extends Item {
	private static final String  name = "SuperPotion";
	private static final String  tipo = "Potion";
	private final int recuperacion = 50;
	public SuperPotion(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public int getRecuperacion() {
		return recuperacion;
	}
	@Override
	public String getTipo() {
        return tipo;
    }
	@Override
    public boolean esPocion() {
        return tipo.toLowerCase().contains("Potion");
    }
	@Override
    public boolean esRevivir() {
        return tipo.toLowerCase().contains("Revive");
    }
    
	@Override
	public String efectoItem(Pokemon pokemon) {
		if (pokemon.getSalud() == 0) {
			if (pokemon.getSalud() == pokemon.getSaludMaxima()) {
				return "No efectuo";
			} else if (pokemon.getSalud() < pokemon.getSaludMaxima()) {
				if (Math.abs(pokemon.getSalud() - pokemon.getSaludMaxima()) <= recuperacion) {
					pokemon.setSalud(pokemon.getSalud() + (recuperacion - Math.abs(recuperacion -Math.abs(pokemon.getSalud() - pokemon.getSaludMaxima()))));
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