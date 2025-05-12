package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MenuVsPanel extends JPanel {
    private PokemonGUI pokemonGUI;
    private JButton btnPvP;
    private JButton btnPvM;
    private JButton btnMvM;
    private JButton btnBack;
    private BufferedImage imagenFondo;
    

    private static final String RUTA_PVP = "/p vs p-Photoroom.png";
    private static final String RUTA_PVM = "/p vs m-Photoroom.png";
    private static final String RUTA_MVM = "/m vs m-Photoroom.png";
    private static final String RUTA_BACK = "/button (2).png";
    private static final String RUTA_FONDO = "/modoMenu.png";
    

    private final int ANCHO_BOTON = 200;
    private final int ALTO_BOTON = 70;
    private final int MARGEN_BOTONES = 10;

    public MenuVsPanel(PokemonGUI pokemonGUI) {
        this.pokemonGUI = pokemonGUI;
        prepareElementosVs();
        prepareAccionesVs();
    }

    
    public void prepareElementosVs() {
        try {
            imagenFondo = ImageIO.read(getClass().getResource(RUTA_FONDO));
        } catch (Exception e) {
            System.err.println("Error al cargar fondo: " + e.getMessage());
            imagenFondo = null;
        }

        setLayout(new GridBagLayout());
        setOpaque(false);
        prepararBotonesVs();
    }

    private void prepararBotonesVs() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(MARGEN_BOTONES, 0, MARGEN_BOTONES, 0);

        btnPvP = crearBotonConImagen(RUTA_PVP, "PvP");
        gbc.gridy = 0;
        add(btnPvP, gbc);

        btnPvM = crearBotonConImagen(RUTA_PVM, "PvM");
        gbc.gridy = 1;
        add(btnPvM, gbc);

        btnMvM = crearBotonConImagen(RUTA_MVM, "MvM");
        gbc.gridy = 2;
        add(btnMvM, gbc);
        
        btnBack = crearBotonConImagen(RUTA_BACK, "Volver");
        btnBack.setPreferredSize(new Dimension(ANCHO_BOTON-50, ALTO_BOTON-20));
        gbc.gridy = 3;
        add(btnBack, gbc);
    }

    private JButton crearBotonConImagen(String rutaRecurso, String texto) {
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaRecurso));
            Image imagenEscalada = icono.getImage().getScaledInstance(
                ANCHO_BOTON, ALTO_BOTON, Image.SCALE_SMOOTH);
            
            JButton boton = new JButton(new ImageIcon(imagenEscalada));
            boton.setPreferredSize(new Dimension(ANCHO_BOTON, ALTO_BOTON));
            boton.setBorderPainted(false);
            boton.setContentAreaFilled(false);
            boton.setFocusPainted(false);
            boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            boton.setHorizontalTextPosition(SwingConstants.CENTER);
            boton.setVerticalTextPosition(SwingConstants.CENTER);
            boton.setForeground(Color.WHITE);
            boton.setFont(new Font("Arial", Font.BOLD, 12));
            
            return boton;
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
            JButton boton = new JButton(texto);
            boton.setPreferredSize(new Dimension(ANCHO_BOTON, ALTO_BOTON));
            boton.setBackground(new Color(70, 70, 150));
            boton.setForeground(Color.WHITE);
            return boton;
        }
    }

    public void prepareAccionesVs() {
        btnPvP.addActionListener(e -> {
            pokemonGUI.agregarModalidad("PvP");
            pokemonGUI.mostrarMenuPvP();
           
        });
        
        btnPvM.addActionListener(e -> {
            pokemonGUI.agregarModalidad("PvM");        
            pokemonGUI.mostrarMenuPvM();
        });
        
        btnMvM.addActionListener(e -> {
            pokemonGUI.agregarModalidad("MvM");   
            pokemonGUI.mostrarMenuMvM();
        });
        
        btnBack.addActionListener(e -> {
            pokemonGUI.mostrarMenuModo();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(40, 40, 80));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}