package ud.prog3.pr02;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

/** "Mundo" del juego del coche.
 * Incluye las f�sicas para el movimiento y los choques de objetos.
 * Representa un espacio 2D en el que se mueven el coche y los objetos de puntuaci�n.
 * @author Andoni Egu�luz Mor�n
 * Facultad de Ingenier�a - Universidad de Deusto
 */
public class MundoJuego {
	private JPanel panel;  // panel visual del juego
	CocheJuego miCoche;    // Coche del juego
	static ArrayList<Estrella> aEstrellas;
	static JLabel lmensaje;
	
	/** Construye un mundo de juego
	 * @param panel	Panel visual del juego
	 */
	public MundoJuego( JPanel panel ) {
		this.panel = panel;
		aEstrellas = new ArrayList<Estrella>();
		lmensaje=new JLabel();
		panel.add(lmensaje);
		lmensaje.setVisible(true);
		lmensaje.setPreferredSize(new Dimension(100,100));
	}

	/** Crea un coche nuevo y lo a�ade al mundo y al panel visual
	 * @param posX	Posici�n X de pixel del nuevo coche
	 * @param posY	Posici�n Y de p�xel del nuevo coche
	 */
	public void creaCoche( int posX, int posY ) {
		// Crear y a�adir el coche a la ventana
		miCoche = new CocheJuego();
		miCoche.setPosicion( posX, posY );
		panel.add( miCoche.getGrafico() );  // A�ade al panel visual
		miCoche.getGrafico().repaint();  // Refresca el dibujado del coche
	}
	
	/** Devuelve el coche del mundo
	 * @return	Coche en el mundo. Si no lo hay, devuelve null
	 */
	public CocheJuego getCoche() {
		return miCoche;
	}

	/** Calcula si hay choque en horizontal con los l�mites del mundo
	 * @param coche	Coche cuyo choque se comprueba con su posici�n actual
	 * @return	true si hay choque horizontal, false si no lo hay
	 */
	public boolean hayChoqueHorizontal( CocheJuego coche ) {
		return (coche.getPosX() < JLabelCoche.RADIO_ESFERA_COCHE-JLabelCoche.TAMANYO_COCHE/2 
				|| coche.getPosX()>panel.getWidth()-JLabelCoche.TAMANYO_COCHE/2-JLabelCoche.RADIO_ESFERA_COCHE );
	}
	
	/** Calcula si hay choque en vertical con los l�mites del mundo
	 * @param coche	Coche cuyo choque se comprueba con su posici�n actual
	 * @return	true si hay choque vertical, false si no lo hay
	 */
	public boolean hayChoqueVertical( CocheJuego coche ) {
		return (coche.getPosY() < JLabelCoche.RADIO_ESFERA_COCHE-JLabelCoche.TAMANYO_COCHE/2 
				|| coche.getPosY()>panel.getHeight()-JLabelCoche.TAMANYO_COCHE/2-JLabelCoche.RADIO_ESFERA_COCHE );
	}

	/** Realiza un rebote en horizontal del objeto de juego indicado
	 * @param coche	Objeto que rebota en horizontal
	 */
	public void rebotaHorizontal( CocheJuego coche ) {
		// System.out.println( "Choca X");
		double dir = coche.getDireccionActual();
		dir = 180-dir;   // Rebote espejo sobre OY (complementario de 180)
		if (dir < 0) dir = 360+dir;  // Correcci�n para mantenerlo en [0,360)
		coche.setDireccionActual( dir );
	}
	
	/** Realiza un rebote en vertical del objeto de juego indicado
	 * @param coche	Objeto que rebota en vertical
	 */
	public void rebotaVertical( CocheJuego coche ) {
		// System.out.println( "Choca Y");
		double dir = miCoche.getDireccionActual();
		dir = 360 - dir;  // Rebote espejo sobre OX (complementario de 360)
		miCoche.setDireccionActual( dir );
	}
	
	/** Calcula y devuelve la posici�n X de un movimiento
	 * @param vel    	Velocidad del movimiento (en p�xels por segundo)
	 * @param dir    	Direcci�n del movimiento en grados (0� = eje OX positivo. Sentido antihorario)
	 * @param tiempo	Tiempo del movimiento (en segundos)
	 * @return
	 */
	public static double calcMovtoX( double vel, double dir, double tiempo ) {
		return vel * Math.cos(dir/180.0*Math.PI) * tiempo;
	}
	
	/** Calcula y devuelve la posici�n X de un movimiento
	 * @param vel    	Velocidad del movimiento (en p�xels por segundo)
	 * @param dir    	Direcci�n del movimiento en grados (0� = eje OX positivo. Sentido antihorario)
	 * @param tiempo	Tiempo del movimiento (en segundos)
	 * @return
	 */
	public static double calcMovtoY( double vel, double dir, double tiempo ) {
		return vel * -Math.sin(dir/180.0*Math.PI) * tiempo;
		// el negativo es porque en pantalla la Y crece hacia abajo y no hacia arriba
	}
	
	/** Calcula el cambio de velocidad en funci�n de la aceleraci�n
	 * @param vel		Velocidad original
	 * @param acel		Aceleraci�n aplicada (puede ser negativa) en pixels/sg2
	 * @param tiempo	Tiempo transcurrido en segundos
	 * @return	Nueva velocidad
	 */
	public static double calcVelocidadConAceleracion( double vel, double acel, double tiempo ) {
		return vel + (acel*tiempo);
	}
	
	public static double calcFuerzaRozamiento( double masa, double coefRozSuelo, 
			 double coefRozAire, double vel ) { 
			 double fuerzaRozamientoAire = coefRozAire * (-vel); // En contra del movimiento 
			 double fuerzaRozamientoSuelo = masa * coefRozSuelo * ((vel>0)?(-1):1); // Contra mvto 
			 return fuerzaRozamientoAire + fuerzaRozamientoSuelo; 
	} 
	
	public static double calcAceleracionConFuerza( double fuerza, double masa ) { 
		 return fuerza/masa; 
	}
	
	public static void aplicarFuerza( double fuerza, Coche coche ) { 
		 double fuerzaRozamiento = calcFuerzaRozamiento( Coche.MASA , 
		 Coche.COEF_RZTO_SUELO, Coche.COEF_RZTO_AIRE, coche.getVelocidad() ); 
		 double aceleracion = calcAceleracionConFuerza( fuerza+fuerzaRozamiento, Coche.MASA ); 
		 if (fuerza==0) { 
			 // No hay fuerza, solo se aplica el rozamiento 
			 double velAntigua = coche.getVelocidad(); 
			 coche.acelera( aceleracion, 0.04 ); 
			 if (velAntigua>=0 && coche.getVelocidad()<0 || velAntigua<=0 && coche.getVelocidad()>0) { 
				 coche.setVelocidad(0); // Si se est� frenando, se para (no anda al rev�s) 
			 }
		 }		 
		 else { 
			 coche.acelera( aceleracion, 0.04 ); 
		 } 
	}
	
	public void creaEstrella() 
	{
		
		Random num = new Random();
		 
		Long segundo2, segundo1;
		Calendar cal1 = Calendar.getInstance();
		int posX,posY;
		
	    posX = num.nextInt(1000);
	    posY = num.nextInt(750);
	 
	     //Crear y a�adir la estrella a la ventana
		if(aEstrellas.size()==0)
		{
		    Estrella miEstrella = new Estrella(posX,posY,cal1.getTimeInMillis());
		    //miEstrella.setPosicion( 150, 100 );
		    //miEstrella.getGrafico().setHoraCreacion(cal1.getTimeInMillis());
		    panel.add( miEstrella.getGrafico() );  // A�ade al panel visual
		    miEstrella.getGrafico().repaint();  // Refresca el dibujado de la estrella
		    aEstrellas.add(miEstrella);
		   lmensaje.setText("Nueva estrella");
		}
		else
	    	if(cal1.getTimeInMillis()-(aEstrellas.get(aEstrellas.size()-1)).getGrafico().getHoraCreacion()>=1200)
	    	{
	    		//Estrella miEstrella = new Estrella(posX, posY,cal1.getTimeInMillis());
	    	    aEstrellas.add(new Estrella(posX, posY,cal1.getTimeInMillis()));
	    	    panel.add( aEstrellas.get(aEstrellas.size()-1).getGrafico() );  // A�ade al panel visual
	    	    aEstrellas.get(aEstrellas.size()-1).getGrafico().repaint();  // Refresca el dibujado de la estrella
	    	    //aEstrellas.add(miEstrella);
	    	    
	    	}
}
public int quitaYRotaEstrellas( long maxTiempo )
	{
		int contEstrellas=0;

		Calendar cal = Calendar.getInstance();
		for(int i=0;i<aEstrellas.size();i++)
		{
			if(cal.getTimeInMillis()-aEstrellas.get(i).getGrafico().getHoraCreacion()>=maxTiempo)
			{
				contEstrellas++;
				aEstrellas.get(i).getGrafico().setVisible(false);
				aEstrellas.remove(i);	
			}
			else
			{
				aEstrellas.get(i).gira(+10);
				//aEstrellas.get(i).getGrafico().repaint();
			}
		}
		return contEstrellas;
	}
public int choquesConEstrellas()
{
	
	int ContChoque=0;

	for(int i=0;i<aEstrellas.size();i++)
	{
	//if ((aEstrellas.get(i).getPosX()-JLabelEstrella.RADIO_ESFERA_ESTRELLA==miCoche.getPosX()+JLabelCoche.RADIO_ESFERA_COCHE) || (aEstrellas.get(i).getPosY()-JLabelEstrella.RADIO_ESFERA_ESTRELLA==miCoche.getPosY()+JLabelCoche.RADIO_ESFERA_COCHE)){
		double distancia= Math.sqrt(Math.pow(aEstrellas.get(i).getPosX()-miCoche.getPosX(),2)+Math.pow(aEstrellas.get(i).getPosY()-miCoche.getPosY(),2));	

		if(distancia<JLabelEstrella.RADIO_ESFERA_ESTRELLA+JLabelCoche.RADIO_ESFERA_COCHE)
		{
			ContChoque++;
			aEstrellas.get(i).getGrafico().setVisible(false);
			aEstrellas.remove(i);
			lmensaje.setText("La puntuaci�n es de "+ ContChoque/5);
			
		}

	}
	return ContChoque;

}


}