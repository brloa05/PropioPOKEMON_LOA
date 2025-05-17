package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import domain.Pokemon.Tipo;

public abstract class Entrenador {
    protected String name;  // Quita el static aquí
    protected ArrayList<Pokemon> pokemones;
    protected ArrayList<Item> items;
    protected String tipo;
   
    
    public Entrenador(String name, String tipo) {
        this.name = name;
        this.tipo = tipo;
        this.pokemones = new ArrayList<>();
        this.items = new ArrayList<>();
        
    }
    

    public void setPokemones(List<Pokemon> pokemones) {
        this.pokemones = new ArrayList<>(pokemones); 
    }
    
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
    
    
    
    public String getTipo() {
        return tipo;
    }
    
    public String getName() {
        return name;
    }
    
    public ArrayList<Item> getItems(){
        return items;
    }
    protected static <K, V> K obtenerLlaveAleatoria(Map<K, V> map, Set<K> llavesExcluidas) {
        if (map.isEmpty()) {
            throw new NoSuchElementException("El Map está vacío");
        }

        // Crear lista de llaves disponibles (no excluidas)
        List<K> llavesDisponibles = new ArrayList<>();
        for (K llave : map.keySet()) {
            if (!llavesExcluidas.contains(llave)) {
                llavesDisponibles.add(llave);
            }
        }

        if (llavesDisponibles.isEmpty()) {
            throw new NoSuchElementException("No hay llaves disponibles no excluidas");
        }

        Random random = new Random();
        return llavesDisponibles.get(random.nextInt(llavesDisponibles.size()));
    }

    public static <T> T obtenerUnElementoAleatorio(List<T> lista, Set<T> elementosExcluidos) {
        if (lista == null || lista.isEmpty()) {
            throw new NoSuchElementException("La lista está vacía");
        }

        // Filtrar elementos no excluidos
        List<T> elementosDisponibles = new ArrayList<>();
        for (T elemento : lista) {
            if (!elementosExcluidos.contains(elemento)) {
                elementosDisponibles.add(elemento);
            }
        }

        if (elementosDisponibles.isEmpty()) {
            throw new NoSuchElementException("No hay elementos disponibles no excluidos");
        }

        Random random = new Random();
        return elementosDisponibles.get(random.nextInt(elementosDisponibles.size()));
    }

    
    public ArrayList<Pokemon> getPokemones() {
	    return pokemones;
	}
    
    public void itemsAleatorios() {
		
	}
    
    public void pokemonesAleatorios() {
    	
    }
    public void movimientosAleatorios() {
    
    }
    
    public abstract void escogerPokemonesTipo(Map<Tipo,List<Pokemon>> pokemonesMachine);
    public abstract void escogerItemsTipo(ArrayList<Item> items);
    public abstract void agregarMovimientos(Pokemon pokemon,  Map<Tipo, List<Movimiento>> movimientosPorTipo, List<Movimiento> movimientosIsPlayer);
}