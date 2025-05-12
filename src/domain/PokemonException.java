package domain;

public class PokemonException extends Exception {
	public static final String MODO_NO_ENCONTRADA = "El modo no existe o aun no se encuentra disponible";
	public static final String MODO_SUPERIVENCIA_NO_DISPONIBLE ="El modo supervivencia solo esta disponible en PvP";

	public PokemonException(String menssage) {
		super(menssage);
	}

}
