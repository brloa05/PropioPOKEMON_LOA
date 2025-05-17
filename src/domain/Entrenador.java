package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import domain.Pokemon.Tipo;

public abstract class Entrenador {
    protected String name;  
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
    
    public void itemsAleatorios(ArrayList<Item> items) {
    	Map<String, Integer> pocionesSeleccionadas = new HashMap<>();
        Map<String, Integer> revivirSeleccionados = new HashMap<>();
        ArrayList<Item> seleccionados = new ArrayList<>();

        Collections.shuffle(items); // Aleatorizar la lista

        for (Item item : items) {
            if (seleccionados.size() == 3) break;

            String tipo = item.getTipo(); // Ej: "Potion", "HyperPotion", "Revive", etc.

            if (item.esPocion()) {
                int cantidad = pocionesSeleccionadas.getOrDefault(tipo, 0);
                if (cantidad < 2) {
                    seleccionados.add(item);
                    pocionesSeleccionadas.put(tipo, cantidad + 1);
                }
            } else if (item.esRevivir()) {
                int cantidad = revivirSeleccionados.getOrDefault(tipo, 0);
                if (cantidad < 1) {
                    seleccionados.add(item);
                    revivirSeleccionados.put(tipo, 1);
                }
            } else {
                // Otros ítems sin restricciones
                seleccionados.add(item);
            }
        }
        this.items = seleccionados;

        
	}
    
    public void pokemonesAleatorios(ArrayList<Pokemon> pokemones) {
        Map<Tipo, List<Pokemon>> pokemonesPorTipo = new HashMap<>();
        Set<Pokemon> seleccionados = new HashSet<>();

        // Agrupar Pokémon por cada uno de sus tipos
        for (Pokemon p : pokemones) {
            for (Tipo tipo : p.getTipos()) {
                pokemonesPorTipo.computeIfAbsent(tipo, k -> new ArrayList<>()).add(p);
            }
        }

        // Verificar que haya al menos 2 Pokémon por tipo
        for (Tipo tipo : pokemonesPorTipo.keySet()) {
            List<Pokemon> lista = pokemonesPorTipo.get(tipo);
            if (lista.size() < 2) {
                throw new IllegalArgumentException("No hay suficientes Pokémon del tipo: " + tipo);
            }
        }

        // Elegir 2 Pokémon aleatorios por cada tipo (sin repetir)
        for (Map.Entry<Tipo, List<Pokemon>> entry : pokemonesPorTipo.entrySet()) {
            List<Pokemon> lista = new ArrayList<>(entry.getValue());
            Collections.shuffle(lista);

            int count = 0;
            for (Pokemon p : lista) {
                if (seleccionados.add(p)) { // solo se añade si no está repetido
                    count++;
                }
                if (count == 2) break;
            }
        }

        // Si aún hay menos de 6, completar aleatoriamente sin repetir
        if (seleccionados.size() < 6) {
            List<Pokemon> restantes = new ArrayList<>(pokemones);
            Collections.shuffle(restantes);
            for (Pokemon p : restantes) {
                if (seleccionados.size() >= 6) break;
                seleccionados.add(p); // no se repite gracias al Set
            }
        }

        // Convertir a lista y mezclar
        List<Pokemon> resultadoFinal = new ArrayList<>(seleccionados);
        Collections.shuffle(resultadoFinal);

        // Mostrar resultados (o asignarlos)
        for (Pokemon p : resultadoFinal) {
            System.out.println(p.getNombre() + " " + p.getTipos());
        }
    }
    public void movimientosAleatorios(Map<Pokemon.Tipo, List<Movimiento>> movimientos) {
    
    }
    
    public abstract void escogerPokemonesTipo(Map<Tipo,List<Pokemon>> pokemonesMachine);
    //items
  	//
    public abstract void escogerItemsTipo(ArrayList<Item> items);
    public abstract void agregarMovimientos(Pokemon pokemon,  Map<Tipo, List<Movimiento>> movimientosPorTipo, List<Movimiento> movimientosIsPlayer);
}