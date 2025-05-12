package domain;

public class DeEstado extends Movimiento {
    public DeEstado(String name, int potencia, int presicion, int pp,
                   String descripcionEfecto, TipoEfecto tipoEfecto,
                   ObjetivoEfecto objetivoEfecto, int prioridad) {
        super(name, potencia, presicion, pp, descripcionEfecto,
              tipoEfecto, objetivoEfecto, prioridad);
    }
    
}
