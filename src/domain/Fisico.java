package domain;

public class Fisico extends Movimiento {
    public Fisico(String name, int potencia, int presicion, int pp, 
                 String descripcionEfecto, TipoEfecto tipoEfecto,
                 ObjetivoEfecto objetivoEfecto, int prioridad) throws PokemonException {
        super(name, potencia, presicion, pp, descripcionEfecto, 
              tipoEfecto, objetivoEfecto, prioridad);
    }
    
    @Override
    public String toString() {
        return "[Físico] " + name + " ( " + potencia + " de daño)";
    }
}

