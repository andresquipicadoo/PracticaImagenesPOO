package UIImagen;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Imagenes extends JFrame {
    private JPanel contentPane;
    private List<Painter> painters;
    private Timer timer;

    //Este es el método principal para ejecutar la aplicación
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Imagenes frame = new Imagenes(); // Crea una nueva instancia de UImagenes
                    frame.setVisible(true); // Hace visible la ventana de la aplicación
                } catch (Exception e) {
                    e.printStackTrace(); // Imprime la pila de llamadas si ocurre una excepción
                }
            }
        });
    }

    public Imagenes() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configura la operación de cierre por defecto
        setBounds(100, 100, 800, 600); // Establece el tamaño y la posición de la ventana
        contentPane = new JPanel(); // Crea un nuevo panel
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Establece el borde del panel
        setContentPane(contentPane); // Establece el panel como el panel de contenido de la ventana
        contentPane.setLayout(new BorderLayout(0, 0)); // Establece el diseño del panel

        // Crea una lista de pintores
        painters = new ArrayList<>();
        painters.add(new Painter("Circles", PaintingType.CIRCLES)); // Agrega un pintor de círculos a la lista
        painters.add(new Painter("Polygons", PaintingType.POLYGONS)); // Agrega un pintor de polígonos a la lista
        painters.add(new Painter("Stripes", PaintingType.STRIPES)); // Agrega un pintor de rayas a la lista

        // Configura un temporizador para disparar a los pintores
        timer = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Painter painter : painters) { // Para cada pintor en la lista
                    painter.paintRandomImage(); // Pide que pinten una imagen aleatoria
                }
            }
        });
        timer.start(); // Inicia el temporizador
    }
}

enum PaintingType {
    CIRCLES,
    POLYGONS,
    STRIPES
}

class PaintingCanvas extends JPanel {
    private BufferedImage canvas;

    public PaintingCanvas(int width, int height) {
    	// Esta linea crea un lienzo inicial
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, this);
    }

    public void paintElement(PaintingElement element) {
        Graphics2D g2d = canvas.createGraphics();
        element.paint(g2d);
        g2d.dispose();
        repaint();
    }
}

interface PaintingElement {
    void paint(Graphics2D g2d);
}

class Painter {
    private String name;
    private PaintingType paintingType;
    private PaintingCanvas canvas;

    public Painter(String name, PaintingType paintingType) {
        this.name = name;
        this.paintingType = paintingType;
        this.canvas = new PaintingCanvas(400, 300); // Define el tamaño de la ventana individual
    }

    public void paintRandomImage() {
        // Decide qué elemento pintar basándose en el tipo de pintura del pintor
        PaintingElement element = null;
        switch (paintingType) {
            case CIRCLES:
                element = createRandomCircles(); // Crea un círculo aleatorio para pintar
                break;
            case POLYGONS:
                element = createRandomPolygons(); // Crea un polígono aleatorio para pintar
                break;
            case STRIPES:
                element = createRandomStripes(); // Crea unas rayas aleatorias para pintar
                break;
        }

        if (element != null) {
            JFrame frame = new JFrame(name); // Crea una nueva ventana para el pintor
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Configura la operación de cierre por defecto
            frame.setBounds(100, 100, 400, 300); // Establece el tamaño y la posición de la ventana
            frame.add(canvas); // Añade el lienzo del pintor a la ventana
            frame.setVisible(true); // Hace visible la ventana del pintor

            // Pinta el elemento en el lienzo del pintor
            canvas.paintElement(element);
        }
    }

    private PaintingElement createRandomCircles() {
        return g2d -> {
            Random random = new Random();
            g2d.setColor(getRandomColor()); // Establece el color del círculo
            int x = randomPosition(400); // Establece la posición x del círculo
            int y = randomPosition(300); // Establece la posición y del círculo
            int size = randomSize(10, 50); // Establece el tamaño del círculo
            g2d.fillOval(x, y, size, size); // Dibuja el círculo
        };
    }

    private PaintingElement createRandomPolygons() {
        return g2d -> {
            Random random = new Random();
            g2d.setColor(getRandomColor()); // Establece el color del polígono
            int sides = randomSides(3, 8); // Establece el número de lados del polígono
            int[] xPoints = new int[sides]; // Inicializa los puntos x del polígono
            int[] yPoints = new int[sides]; // Inicializa los puntos y del polígono

            for (int i = 0; i < sides; i++) {
                xPoints[i] = randomPosition(400); // Establece la posición x del punto
                yPoints[i] = randomPosition(300); // Establece la posición y del punto
            }

            g2d.fillPolygon(xPoints, yPoints, sides); // Dibuja el polígono
        };
    }

    private PaintingElement createRandomStripes() {
        return g2d -> {
            Random random = new Random();
            g2d.setColor(getRandomColor()); // Establece el color de las rayas
            int y = randomPosition(300); // Establece la posición y de las rayas
            int rectHeight = randomSize(10, 50); // Establece la altura del rectángulo de las rayas
            g2d.fillRect(0, y, 400, rectHeight); // Dibuja las rayas como un rectángulo vertical
        };
    }

    /**
     * Método para obtener una posición aleatoria.
     */
    private int randomPosition(int max) {
        Random random = new Random();
        return random.nextInt(max); // Devuelve un número aleatorio entre 0 y max
    }

    /**
     * Método para obtener un tamaño aleatorio.
     */
    private int randomSize(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min; // Devuelve un número aleatorio entre min y max
    }

    /**
     * Método para obtener el número de lados aleatorio de un polígono.
     */
    private int randomSides(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min; // Devuelve un número aleatorio entre min y max
    }

    /**
     * Método para obtener un color aleatorio.
     */
    private Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)); // Devuelve un color aleatorio
    }
}
