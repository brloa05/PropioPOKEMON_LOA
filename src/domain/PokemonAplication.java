package domain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import domain.Pokemon.Tipo;


public class PokemonAplication {
	private ArrayList<Pokemon> pokemones;
	private ArrayList<Entrenador> entrenadores;
	private ArrayList<Item> items;
	private ModalidadJuego modalidadJuego;
	private String modo;
	private Map<Pokemon.Tipo, List<Movimiento>> movimientosPorTipo;
	private Map<Pokemon, List<Movimiento>> pokemonMovements = new HashMap<>();
	private List<Pokemon> selectedPokemons = new ArrayList<>();
	private static final int MAX_POKEMONS = 6;
	private static final int MAX_SAME_TYPE = 2;
	
	
	
	
	public PokemonAplication() throws PokemonException {
			this.entrenadores =  new ArrayList<>();
			this.pokemones = new ArrayList<>();
			this.movimientosPorTipo = new HashMap<>();
			this.items =  new ArrayList<>();
	        inicializarMovimientos();
	        inicializarPokemonesDisponibles();
	        inicializarItems();
		
	}
	
	//modificicacion
	private void inicializarItems()  {
	    try (BufferedReader br = new BufferedReader(new FileReader("resources/items.txt"))) {
	        String linea;
	        while ((linea = br.readLine()) != null) {
	            String[] partes = linea.split(",");
	            String clase = partes[0];
	            String nombre = partes[1];
	            try {
	                // Usar el nombre real del paquete donde están tus clases de items
	                Class<?> clazz = Class.forName("domain." + clase);
	                Constructor<?> constructor = clazz.getConstructor(String.class);
	                Item item = (Item) constructor.newInstance(nombre);
	                items.add(item);
	            } catch (Exception e) {
	                System.out.println("No se pudo instanciar el item: " + clase);
	                e.printStackTrace();
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 
	}
	
	
	private void inicializarMovimientos() throws PokemonException {
	    Map<Pokemon.Tipo, List<Movimiento>> movimientosPorTipo = new HashMap<>();
	    try (BufferedReader br = new BufferedReader(new FileReader("resources/movimientos.txt"))) {
	        String linea;
	        while ((linea = br.readLine()) != null) {
	            if (linea.trim().isEmpty() || linea.startsWith("#")) continue;
	            String[] partes = linea.split(",", -1);

	            // Extraer y parsear los campos
	            Pokemon.Tipo tipo = Pokemon.Tipo.valueOf(partes[0].trim());
	            String nombre = partes[1].trim();
	            String clase = partes[2].trim().toUpperCase();
	            int poder = Integer.parseInt(partes[3].trim());
	            int precision = Integer.parseInt(partes[4].trim());
	            int pp = Integer.parseInt(partes[5].trim());
	            String descripcion = partes[6].trim();
	            Movimiento.TipoEfecto tipoEfecto = Movimiento.TipoEfecto.valueOf(partes[7].trim());
	            Movimiento.ObjetivoEfecto objetivoEfecto = Movimiento.ObjetivoEfecto.valueOf(partes[8].trim());
	            int valorEfecto = Integer.parseInt(partes[9].trim());

	            Movimiento movimiento;
	            switch (clase) {
	                case "FISICO":
	                    movimiento = new Fisico(nombre, poder, precision, pp, descripcion, tipoEfecto, objetivoEfecto, valorEfecto);
	                    break;
	                case "ESPECIAL":
	                    movimiento = new Especial(nombre, poder, precision, pp, descripcion, tipoEfecto, objetivoEfecto, valorEfecto);
	                    break;
	                case "DEESTADO":
	                    movimiento = new DeEstado(nombre, poder, precision, pp, descripcion, tipoEfecto, objetivoEfecto, valorEfecto);
	                    break;
	                default:
	                    continue; // O puedes lanzar excepción si prefieres
	            }

	            movimientosPorTipo.computeIfAbsent(tipo, k -> new ArrayList<>()).add(movimiento);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    this.movimientosPorTipo = movimientosPorTipo;
	}
	
	
	 public List<Movimiento> getMovimientosPorTipo(Pokemon.Tipo tipo) {
	        return new ArrayList<>(movimientosPorTipo.getOrDefault(tipo, new ArrayList<>()));
	    }
	 
	 
	 private void inicializarPokemonesDisponibles() {
		    try (BufferedReader br = new BufferedReader(new FileReader("resources/pokemones.txt"))) {
		        String linea;
		        while ((linea = br.readLine()) != null) {
		            String[] partes = linea.split(",");
		            String nombre = partes[0];
		            int vida = Integer.parseInt(partes[1]);
		            String tipoPokemon = partes[2];
		            Pokemon.Tipo tipo = Pokemon.Tipo.valueOf(partes[3].toUpperCase());

		            if ("UNO".equalsIgnoreCase(tipoPokemon)) {
		                pokemones.add(new PokemonUnTipo(nombre, vida, getMovimientosPorTipo(tipo)));
		            } else if ("DOS".equalsIgnoreCase(tipoPokemon)) {
		                pokemones.add(new PokemonDosTipos(nombre, vida, getMovimientosPorTipo(tipo)));
		            }
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	}
	 
	 public void setModo(String modo) throws PokemonException {
		 if (modo.equals("Supervivencia")){
			 this.modo = "Normal";
		 } else if (modo.equals("Supervivencia")){
			 this.modo = "Supervivencia";
		 } else {
			 throw new PokemonException(PokemonException.MODO_JUEGO_INVALIDO);
		 }
	 }
	 
	 
	 public List<Pokemon> getPokemonesPorTipo(Pokemon.Tipo tipo) {
	        List<Pokemon> resultado = new ArrayList<>();
	        for (Pokemon pokemon : pokemones) {
	            if (pokemon.getTipos().contains(tipo)) {
	                resultado.add(pokemon);
	            }
	        }
	        return resultado;
	  }
	 
	public ArrayList<Item> getItems(){
		return items;
	}
	
	public void elegirModalidad(String  modalidadJuego) throws PokemonException  {
			if (modalidadJuego == "PvP") {
				 this.modalidadJuego = new ModalidadPvP("PvP");
			} else if(modalidadJuego == "PvM") {
				 this.modalidadJuego = new ModalidadPvM("PvM");
			}else {
				 this.modalidadJuego = new ModalidadMvM("MvM");
			}
	}
	
	
	
	public void resetModalidad() {
		this.modalidadJuego = null;
	}
	
	public void resetEntrenadore(String name) {
	    Iterator<Entrenador> it = entrenadores.iterator();
	    while (it.hasNext()) {
	        Entrenador e = it.next();
	        if (e.getName().equals(name)) {
	            it.remove();
	        }
	    }
	}
	
	public void  crearEntrenadors(Map<String, String> nombresAndTipo) throws PokemonException{
			this.entrenadores =  modalidadJuego.crearEntrenadores(nombresAndTipo);
	}
	
	
	
	
	public ArrayList<Entrenador> getEntrenadores(){
		return entrenadores;
	}
		
	public void agregarPokemonesPlayer(Map<Entrenador,ArrayList<Pokemon>> playerPokemones) throws PokemonException {
		Map<Tipo,List<Pokemon>> pokemonesMachine = new HashMap<>();
		if (!modalidadJuego.getName().equals("PvP")) {
			for (Tipo tipo : Tipo.values()) {
				pokemonesMachine.put(tipo, getPokemonesPorTipo(tipo));
			}
			if (modalidadJuego.getName() == "PvM") {
				((ModalidadPvM) modalidadJuego).agregarPokemones(playerPokemones, pokemonesMachine );
			} else if(modalidadJuego.getName() == "MvM") {
				((ModalidadMvM) modalidadJuego).agregarPokemones(playerPokemones,  pokemonesMachine);
			}
		} else {
			((ModalidadPvP) modalidadJuego).agregarPokemones(playerPokemones,  pokemonesMachine);
		}
	}
	
	
	public void agregarMovimientos(Map<Map<Entrenador, Pokemon>, List<Movimiento>> playerMovimientos) throws PokemonException {
		if(modalidadJuego.getName().equals("PvP")) {
			((ModalidadPvP) modalidadJuego).agregarMoimientosPlayer(playerMovimientos, movimientosPorTipo);
		} else if (modalidadJuego.getName().equals("PvM")) {
			((ModalidadPvM) modalidadJuego).agregarMoimientosPlayer(playerMovimientos, movimientosPorTipo);
		} else if (modalidadJuego.getName().equals("MvM")) {
			((ModalidadMvM) modalidadJuego).agregarMoimientosPlayer(playerMovimientos, movimientosPorTipo);
		}
		
		for (Entrenador e : entrenadores) {
			System.out.println("-----------------------------");
			System.out.println(e.getName());
			ArrayList<Item> items = e.getItems();
			ArrayList<Pokemon> poke = e.getPokemones();
			for(Item i : items) {
				System.out.println("--------------Items---------------");
				System.out.println(i.getId());
			}
			for(Pokemon p : poke) {
			    System.out.println("--------------pokemones---------------");
			    System.out.println(p.getNombre());
			    List<Movimiento> movi = p.getMovimientos();
			    if (movi == null) {
			        System.out.println("Movimientos es null");
			    } else if (movi.isEmpty()) {
			        System.out.println("No hay movimientos asignados");
			    } else {
			        for (Movimiento m : movi) {
			            System.out.println("--------------movimientos---------------");
			            System.out.println(m.getName());
			        }
			    }
			}
			
			
			
		}
		
	}
	
	public void agregarItemsLucha(Map<Entrenador,ArrayList<Item>> playerItems) throws PokemonException {
		if (modalidadJuego.getName() == "PvP") {
			((ModalidadPvP) modalidadJuego).agregarItems(playerItems, this.items);
		} else if (modalidadJuego.getName() == "PvM") {
			((ModalidadPvM) modalidadJuego).agregarItems(playerItems,this.items);
		} else if(modalidadJuego.getName() == "MvM") {
			((ModalidadMvM) modalidadJuego).agregarItems(playerItems,this.items);
		}
		
		

		
		
	}
	

	public void agregarObjetosSupervivencia() {
		for (Entrenador e: this.entrenadores) {
			e.pokemonesAleatorios(this.pokemones);
			e.movimientosAleatorios(this.movimientosPorTipo);
		}
		
		for (Entrenador e : entrenadores) {
			System.out.println("-----------------------------");
			System.out.println(e.getName());
			ArrayList<Item> items = e.getItems();
			ArrayList<Pokemon> poke = e.getPokemones();
			for(Item i : items) {
				System.out.println("--------------Items---------------");
				System.out.println(i.getId());
			}
			for(Pokemon p : poke) {
			    System.out.println("--------------pokemones---------------");
			    System.out.println(p.getNombre());
			    List<Movimiento> movi = p.getMovimientos();
			    if (movi == null) {
			        System.out.println("Movimientos es null");
			    } else if (movi.isEmpty()) {
			        System.out.println("No hay movimientos asignados");
			    } else {
			        for (Movimiento m : movi) {
			            System.out.println("--------------movimientos---------------");
			            System.out.println(m.getName());
			        }
			    }
			}
			
			
			
		}
		
		
		
	}
	
	public ArrayList<Item> escojerItems(ArrayList<String> itemsEscogidos ) throws PokemonException {
		
		if (itemsEscogidos.size() > 3) {
			//PokemonException
		}
		ArrayList<Item> itemEscogidos =  new ArrayList<>();
		int numeroPosiones = 0;
		int numeroSuperPosiones = 0;
		int numeroHyperPosiones = 0;
		int numeroDeRevivir = 0;
		
		for (String item: itemsEscogidos) {
			if (item.equals("Potion") && numeroPosiones < 2) {
				itemEscogidos.add(new Potion("potion" + numeroPosiones));
				numeroPosiones += 1;
			} else if (item.equals("HyperPotion") && numeroHyperPosiones < 2) {
				itemEscogidos.add(new HyperPotion("HyperPotion" + numeroHyperPosiones));
				numeroHyperPosiones += 1;
			} else if (item.equals("SuperPotion") && numeroSuperPosiones < 2) {
				itemEscogidos.add(new SuperPotion("SuperPotion" + numeroSuperPosiones));
				numeroSuperPosiones += 1;
			} else if (item.equals("Revive") && numeroDeRevivir < 1) {
				itemEscogidos.add(new Revive("Revive" + numeroDeRevivir));
				 numeroDeRevivir += 1;
			} else {
				//se lanza una PokemonException
			}
		}
		return itemEscogidos;		

	}
	
	
	public boolean usarItem(Pokemon pokemon,Item item, Entrenador entrenador) {
		String idORespuesta = item.efectoItem(pokemon);
		if (idORespuesta.equals("No efectuo")) {
			return false;
		} else {
			ArrayList<Item> items = entrenador.getItems();
			for (Item i: items) {
				if (i.getId().equals(item.getId())) {
					items.remove(i);
				}
			}
			return true;
			
		}	
	}
	
//desde aca 
	public List<String> getPokemonNames() {
	    List<String> nombres = new ArrayList<>();
	    for (Pokemon pokemon : pokemones) {
	        nombres.add(pokemon.getNombre());
	    }
	    return nombres;
	}

	public List<String> getMovimientoNames() {
	    List<String> nombres = new ArrayList<>();
	    for (List<Movimiento> movimientos : movimientosPorTipo.values()) {
	        for (Movimiento movimiento : movimientos) {
	            if (!nombres.contains(movimiento.getName())) {
	                nombres.add(movimiento.getName());
	            }
	        }
	    }
	    return nombres;
	}

	public void setPokemonSeleccionadosPlayer1(List<String> seleccionados) {
	    // Implementar lógica para asignar pokémon al player 1
	}

	public void setPokemonSeleccionadosPlayer2(List<String> seleccionados) {
	    // Implementar lógica para asignar pokémon al player 2
	}

	public void setPokemonSeleccionadosPlayer(List<String> seleccionados) {
	    // Implementar lógica para asignar pokémon al player en PvM
	}

	public void setMovimientosSeleccionadosPlayer2(List<String> seleccionados) {
	    // Implementar lógica para asignar movimientos al player 2
	}

	public void setMovimientosSeleccionadosPlayer(List<String> seleccionados) {
	    // Implementar lógica para asignar movimientos al player en PvM
	}

	public void prepararBatallaPvP() {
	    // Implementar lógica de preparación para batalla PvP
	}

	public void prepararBatallaPvM() {
	    // Implementar lógica de preparación para batalla PvM
	}
	
	// Métodos para manejar selección de Pokémon
	public List<Pokemon> getSelectedPokemons() {
	    return new ArrayList<>(selectedPokemons);
	}

	public void setSelectedPokemons(List<Pokemon> pokemons) {
	    this.selectedPokemons = new ArrayList<>(pokemons);
	}

	// Métodos para manejar movimientos
	public Map<Pokemon, List<Movimiento>> getPokemonMovements() {
	    return new HashMap<>(pokemonMovements);
	}

	public void setPokemonMovements(Map<Pokemon, List<Movimiento>> movements) {
	    this.pokemonMovements = new HashMap<>(movements);
	}


	// Método para cargar movimientos de un Pokémon
	public List<Movimiento> loadPokemonMovements(Pokemon pokemon) {
	    List<Movimiento> allPossibleMovements = new ArrayList<>();
	    
	    // Obtener movimientos de todos los tipos del Pokémon
	    for (Tipo tipo : pokemon.getTipos()) {
	        allPossibleMovements.addAll(getMovimientosPorTipo(tipo));
	    }
	    
	    // Filtrar movimientos ya asignados a otros Pokémon
	    List<Movimiento> assignedToOthers = new ArrayList<>();
	    pokemonMovements.entrySet().stream()
	        .filter(entry -> !entry.getKey().equals(pokemon))
	        .forEach(entry -> assignedToOthers.addAll(entry.getValue()));
	    
	    // Obtener movimientos ya asignados a este Pokémon
	    List<Movimiento> alreadyAssigned = pokemonMovements.getOrDefault(pokemon, new ArrayList<>());
	    
	    // Crear lista final de movimientos disponibles
	    List<Movimiento> availableMovements = new ArrayList<>();
	    for (Movimiento m : allPossibleMovements) {
	        if (!assignedToOthers.contains(m) || alreadyAssigned.contains(m)) {
	            availableMovements.add(m);
	        }
	    }
	    
	    return availableMovements;
	}

	// Método para guardar movimientos de un Pokémon
	public void savePokemonMovements(Pokemon pokemon, List<Movimiento> movements) {
	    if (pokemon != null && movements != null) {
	        pokemonMovements.put(pokemon, new ArrayList<>(movements));
	    }
	}

	// Método para verificar si todos los Pokémon tienen 4 movimientos
	public boolean areAllMovementsAssigned() {
	    if (selectedPokemons.isEmpty()) return false;
	    
	    return selectedPokemons.stream()
	        .allMatch(p -> pokemonMovements.getOrDefault(p, List.of()).size() == 4);
	}
	
	public boolean canAddPokemon(Pokemon pokemon) {
	    if (selectedPokemons.size() >= MAX_POKEMONS) {
	        return false;
	    }
	    
	    // Contar tipos actuales
	    Map<Tipo, Integer> typeCount = new EnumMap<>(Tipo.class);
	    for (Pokemon p : selectedPokemons) {
	        for (Tipo tipo : p.getTipos()) {
	            typeCount.put(tipo, typeCount.getOrDefault(tipo, 0) + 1);
	        }
	    }
	    
	    // Verificar límites de tipo para el nuevo Pokémon
	    for (Tipo tipo : pokemon.getTipos()) {
	        if (typeCount.getOrDefault(tipo, 0) >= MAX_SAME_TYPE) {
	            return false;
	        }
	    }
	    
	    return true;
	}

	public String getSelectionStatus() {
	    return "Pokémon seleccionados: " + selectedPokemons.size() + "/" + MAX_POKEMONS;
	}
	
	
	
	//prueba agregar
	public static void main(String[] args) {
		PokemonAplication p;
		try {
			p = new PokemonAplication();
		
		Map<Tipo,List<Pokemon>> pokemonesMachine = new HashMap<>();
		for (Tipo tipo : Tipo.values()) {
			pokemonesMachine.put(tipo, p.getPokemonesPorTipo(tipo));
		}
		for (Map.Entry<Tipo,List<Pokemon>> r: pokemonesMachine.entrySet()) {
			System.out.println(r.getKey() + "+++");
			for (Pokemon p2: r.getValue()) {
				System.out.println(p2.getNombre());
				
			}
		}
		} catch (PokemonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
	
	
	
	
	
