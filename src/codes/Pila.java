package codes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Pila {

	private final int LIMITE = 20;
	
	private LinkedList<Plato> pila_platos = new LinkedList<Plato>();
	
	private Lock cerrojo = new ReentrantLock();
	private Condition pilaLlena = cerrojo.newCondition();
	private Condition pilaVacia = cerrojo.newCondition();
	
	private DateTimeFormatter formatoHora;

	public Pila(DateTimeFormatter formatoHora) {
		this.formatoHora = formatoHora;
	}
	
	public Plato cogerPlato() throws InterruptedException {
		Plato plato;
		
		cerrojo.lock();
		
		try {
			while (pila_platos.isEmpty()) {
				//Si la bandeja de platos limpios esta vacia el hilo se duerme hasta que haya alguno
				System.out.printf("Hora %s: %s esta esperando a que haya platos en la pila de platos......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				pilaVacia.await();
			}
			//
			plato = pila_platos.removeFirst();
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha recogido el plato #%d de la pila......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//
			pilaVacia.signalAll();
			//Si coloco la expresion "return x" dentro del bloque try se sigue ejecutando el bloque finally?
			return plato;
		} finally {
			//
			cerrojo.unlock();
		}
	}
	
	public void dejarPlato(Plato plato) throws InterruptedException {
		//
		cerrojo.lock();
		//
		try {
			//
			while (pila_platos.size() >= LIMITE) {
				//Si la bandeja de platos limpios esta vacia el hilo se duerme hasta que haya alguno
				System.out.printf("Hora %s: %s esta esperando a que haya hueco en la pila de platos......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				pilaLlena.await();
			}
			//
			pila_platos.add(plato);
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha colocado el plato #%d......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//
			pilaLlena.signalAll();
		} finally {
			//
			cerrojo.unlock();
		}
	}
	//Metodo get de la lista de platos
	public LinkedList<Plato> getPila() {
		return pila_platos;
	}
}
