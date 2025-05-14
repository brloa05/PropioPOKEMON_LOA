package domain;

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
	private ModalidadJuego modalidadJuego;
	private Map<Pokemon.Tipo, List<Movimiento>> movimientosPorTipo;
	private Map<Pokemon, List<Movimiento>> pokemonMovements = new HashMap<>();
	private List<Pokemon> selectedPokemons = new ArrayList<>();
	private static final int MAX_POKEMONS = 6;
	private static final int MAX_SAME_TYPE = 2;
	
	
	public PokemonAplication() {
			this.entrenadores =  new ArrayList<>();
			this.pokemones = new ArrayList<>();
			this.movimientosPorTipo = new HashMap<>();
	        inicializarMovimientos();
	        inicializarPokemonesDisponibles();
		
	}
	
	private void inicializarMovimientos() {
        // Movimientos de tipo AGUA
        List<Movimiento> movimientosAgua = new ArrayList<>();
        movimientosAgua.add(new Especial("Burbuja", 40, 100, 30, "Puede reducir Velocidad(10)%", Movimiento.TipoEfecto.REDUCIR_VELOCIDAD, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosAgua.add(new Fisico("Cascada", 80, 100, 15, "Amedrentar al objetivo (20%)", Movimiento.TipoEfecto.AMEDRENTAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosAgua.add(new Especial("Hidrobomba", 110, 80, 5, "Potente chorro de agua sin efectos adicionales", Movimiento.TipoEfecto.NINGUNO, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosAgua.add(new Fisico("Martillazo", 100, 90, 10, "Golpe contundente sin efectos adicionales", Movimiento.TipoEfecto.NINGUNO, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosAgua.add(new Especial("Pistola agua", 40, 100, 25, "Disparo de agua a presión sin efectos adicionales", Movimiento.TipoEfecto.NINGUNO, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosAgua.add(new Especial("Rayo burbuja", 65, 100, 20, "Puede reducir Velocidad(10)%", Movimiento.TipoEfecto.REDUCIR_VELOCIDAD, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosAgua.add(new DeEstado("Refugio", 0, 0, 40, "Aumenta Defensa del usuario", Movimiento.TipoEfecto.AUMENTAR_DEFENSA, Movimiento.ObjetivoEfecto.USUARIO, 0));

        movimientosAgua.add(new Especial("Surf", 95, 100, 15, "Ola gigante sin efectos adicionales", Movimiento.TipoEfecto.NINGUNO, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        
        
        // Movimientos de tipo BICHO
        List<Movimiento> movimientosBicho = new ArrayList<>();
        movimientosBicho.add(new Fisico("Chupavidas", 20, 100, 15, "Recupera 50% del daño infligido", Movimiento.TipoEfecto.CURAR_PORCENTAJE, Movimiento.ObjetivoEfecto.USUARIO, 0));
        
        movimientosBicho.add(new DeEstado("Disparo demora", 0, 95, 40, "Reduce Velocidad (100%)", Movimiento.TipoEfecto.REDUCIR_VELOCIDAD, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
       
        // Movimientos de tipo DRAGON
        List<Movimiento> movimientosDragon = new ArrayList<>();
        movimientosDragon.add(new Especial("Furia dragón", 0, 100, 10, "Inflige 40 PS exactos", Movimiento.TipoEfecto.DANO_FIJO, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        // Movimientos de tipo ELECTRICO
        List<Movimiento> movimientosElectrico = new ArrayList<>();
        movimientosElectrico.add(new Especial("Impactrueno", 40, 100, 30, "Puede paralizar (10%)", Movimiento.TipoEfecto.PARALIZAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosElectrico.add(new DeEstado("Onda trueno", 0, 100, 20, "Paraliza (100%)", Movimiento.TipoEfecto.PARALIZAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosElectrico.add(new Fisico("Puño trueno", 75, 100, 15, "Puede paralizar (10%)", Movimiento.TipoEfecto.PARALIZAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosElectrico.add(new Especial("Rayo", 95, 100, 15, "Puede paralizar (10%)", Movimiento.TipoEfecto.PARALIZAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosElectrico.add(new Especial("Trueno", 110, 70, 10, "Puede paralizar (30%)", Movimiento.TipoEfecto.PARALIZAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        
        // Movimientos de tipo FANTASMA
        List<Movimiento> movimientosFantasma = new ArrayList<>();
        movimientosFantasma.add(new Fisico("Lengüetazo", 20, 100, 30, "Puede paralizar (30%)", Movimiento.TipoEfecto.PARALIZAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosFantasma.add(new DeEstado("Rayo confuso", 0, 100, 10, "Confunde al objetivo (100%)", Movimiento.TipoEfecto.CONFUNDIR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosFantasma.add(new Especial("Tinieblas", 0, 100, 10, "Confunde al objetivo (100%)", Movimiento.TipoEfecto.CONFUNDIR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        
        // Movimientos de tipo FUEGO
        List<Movimiento> movimientosFuego = new ArrayList<>();
        movimientosFuego.add(new Especial("Giro fuego", 40, 100, 25, "Puede quemar (10%)", Movimiento.TipoEfecto.QUEMAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosFuego.add(new Especial("Llamarada", 90, 100, 15, "Puede quemar (10%)", Movimiento.TipoEfecto.QUEMAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosFuego.add(new Fisico("Puño fuego", 110, 85, 5, "Puede quemar (10%)", Movimiento.TipoEfecto.QUEMAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        // Movimientos de tipo HIELO
        List<Movimiento> movimientosHielo = new ArrayList<>();
        movimientosHielo.add(new DeEstado("Neblina", 75, 100, 15, "Puede quemar (10%)", Movimiento.TipoEfecto.QUEMAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosHielo.add(new DeEstado("Niebla", 0, 0, 30, "Evita reducción de estadísticas del equipo", Movimiento.TipoEfecto.PROTECCION_ESTADISTICAS, Movimiento.ObjetivoEfecto.USUARIO, 0));

        movimientosHielo.add(new Fisico("Puño hielo", 0, 0, 30, "Elimina cambios de estadísticas del objetivo", Movimiento.TipoEfecto.RESET_ESTADISTICAS, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosHielo.add(new Especial("Rayo aurora", 75, 100, 15, "Puede congelar (10%)", Movimiento.TipoEfecto.CONGELAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosHielo.add(new Especial("Rayo hielo", 65, 100, 20, "Puede congelar (10%)", Movimiento.TipoEfecto.CONGELAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosHielo.add(new Especial("Ventisca", 90, 100, 10, "Puede congelar (10%)", Movimiento.TipoEfecto.CONGELAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        
        // Movimientos de tipo LUCHA
        List<Movimiento> movimientosLucha = new ArrayList<>();
        movimientosLucha.add(new Fisico("Contraataque", 110, 70, 5, "Ninguno", Movimiento.TipoEfecto.NINGUNO, Movimiento.ObjetivoEfecto.OBJETIVO, 0));

        movimientosLucha.add(new Fisico("Patada baja", 50, 100, 25, "Alta probabilidad de golpe crítico", Movimiento.TipoEfecto.AUMENTAR_CRITICO, Movimiento.ObjetivoEfecto.USUARIO, 0));

        movimientosLucha.add(new Fisico("Sumisión", 80, 80, 25, "Causa daño de retroceso (25%)", Movimiento.TipoEfecto.RETROCESO, Movimiento.ObjetivoEfecto.AMBOS, 0));
        
        // Movimientos de tipo PLANTA
        List<Movimiento> movimientosPlanta = new ArrayList<>();
        movimientosPlanta.add(new Especial("Absorber", 20, 100, 25, "Recupera 50% del daño causado", Movimiento.TipoEfecto.CURAR_PORCENTAJE, Movimiento.ObjetivoEfecto.USUARIO, 0));
        movimientosPlanta.add(new Especial("Danza pétalo", 120, 100, 10, "Confunde al usuario al finalizar", Movimiento.TipoEfecto.CONFUNDIR, Movimiento.ObjetivoEfecto.USUARIO, 0));
        movimientosPlanta.add(new DeEstado("Espora", 0, 100, 15, "Duerme al objetivo", Movimiento.TipoEfecto.DORMIR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosPlanta.add(new Fisico("Hoja afilada", 55, 95, 25, "Alta probabilidad de golpe crítico", Movimiento.TipoEfecto.AUMENTAR_CRITICO, Movimiento.ObjetivoEfecto.USUARIO, 0));
        movimientosPlanta.add(new Fisico("Látigo cepa", 45, 100, 25, "Ninguno", Movimiento.TipoEfecto.NINGUNO, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosPlanta.add(new Especial("Megaagotar", 40, 100, 15, "Recupera 50% del daño causado", Movimiento.TipoEfecto.CURAR_PORCENTAJE, Movimiento.ObjetivoEfecto.USUARIO, 0));
        movimientosPlanta.add(new DeEstado("Paralizador", 0, 75, 20, "Paraliza al objetivo", Movimiento.TipoEfecto.PARALIZAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosPlanta.add(new DeEstado("Somnífero", 0, 75, 15, "Duerme al objetivo", Movimiento.TipoEfecto.DORMIR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        
        // Movimientos de tipo PSIQUICO
        List<Movimiento> movimientosPsiquico = new ArrayList<>();
        movimientosPsiquico.add(new DeEstado("Agilidad", 0, 0, 30, "Aumenta 2 niveles la Velocidad del usuario", Movimiento.TipoEfecto.AUMENTAR_VELOCIDAD, Movimiento.ObjetivoEfecto.USUARIO, 0));
        movimientosPsiquico.add(new DeEstado("Amnesia", 0, 0, 20, "Aumenta 2 niveles la Defensa Especial del usuario", Movimiento.TipoEfecto.AUMENTAR_DEFENSA_ESPECIAL, Movimiento.ObjetivoEfecto.USUARIO, 0));
        movimientosPsiquico.add(new DeEstado("Barrera", 0, 0, 30, "Aumenta 2 niveles la Defensa del usuario", Movimiento.TipoEfecto.AUMENTAR_DEFENSA, Movimiento.ObjetivoEfecto.USUARIO, 0));
        movimientosPsiquico.add(new Especial("Confusión", 50, 100, 25, "10% de confundir al objetivo", Movimiento.TipoEfecto.CONFUNDIR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosPsiquico.add(new DeEstado("Hipnosis", 0, 60, 20, "Duerme al objetivo", Movimiento.TipoEfecto.DORMIR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosPsiquico.add(new DeEstado("Kinético", 0, 80, 15, "Reduce la Precisión del objetivo en 1 nivel", Movimiento.TipoEfecto.REDUCIR_PRECISION, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosPsiquico.add(new DeEstado("Meditación", 0, 0, 40, "Aumenta 1 nivel el Ataque del usuario", Movimiento.TipoEfecto.AUMENTAR_ATAQUE, Movimiento.ObjetivoEfecto.USUARIO, 0));
        movimientosPsiquico.add(new Especial("Psicorrayo", 65, 100, 20, "10% de confundir al objetivo", Movimiento.TipoEfecto.CONFUNDIR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosPsiquico.add(new Especial("Psíquico", 90, 100, 10, "10% de reducir la Defensa Especial del objetivo", Movimiento.TipoEfecto.REDUCIR_DEFENSA_ESPECIAL, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        
        // Movimientos de tipo ROCA
        List<Movimiento> movimientosRoca = new ArrayList<>();
        movimientosRoca.add(new Fisico("Avalancha", 60, 100, 10, "Ninguno", Movimiento.TipoEfecto.NINGUNO, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosRoca.add(new Fisico("Lanzarrocas", 50, 100, 15, "10% de reducir la velocidad del objetivo", Movimiento.TipoEfecto.REDUCIR_VELOCIDAD, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        
        // Movimientos de tipo SINIESTRO
        List<Movimiento> movimientosSiniestro = new ArrayList<>();
        movimientosSiniestro.add(new Fisico("Mordisco", 60, 100, 25, "10% de hacer retroceder al objetivo", Movimiento.TipoEfecto.RETROCESO, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        
        // Movimientos de tipo TIERRA
        List<Movimiento> movimientosTierra = new ArrayList<>();
        movimientosTierra.add(new DeEstado("Ataque arena", 0, 0, 15, "Reduce la precisión del objetivo en 1 nivel", Movimiento.TipoEfecto.REDUCIR_PRECISION, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosTierra.add(new Fisico("Excavar", 80, 100, 10, "El usuario se esconde bajo tierra durante 1 turno y ataca al siguiente", Movimiento.TipoEfecto.ESCONDERSE, Movimiento.ObjetivoEfecto.USUARIO, 0));
        movimientosTierra.add(new Fisico("Hueso palo", 65, 85, 20, "10% de amedrentar al objetivo", Movimiento.TipoEfecto.AMEDRENTAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosTierra.add(new Fisico("Huesomerang", 50, 90, 10, "Golpea dos veces consecutivas", Movimiento.TipoEfecto.MULTIGOLPE, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosTierra.add(new Fisico("Terremoto", 100, 100, 10, "Ninguno", Movimiento.TipoEfecto.NINGUNO, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        
        // Movimientos de tipo VENENO
        List<Movimiento> movimientosVeneno = new ArrayList<>();
        movimientosVeneno.add(new Especial("Ácido", 40, 100, 30, "10% de reducir la Defensa del objetivo", Movimiento.TipoEfecto.REDUCIR_DEFENSA, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosVeneno.add(new DeEstado("Armadura ácida", 0, 0, 20, "Aumenta la Defensa Especial del usuario en 2 niveles", Movimiento.TipoEfecto.AUMENTAR_DEFENSA_ESPECIAL, Movimiento.ObjetivoEfecto.USUARIO, 0));
        movimientosVeneno.add(new DeEstado("Gas venenoso", 0, 90, 40, "Envenena al objetivo", Movimiento.TipoEfecto.ENVENENAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosVeneno.add(new Fisico("Picotazo veneno", 15, 100, 35, "10% de envenenar al objetivo", Movimiento.TipoEfecto.ENVENENAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosVeneno.add(new Especial("Polución", 65, 100, 20, "10% de envenenar al objetivo", Movimiento.TipoEfecto.ENVENENAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosVeneno.add(new DeEstado("Polvo veneno", 0, 75, 20, "Envenena al objetivo", Movimiento.TipoEfecto.ENVENENAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosVeneno.add(new Especial("Residuos", 50, 100, 20, "10% de envenenar al objetivo", Movimiento.TipoEfecto.ENVENENAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosVeneno.add(new DeEstado("Tóxico", 0, 90, 10, "Envenena al objetivo", Movimiento.TipoEfecto.ENVENENAR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        
        // Movimientos de tipo VOLADOR
        List<Movimiento> movimientosVolador = new ArrayList<>();
        movimientosVolador.add(new Fisico("Ataque aéreo", 0, 100, 10, "Alta probabilidad de golpe crítico", Movimiento.TipoEfecto.AUMENTAR_CRITICO, Movimiento.ObjetivoEfecto.USUARIO, 0));
        movimientosVolador.add(new Fisico("Ataque ala", 60, 100, 25, "10% de confundir al objetivo", Movimiento.TipoEfecto.CONFUNDIR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosVolador.add(new Fisico("Pico taladro", 80, 100, 20, "10% de hacer retroceder al objetivo", Movimiento.TipoEfecto.RETROCESO, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosVolador.add(new Fisico("Picotazo", 50, 100, 25, "10% de hacer retroceder al objetivo", Movimiento.TipoEfecto.RETROCESO, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
        movimientosVolador.add(new Especial("Tornado", 110, 70, 5, "10% de confundir al objetivo", Movimiento.TipoEfecto.CONFUNDIR, Movimiento.ObjetivoEfecto.OBJETIVO, 0));
    }
	
	 public List<Movimiento> getMovimientosPorTipo(Pokemon.Tipo tipo) {
	        return new ArrayList<>(movimientosPorTipo.getOrDefault(tipo, new ArrayList<>()));
	    }
	 
	 public void inicializarPokemonesDisponibles() {
		 
		 pokemones.add(new PokemonUnTipo("BLASTOISE", 100, getMovimientosPorTipo(Pokemon.Tipo.AGUA)));
		 pokemones.add(new PokemonUnTipo("SNORLAX", 100, getMovimientosPorTipo(Pokemon.Tipo.NORMAL)));
         pokemones.add(new PokemonUnTipo("DONPHAN", 100, getMovimientosPorTipo(Pokemon.Tipo.TIERRA)));
	     pokemones.add(new PokemonUnTipo("MACHAMP", 100, getMovimientosPorTipo(Pokemon.Tipo.LUCHA)));
         pokemones.add(new PokemonUnTipo("RAICHU", 100, getMovimientosPorTipo(Pokemon.Tipo.ELECTRICO)));
		        
		 
		 pokemones.add(new PokemonDosTipos("CHARIZARD", 50, getMovimientosPorTipo(Pokemon.Tipo.FUEGO)));
		 pokemones.add(new PokemonDosTipos("VENUSAUR", 50, getMovimientosPorTipo(Pokemon.Tipo.PLANTA)));
		 pokemones.add(new PokemonDosTipos("GENGAR", 50, getMovimientosPorTipo(Pokemon.Tipo.FANTASMA)));
		 pokemones.add(new PokemonDosTipos("DRAGONITE", 50, getMovimientosPorTipo(Pokemon.Tipo.DRAGON)));
		 pokemones.add(new PokemonDosTipos("TOGEPIC", 50, getMovimientosPorTipo(Pokemon.Tipo.HADA)));
		 pokemones.add(new PokemonDosTipos("TYRANITAR", 50, getMovimientosPorTipo(Pokemon.Tipo.ROCA)));
		 pokemones.add(new PokemonDosTipos("GARDEVOIR", 50, getMovimientosPorTipo(Pokemon.Tipo.PSIQUICO)));
		 pokemones.add(new PokemonDosTipos("METAGROSS", 50, getMovimientosPorTipo(Pokemon.Tipo.ACERO)));
		 pokemones.add(new PokemonDosTipos("DELIBIRD", 50, getMovimientosPorTipo(Pokemon.Tipo.HIELO)));
		 
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
	 
	
	
	public void elegirModalidad(String  modalidadJuego)  {
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
	
	public void  crearEntrenadors(Map<String, String> nombresAndTipo){
			this.entrenadores =  modalidadJuego.crearEntrenadores(nombresAndTipo);
	}
	
	
	
	
	public ArrayList<Entrenador> getEntrenadores(){
		return entrenadores;
	}
		
	public void agregarPokemonesPlayer(Map<Entrenador,ArrayList<Pokemon>> playerPokemones) {
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
	
	public void agregarMovivmientos(Map<Map<Entrenador, Pokemon>, List<Movimiento>> playerMovimientos) {
		if(modalidadJuego.getName().equals("PvP")) {
			((ModalidadPvP) modalidadJuego).agregarMoimientosPlayer(playerMovimientos, movimientosPorTipo);
		} else if (modalidadJuego.getName().equals("PvM")) {
			((ModalidadPvM) modalidadJuego).agregarMoimientosPlayer(playerMovimientos, movimientosPorTipo);
		} else if (modalidadJuego.getName().equals("MvM")) {
			((ModalidadMvM) modalidadJuego).agregarMoimientosPlayer(playerMovimientos, movimientosPorTipo);
		}
		
	}
	
	public void agregarItemsLucha(Map<Entrenador,ArrayList<Item>> playerItems) {
		if (modalidadJuego.getName() == "PvP") {
			((ModalidadPvP) modalidadJuego).agregarItems(playerItems);
		} else if (modalidadJuego.getName() == "PvM") {
			((ModalidadPvM) modalidadJuego).agregarItems(playerItems);
		} else if(modalidadJuego.getName() == "MvM") {
			((ModalidadMvM) modalidadJuego).agregarItems(playerItems);
		}
		
		
		for (Entrenador e: entrenadores) {
			System.out.println(e.getName());
			ArrayList<Item> item = e.getItems();
			for(Item i : item) {
				System.out.println(i.getId());
			}
		}
		
		
		
	}
	
	public ArrayList<Item> escojerItems(ArrayList<String> itemsEscogidos ) {
		ArrayList<Item> itemEscogidos =  new ArrayList<>();
		int numeroPosiones = 0;
		int numeroSuperPosiones = 0;
		int numeroHyperPosiones = 0;
		int numeroDeRevivir = 0;
		
		for (String item: itemsEscogidos) {
			if (item.equals("Potion") && numeroPosiones < 2) {
				itemEscogidos.add(new Potion("potion" + numeroPosiones, 20));
				numeroPosiones += 1;
			} else if (item.equals("HyperPotion") && numeroHyperPosiones < 2) {
				itemEscogidos.add(new HyperPotion("HyperPotion" + numeroHyperPosiones, 50));
				numeroHyperPosiones += 1;
			} else if (item.equals("SuperPotion") && numeroSuperPosiones < 2) {
				itemEscogidos.add(new SuperPotion("SuperPotion" + numeroSuperPosiones,200));
				numeroSuperPosiones += 1;
			} else if (item.equals("Revive") && numeroDeRevivir < 1) {
				itemEscogidos.add(new Revive("Revive" + numeroDeRevivir, 400));
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
		PokemonAplication p = new PokemonAplication();
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
		
	}
}
	
	
	
	
	
