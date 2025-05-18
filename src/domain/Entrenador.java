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
import java.util.stream.Collectors;

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
    
    
    
    public void pokemonesAleatorios(ArrayList<Pokemon> pokemones) {
        Map<Tipo, List<Pokemon>> pokemonesPorTipo = new HashMap<>();
        Set<Pokemon> seleccionados = new HashSet<>();
        
     // Verificar que haya al menos 2 Pokémon por tipo
        //for (Tipo tipo : pokemonesPorTipo.keySet()) {
            //List<Pokemon> lista = pokemonesPorTipo.get(tipo);
            //if (lista.size() < 2) {
                //throw new IllegalArgumentException("No hay suficientes Pokémon del tipo: " + tipo);
           // }
      	//}

        // Agrupar Pokémon por tipo
        for (Pokemon p : pokemones) {
            for (Tipo tipo : p.getTipos()) {
                pokemonesPorTipo.computeIfAbsent(tipo, k -> new ArrayList<>()).add(p);
            }
        }

        // Intentar seleccionar 2 Pokémon por tipo, si hay suficientes
        for (List<Pokemon> lista : pokemonesPorTipo.values()) {
            Collections.shuffle(lista);
            for (Pokemon p : lista) {
                if (seleccionados.size() >= 6) break;
                seleccionados.add(p);
            }
            if (seleccionados.size() >= 6) break;
        }

        // Si aún faltan Pokémon, completar aleatoriamente
        if (seleccionados.size() < 6) {
            List<Pokemon> restantes = new ArrayList<>(pokemones);
            Collections.shuffle(restantes);
            for (Pokemon p : restantes) {
                if (seleccionados.size() >= 6) break;
                seleccionados.add(p);
            }
        }

        // Convertir a lista y mezclar
        ArrayList<Pokemon> resultadoFinal = new ArrayList<>(seleccionados);
        Collections.shuffle(resultadoFinal);

        // Asignar al atributo de la clase
        this.pokemones = resultadoFinal;
    }
    public void movimientosAleatorios(Map<Tipo, List<Movimiento>> movimientosPorTipo) {
        for (Pokemon pokemon : this.pokemones) {
            List<Movimiento> movimientosSeleccionados = new ArrayList<>();
            Set<Movimiento> movimientosUsados = new HashSet<>();
            List<Tipo> tiposDisponibles = new ArrayList<>(movimientosPorTipo.keySet());

            Collections.shuffle(tiposDisponibles);  // Mezclamos tipos para aleatoriedad

            while (movimientosSeleccionados.size() < 4 && !tiposDisponibles.isEmpty()) {
                Tipo tipoActual = tiposDisponibles.remove(0); // Tomamos un tipo
                List<Movimiento> movimientosDeTipo = new ArrayList<>(movimientosPorTipo.get(tipoActual));
                Collections.shuffle(movimientosDeTipo);

                for (Movimiento m : movimientosDeTipo) {
                    if (!movimientosUsados.contains(m)) {
                        movimientosSeleccionados.add(m);
                        movimientosUsados.add(m);
                        break; // Solo 1 movimiento por tipo
                    }
                }
            }

            // Si aún no tenemos 4 movimientos, rellenamos aleatoriamente
            if (movimientosSeleccionados.size() < 4) {
                List<Movimiento> todos = movimientosPorTipo.values().stream()
                    .flatMap(List::stream).distinct().collect(Collectors.toList());
                Collections.shuffle(todos);

                for (Movimiento m : todos) {
                    if (movimientosSeleccionados.size() >= 4) break;
                    if (!movimientosUsados.contains(m)) {
                        movimientosSeleccionados.add(m);
                        movimientosUsados.add(m);
                    }
                }
            }

            pokemon.setMovimientos(movimientosSeleccionados);
        }
    }

    
    public abstract void escogerPokemonesTipo(Map<Tipo,List<Pokemon>> pokemonesMachine);
    //items
  	//
    public abstract void escogerItemsTipo(ArrayList<Item> items);
    public abstract void agregarMovimientos(Pokemon pokemon,  Map<Tipo, List<Movimiento>> movimientosPorTipo, List<Movimiento> movimientosIsPlayer);
}