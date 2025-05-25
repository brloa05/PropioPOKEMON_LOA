package domain;

public class SuperPotion extends Item {
	private static final String  name = "SuperPotion";
	private static final String  tipo = "Potion";
	private final int recuperacion = 50;
	public SuperPotion(String id) throws PokemonException {
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
        if (pokemon.estaDebilitado()) { // Verifica si el Pokémon tiene 0 HP o menos
            return "No efectuo"; // Las pociones no deben funcionar en Pokémon debilitados
        }
        if (pokemon.getSalud() == pokemon.getSaludMaxima()) {
            return "No efectuo"; // El Pokémon ya tiene la salud al máximo
        }

        // Si llegamos aquí, el Pokémon tiene HP > 0 y HP < SaludMáxima, por lo tanto puede ser curado.
        int vidaActual = pokemon.getSalud();
        int vidaMaxima = pokemon.getSaludMaxima();
        // this.recuperacion es el valor definido en HyperPotion (200) [cite: 136]
        // o usa this.getRecuperacion() que es el método abstracto implementado [cite: 137]
        int cantidadRecuperacionItem = this.getRecuperacion();

        int vidaQueFaltaParaMaximo = vidaMaxima - vidaActual;

        // La curación real será lo mínimo entre lo que le falta para el máximo y lo que cura la poción
        int curacionEfectiva = Math.min(vidaQueFaltaParaMaximo, cantidadRecuperacionItem);

        if (curacionEfectiva > 0) {
            pokemon.setSalud(vidaActual + curacionEfectiva);
            return this.id; // Retorna el ID del ítem para confirmar que se usó
        } else {
            // Este caso podría darse si, por alguna razón, curacionEfectiva es 0 (ej. recuperacionBase es 0)
            // aunque las comprobaciones anteriores deberían evitarlo para pociones estándar.
            return "No efectuo";
        }
    }
	
	public String getNameItem() {
		return name;
	}	

}