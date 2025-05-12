package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class MenuPanel extends JPanel {
    private PokemonGUI pokemonGUI;
    private JButton btnNuevoJuego;
    private JButton btnSeguirJugando;
    

    private final int ANCHO_BOTON = 300;
    private final int ALTO_BOTON = 100;
    

    private static final String RUTA_NUEVO_JUEGO = "/Nuevo Juego-Photoroom.png";
    private static final String RUTA_SEGUIR_JUGANDO = "/Seguir Jugando-Photoroom.png";
    private static final String RUTA_FONDO = "/pokémon-emerald-remake-title-screen-wallpaper-mobile-desktop-v0-wyy5o7rqhp4a1.png";
    
    public MenuPanel(PokemonGUI jFramePadre) {
        this.pokemonGUI = jFramePadre;
        prepareElementosMenu();
        prepareAccionesMenu();
    }
    
    public void prepareElementosMenu() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        prepareBotonesMenu();
    }
    
    public void prepareAccionesMenu() {
        btnNuevoJuego.addActionListener(e -> {
            pokemonGUI.iniciarNuevaAplicacion();
            pokemonGUI.mostrarMenuModo();
        });
        
        btnSeguirJugando.addActionListener(e -> {
            JOptionPane.showMessageDialog(pokemonGUI, "Cargando partida existente...");
        });
    }
    
    public void prepareBotonesMenu() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0);
        
        btnNuevoJuego = crearBotonConImagen(RUTA_NUEVO_JUEGO, ANCHO_BOTON, ALTO_BOTON);
        gbc.gridy = 0;
        add(btnNuevoJuego, gbc);
        
        btnSeguirJugando = crearBotonConImagen(RUTA_SEGUIR_JUGANDO, ANCHO_BOTON, ALTO_BOTON);
        gbc.gridy = 1;
        add(btnSeguirJugando, gbc);
    }
    
    private JButton crearBotonConImagen(String rutaRecurso, int ancho, int alto) {
        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(rutaRecurso));
            Image imagenEscalada = iconoOriginal.getImage()
                                  .getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            
            JButton boton = new JButton(new ImageIcon(imagenEscalada));
            boton.setBorderPainted(false);
            boton.setContentAreaFilled(false);
            boton.setFocusPainted(false);
            boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            boton.setPreferredSize(new Dimension(ancho, alto));
            
            return boton;
        } catch (Exception e) {
            JButton boton = new JButton("Texto Botón");
            boton.setPreferredSize(new Dimension(ancho, alto));
            System.err.println("Error cargando imagen: " + e.getMessage());
            return boton;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon imagenFondo = new ImageIcon(getClass().getResource(RUTA_FONDO));
        Image img = imagenFondo.getImage();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}