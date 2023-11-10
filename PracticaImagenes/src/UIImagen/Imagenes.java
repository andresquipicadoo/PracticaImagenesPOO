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
    private List<Pintor> pintores;
    private Timer temporizador;

    //Este es el método principal para ejecutar la aplicación
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Imagenes frame = new Imagenes(); // Esta linea crea una nueva instancia de UImagenes
                    frame.setVisible(true); // Se encarga de hacer visible la ventana de la aplicación
                } catch (Exception e) {
                    e.printStackTrace(); //Se encarga de imprimir la pila de llamadas si ocurre una excepción
                }
            }
        });
    }

    public Imagenes() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configura la operación de cierre por defecto
        setBounds(100, 100, 800, 600); // Establece el tamaño y la posición de la ventana
        contentPane = new JPanel(); // Esta linea se encarga de crear un nuevo panel
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); //  Esta linea se encarga de establecer el borde del panel
        setContentPane(contentPane); // Esta linea se encarga de establecer el panel como el panel de contenido de la ventana
        contentPane.setLayout(new BorderLayout(0, 0)); // Esta linea se encarga de establecer el diseño del panel

        // Estas lineas crean una lista de pintores
        pintores = new ArrayList<>();
        pintores.add(new Pintor("Circulos", PintadoTipoFigura.CIRCULOS)); //Esta linea agrega un pintor de círculos a la lista
        pintores.add(new Pintor("Poligonos", PintadoTipoFigura.POLIGONOS)); //Esta linea agrega un pintor de polígonos a la lista
        pintores.add(new Pintor("Rayas", PintadoTipoFigura.RAYAS)); //Esta linea agrega un pintor de rayas a la lista

        //Esta linea configura un temporizador para disparar a los pintores
        temporizador = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Pintor pintor : pintores) { //  Esta linea es para cada pintor en la lista
                    pintor.pintarImagenRandom(); //  Esta linea se encarga de pedir que pinten una imagen aleatoria
                }
            }
        });
        temporizador.start(); //  Esta linea se encarga de inicializar el temporizador
    }
}

enum PintadoTipoFigura {
    CIRCULOS,
    POLIGONOS,
    RAYAS
}

class PintarCanvas extends JPanel {
    private BufferedImage canvas;

    public PintarCanvas(int ancho, int altura) {
    	// Esta linea crea un lienzo inicial
        canvas = new BufferedImage(ancho, altura, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, this);
    }

    public void pintarElemento(pintandoElemento elemento) {
        Graphics2D g2d = canvas.createGraphics();
        elemento.pintar(g2d);
        g2d.dispose();
        repaint();
    }
}

interface pintandoElemento {
    void pintar(Graphics2D g2d);
}

class Pintor {
    private String nombre;
    private PintadoTipoFigura tipoPintura;
    private PintarCanvas canvas;

    public Pintor(String nomb, PintadoTipoFigura pintadoTipoFigura) {
        this.nombre = nomb;
        this.tipoPintura = pintadoTipoFigura;
        this.canvas = new PintarCanvas(400, 300); // Esta linea se encarga de definir  el tamaño de la ventana individual
    }

    public void pintarImagenRandom() {
        // Decide qué elemento pintar basándose en el tipo de pintura del pintor
        pintandoElemento elemento = null;
        switch (tipoPintura) {
            case CIRCULOS:
                elemento = cearCirculosAleatorios(); // Esta linea crea un círculo aleatorio para pintar
                break;
            case POLIGONOS:
                elemento = crearPoligonosaleatorios(); // Esta linea crea  un polígono aleatorio para pintar
                break;
            case RAYAS:
                elemento = crearRayasAleatorias(); // Esta linea crea  unas rayas aleatorias para pintar
                break;
        }

        if (elemento != null) {
            JFrame frame = new JFrame(nombre); // Esta linea crea  una nueva ventana para el pintor
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Esta linea configura la operación de cierre por defecto
            frame.setBounds(100, 100, 400, 300); // Esta linea establece el tamaño y la posición de la ventana
            frame.add(canvas); // Esta linea añade el lienzo del pintor a la ventana
            frame.setVisible(true); // Esta linea  hace  que sea visible la ventana del pintor

            //elemento en el lienzo del pintor
            canvas.pintarElemento(elemento);
        }
    }

    /**
     * Método para generar circulos aleatorios.
     */
    private pintandoElemento cearCirculosAleatorios() {
        return g2d -> {
            Random random = new Random();
            g2d.setColor(obtenerColorAleatorio()); // Esta linea establece el color del círculo
            int x = PosicionRandom(400); // Esta linea establece la posición x del círculo
            int y = PosicionRandom(300); //Esta linea  establece la posición y del círculo
            int tam = TamRandom(10, 50); // Esta linea establece el tamaño del círculo
            g2d.fillOval(x, y, tam, tam); // Dibuja el círculo
        };
    }

    /**
     * Método para generar poligonos aleatorios.
     */
    private pintandoElemento crearPoligonosaleatorios() {
        return g2d -> {
            Random random = new Random();
            g2d.setColor(obtenerColorAleatorio()); // Esta linea establece el color del polígono
            int lados = LadosRandom(3, 8); // Esta linea estanblece el número de lados del polígono
            int[] xPoints = new int[lados]; // Esta linea inicializa los puntos x del polígono
            int[] yPoints = new int[lados]; // Esta linea inicializa los puntos y del polígono

            for (int i = 0; i < lados; i++) {
                xPoints[i] = PosicionRandom(400); //  Esta linea establece la posición x del punto
                yPoints[i] = PosicionRandom(300); //  Esta linea establece la posición y del punto
            }

            g2d.fillPolygon(xPoints, yPoints, lados); //  Esta linea dibuja el polígono
        };
    }

    /**
     * Método para generar rayas aleatorias.
     */
    private pintandoElemento crearRayasAleatorias() {
        return g2d -> {
            Random random = new Random();
            g2d.setColor(obtenerColorAleatorio()); //  Esta linea establece el color de las rayas
            int y = PosicionRandom(300); //  Esta linea establece la posición y de las rayas
            int rectHeight = TamRandom(10, 50); // Esta linea establece la altura del rectángulo de las rayas
            g2d.fillRect(0, y, 400, rectHeight); // Esta linea dibuja  las rayas como un rectángulo vertical
        };
    }

    /**
     * Método para obtener una posición aleatoria.
     */
    private int PosicionRandom(int max) {
        Random random = new Random();
        return random.nextInt(max); // Esta linea devuelve un número aleatorio entre 0 y max
    }

    /**
     * Método para obtener un tamaño aleatorio.
     */
    private int TamRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min; // Esta linea devuelve un número aleatorio entre min y max
    }

    /**
     * Método para obtener el número de lados aleatorio de un polígono.
     */
    private int LadosRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min; // Esta linea devuelve un número aleatorio entre min y max
    }

    /**
     * Método para obtener un color aleatorio.
     */
    private Color obtenerColorAleatorio() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)); // Devuelve un color aleatorio
    }
}
