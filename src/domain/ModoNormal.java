package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import domain.Pokemon.Tipo;

public interface ModoNormal {
	abstract public void agregarPokemones(Map<Entrenador,ArrayList<Pokemon>> playerPokemones, Map<Tipo,List<Pokemon>> pokemonesMachine);
	abstract public void agregarItems(Map<Entrenador,ArrayList<Item>> playerItems);
	abstract public void agregarMoimientosPlayer(Map<Map<Entrenador, Pokemon>, List<Movimiento>> playerMovimientos, Map<Tipo, List<Movimiento>> movimientosPorTipo);
}
