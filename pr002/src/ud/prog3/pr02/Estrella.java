package ud.prog3.pr02;

public class Estrella {
	private double PosX;
	private double PosY;
	private JLabelEstrella miGrafico;  // Etiqueta gráfica de la estrella
	private double miDireccionActual;


	public Estrella(int posx, int posy, long hc) {
		super();
		this.PosX = posx;
		this.PosY = posy;
		miGrafico = new JLabelEstrella(posx,posy);
		miGrafico.setHoraCreacion(hc);
		miDireccionActual=0;
	}
	public JLabelEstrella getGrafico() {
		return miGrafico;
	}
	public Estrella() {
		super();
		// TODO Auto-generated constructor stub
	}
	public double getPosX() {
		return PosX;
	}
	public void setPosX(double posX) {
		//miGrafico.setLocation( (int)posX, (int)PosY );
		this.PosX = posX;
	}
	public double getPosY() {
		return PosY;
	}
	public void setPosY(double posY) {
		//miGrafico.setLocation( (int)PosX, (int)posY );
		this.PosY = posY;
	}
	public void setPosicion( double posX, double posY ) {
		setPosX( posX );
		setPosY( posY );
	}
	public void getPosicion(double posX, double posY){
		setPosX(posX);
		setPosY(posY);
	}
	public void setDireccionActualt( double dir ) {
		setDireccionActual(dir);
		miGrafico.setGiro( miDireccionActual );
		miGrafico.repaint();  // Necesario porque Swing no redibuja al cambiar el giro (no pasa nada en el JLabel)
	}
	public void setDireccionActual( double dir ) {
		// if (dir < 0) dir = 360 + dir;
		if (dir > 360) dir = dir - 360;
			miDireccionActual = dir;
	}

	public void gira( double giro ) {
		setDireccionActualt( miDireccionActual + giro );
	}

}


