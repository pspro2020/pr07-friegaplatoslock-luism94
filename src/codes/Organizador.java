package codes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Organizador implements Runnable {

	private int MIN_DURACION = 1;
	// Objeto cerrojo recibido por parametro del constructor
	private Pila pila, alacena;
	// Formato de hora recibido por parametro del constructor
	private DateTimeFormatter formatoHora;

	public Organizador(Pila pila, Pila alacena, DateTimeFormatter formatoHora) {
		this.pila = pila;
		this.alacena = alacena;
		this.formatoHora = formatoHora;
	}

	@Override
	public void run() {
		// Metodo que contiene las instrucciones del hilo secundario a ejecutar
		Plato plato;
		while (!Thread.currentThread().isInterrupted()) {
			try {
				// El hilo coge un plato de la bandeja de platos secos
				plato = pila.cogerPlato();
			} catch (InterruptedException e) {
				System.out.printf("Hora %s: Se ha interrumpido al Organizador mientras cogia un plato......\n",
						LocalDateTime.now().format(formatoHora).toString());
				return;
			}

			try {
				// El hilo coloca el plato en la alacena de platos
				alacena.dejarPlato(plato);
			} catch (InterruptedException e) {
				System.out.printf("Hora %s: Se ha interrumpido al Organizador mientras colocaba un plato......\n",
						LocalDateTime.now().format(formatoHora).toString());
				return;
			}
		}
	}

}
