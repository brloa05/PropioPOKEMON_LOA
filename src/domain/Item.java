package domain;

public abstract class Item {
	protected String id;
	
	
	public Item(String id) throws PokemonException {
		if (id.equals("") || id.equals(null)) {
			throw new PokemonException(PokemonException.ID_ITEM_INVALIDO);
		}
		this.id = id;
	}
	
	public abstract String efectoItem(Pokemon pokemon);
	
	public String getId() {
		return id;
	}
	
	public abstract int getRecuperacion();
	public boolean esRevive() {
        return false;
    }
	
	public abstract String getTipo();

    public abstract boolean esPocion();

    public abstract boolean esRevivir();


}

