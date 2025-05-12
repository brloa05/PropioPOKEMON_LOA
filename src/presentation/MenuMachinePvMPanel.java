package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class MenuMachinePvMPanel extends JPanel {
    private PokemonGUI pokemonGUI;
    private JButton btnIniciar, btnRegresar;
    private JButton btnExpert, btnAttacking, btnChanging, btnDefensive;
    private String tipoMaquinaSeleccionada;
    private BufferedImage imagenFondo, imagenLateral;

    private final int ANCHO_BOTON_TIPO = 120;
    private final int ALTO_BOTON_TIPO = 120;
    private final int ANCHO_BOTON_ACCION = 100;
    private final int ALTO_BOTON_ACCION = 30;

    public MenuMachinePvMPanel(PokemonGUI pokemonGUI) {
        this.pokemonGUI = pokemonGUI;
        tipoMaquinaSeleccionada = "ExpertTrainer"; // por default
        prepareElementos();
        prepareAcciones();
    }

    private void prepareElementos() {
        setLayout(new BorderLayout());

        
        try {
            URL imagenFondoUrl = getClass().getResource("/modoMenu.png");
            if (imagenFondoUrl != null) {
                imagenFondo = ImageIO.read(imagenFondoUrl);
            } 
            
            String lateralPath = "/entrenadores.png";
            File lateralFile = new File(lateralPath);
            if (lateralFile.exists()) {
                imagenLateral = ImageIO.read(lateralFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            crearImagenesDeRespaldo();
        }

        
        JPanel panelIzquierdo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenLateral != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                                       RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(imagenLateral, 0, 0, getWidth(), getHeight(), this);
                } else {
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(
                        0, 0, new Color(40, 45, 50), 
                        getWidth(), getHeight(), new Color(60, 65, 70));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.setColor(Color.WHITE);
                    g2d.drawString("Imagen lateral no disponible", 30, 30);
                }
            }
        };
        panelIzquierdo.setPreferredSize(new Dimension(250, 600)); // Reducido de 300
        add(panelIzquierdo, BorderLayout.WEST);

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setOpaque(false);
        
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

       
        JLabel titulo = new JLabel("MÁQUINA 1", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24)); // Reducido de 28
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDerecho.add(titulo);

        
        JLabel subtitulo = new JLabel("Selecciona el tipo de entrenador", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14)); // Reducido de 16
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 5)));
        panelDerecho.add(subtitulo);

        JPanel panelTiposMaquina = new JPanel(new GridLayout(2, 2, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 0));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelTiposMaquina.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        panelTiposMaquina.setOpaque(false);
        panelTiposMaquina.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnExpert = crearBotonConImagen("/ruta/expert_trainer.png", "ExpertTrainer");
        btnAttacking = crearBotonConImagen("/ruta/attacking_trainer.png", "AttackingTrainer");
        btnChanging = crearBotonConImagen("/ruta/changing_trainer.png", "ChangingTrainer");
        btnDefensive = crearBotonConImagen("/ruta/defensive_trainer.png", "DefensiveTrainer");

        panelTiposMaquina.add(btnExpert);
        panelTiposMaquina.add(btnAttacking);
        panelTiposMaquina.add(btnChanging);
        panelTiposMaquina.add(btnDefensive);

        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDerecho.add(panelTiposMaquina);

        JPanel panelBotonesAccion = new JPanel();
        panelBotonesAccion.setLayout(new BoxLayout(panelBotonesAccion, BoxLayout.X_AXIS));
        panelBotonesAccion.setOpaque(false);
        panelBotonesAccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelBotonesAccion.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        btnRegresar = crearBotonConImagen("/ruta/boton_regresar.png", "Regresar");
        btnIniciar = crearBotonConImagen("/ruta/boton_iniciar.png", "Iniciar");

        panelBotonesAccion.add(Box.createHorizontalGlue());
        panelBotonesAccion.add(btnRegresar);
        panelBotonesAccion.add(Box.createRigidArea(new Dimension(20, 0)));
        panelBotonesAccion.add(btnIniciar);
        panelBotonesAccion.add(Box.createHorizontalGlue());

        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDerecho.add(panelBotonesAccion);

        add(panelDerecho, BorderLayout.CENTER);
        
   
        resaltarBotonSeleccionado(btnExpert);
        
  
        revalidate();
        repaint();
    }
    
    private void crearImagenesDeRespaldo() {
        imagenFondo = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagenFondo.createGraphics();
        GradientPaint gp = new GradientPaint(
            0, 0, new Color(50, 60, 80), 
            800, 600, new Color(70, 80, 100));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, 800, 600);
        g2d.dispose();

        imagenLateral = new BufferedImage(250, 600, BufferedImage.TYPE_INT_RGB);
        g2d = imagenLateral.createGraphics();
        gp = new GradientPaint(
            0, 0, new Color(40, 45, 50), 
            250, 600, new Color(60, 65, 70));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, 250, 600);
        g2d.dispose();
    }
    
    private JButton crearBotonConImagen(String rutaImagen, String actionCommand) {
        try {
            BufferedImage imagenOriginal = ImageIO.read(new File(rutaImagen));
            int ancho = actionCommand.startsWith("Expert") ? ANCHO_BOTON_TIPO : ANCHO_BOTON_ACCION;
            int alto = actionCommand.startsWith("Expert") ? ALTO_BOTON_TIPO : ALTO_BOTON_ACCION;
            
            // Escalar manteniendo proporciones
            double ratio = Math.min(
                (double)ancho / imagenOriginal.getWidth(),
                (double)alto / imagenOriginal.getHeight()
            );
            int nuevoAncho = (int)(imagenOriginal.getWidth() * ratio);
            int nuevoAlto = (int)(imagenOriginal.getHeight() * ratio);
            
            Image imagenEscalada = imagenOriginal.getScaledInstance(
                nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
            
            JButton boton = new JButton(new ImageIcon(imagenEscalada));
            boton.setActionCommand(actionCommand);
            boton.setBorderPainted(false);
            boton.setContentAreaFilled(false);
            boton.setFocusPainted(false);
            boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            boton.setPreferredSize(new Dimension(nuevoAncho, nuevoAlto));
            boton.setMaximumSize(new Dimension(nuevoAncho, nuevoAlto));
            
            return boton;
        } catch (Exception e) {
            System.err.println("Error al cargar imagen del botón: " + e.getMessage());
            // Fallback: botón con texto
            JButton boton = new JButton(actionCommand);
            boton.setPreferredSize(new Dimension(
                actionCommand.startsWith("Expert") ? ANCHO_BOTON_TIPO : ANCHO_BOTON_ACCION,
                actionCommand.startsWith("Expert") ? ALTO_BOTON_TIPO : ALTO_BOTON_ACCION));
            return boton;
        }
    }

    private void resaltarBotonSeleccionado(JButton botonSeleccionado) {
        btnExpert.setBorder(null);
        btnAttacking.setBorder(null);
        btnChanging.setBorder(null);
        btnDefensive.setBorder(null);
        
        botonSeleccionado.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // Reducido de 3
    }

    private void prepareAcciones() {
        ActionListener tipoMaquinaListener = e -> {
            tipoMaquinaSeleccionada = e.getActionCommand();
            resaltarBotonSeleccionado((JButton)e.getSource());
        };
        
        btnExpert.addActionListener(tipoMaquinaListener);
        btnAttacking.addActionListener(tipoMaquinaListener);
        btnChanging.addActionListener(tipoMaquinaListener);
        btnDefensive.addActionListener(tipoMaquinaListener);

        btnIniciar.addActionListener(e -> {
            System.out.println("Máquina seleccionada: " + tipoMaquinaSeleccionada);
               pokemonGUI.comienzoAgregarEntrenadoresPorTipo1("Machine1",tipoMaquinaSeleccionada,"player2");
               pokemonGUI.agregarEntrenadores();
               pokemonGUI.mostrarMenuItemsPvMPanel();
        });

        btnRegresar.addActionListener(e -> {
            pokemonGUI.resetearEntrenador();
            pokemonGUI.mostrarMenuPvM();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}