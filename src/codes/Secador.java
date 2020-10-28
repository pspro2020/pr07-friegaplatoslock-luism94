package codes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Secador implements Runnable {
	private int MIN_DURACION = 1;
	private int MAX_DURACION = 3;
	// Formato de hora recibido por parametro del constructor
	private DateTimeFormatter formatoHora;
	// Objeto cerrojo recibido por parametro del constructor
	private Pila pila;

	public Secador(Pila pila, DateTimeFormatter formatoHora) {
		this.pila = pila;
		this.formatoHora = formatoHora;
	}

	@Override
	public void run() {
		// Metodo que contiene las instrucciones del hilo secundario a ejecutar
		Plato plato;
		while (!Thread.currentThread().isInterrupted()) {
			try {
				// El hilo coge un plato de la bandeja de platos limpios
				plato = pila.cogerPlatoLimpio();
			} catch (InterruptedException e) {
				System.out.printf("Hora %s: Se ha interrumpido a %s mientras cogia un plato......\n",
						LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				return;
			}

			try {
				// El hilo se pone a secar el plato
				secarPlato(plato);
			} catch (InterruptedException e) {
				System.out.printf("Hora %s: Se ha interrumpido a %s mientras secaba un plato......\n",
						LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				return;
			}

			try {
				// El hilo coloca el plato seco en la bandeja de platos secos
				pila.dejarPlatoSeco(plato);
			} catch (InterruptedException e) {
				System.out.printf("Hora %s: Se ha interrumpido a %s mientras colocaba un plato......\n",
						LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				return;
			}
		}
	}

	private void secarPlato(Plato plato) throws InterruptedException {
		// Se muestra un mensaje por pantalla y el hilo se suspende durante unos
		// segundos
		System.out.printf("Hora %s: %s coge un plato y se pone a secar el plato %s...\n",
				LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(),
				plato.getNumSerie());
		TimeUnit.SECONDS.sleep(new Random().nextInt(MAX_DURACION) + MIN_DURACION);
	}

}
