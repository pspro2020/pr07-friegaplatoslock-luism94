package codes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class Main {
	
	private static final int MAX_HILOS = 3;
	private static final int MAX_SEGUNDOS = 60;
	private static final int LIMITE = 20;
	private static DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");

	public static void main(String[] args) {
		//Objetos cerrojo comun a los hilos
		Pila pila = new Pila(formatoHora);

		//Hilos secundarios declarados con nombre
		Thread fregador = new Thread(new Fregador(pila, formatoHora), "Fregador");
		Thread secador = new Thread(new Secador(pila, formatoHora), "Secador");
		Thread organizador = new Thread(new Organizador(pila, formatoHora), "Organizador");
		//Array de hilos secundarios
		Thread[] hilos = {fregador, secador, organizador};
		
		//Se inician los hilos
		for (int i = 0; i < MAX_HILOS; i++) {
			hilos[i].start();
		}
				
		try {
			// Hilo principal espera 60 segundos antes de interrumpir los hilos secundarios
			TimeUnit.SECONDS.sleep(MAX_SEGUNDOS);

			for (int i = 0; i < MAX_HILOS; i++) {
				hilos[i].interrupt();
			}
			// Se muestra por pantalla el mensaje CUMPLEAÑOS FELIZ
			System.out.printf("Hora %s: --Todos: CUMPLEAÑOS FELIZ!!\n",
					LocalDateTime.now().format(formatoHora).toString());
		} catch (InterruptedException e) {
			// Si por si algo interrumpe el hilo principal, se muestra un mensaje por
			// pantalla
			System.out.println("ERROR -- CUMPLEAÑERO INTERRUMPIDO");
			e.printStackTrace();
		}
	}

}
