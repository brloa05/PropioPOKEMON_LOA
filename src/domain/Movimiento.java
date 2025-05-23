package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class Movimiento {
    
    public enum TipoEfecto {
        REDUCIR_VELOCIDAD,
        AMEDRENTAR,
        AUMENTAR_DEFENSA,
        CONGELAR,               
        PROTECCION_ESTADISTICAS, 
        RESET_ESTADISTICAS,      
        AUMENTAR_CRITICO,        
        RETROCESO,  
        NINGUNO,
        PARALIZAR,
        QUEMAR,           
        CONFUNDIR,
        CURAR_PORCENTAJE,
        DANO_FIJO, 
        AUMENTAR_ATAQUE, 
        REDUCIR_DEFENSA_ESPECIAL, 
        DORMIR, 
        AUMENTAR_VELOCIDAD, 
        REDUCIR_PRECISION,  
        AUMENTAR_DEFENSA_ESPECIAL, 
        ENVENENAR,  
        ESCONDERSE, 
        MULTIGOLPE, 
        REDUCIR_DEFENSA,
        }
    
    public enum ObjetivoEfecto {
        USUARIO,
        OBJETIVO,
        AMBOS
    }
    
    protected String name;
    protected int potencia;
    protected int presicion;
    protected int pp;
    protected String descripcionEfecto;
    protected TipoEfecto tipoEfecto;
    protected ObjetivoEfecto objetivoEfecto;
    protected int prioridad;
    protected int probabilidadEfecto;
    private static final Map<Pokemon.Tipo, List<Movimiento>> movimientosPorTipo = new HashMap<>();
    
    static {
        try {
			inicializarMovimientos();
		} catch (PokemonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public Movimiento(String name, int potencia, int presicion, int pp, 
                     String descripcionEfecto, TipoEfecto tipoEfecto,
                     ObjetivoEfecto objetivoEfecto, int prioridad) throws PokemonException {
    	if (name.equals(null)) {
    		throw new PokemonException(PokemonException.NOMBRE_MOVIMIENTO_NULL);
    	}
    	
    	if (descripcionEfecto.equals(null)) {
    		throw new PokemonException(PokemonException.DESCRIPCION_EFECTO_NULA);
    	}
  
        this.name = name;
        this.potencia = potencia;
        this.presicion = presicion;
        this.pp = pp;
        this.descripcionEfecto = descripcionEfecto;
        this.tipoEfecto = tipoEfecto;
        this.objetivoEfecto = objetivoEfecto;
        this.prioridad = prioridad;
        this.probabilidadEfecto = extraerProbabilidad(descripcionEfecto);
    }
    
    
    private int extraerProbabilidad(String descripcion) {
        try {
            String porcentaje = descripcion.split("\\(")[1].split("\\)")[0];
            return Integer.parseInt(porcentaje);
        } catch (Exception e) {
            return 0;
        }
    }
    
    public void aplicarEfecto(Pokemon usuario, Pokemon objetivo) {
        if (tipoEfecto == TipoEfecto.NINGUNO || probabilidadEfecto <= 0) return;
        
        Random rand = new Random();
        if (rand.nextInt(100) < probabilidadEfecto) {
            switch(objetivoEfecto) {
                case USUARIO:
                    aplicarEfectoIndividual(usuario);
                    break;
                case OBJETIVO:
                    aplicarEfectoIndividual(objetivo);
                    break;
                case AMBOS:
                    aplicarEfectoIndividual(usuario);
                    aplicarEfectoIndividual(objetivo);
                    break;
            }
            System.out.println("¡Efecto aplicado! " + descripcionEfecto);
        }
    }
    
    protected void aplicarEfectoIndividual(Pokemon pokemon) {
        switch(tipoEfecto) {

            case REDUCIR_VELOCIDAD:
                pokemon.modificarVelocidad(-1);
                break;
            case AMEDRENTAR:
                pokemon.modificarAtaque(-1);
                break;
            case AUMENTAR_DEFENSA:
                pokemon.modificarDefensa(1);
                break;
            case CONGELAR:
                pokemon.aplicarEstado("CONGELADO");
                break;
            case PROTECCION_ESTADISTICAS:
                pokemon.aplicarEstado("PROTEGIDO_ESTADISTICAS");
                break;
            case RESET_ESTADISTICAS:
                pokemon.resetearEstadisticas();
                break;
            case AUMENTAR_CRITICO:
                pokemon.aumentarProbabilidadCritico();
                break;
            case RETROCESO:
                break;
            case NINGUNO:
                break;
            case PARALIZAR:
                pokemon.aplicarEstado("PARALIZADO");
                break;
            case QUEMAR:
                pokemon.aplicarEstado("QUEMADO");
                break;
            case CONFUNDIR:
                pokemon.aplicarEstado("CONFUNDIDO");
                break;
            case CURAR_PORCENTAJE:
                // Se manejará en el cálculo de daño
                break;
            case DANO_FIJO:
                // Se manejará en el cálculo de daño
                break;
            case AUMENTAR_ATAQUE:
                pokemon.modificarAtaque(1);
                System.out.println(pokemon.getNombre() + ": Ataque aumentado en 1 nivel");
                break;
                
            case REDUCIR_DEFENSA_ESPECIAL:
                pokemon.modificarDefensaEspecial(-1);
                System.out.println(pokemon.getNombre() + ": Defensa especial reducida en 1 nivel");
                break;
                
            case DORMIR:
                pokemon.aplicarEstado("DORMIDO");
                System.out.println(pokemon.getNombre() + ": Se ha quedado dormido");
                break;
                
            case AUMENTAR_VELOCIDAD:
                pokemon.modificarVelocidad(1);
                System.out.println(pokemon.getNombre() + ": Velocidad aumentada en 1 nivel");
                break;
                
            case REDUCIR_PRECISION:

                System.out.println(pokemon.getNombre() + ": Precisión reducida (implementar lógica en clase Pokemon)");
                break;
                
            case AUMENTAR_DEFENSA_ESPECIAL:
                pokemon.modificarDefensaEspecial(1);
                System.out.println(pokemon.getNombre() + ": Defensa especial aumentada en 1 nivel");
                break;
                
            case ENVENENAR:
                pokemon.aplicarEstado("ENVENENADO");
                System.out.println(pokemon.getNombre() + ": Envenenamiento aplicado");
                break;
                
            case ESCONDERSE:
                pokemon.aplicarEstado("ESCONDIDO");
                System.out.println(pokemon.getNombre() + ": Se ha escondido");
                break;
                
            case MULTIGOLPE:
                // Este efecto se manejará en la lógica de ataque
                System.out.println(pokemon.getNombre() + ": Preparado para atacar múltiples veces");
                break;
                
            case REDUCIR_DEFENSA:
                pokemon.modificarDefensa(-1);
                System.out.println(pokemon.getNombre() + ": Defensa reducida en 1 nivel");
                break;
            
        }
    }
    
    private static void inicializarMovimientos() throws PokemonException {
        agregarMovimiento(Pokemon.Tipo.AGUA, new Especial("Burbuja", 40, 100, 30, "Puede reducir Velocidad(10)%", TipoEfecto.REDUCIR_VELOCIDAD,ObjetivoEfecto.OBJETIVO, 0));
        agregarMovimiento(Pokemon.Tipo.AGUA, 
            new Fisico("Cascada", 80, 100, 15, 
                "Amedrentar al objetivo (20%)", 
                Movimiento.TipoEfecto.AMEDRENTAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.AGUA, 
            new Especial("Hidrobomba", 110, 80, 5, 
                "Potente chorro de agua sin efectos adicionales", 
                Movimiento.TipoEfecto.NINGUNO,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.AGUA, 
            new Fisico("Martillazo", 100, 90, 10, 
                "Golpe contundente sin efectos adicionales", 
                Movimiento.TipoEfecto.NINGUNO,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.AGUA, 
            new Especial("Pistola agua", 40, 100, 25, 
                "Disparo de agua a presión sin efectos adicionales", 
                Movimiento.TipoEfecto.NINGUNO,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.AGUA, 
            new Especial("Rayo burbuja", 65, 100, 20, 
                "Puede reducir Velocidad(10)%", 
                Movimiento.TipoEfecto.REDUCIR_VELOCIDAD,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.AGUA, 
            new DeEstado("Refugio", 0, 0, 40, 
                "Aumenta Defensa del usuario", 
                Movimiento.TipoEfecto.AUMENTAR_DEFENSA,
                Movimiento.ObjetivoEfecto.USUARIO, 
                0));

        agregarMovimiento(Pokemon.Tipo.AGUA, 
            new Especial("Surf", 95, 100, 15, 
                "Ola gigante sin efectos adicionales", 
                Movimiento.TipoEfecto.NINGUNO,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));
        
     // Movimientos de tipo BICHO
        agregarMovimiento(Pokemon.Tipo.BICHO, 
            new Fisico("Chupavidas", 20, 100, 15, 
                "Recupera 50% del daño infligido", 
                Movimiento.TipoEfecto.CURAR_PORCENTAJE,
                Movimiento.ObjetivoEfecto.USUARIO, 
                0));

        agregarMovimiento(Pokemon.Tipo.BICHO, 
            new DeEstado("Disparo demora", 0, 95, 40, 
                "Reduce Velocidad (100%)", 
                Movimiento.TipoEfecto.REDUCIR_VELOCIDAD,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.DRAGON, 
            new Especial("Furia dragón", 0, 100, 10, 
                "Inflige 40 PS exactos", 
                Movimiento.TipoEfecto.DANO_FIJO,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.ELECTRICO, 
            new Especial("Impactrueno", 40, 100, 30, 
                "Puede paralizar (10%)", 
                Movimiento.TipoEfecto.PARALIZAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.ELECTRICO, 
            new DeEstado("Onda trueno", 0, 100, 20, 
                "Paraliza (100%)", 
                Movimiento.TipoEfecto.PARALIZAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.ELECTRICO, 
            new Fisico("Puño trueno", 75, 100, 15, 
                "Puede paralizar (10%)", 
                Movimiento.TipoEfecto.PARALIZAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.ELECTRICO, 
            new Especial("Rayo", 95, 100, 15, 
                "Puede paralizar (10%)", 
                Movimiento.TipoEfecto.PARALIZAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.ELECTRICO, 
            new Especial("Trueno", 110, 70, 10, 
                "Puede paralizar (30%)", 
                Movimiento.TipoEfecto.PARALIZAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));
        
        agregarMovimiento(Pokemon.Tipo.FANTASMA, 
            new Fisico("Lengüetazo", 20, 100, 30, 
                "Puede paralizar (30%)", 
                Movimiento.TipoEfecto.PARALIZAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.FANTASMA, 
            new DeEstado("Rayo confuso", 0, 100, 10, 
                "Confunde al objetivo (100%)", 
                Movimiento.TipoEfecto.CONFUNDIR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.FANTASMA, 
            new Especial("Tinieblas", 0, 100, 10, 
                "Confunde al objetivo (100%)", 
                Movimiento.TipoEfecto.CONFUNDIR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.FUEGO, 
            new Especial("Giro fuego", 40, 100, 25, 
                "Puede quemar (10%)", 
                Movimiento.TipoEfecto.QUEMAR, 
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.FUEGO, 
            new Especial("Llamarada", 90, 100, 15, 
                "Puede quemar (10%)", 
                Movimiento.TipoEfecto.QUEMAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.FUEGO, 
            new Fisico("Puño fuego", 110, 85, 5, 
                "Puede quemar (10%)", 
                Movimiento.TipoEfecto.QUEMAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));
        
        agregarMovimiento(Pokemon.Tipo.HIELO, 
            new DeEstado("Neblina", 75, 100, 15,
                "Puede quemar (10%)",
                Movimiento.TipoEfecto.QUEMAR,
                Movimiento.ObjetivoEfecto.OBJETIVO,
                0));

        agregarMovimiento(Pokemon.Tipo.HIELO, 
            new DeEstado("Niebla", 0, 0, 30,
                "Evita reducción de estadísticas del equipo",
                Movimiento.TipoEfecto.PROTECCION_ESTADISTICAS,
                Movimiento.ObjetivoEfecto.USUARIO,
                0));

        agregarMovimiento(Pokemon.Tipo.HIELO, 
            new Fisico("Puño hielo", 0, 0, 30,
                "Elimina cambios de estadísticas del objetivo",
                Movimiento.TipoEfecto.RESET_ESTADISTICAS,
                Movimiento.ObjetivoEfecto.OBJETIVO,
                0));

        agregarMovimiento(Pokemon.Tipo.HIELO, 
            new Especial("Rayo aurora", 75, 100, 15,
                "Puede congelar (10%)",
                Movimiento.TipoEfecto.CONGELAR,
                Movimiento.ObjetivoEfecto.OBJETIVO,
                0));

        agregarMovimiento(Pokemon.Tipo.HIELO, 
            new Especial("Rayo hielo", 65, 100, 20,
                "Puede congelar (10%)",
                Movimiento.TipoEfecto.CONGELAR,
                Movimiento.ObjetivoEfecto.OBJETIVO,
                0));

        agregarMovimiento(Pokemon.Tipo.HIELO, 
            new Especial("Ventisca", 90, 100, 10,
                "Puede congelar (10%)",
                Movimiento.TipoEfecto.CONGELAR,
                Movimiento.ObjetivoEfecto.OBJETIVO,
                0));

        agregarMovimiento(Pokemon.Tipo.LUCHA, 
            new Fisico("Contraataque", 110, 70, 5,
                "Ninguno", 
                Movimiento.TipoEfecto.NINGUNO,
                Movimiento.ObjetivoEfecto.OBJETIVO,
                0));

        agregarMovimiento(Pokemon.Tipo.LUCHA, 
            new Fisico("Patada baja", 50, 100, 25,
                "Alta probabilidad de golpe crítico",
                Movimiento.TipoEfecto.AUMENTAR_CRITICO,
                Movimiento.ObjetivoEfecto.USUARIO,
                0));

        agregarMovimiento(Pokemon.Tipo.LUCHA, 
            new Fisico("Sumisión", 80, 80, 25,
                "Causa daño de retroceso (25%)",
                Movimiento.TipoEfecto.RETROCESO,
                Movimiento.ObjetivoEfecto.AMBOS,
                0));
        
        agregarMovimiento(Pokemon.Tipo.PLANTA, 
            new Especial("Absorber", 20, 100, 25, 
                "Recupera 50% del daño causado", 
                Movimiento.TipoEfecto.CURAR_PORCENTAJE,
                Movimiento.ObjetivoEfecto.USUARIO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PLANTA, 
            new Especial("Danza pétalo", 120, 100, 10, 
                "Confunde al usuario al finalizar", 
                Movimiento.TipoEfecto.CONFUNDIR,
                Movimiento.ObjetivoEfecto.USUARIO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PLANTA, 
            new DeEstado("Espora", 0, 100, 15, 
                "Duerme al objetivo", 
                Movimiento.TipoEfecto.DORMIR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PLANTA, 
            new Fisico("Hoja afilada", 55, 95, 25, 
                "Alta probabilidad de golpe crítico", 
                Movimiento.TipoEfecto.AUMENTAR_CRITICO,
                Movimiento.ObjetivoEfecto.USUARIO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PLANTA, 
            new Fisico("Látigo cepa", 45, 100, 25, 
                "Ninguno", 
                Movimiento.TipoEfecto.NINGUNO,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PLANTA, 
            new Especial("Megaagotar", 40, 100, 15, 
                "Recupera 50% del daño causado", 
                Movimiento.TipoEfecto.CURAR_PORCENTAJE,
                Movimiento.ObjetivoEfecto.USUARIO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PLANTA, 
            new DeEstado("Paralizador", 0, 75, 20, 
                "Paraliza al objetivo", 
                Movimiento.TipoEfecto.PARALIZAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PLANTA, 
            new DeEstado("Somnífero", 0, 75, 15, 
                "Duerme al objetivo", 
                Movimiento.TipoEfecto.DORMIR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PSIQUICO, 
            new DeEstado("Agilidad", 0, 0, 30, 
                "Aumenta 2 niveles la Velocidad del usuario", 
                Movimiento.TipoEfecto.AUMENTAR_VELOCIDAD,
                Movimiento.ObjetivoEfecto.USUARIO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PSIQUICO, 
            new DeEstado("Amnesia", 0, 0, 20, 
                "Aumenta 2 niveles la Defensa Especial del usuario", 
                Movimiento.TipoEfecto.AUMENTAR_DEFENSA_ESPECIAL,
                Movimiento.ObjetivoEfecto.USUARIO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PSIQUICO, 
            new DeEstado("Barrera", 0, 0, 30, 
                "Aumenta 2 niveles la Defensa del usuario", 
                Movimiento.TipoEfecto.AUMENTAR_DEFENSA,
                Movimiento.ObjetivoEfecto.USUARIO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PSIQUICO, 
            new Especial("Confusión", 50, 100, 25, 
                "10% de confundir al objetivo", 
                Movimiento.TipoEfecto.CONFUNDIR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PSIQUICO, 
            new DeEstado("Hipnosis", 0, 60, 20, 
                "Duerme al objetivo", 
                Movimiento.TipoEfecto.DORMIR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PSIQUICO, 
            new DeEstado("Kinético", 0, 80, 15, 
                "Reduce la Precisión del objetivo en 1 nivel", 
                Movimiento.TipoEfecto.REDUCIR_PRECISION,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PSIQUICO, 
            new DeEstado("Meditación", 0, 0, 40, 
                "Aumenta 1 nivel el Ataque del usuario", 
                Movimiento.TipoEfecto.AUMENTAR_ATAQUE,
                Movimiento.ObjetivoEfecto.USUARIO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PSIQUICO, 
            new Especial("Psicorrayo", 65, 100, 20, 
                "10% de confundir al objetivo", 
                Movimiento.TipoEfecto.CONFUNDIR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.PSIQUICO, 
            new Especial("Psíquico", 90, 100, 10, 
                "10% de reducir la Defensa Especial del objetivo", 
                Movimiento.TipoEfecto.REDUCIR_DEFENSA_ESPECIAL,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));
        
        agregarMovimiento(Pokemon.Tipo.ROCA, 
            new Fisico("Avalancha", 60, 100, 10, 
                "Ninguno", 
                Movimiento.TipoEfecto.NINGUNO,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.ROCA, 
            new Fisico("Lanzarrocas", 50, 100, 15, 
                "10% de reducir la velocidad del objetivo", 
                Movimiento.TipoEfecto.REDUCIR_VELOCIDAD,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.SINIESTRO, 
            new Fisico("Mordisco", 60, 100, 25, 
                "10% de hacer retroceder al objetivo", 
                Movimiento.TipoEfecto.RETROCESO,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.TIERRA, 
            new DeEstado("Ataque arena", 0, 0, 15, 
                "Reduce la precisión del objetivo en 1 nivel", 
                Movimiento.TipoEfecto.REDUCIR_PRECISION,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.TIERRA, 
            new Fisico("Excavar", 80, 100, 10, 
                "El usuario se esconde bajo tierra durante 1 turno y ataca al siguiente", 
                Movimiento.TipoEfecto.ESCONDERSE,
                Movimiento.ObjetivoEfecto.USUARIO, 
                0));

        agregarMovimiento(Pokemon.Tipo.TIERRA, 
            new Fisico("Hueso palo", 65, 85, 20, 
                "10% de amedrentar al objetivo", 
                Movimiento.TipoEfecto.AMEDRENTAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.TIERRA, 
            new Fisico("Huesomerang", 50, 90, 10, 
                "Golpea dos veces consecutivas", 
                Movimiento.TipoEfecto.MULTIGOLPE,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.TIERRA, 
            new Fisico("Terremoto", 100, 100, 10, 
                "Ninguno", 
                Movimiento.TipoEfecto.NINGUNO,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.VENENO, 
            new Especial("Ácido", 40, 100, 30, 
                "10% de reducir la Defensa del objetivo", 
                Movimiento.TipoEfecto.REDUCIR_DEFENSA,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.VENENO, 
            new DeEstado("Armadura ácida", 0, 0, 20, 
                "Aumenta la Defensa Especial del usuario en 2 niveles", 
                Movimiento.TipoEfecto.AUMENTAR_DEFENSA_ESPECIAL,
                Movimiento.ObjetivoEfecto.USUARIO, 
                0));

        agregarMovimiento(Pokemon.Tipo.VENENO, 
            new DeEstado("Gas venenoso", 0, 90, 40, 
                "Envenena al objetivo", 
                Movimiento.TipoEfecto.ENVENENAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.VENENO, 
            new Fisico("Picotazo veneno", 15, 100, 35, 
                "10% de envenenar al objetivo", 
                Movimiento.TipoEfecto.ENVENENAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.VENENO, 
            new Especial("Polución", 65, 100, 20, 
                "10% de envenenar al objetivo", 
                Movimiento.TipoEfecto.ENVENENAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.VENENO, 
            new DeEstado("Polvo veneno", 0, 75, 20, 
                "Envenena al objetivo", 
                Movimiento.TipoEfecto.ENVENENAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.VENENO, 
            new Especial("Residuos", 50, 100, 20, 
                "10% de envenenar al objetivo", 
                Movimiento.TipoEfecto.ENVENENAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));

        agregarMovimiento(Pokemon.Tipo.VENENO, 
            new DeEstado("Tóxico", 0, 90, 10, 
                "Envenena al objetivo", 
                Movimiento.TipoEfecto.ENVENENAR,
                Movimiento.ObjetivoEfecto.OBJETIVO, 
                0));
        
        agregarMovimiento(Pokemon.Tipo.VOLADOR, 
        	    new Fisico("Ataque aéreo", 0, 100, 10, 
        	        "Alta probabilidad de golpe crítico", 
        	        Movimiento.TipoEfecto.AUMENTAR_CRITICO,
        	        Movimiento.ObjetivoEfecto.USUARIO, 
        	        0));

        	agregarMovimiento(Pokemon.Tipo.VOLADOR, 
        	    new Fisico("Ataque ala", 60, 100, 25, 
        	        "10% de confundir al objetivo", 
        	        Movimiento.TipoEfecto.CONFUNDIR,
        	        Movimiento.ObjetivoEfecto.OBJETIVO, 
        	        0));

        	agregarMovimiento(Pokemon.Tipo.VOLADOR, 
        	    new Fisico("Pico taladro", 80, 100, 20, 
        	        "10% de hacer retroceder al objetivo", 
        	        Movimiento.TipoEfecto.RETROCESO,
        	        Movimiento.ObjetivoEfecto.OBJETIVO, 
        	        0));

        	agregarMovimiento(Pokemon.Tipo.VOLADOR, 
        	    new Fisico("Picotazo", 50, 100, 25, 
        	        "10% de hacer retroceder al objetivo", 
        	        Movimiento.TipoEfecto.RETROCESO,
        	        Movimiento.ObjetivoEfecto.OBJETIVO, 
        	        0));

        	agregarMovimiento(Pokemon.Tipo.VOLADOR, 
        	    new Especial("Tornado", 110, 70, 5, 
        	        "10% de confundir al objetivo", 
        	        Movimiento.TipoEfecto.CONFUNDIR,
        	        Movimiento.ObjetivoEfecto.OBJETIVO, 
        	        0));

        
        // proximos moviemientos con la misma logica 
        
    }

    private static void agregarMovimiento(Pokemon.Tipo tipo, Movimiento movimiento) {
        movimientosPorTipo.computeIfAbsent(tipo, k -> new ArrayList<>()).add(movimiento);
    }

    public static List<Movimiento> getMovimientosPorTipo(Pokemon.Tipo tipo) {
        return new ArrayList<>(movimientosPorTipo.getOrDefault(tipo, new ArrayList<>()));
    }
    
    // Getters
    public String getName() { return name; }
    public int getPotencia() { return potencia; }
    public int getPresicion() { return presicion; }
    public int getPp() { return pp; }
    public int getPrioridad() { return prioridad; }
    public String getDescripcionEfecto() { return descripcionEfecto; }
}
