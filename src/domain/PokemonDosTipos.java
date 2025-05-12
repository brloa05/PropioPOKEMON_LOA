package domain;

import java.util.Arrays;
import java.util.List;


/**
 * Clase que representa un Pokémon de dos tipos.
 * Hereda de la clase abstracta Pokemon.
 */
public class PokemonDosTipos extends Pokemon {
    public enum PokemonBase {
        CHARIZARD(Tipo.FUEGO, Tipo.VOLADOR, 360, 293, 280, 348, 295, 328),
        VENUSAUR(Tipo.PLANTA, Tipo.VENENO, 364, 289, 291, 328, 328, 284),
        GENGAR(Tipo.FANTASMA, Tipo.VENENO, 324, 251, 240, 394, 273, 350),
        DRAGONITE(Tipo.DRAGON, Tipo.VOLADOR, 386, 403, 317, 328, 328, 284),
        TOGETIC(Tipo.HADA, Tipo.VOLADOR, 314, 196, 295, 284, 339, 196),
        TYRANITAR(Tipo.ROCA, Tipo.SINIESTRO, 404, 403, 350, 17, 328, 243),
        GARDEVOIR(Tipo.PSIQUICO, Tipo.HADA, 340, 251, 251, 383, 361, 284),
        METAGROSS(Tipo.ACERO, Tipo.PSIQUICO, 364, 405, 394, 317, 306, 262),
        DELIBIRD(Tipo.HIELO, Tipo.VOLADOR, 294, 229, 207, 251, 207, 273);

        final Tipo tipo1;
        final Tipo tipo2;
        final int[] stats; // [saludMax, ataque, defensa, ataqueE, defensaE, velocidad]

        PokemonBase(Tipo tipo1, Tipo tipo2, int... stats) {
            this.tipo1 = tipo1;
            this.tipo2 = tipo2;
            this.stats = stats;
        }
    }

    private Tipo tipoPrimario;
    private Tipo tipoSecundario;

    /**
     * Constructor para Pokémon de dos tipos.
     * 
     * @param nombre Nombre del Pokémon
     * @param tipoPrimario Tipo principal del Pokémon
     * @param tipoSecundario Tipo secundario del Pokémon
     * @param nivel Nivel del Pokémon
     * @param saludMaxima Salud máxima del Pokémon
     * @param ataque Valor de ataque físico
     * @param defensa Valor de defensa física
     * @param ataqueEspecial Valor de ataque especial
     * @param defensaEspecial Valor de defensa especial
     * @param velocidad Valor de velocidad
     * @param movimientos Lista de movimientos del Pokémon
     */
    public PokemonDosTipos(String nombre, int nivel, List<Movimiento> movimientos) {
        super(nombre, nivel, 0, 0, 0, 0, 0, 0, movimientos);
        
        // Buscar el Pokémon por nombre
        for (PokemonBase pk : PokemonBase.values()) {
            if (pk.name().equalsIgnoreCase(nombre)) {
                this.tipoPrimario = pk.tipo1;
                this.tipoSecundario = pk.tipo2;
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
        return Arrays.asList(tipoPrimario, tipoSecundario);
    }

    @Override
    public double getEfectividad(Tipo tipoAtaque) {
        double efectividadPrimario = calcularEfectividad(tipoPrimario, tipoAtaque);
        double efectividadSecundario = calcularEfectividad(tipoSecundario, tipoAtaque);
        
        return efectividadPrimario * efectividadSecundario;
    }

    private double calcularEfectividad(Tipo tipoDefensa, Tipo tipoAtaque) {

        switch (tipoDefensa) {
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
                
            case VOLADOR:
                if (tipoAtaque == Tipo.ELECTRICO || tipoAtaque == Tipo.HIELO || 
                    tipoAtaque == Tipo.ROCA) return 2.0;
                if (tipoAtaque == Tipo.PLANTA || tipoAtaque == Tipo.LUCHA || 
                    tipoAtaque == Tipo.BICHO) return 0.5;
                if (tipoAtaque == Tipo.TIERRA) return 0.0; // Inmune
                break;
                
             // faltan agregar los otros casos
                
            default:
                return 1.0;
        }
        return 1.0;
    }
    

    public Tipo getTipoPrimario() {
        return tipoPrimario;
    }
    
    public Tipo getTipoSecundario() {
        return tipoSecundario;
    }
    
    @Override
    public String toString() {
        return super.toString() + " - Tipos: " + tipoPrimario + "/" + tipoSecundario;
    }

	
}

