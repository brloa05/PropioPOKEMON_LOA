package domain;

public abstract class Item {
	protected String id;
	protected int recuperacion;
	
	public Item(String id, int recuperacion) {
		this.id = id;
		this.recuperacion = recuperacion;
	}
	
	public abstract String efectoItem(Pokemon pokemon);
	
	public String getId() {
		return id;
	}
	
	public int getRecuperacion() {
		return recuperacion;
	}
	public boolean esRevive() {
        return false;
    }


}

