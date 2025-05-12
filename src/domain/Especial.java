package domain;

public class Especial extends Movimiento {
    public Especial(String name, int potencia, int presicion, int pp,
                   String descripcionEfecto, TipoEfecto tipoEfecto,
                   ObjetivoEfecto objetivoEfecto, int prioridad) {
        super(name, potencia, presicion, pp, descripcionEfecto,
              tipoEfecto, objetivoEfecto, prioridad);
    }
    
    @Override
    public String toString() {
        return "[Especial] " + name + " ( " + potencia + " de daño)";
    }
}
