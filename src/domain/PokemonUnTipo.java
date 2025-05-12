package domain;

import java.util.List;
import java.util.Collections;

/**
 * Clase que representa un Pokémon de un solo tipo.
 * Hereda de la clase abstracta Pokemon.
 */

public class PokemonUnTipo extends Pokemon {
	public enum PokemonBase {
        BLASTOISE(Tipo.AGUA, 362, 291, 328, 295, 339, 280),
        SNORLAX(Tipo.NORMAL, 524, 350, 251, 251, 350, 174),
        DONPHAN(Tipo.TIERRA, 384, 372, 372, 240, 240, 218),
        MACHAMP(Tipo.LUCHA, 384, 394, 284, 251, 295, 229),
        RAICHU(Tipo.ELECTRICO, 324, 306, 229, 306, 284, 350);

        final Tipo tipo;
        final int[] stats; // [saludMax, ataque, defensa, ataqueE, defensaE, velocidad]

        PokemonBase(Tipo tipo, int... stats) {
            this.tipo = tipo;
            this.stats = stats;
        }
    }

    private Tipo tipo;

    /**
     * Constructor para Pokémon de un solo tipo.
     * 
     * @param nombre Nombre del Pokémon
     * @param tipo Tipo único del Pokémon
     * @param nivel Nivel del Pokémon
     * @param saludMaxima Salud máxima del Pokémon
     * @param ataque Valor de ataque físico
     * @param defensa Valor de defensa física
     * @param ataqueEspecial Valor de ataque especial
     * @param defensaEspecial Valor de defensa especial
     * @param velocidad Valor de velocidad
     * @param movimientos Lista de movimientos del Pokémon
     */
    public PokemonUnTipo(String nombre, int nivel, List<Movimiento> movimientos) {
        super(nombre, nivel, 0, 0, 0, 0, 0, 0, movimientos);
        
        // Buscar el Pokémon por nombre 
        
        for (PokemonBase pk : PokemonBase.values()) {
            if (pk.name().equalsIgnoreCase(nombre)) {
                this.tipo = pk.tipo;
                this.saludMaxima = pk.stats[0];
                this.ataque = pk.stats[1];
                this.defensa = pk.stats[2];
                this.ataqueEspecial = pk.stats[3];
                this.defensaEspecial = pk.stats[4];
                this.velocidad = pk.stats[5];
                this.salud = this.saludMaxima;
                break;
            }
        }
    }

    @Override
    public List<Tipo> getTipos() {
        return Collections.singletonList(tipo);
    }

    @Override
    public double getEfectividad(Tipo tipoAtaque) {

        switch (this.tipo) {
            case FUEGO:
                if (tipoAtaque == Tipo.AGUA) return 2.0;
                if (tipoAtaque == Tipo.PLANTA || tipoAtaque == Tipo.HIELO || 
                    tipoAtaque == Tipo.BICHO || tipoAtaque == Tipo.ACERO) return 0.5;
                break;
                
            case AGUA:
                if (tipoAtaque == Tipo.PLANTA || tipoAtaque == Tipo.ELECTRICO) return 2.0;
                if (tipoAtaque == Tipo.FUEGO || tipoAtaque == Tipo.TIERRA || 
                    tipoAtaque == Tipo.ROCA) return 0.5;
                break;
                
            case PLANTA:
                if (tipoAtaque == Tipo.FUEGO || tipoAtaque == Tipo.HIELO || 
                    tipoAtaque == Tipo.VENENO || tipoAtaque == Tipo.VOLADOR || 
                    tipoAtaque == Tipo.BICHO) return 2.0;
                if (tipoAtaque == Tipo.AGUA || tipoAtaque == Tipo.TIERRA || 
                    tipoAtaque == Tipo.ELECTRICO) return 0.5;
                break;
                
            // faltan agregar los otros casos
                
            default:
                return 1.0;
        }
        return 1.0;
    }
    
    
    
    @Override
    public String toString() {
        return super.toString() + " - Tipo: " + tipo;
    }
}

