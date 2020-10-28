package codes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Fregador implements Runnable {

	private final int MIN_DURACION = 4;
	// Objeto cerrojo recibido por parametro del constructor
	private Pila pila_uno, pila_dos;
	// Formato de hora recibido por parametro del constructor
	private DateTimeFormatter formatoHora;

	public Fregador(Pila pila_uno, Pila pila_dos, DateTimeFormatter formatoHora) {
		this.pila_uno = pila_uno;
		this.pila_dos = pila_dos;
		this.formatoHora = formatoHora;
	}

	@Override
	public void run() {
		// Metodo que contiene las instrucciones del hilo secundario a ejecutar
		Plato plato;
		while (!Thread.currentThread().isInterrupted()) {
			try {
				// El hilo coge un plato de la bandeja de platos sucios
				plato = pila_uno.cogerPlato();
			} catch (InterruptedException e) {
				System.out.printf("Hora %s: Se ha interrumpido a %s mientras cogia un plato......\n",
						LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				return;
			}

			try {
				// El hilo limpia el plato
				limpiarPlato(plato);
			} catch (InterruptedException e) {
				System.out.printf("Hora %s: Se ha interrumpido a %s mientras limpiaba un plato......\n",
						LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				return;
			}

			try {
				// El hilo coloca el plato en la bandeja de platos limpios para el siguiente
				// hilo
				pila_dos.dejarPlato(plato);
			} catch (InterruptedException e) {
				System.out.printf("Hora %s: Se ha interrumpido a %s mientras colocaba un plato......\n",
						LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				return;
			}
		}
	}
	
	private void limpiarPlato(Plato plato) throws InterruptedException {
		//Se meustra un mensaje por pantalla y el hilo se suspende durante unos segundos
		System.out.printf("Hora %s: %s se pone a fregar el plato %s...\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
		TimeUnit.SECONDS.sleep(new Random().nextInt(MIN_DURACION + 1) + 4);
	}

}
