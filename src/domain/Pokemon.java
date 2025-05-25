package domain;


import java.util.ArrayList;
import java.util.List;
public abstract class Pokemon {
	
	public enum Tipo {
        FUEGO, AGUA, PLANTA, ELECTRICO, VOLADOR, VENENO,
        FANTASMA, DRAGON, HADA, ROCA, SINIESTRO, PSIQUICO,
        NORMAL, ACERO, TIERRA, LUCHA, HIELO, BICHO
    }
	
    private String nombre;
    protected int nivel;
    protected int salud;
    protected int saludMaxima;
    protected int ataque;
    protected int defensa;
    protected int ataqueEspecial;
    protected int defensaEspecial;
    protected int velocidad;
    private String estado;
    protected int  velocidadOriginal;
    protected List<Movimiento> movimientos;

    
    /**
     * Constructor de la clase base Pokémon.
     * 
     * @param nombre Nombre del Pokémon
     * @param nivel Nivel del Pokémon
     * @param saludMaxima Salud máxima del Pokémon
     * @param ataque Valor de ataque físico
     * @param defensa Valor de defensa física
     * @param ataqueEspecial Valor de ataque especial
     * @param defensaEspecial Valor de defensa especial
     * @param velocidad Valor de velocidad
     * @param movimientos 
     * @param movimientos Lista de movimientos del Pokémon
     */
    
    public Pokemon(String nombre, int nivel, int saludMaxima, 
            int ataque, int defensa, int ataqueEspecial,
            int defensaEspecial, int velocidad, List<Movimiento> movimientos) {
	  this.nombre = nombre;
	  this.nivel = nivel;
	  this.saludMaxima = saludMaxima;
	  this.salud = saludMaxima;
	  this.ataque = ataque;
	  this.defensa = defensa;
	  this.ataqueEspecial = ataqueEspecial;
	  this.defensaEspecial = defensaEspecial;
	  this.velocidad = velocidad;
	  this.velocidadOriginal = velocidad;
	  this.estado = "NORMAL";
	  this.movimientos = movimientos;
	}

    
    /**
     * Obtiene los tipos del Pokémon.
     * @return Lista de tipos del Pokémon
     */
    public abstract List<Tipo> getTipos();
    
    /**
     * Calcula la efectividad de un tipo de ataque contra este Pokémon.
     * @param tipoAtaque Tipo del movimiento atacante
     * @return Multiplicador de efectividad (2.0 para super efectivo, 0.5 para poco efectivo, etc.)
     */
    public abstract double getEfectividad(Tipo tipoAtaque);

    
    /**
     * Reduce la salud del Pokémon según el daño recibido.
     * @param  Cantidad de daño a recibir
     */
    public void recibirDanio(int danio) {
        this.salud = Math.max(0, this.salud - danio);
    }
    
    /**
     * Restaura salud al Pokémon.
     * @param puntos Cantidad de salud a restaurar
     */
    public void curar(int puntos) {
        this.salud = Math.min(this.saludMaxima, this.salud + puntos);
    }
    
    /**
     * Verifica si el Pokémon está debilitado.
     * @return true si la salud es <= 0, false en caso contrario
     */
    public boolean estaDebilitado() {
        return this.salud <= 0;
    }
    
    public int getSalud() {
    	return salud;
    }
    
    
    public String getNombre() {
        return nombre;
    }

    public int getNivel() {
        return nivel;
    }
    
    public void setSalud(int salud) {
    	this.salud = salud;
    }
    
    public int getSaludMaxima() {
        return saludMaxima;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getAtaqueEspecial() {
        return ataqueEspecial;
    }

    public int getDefensaEspecial() {
        return defensaEspecial;
    }

    public int getVelocidad() {
        return velocidad;
    }

    
    public void modificarVelocidad(int cambio) {
        // Guardar velocidad original 
        if (this.velocidadOriginal == 0) {
            this.velocidadOriginal = this.velocidad;
        }
        this.velocidad = Math.max(1, this.velocidad + cambio);
        System.out.println(nombre + ": Velocidad modificada en " + cambio);
    }

    public void modificarAtaque(int cambio) {
        this.ataque = Math.max(1, this.ataque + cambio);
        System.out.println(nombre + ": Ataque modificado en " + cambio);
    }
    
    public void modificarAtaqueEspecial(int cambio) {
        this.ataqueEspecial = Math.max(1, this.ataqueEspecial + cambio);
        System.out.println(nombre + ": Ataque especial modificado en " + cambio);
    }

    public void modificarDefensa(int cambio) {
        this.defensa = Math.max(1, this.defensa + cambio);
        System.out.println(nombre + ": Defensa modificada en " + cambio);
    }
    
    public void modificarDefensaEspecial(int cambio) {
        this.defensaEspecial = Math.max(1, this.defensaEspecial + cambio);
        System.out.println(nombre + ": Defensa especial modificada en " + cambio);
    }

    public void aplicarEstado(String estado) {
        String estadoAnterior = this.estado;
        this.estado = estado;
        
        if ("PARALIZADO".equals(estado) && !"PARALIZADO".equals(estadoAnterior)) {
            this.velocidadOriginal = this.velocidad; // Guardar velocidad actual
            this.velocidad = Math.max(1, this.velocidad / 4); // Reducir al 25%
            System.out.println(nombre + ": ¡Velocidad reducida por parálisis!");
        }
        
        System.out.println(nombre + ": Estado cambiado a " + estado);
    }
    
    public boolean verificarParalisis() {
        if ("PARALIZADO".equals(estado)) {
            if (Math.random() < 0.25) { // 25% de probabilidad de no atacar
                System.out.println(nombre + " está paralizado y no puede moverse!");
                return false;
            }
        }
        return true;
    }

    public void recibirDano(int dano) {
        this.salud = Math.max(0, this.salud - dano);
        System.out.println(nombre + " recibió " + dano + " de daño");
    }
    
    public void resetearEstadisticas() {

        System.out.println(nombre + ": Estadísticas reseteadas a valores base");

    }
    
    public void aumentarProbabilidadCritico() {
        System.out.println(nombre + ": Probabilidad de golpe crítico aumentada");

    }
    
    public void recibirDanioRetroceso(int danioCausado) {
        int danioRetroceso = danioCausado / 4; // 25% del daño causado
        this.salud = Math.max(0, this.salud - danioRetroceso);
        System.out.println(nombre + ": Recibe " + danioRetroceso + " PS por retroceso");
    }
    
    public boolean aplicarDanoPorEstado() {
        if ("QUEMADO".equals(estado) || "ENVENENADO".equals(estado)) {
            int dano = 25; // Daño por turno       
            salud = Math.max(0, salud - dano);
            System.out.println(nombre + " sufre " + dano + " de daño por estar " + estado);
            
            if (salud == 0) {
                System.out.println(nombre + " ha sido debilitado!");
                return false;
            }
        }
        return true;
    }
    
    public boolean puedeAtacar(Movimiento movimiento) {
        if ("CONGELADO".equals(estado)) {
            // Verificar si el movimiento es de tipo fuego
            if (movimiento instanceof Fisico || movimiento instanceof Especial || movimiento instanceof DeEstado) {
                for (Tipo tipo : this.getTipos()) {
                    if (tipo == Tipo.FUEGO) {
                        this.estado = "NORMAL";
                        System.out.println(nombre + " se ha descongelado al usar un movimiento de fuego!");
                        return true;
                    }
                }
            }
            
            // Probabilidad de descongelarse (30%)
            if (Math.random() < 0.30) {
                this.estado = "NORMAL";
                System.out.println(nombre + " se ha descongelado!");
                return true;
            }
            
            System.out.println(nombre + " está congelado y no puede moverse!");
            return false;
        }
        return true;
    }
    
    public boolean verificarEstado() {
        if ("DORMIDO".equals(estado) || "CONGELADO".equals(estado) || "PARALIZADO".equals(estado)) {

            if ("DORMIDO".equals(estado)) {

                return false;
            }
            if ("PARALIZADO".equals(estado)) {

                return Math.random() > 0.25; // 75% de probabilidad de moverse
            }
        }
        return true;
    }
    
    public void setMovimientos(List<Movimiento> movimientos) {
    	this.movimientos = movimientos;
    }
    
    public boolean intentarAtaque(Pokemon atacante, Movimiento movimiento, Pokemon objetivo) {

        if (!atacante.verificarEstado()) {
            return false;
        }
        

        if (!atacante.puedeAtacar(movimiento)) {
            return false;
        }
        

        return true;
    }
    
    /**
     * Obtiene la lista de movimientos del Pokémon.
     * Si no hay movimientos asignados, devuelve una lista vacía en lugar de null.
     * @return Lista de movimientos.
     */
    public List<Movimiento> getMovimientos() {
        if (this.movimientos == null) {
            return new ArrayList<>(); 
        }
        return this.movimientos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || !(o instanceof Pokemon)) return false; 
        Pokemon pokemon = (Pokemon) o;

        if (nombre != null ? !nombre.equals(pokemon.nombre) : pokemon.nombre != null) return false;

        return true;
    }

    @Override
    public int hashCode() {

        int result = nombre != null ? nombre.hashCode() : 0;

        return result;
    }
    
    @Override
    public String toString() {
        return nombre + " (Nivel " + nivel + ") - PS: " + salud + "/" + saludMaxima;
    }


	


	
}
