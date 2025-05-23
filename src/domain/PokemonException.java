package domain;

public class PokemonException extends Exception {
	public static final String TIPO_ENTRENADOR_INVALIDO ="El tipo de entrenador es invalido";
	public static final String NOMBRE_JUGADOR_INVALIDO = "Nombre de jugador invalido";
	public static final String MAPA_ENTRENADORES_VACIO ="El mapa de nombres y tipos no puede ser null";
	public static final String ID_ITEM_INVALIDO ="El id del item es invalido";
	public static final String LISTA_ITEMS_VACIA ="Lista de items esta vacia";
	public static final String POKEMON_IS_NULL ="El pokemon es nulo";
	public static final String MOVIMIENTOSPORTIPO_IS_NULL ="Lista de movimientosPorTipo es nula";
	public static final String MOVIMIENTOSISPLAYER_IS_NULL ="Lista de de movimientosIsPlayer es nula";
	public static final String POKEMONESMACHINE_IS_NULL ="Lista de PokemonesMachine is null";
	public static final String MODO_JUEGO_INVALIDO ="El modo de juego es invalido";
	public static final String MODALIDAD_JUEGO_INVALIDAD ="Modalidad de juego invalidad";
	public static final String PLAYERPOKEMONES_IS_NULL ="Lista de playerPokemones is null";
	public static final String NOMBRE_MOVIMIENTO_NULL = "Nombre del movimiento no puede ser nulo ";
	public static final String DESCRIPCION_EFECTO_NULA ="Descripcion del efecto es nula";



	public PokemonException(String menssage) {
		super(menssage);
	}

}
