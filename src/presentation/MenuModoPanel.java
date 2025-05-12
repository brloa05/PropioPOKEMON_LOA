package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MenuModoPanel extends JPanel {
    private PokemonGUI pokemonGUI;
    private JButton btnModoNormal;
    private JButton btnModoSupervivencia;
    private JButton btnRegresar;
    private BufferedImage imagenFondo;
    
    // Rutas de recursos
    private static final String RUTA_BOTON_NORMAL = "/Modo NormalPhotoroom.png";
    private static final String RUTA_BOTON_SUPERVIVENCIA = "/Modo SupervivenciaPhotoroom.png";
    private static final String RUTA_BOTON_REGRESAR = "/button (2).png";
    private static final String RUTA_FONDO = "/modoMenu.png";
    
    // Tamaños de botones
    private final int ANCHO_BOTON = 300;
    private final int ALTO_BOTON = 100;
    private final int ANCHO_BOTON_PEQUEÑO = 150;
    private final int ALTO_BOTON_PEQUEÑO = 50;

    public MenuModoPanel(PokemonGUI pokemonGUI) {
        this.pokemonGUI = pokemonGUI;
        prepareElementosModo();
        prepareAccionesModo();
    }

    public void prepareElementosModo() {
        try {
            imagenFondo = ImageIO.read(getClass().getResource(RUTA_FONDO));
        } catch (Exception e) {
            System.err.println("Error al cargar imagen de fondo: " + e.getMessage());
            imagenFondo = null;
        }

        setLayout(new GridBagLayout());
        setOpaque(false);
        prepareBotonesModo();
    }

    private void prepareBotonesModo() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0);

        btnModoNormal = crearBotonConImagen(RUTA_BOTON_NORMAL, ANCHO_BOTON, ALTO_BOTON);
        gbc.gridy = 0;
        add(btnModoNormal, gbc);

        btnModoSupervivencia = crearBotonConImagen(RUTA_BOTON_SUPERVIVENCIA, ANCHO_BOTON, ALTO_BOTON);
        gbc.gridy = 1;
        add(btnModoSupervivencia, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.insets = new Insets(0, 20, 20, 0);
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        btnRegresar = crearBotonConImagen(RUTA_BOTON_REGRESAR, ANCHO_BOTON_PEQUEÑO, ALTO_BOTON_PEQUEÑO);
        add(btnRegresar, gbc);
    }

    private JButton crearBotonConImagen(String rutaRecurso, int ancho, int alto) {
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaRecurso));
            Image imagenEscalada = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            
            JButton boton = new JButton(new ImageIcon(imagenEscalada));
            boton.setPreferredSize(new Dimension(ancho, alto));
            boton.setBorderPainted(false);
            boton.setContentAreaFilled(false);
            boton.setFocusPainted(false);
            boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            return boton;
        } catch (Exception e) {
            System.err.println("Error cargando " + rutaRecurso + ": " + e.getMessage());
            return crearBotonFallback(rutaRecurso, ancho, alto);
        }
    }

    private JButton crearBotonFallback(String rutaRecurso, int ancho, int alto) {
        String texto;
        if (rutaRecurso.contains("Normal")) {
            texto = "MODO NORMAL";
        } else if (rutaRecurso.contains("Supervivencia")) {
            texto = "MODO SUPERVIVENCIA";
        } else {
            texto = "REGRESAR";
        }
        
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(ancho, alto));
        boton.setBackground(texto.equals("REGRESAR") ? Color.GRAY : 
                          (texto.contains("NORMAL") ? Color.BLUE : Color.RED));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, texto.equals("REGRESAR") ? 12 : 16));
        return boton;
    }

    public void prepareAccionesModo() {
        btnModoNormal.addActionListener(e -> {
            pokemonGUI.mostrarMenuModoVs();
        });
        
        btnModoSupervivencia.addActionListener(e -> {
            JOptionPane.showMessageDialog(pokemonGUI, "Iniciando Modo Supervivencia...");
        });
        
        btnRegresar.addActionListener(e -> {
            pokemonGUI.resetearPokemonAplication();
            pokemonGUI.mostrarMenuPrincipal();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(30, 30, 70));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}