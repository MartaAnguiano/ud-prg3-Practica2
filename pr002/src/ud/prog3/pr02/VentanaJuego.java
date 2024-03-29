package ud.prog3.pr02;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

/** Clase principal de minijuego de coche para Pr�ctica 02 - Prog III
 * Ventana del minijuego.
 * @author Andoni Egu�luz
 * Facultad de Ingenier�a - Universidad de Deusto (2014)
 */
public class VentanaJuego extends JFrame {
	private static final long serialVersionUID = 1L;  // Para serializaci�n
	JPanel pPrincipal, pMensaje;      // Panel del juego (layout nulo)
	MundoJuego miMundo;        // Mundo del juego
	CocheJuego miCoche;        // Coche del juego
	MiRunnable miHilo = null;  // Hilo del bucle principal de juego	
	boolean aBooleanos [];
	JLabel lmensaje,lmensaje2;
	
	
	/** Constructor de la ventana de juego. Crea y devuelve la ventana inicializada
	 * sin coches dentro
	 */
	public VentanaJuego() {
		
		//Se crea el array de booleanos
		aBooleanos = new boolean[4];
		//Se inicializa a false
		for(int i=0;i<aBooleanos.length; i++)
			aBooleanos[i]=false;
		// Liberaci�n de la ventana por defecto al cerrar
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		// Creaci�n contenedores y componentes
		pPrincipal = new JPanel();
		 //pMensaje = new JPanel();
		 lmensaje=new JLabel();
		 lmensaje.setVisible(true);
		 lmensaje2=new JLabel();
		 lmensaje2.setVisible(true);
		// pMensaje.add(lmensaje);
		 
		JPanel pBotonera = new JPanel();
		//JButton bAcelerar = new JButton( "Acelera" );
		//JButton bFrenar = new JButton( "Frena" );
		//JButton bGiraIzq = new JButton( "Gira Izq." );
		//JButton bGiraDer = new JButton( "Gira Der." );
		// Formato y layouts
		pPrincipal.setLayout( null );
		pPrincipal.setBackground( Color.white );
		// A�adido de componentes a contenedores
		add( pPrincipal, BorderLayout.CENTER );
		
		//pBotonera.add( bAcelerar );
		//pBotonera.add( bFrenar );
		//pBotonera.add( bGiraIzq );
		//pBotonera.add( bGiraDer );
		pBotonera.add(lmensaje);
		pBotonera.add(lmensaje2);

		add( pBotonera, BorderLayout.SOUTH );
		// Formato de ventana
		setSize( 1000, 750);
		setResizable( false );
		// Escuchadores de botones
		//	bAcelerar.addActionListener( new ActionListener() {
			//@Override
//			public void actionPerformed(ActionEvent e) {
//				miCoche.acelera( +10, 1 );
//				// System.out.println( "Nueva velocidad de coche: " + miCoche.getVelocidad() );
//			}
//		});
//		bFrenar.addActionListener( new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				miCoche.acelera( -10, 1 );
//				// System.out.println( "Nueva velocidad de coche: " + miCoche.getVelocidad() );
//			}
//		});
//		bGiraIzq.addActionListener( new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				miCoche.gira( +10 );
//				// System.out.println( "Nueva direcci�n de coche: " + miCoche.getDireccionActual() );
//			}
//		});
//		bGiraDer.addActionListener( new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				miCoche.gira( -10 );
//				// System.out.println( "Nueva direcci�n de coche: " + miCoche.getDireccionActual() );
//			}
//		});
		
		// A�adido para que tambi�n se gestione por teclado con el KeyListener
		pPrincipal.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP: {
					//	miCoche.acelera( +5, 1 );
						
						aBooleanos[1]=true;
						break;
					}
					case KeyEvent.VK_DOWN: {
						//miCoche.acelera( -5, 1 );
						aBooleanos[2]=true;
						break;
					}
					case KeyEvent.VK_LEFT: {
						//miCoche.gira( +10 );
						aBooleanos[0]=true;
						break;
					}
					case KeyEvent.VK_RIGHT: {
						//miCoche.gira( -10 );
						aBooleanos[3]=true;
						break;
					}
				}
			}
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP: {
					//	miCoche.acelera( +5, 1 );
						aBooleanos[1]=false;
						break;
					}
					case KeyEvent.VK_DOWN: {
						//miCoche.acelera( -5, 1 );
						aBooleanos[2]=false;
						break;
					}
					case KeyEvent.VK_LEFT: {
						//miCoche.gira( +10 );
						aBooleanos[0]=false;
						break;
					}
					case KeyEvent.VK_RIGHT: {
						//miCoche.gira( -10 );
						aBooleanos[3]=false;
						break;
					}
				}
			}
		});
		pPrincipal.setFocusable(true);
		pPrincipal.requestFocus();
		pPrincipal.addFocusListener( new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				pPrincipal.requestFocus();
			}
		});
		// Cierre del hilo al cierre de la ventana
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (miHilo!=null) miHilo.acaba();
			}
		});
	}
	
	/** Programa principal de la ventana de juego
	 * @param args
	 */
	public static void main(String[] args) {
		// Crea y visibiliza la ventana con el coche
		try {
			final VentanaJuego miVentana = new VentanaJuego();
			SwingUtilities.invokeAndWait( new Runnable() {
				@Override
				public void run() {
					miVentana.setVisible( true );
				}
			});
			miVentana.miMundo = new MundoJuego( miVentana.pPrincipal );
			miVentana.miMundo.creaCoche( 150, 100 );
			
			miVentana.miCoche = miVentana.miMundo.getCoche();
			miVentana.miCoche.setPiloto( "Fernando Alonso" );
			// Crea el hilo de movimiento del coche y lo lanza
			miVentana.miHilo = miVentana.new MiRunnable();  // Sintaxis de new para clase interna
			Thread nuevoHilo = new Thread( miVentana.miHilo );
			nuevoHilo.start();
		} catch (Exception e) {
			System.exit(1);  // Error anormal
		}
	}
	
	/** Clase interna para implementaci�n de bucle principal del juego como un hilo
	 * @author Andoni Egu�luz
	 * Facultad de Ingenier�a - Universidad de Deusto (2014)
	 */
	class MiRunnable implements Runnable {
		boolean sigo = true;
		@Override
		public void run() {
			// Bucle principal forever hasta que se pare el juego...
			while (sigo) {
				//Crear la estrella
				miMundo.creaEstrella();
				lmensaje2.setText("Se crea estrella");
				
				
				miMundo.quitaYRotaEstrellas(200000);
				
				int cont=miMundo.choquesConEstrellas();
				int puntuacion= cont/5;
				lmensaje.setText("La puntuacion es "+ puntuacion);
				// Mover coche
				miCoche.mueve( 0.040 );
				// Chequear choques
				// (se comprueba tanto X como Y porque podr�a a la vez chocar en las dos direcciones (esquinas)
				if (miMundo.hayChoqueHorizontal(miCoche)) // Espejo horizontal si choca en X
					miMundo.rebotaHorizontal(miCoche);
				if (miMundo.hayChoqueVertical(miCoche)) // Espejo vertical si choca en Y
					miMundo.rebotaVertical(miCoche);
				//Compruebo el array de booleanos para movel el coche
				if(aBooleanos[0])
					miCoche.gira( +10 );
				if(aBooleanos[1])
				{	
					MundoJuego.aplicarFuerza(miCoche.fuerzaAceleracionAdelante(),miCoche);
					
					//miCoche.acelera( +5, 1 );
				}
				if(aBooleanos[2])
					//miCoche.acelera( -5, 1 );
					MundoJuego.aplicarFuerza(miCoche.fuerzaAceleracionAtras(),miCoche);
					//miCoche.setVelocidad(0);
				if(aBooleanos[3])
					miCoche.gira( -10 );
				// Dormir el hilo 40 milisegundos
				int t=MundoJuego.aEstrellas.size();
				if(t>=10)
				{
					sigo=false;
				}
				try {
					Thread.sleep( 40 );
				} catch (Exception e) {
				}
				
				
			};
			

		}
		/** Ordena al hilo detenerse en cuanto sea posible
		 */
		public void acaba() {
			sigo = false;
		
			
			
				
			
		}
	};
	
}
