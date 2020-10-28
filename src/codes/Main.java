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
		Pila sucios = new Pila(formatoHora);//Solo para el fregador
		Pila limpios = new Pila(formatoHora);//Para el fregador y el secador
		Pila secos = new Pila(formatoHora);//Para el secador y el organizador
		Pila alacena = new Pila(formatoHora);//Solo para el organizador
		//Hilos secundarios declarados con nombre
		Thread fregador = new Thread(new Fregador(sucios, limpios, formatoHora), "Fregador");
		Thread secador = new Thread(new Secador(limpios, secos, formatoHora), "Secador");
		Thread organizador = new Thread(new Organizador(secos, alacena, formatoHora), "Organizador");
		//Array de hilos secundarios
		Thread[] hilos = {fregador, secador, organizador};
		//Relleno la pila de platos sucios
		for (int i = 0; i < LIMITE; i++) {
			sucios.getPila().add(new Plato(i));
		}
		
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
