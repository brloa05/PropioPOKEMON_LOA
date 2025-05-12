package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class MenuItemsPanel extends JPanel {
    private PokemonGUI pokemonGUI;
    private Map<String, Integer> itemsSeleccionados;
    private JLabel contadorLabel;
    
    private JButton btnPotion, btnSuperPotion, btnHyperPotion, btnRevive;
    private JButton btnRegresar, btnSiguiente, btnResetear;
    
    private BufferedImage imagenFondo;
    private ImageIcon iconPotion, iconSuperPotion, iconHyperPotion, iconRevive;
    
    private final int MAX_POCIONES = 2;
    private final int MAX_REVIVE = 1;
    private final int MAX_ITEMS_TOTALES = 3; 
    private final int MAX_POCIONES_COMBINADAS = 2;

    private final String RUTA_BASE = "/";
    private final String RUTA_POTION = RUTA_BASE + "potion.png";
    private final String RUTA_HYPER_POTION = RUTA_BASE + "hyperPotion.png";
    private final String RUTA_SUPER_POTION = RUTA_BASE + "superPotion.png";
    private final String RUTA_REVIVE = RUTA_BASE + "reivir.png";
    private final String RUTA_FONDO = RUTA_BASE + "modoMenu.png";

    public MenuItemsPanel(PokemonGUI pokemonGUI) {
        this.pokemonGUI = pokemonGUI;
        this.itemsSeleccionados = new HashMap<>();
        itemsSeleccionados.put("Potion", 0);
        itemsSeleccionados.put("SuperPotion", 0);
        itemsSeleccionados.put("HyperPotion", 0);
        itemsSeleccionados.put("Revive", 0);
        
        prepareElementosMenu();
        prepareAccionesMenu();
    }

    public void prepareElementosMenu() {
        setLayout(new BorderLayout());
        
        try {
        	imagenFondo = ImageIO.read(getClass().getResource(RUTA_FONDO));
            iconPotion = new ImageIcon(getClass().getResource(RUTA_POTION));
            iconSuperPotion = new ImageIcon(getClass().getResource(RUTA_SUPER_POTION));
            iconHyperPotion = new ImageIcon(getClass().getResource(RUTA_HYPER_POTION));
            iconRevive = new ImageIcon(getClass().getResource(RUTA_REVIVE));
            
            iconPotion = escalarIcono(iconPotion, 100, 100);
            iconSuperPotion = escalarIcono(iconSuperPotion, 100, 100);
            iconHyperPotion = escalarIcono(iconHyperPotion, 100, 100);
            iconRevive = escalarIcono(iconRevive, 100, 100);
        } catch (IOException e) {
            e.printStackTrace();
            crearImagenesDeRespaldo();
            JOptionPane.showMessageDialog(this, 
                "Algunas imágenes no se cargaron correctamente. Se usarán imágenes de respaldo.", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

     // Crear los botones primero
        btnPotion = crearBotonItem(iconPotion, "Potion");
        btnSuperPotion = crearBotonItem(iconSuperPotion, "SuperPotion");
        btnHyperPotion = crearBotonItem(iconHyperPotion, "HyperPotion");
        btnRevive = crearBotonItem(iconRevive, "Revive");

        // Panel principal sin scroll
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(50, 50, 70));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel para el título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setOpaque(false);
        JLabel titulo = new JLabel("SELECCIÓN DE ITEMS PLAYER 1", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        panelTitulo.add(titulo);
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);

        // Panel para los items con GridLayout dentro de un JScrollPane
        JPanel panelItems = new JPanel(new GridLayout(0, 4, 20, 20));
        panelItems.setOpaque(false);
        
        // Solo añadir los botones reales
        panelItems.add(btnPotion);
        panelItems.add(btnSuperPotion);
        panelItems.add(btnHyperPotion);
        panelItems.add(btnRevive);
        
        // Añadir más items de prueba (puedes quitarlos después)
        for (int i = 0; i < 12; i++) {
            JButton dummyBtn = new JButton("Item " + (i+1));
            dummyBtn.setPreferredSize(new Dimension(120, 120));
            panelItems.add(dummyBtn);
        }

        // ScrollPane solo para los items
        JScrollPane scrollItems = new JScrollPane(panelItems);
        scrollItems.setOpaque(false);
        scrollItems.getViewport().setOpaque(false);
        scrollItems.setBorder(BorderFactory.createEmptyBorder());
        scrollItems.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollItems.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollItems.getVerticalScrollBar().setUnitIncrement(16);
        
        // Panel para el contador
        JPanel panelContador = new JPanel();
        panelContador.setOpaque(false);
        contadorLabel = new JLabel("Items seleccionados: 0/7", SwingConstants.CENTER);
        contadorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contadorLabel.setForeground(Color.WHITE);
        panelContador.add(contadorLabel);

        // Panel para los botones de acción
        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotonesAccion.setOpaque(false);
        
        btnRegresar = new JButton("Regresar");
        btnSiguiente = new JButton("Siguiente");
        btnResetear = new JButton("Resetear");
        
        for (JButton btn : new JButton[]{btnRegresar, btnSiguiente, btnResetear}) {
            btn.setPreferredSize(new Dimension(120, 40));
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setBackground(new Color(70, 70, 90));
            btn.setForeground(Color.WHITE);
        }
        
        panelBotonesAccion.add(btnRegresar);
        panelBotonesAccion.add(btnResetear);
        panelBotonesAccion.add(btnSiguiente);

        // Panel sur para contador y botones
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.add(panelContador, BorderLayout.NORTH);
        panelSur.add(panelBotonesAccion, BorderLayout.SOUTH);
        panelSur.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Añadir componentes al panel principal
        panelPrincipal.add(scrollItems, BorderLayout.CENTER);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        add(panelPrincipal, BorderLayout.CENTER);
    }


    private void crearImagenesDeRespaldo() {
        imagenFondo = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagenFondo.createGraphics();
        g2d.setColor(new Color(50, 50, 70));
        g2d.fillRect(0, 0, 800, 600);
        g2d.dispose();
        
        iconPotion = crearIconoRespaldo(Color.RED, "P");
        iconSuperPotion = crearIconoRespaldo(Color.BLUE, "S");
        iconHyperPotion = crearIconoRespaldo(Color.GREEN, "H");
        iconRevive = crearIconoRespaldo(Color.YELLOW, "R");
    }
    
    private ImageIcon crearIconoRespaldo(Color color, String texto) {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        g2d.setColor(color.darker());
        g2d.fillOval(5, 5, 90, 90);
        
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(5, 5, 90, 90);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (100 - fm.stringWidth(texto)) / 2;
        int y = ((100 - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(texto, x, y);
        
        g2d.dispose();
        return new ImageIcon(img);
    }
    
    private JButton crearBotonItem(ImageIcon icono, String tipoItem) {
        JButton boton = new JButton();
        
        if (icono != null) {
            boton.setIcon(icono);
        } else {
            boton.setText(tipoItem);
            boton.setFont(new Font("Arial", Font.BOLD, 14));
            boton.setForeground(Color.WHITE);
            boton.setBackground(new Color(70, 70, 90));
        }
        
        boton.setActionCommand(tipoItem);
        boton.setToolTipText(tipoItem);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(icono == null); 
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(120, 120));
        
        return boton;
    }
    
    private ImageIcon escalarIcono(ImageIcon icono, int ancho, int alto) {
        if (icono == null) return null;
        Image imagen = icono.getImage();
        Image nuevaImagen = imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(nuevaImagen);
    }
    
    private void actualizarContador() {
        int totalPociones = itemsSeleccionados.get("Potion") + 
                          itemsSeleccionados.get("SuperPotion") + 
                          itemsSeleccionados.get("HyperPotion");
        int totalRevives = itemsSeleccionados.get("Revive");
        int totalGeneral = totalPociones + totalRevives;
        
        contadorLabel.setText(String.format("Items seleccionados: %d/3 (Pociones: %d/2, Revives: %d/1)", 
            totalGeneral, totalPociones, totalRevives));
        
        // Habilitar/deshabilitar botones según las restricciones
        boolean puedeMasPociones = totalPociones < MAX_POCIONES_COMBINADAS;
        boolean puedeMasRevives = totalRevives < MAX_REVIVE;
        
        btnPotion.setEnabled(puedeMasPociones);
        btnSuperPotion.setEnabled(puedeMasPociones);
        btnHyperPotion.setEnabled(puedeMasPociones);
        btnRevive.setEnabled(puedeMasRevives && totalGeneral < MAX_ITEMS_TOTALES);
        
        btnSiguiente.setEnabled(totalGeneral > 0);
    }

    public void prepareAccionesMenu() {
    	ActionListener itemListener = e -> {
            String tipoItem = e.getActionCommand();
            int totalPociones = itemsSeleccionados.get("Potion") + 
                              itemsSeleccionados.get("SuperPotion") + 
                              itemsSeleccionados.get("HyperPotion");
            int totalRevives = itemsSeleccionados.get("Revive");
            int totalGeneral = totalPociones + totalRevives;
            
            // Verificar restricciones antes de agregar
            if (tipoItem.equals("Revive")) {
                if (totalRevives < MAX_REVIVE && totalGeneral < MAX_ITEMS_TOTALES) {
                    itemsSeleccionados.put(tipoItem, totalRevives + 1);
                }
            } else {
                if (totalPociones < MAX_POCIONES_COMBINADAS && totalGeneral < MAX_ITEMS_TOTALES) {
                    itemsSeleccionados.put(tipoItem, itemsSeleccionados.get(tipoItem) + 1);
                }
            }
            actualizarContador();
        };
        
        btnPotion.addActionListener(itemListener);
        btnSuperPotion.addActionListener(itemListener);
        btnHyperPotion.addActionListener(itemListener);
        btnRevive.addActionListener(itemListener);

        btnResetear.addActionListener(e -> {
            itemsSeleccionados.replaceAll((k, v) -> 0);
            actualizarContador();
        });

        btnRegresar.addActionListener(e -> {
            itemsSeleccionados.replaceAll((k, v) -> 0);
            actualizarContador();
            pokemonGUI.resetearEntrenadorTwo();
            pokemonGUI.mostrarMenuPvPTwo();
        });

        btnSiguiente.addActionListener(e -> {
            int totalPociones = itemsSeleccionados.get("Potion") + 
                              itemsSeleccionados.get("SuperPotion") + 
                              itemsSeleccionados.get("HyperPotion");
            int totalRevives = itemsSeleccionados.get("Revive");
            int totalGeneral = totalPociones + totalRevives;
            
            if (totalPociones > MAX_POCIONES_COMBINADAS) {
                JOptionPane.showMessageDialog(this, 
                    "No puedes llevar más de 2 pociones combinadas", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (totalRevives > MAX_REVIVE) {
                JOptionPane.showMessageDialog(this, 
                    "No puedes llevar más de 1 Revive", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (totalGeneral > MAX_ITEMS_TOTALES) {
                JOptionPane.showMessageDialog(this, 
                    "No puedes llevar más de 3 ítems en total (2 pociones + 1 revive)", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            pokemonGUI.comienzoAgregarItems1(itemsSeleccionados, "player1");
            pokemonGUI.mostrarMenuItemsTwo();
        });
    
    }
}