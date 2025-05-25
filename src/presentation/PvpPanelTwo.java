package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class PvpPanelTwo extends JPanel {
    private PokemonGUI pokemonGUI;
    private JTextField nombreJugador;
    private JButton btnSiguiente;
    private JButton btnRegresar;
    private JButton btnRegresarInferior;
    private JButton btnSeleccionarColor;
    private JLabel lblColor;
    private Color colorJugador;
    private BufferedImage imagenFondo;
    private BufferedImage imagenLateral;

    public PvpPanelTwo(PokemonGUI pokemonGUI) {
        this.pokemonGUI = pokemonGUI;
        this.colorJugador = Color.RED; 
        prepareElementos();
        prepareAccionesPvP();
    }
    private void prepareElementos() {
    	setLayout(new BorderLayout());


        // 1. Carga de imágenes (código existente se mantiene igual)
    	try {
	        imagenFondo = ImageIO.read(getClass().getResource("/modoMenu.png"));
	        imagenLateral = ImageIO.read(getClass().getResource("/entrenadores.png"));
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, 
	            "Error al cargar imágenes:\n" + e.getMessage(), 
	            "Error", JOptionPane.ERROR_MESSAGE);
	        crearImagenesDeRespaldo();
	    }


        
        JPanel panelIzquierdo = new JPanel(new BorderLayout()) { 
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenLateral != null) {
                    g.drawImage(imagenLateral.getScaledInstance(
                        getWidth(), getHeight(), Image.SCALE_SMOOTH), 
                        0, 0, this);
                } else {
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(
                        0, 0, new Color(40, 40, 50), 
                        0, getHeight(), new Color(60, 60, 70));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.setColor(Color.WHITE);
                    g2d.drawString("Imagen lateral no disponible", 20, 30);
                }
            }
        };
        panelIzquierdo.setPreferredSize(new Dimension(250, 600));

      
        btnRegresarInferior = new JButton("← Regresar");
        btnRegresarInferior.setContentAreaFilled(false);
        btnRegresarInferior.setForeground(Color.WHITE);
        btnRegresarInferior.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegresarInferior.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnRegresarInferior.setFocusPainted(false);
        
       
        btnRegresarInferior.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRegresarInferior.setForeground(new Color(200, 200, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnRegresarInferior.setForeground(Color.WHITE);
            }
        });

        JPanel panelDerecho = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(60, 70, 90));
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.RED);
                    g.drawString("No se pudo cargar la imagen de fondo", 50, 50);
                }
            }
        };
        panelDerecho.setOpaque(false); 

        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);

        
        JLabel titulo = new JLabel("Jugador 2", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(new Color(255, 255, 255, 220)); // Blanco semi-transparente
        gbc.gridy = 0;
        panelDerecho.add(titulo, gbc);

        
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(new Color(255, 255, 255, 200));
        gbc.gridy = 1;
        panelDerecho.add(lblNombre, gbc);

        nombreJugador = new JTextField(20);
        nombreJugador.setPreferredSize(new Dimension(180, 25));
        nombreJugador.setOpaque(false);
        nombreJugador.setForeground(Color.WHITE);
        nombreJugador.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100)));
        gbc.gridy = 2;
        panelDerecho.add(nombreJugador, gbc);

    
        JLabel lblSeleccionColor = new JLabel("Color del Jugador:");
        lblSeleccionColor.setForeground(new Color(255, 255, 255, 200));
        gbc.gridy = 3;
        panelDerecho.add(lblSeleccionColor, gbc);

        lblColor = new JLabel();
        lblColor.setOpaque(true);
        lblColor.setBackground(colorJugador);
        lblColor.setPreferredSize(new Dimension(100, 30));
        lblColor.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        gbc.gridy = 4;
        panelDerecho.add(lblColor, gbc);

        btnSeleccionarColor = new JButton("Elegir Color");
        btnSeleccionarColor.setContentAreaFilled(false);
        btnSeleccionarColor.setForeground(Color.WHITE);
        btnSeleccionarColor.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        gbc.gridy = 5;
        panelDerecho.add(btnSeleccionarColor, gbc);

        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
              
                g.setColor(new Color(0, 0, 0, 0));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelBotones.setOpaque(false);

        btnSiguiente = new JButton("Siguiente");
        btnRegresar = new JButton("Regresar");
        
        // Estilo para los botones
        for (JButton btn : new JButton[]{btnSiguiente, btnRegresar}) {
            btn.setContentAreaFilled(false);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            btn.setPreferredSize(new Dimension(100, 30));
            btn.setFont(new Font("Arial", Font.PLAIN, 12));

        }

        panelBotones.add(btnRegresar);
        panelBotones.add(btnSiguiente);

        gbc.gridy = 6;
        panelDerecho.add(panelBotones, gbc);

        add(panelDerecho, BorderLayout.CENTER);

        
        // 8. Forzar redibujado
        revalidate();
        repaint();
    }

  
    private void crearImagenesDeRespaldo() {
       
        imagenFondo = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagenFondo.createGraphics();
        GradientPaint gp = new GradientPaint(
            0, 0, new Color(50, 60, 80), 
            800, 600, new Color(70, 90, 110));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, 800, 600);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        String texto = "Modo PvP - Imagen no disponible";
        int x = (800 - g2d.getFontMetrics().stringWidth(texto)) / 2;
        g2d.drawString(texto, x, 300);
        g2d.dispose();

      
        imagenLateral = new BufferedImage(300, 600, BufferedImage.TYPE_INT_RGB);
        g2d = imagenLateral.createGraphics();
        g2d.setColor(new Color(40, 45, 50));
        g2d.fillRect(0, 0, 300, 600);
        g2d.dispose();
    }
    public void prepareAccionesPvP() {
    	
    	btnSiguiente.addActionListener(e -> {
    	    String nombre = nombreJugador.getText().trim();
    	    if (nombre.isEmpty()) {
    	        JOptionPane.showMessageDialog(this, "Por favor ingresa un nombre", "Error", JOptionPane.ERROR_MESSAGE);
    	        return;
    	    }

    	    try {
    	        // Verificar nombre único
    	        String nombreJugador1 = pokemonGUI.getNameEntrenador1();
    	        if (nombre.equalsIgnoreCase(nombreJugador1)) {
    	            throw new IllegalArgumentException("El nombre no puede ser igual al del primer jugador");
    	        }
    	        
    	        // Establecer como jugador 2 antes de continuar
    	        pokemonGUI.setCurrentPlayer("player2");
    	        pokemonGUI.comienzoAgregarEntrenadoresPorTipo1(nombre, "PlayerTrainer", "player2");
    	        pokemonGUI.agregarEntrenadores();
    	        pokemonGUI.mostrarMenuItemsTwo();  // Cambiado a mostrarMenuItemsTwo()
    	    } catch (Exception ex) {
    	        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    	        nombreJugador.setText("");
    	        nombreJugador.requestFocus();
    	    }
    	});

        btnRegresar.addActionListener(e -> {
        	pokemonGUI.resetearEntrenador();
            pokemonGUI.mostrarMenuPvP();
        });
        
        
      
        btnSeleccionarColor.addActionListener(e -> {
            Color nuevoColor = JColorChooser.showDialog(
                this,
                "Selecciona un color para el jugador",
                colorJugador);
            if (nuevoColor != null) {
                colorJugador = nuevoColor;
                lblColor.setBackground(colorJugador);
            }
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
