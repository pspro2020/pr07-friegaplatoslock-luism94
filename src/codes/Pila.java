package codes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Pila {

	private final int LIMITE = 20;
	
	private LinkedList<Plato> pila_sucios = new LinkedList<Plato>();
	private LinkedList<Plato> pila_limpios = new LinkedList<Plato>();
	private LinkedList<Plato> pila_secos = new LinkedList<Plato>();
	private LinkedList<Plato> pila_alacena = new LinkedList<Plato>();
	
	private Lock cerrojo = new ReentrantLock();
	private Condition pilaLlena = cerrojo.newCondition();
	private Condition pilaVacia = cerrojo.newCondition();
	
	private DateTimeFormatter formatoHora;

	public Pila(DateTimeFormatter formatoHora) {
		this.formatoHora = formatoHora;
		
		//Relleno la pila de platos sucios
		for (int i = 0; i < LIMITE; i++) {
			pila_sucios.add(new Plato(i));
		}
	}
	
	public Plato cogerPlatoSucio() throws InterruptedException {
		Plato plato;
		//Pongo el cerrojo para que no pase ningun otro hilo mientras el actual se encuentra dentro
		cerrojo.lock();
		//Se intenta ejecutar el siguiente bloque
		try {
			//Mientras la pila siga vacia el hilo espera
			while (pila_sucios.isEmpty()) {
				//Si la bandeja de platos limpios esta vacia el hilo se duerme hasta que haya alguno
				System.out.printf("Hora %s: %s esta esperando a que haya platos en la pila de platos......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				pilaVacia.await();
			}
			//Se recoge el plato
			plato = pila_sucios.removeFirst();
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha recogido el plato #%d de la pila......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//
			pilaLlena.signalAll();
			//Si coloco la expresion "return x" dentro del bloque try se sigue ejecutando el bloque finally?
			return plato;
		} finally {
			//Quito el cerrojo para que pase el siguiente hilo
			cerrojo.unlock();
		}
	}
	
	public void dejarPlatoLimpio(Plato plato) throws InterruptedException {
		//Pongo el cerrojo para que no pase ningun otro hilo mientras el actual se encuentra dentro
		cerrojo.lock();
		//Se intenta ejecutar el siguiente bloque
		try {
			//Mientras la pila siga vacia el hilo espera
			while (pila_limpios.size() >= LIMITE) {
				//Si la bandeja de platos limpios esta vacia el hilo se duerme hasta que haya alguno
				System.out.printf("Hora %s: %s esta esperando a que haya hueco en la pila de platos......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				pilaLlena.await();
			}
			//Se coloca el plato
			pila_limpios.add(plato);
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha colocado el plato #%d......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//
			pilaVacia.signalAll();
		} finally {
			//Quito el cerrojo para que pase el siguiente hilo
			cerrojo.unlock();
		}
	}
	
	public Plato cogerPlatoLimpio() throws InterruptedException {
		Plato plato;
		//Pongo el cerrojo para que no pase ningun otro hilo mientras el actual se encuentra dentro
		cerrojo.lock();
		//Se intenta ejecutar el siguiente bloque
		try {
			//Mientras la pila siga vacia el hilo espera
			while (pila_limpios.isEmpty()) {
				//Si la bandeja de platos limpios esta vacia el hilo se duerme hasta que haya alguno
				System.out.printf("Hora %s: %s esta esperando a que haya platos en la pila de platos......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				pilaVacia.await();
			}
			//Se recoge el plato
			plato = pila_limpios.removeFirst();
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha recogido el plato #%d de la pila......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//Se avisa a los demas hilos de que pueden continuar
			pilaVacia.signalAll();
			//Si coloco la expresion "return x" dentro del bloque try se sigue ejecutando el bloque finally?
			return plato;
		} finally {
			//Quito el cerrojo para que pase el siguiente hilo
			cerrojo.unlock();
		}
	}
	
	public void dejarPlatoSeco(Plato plato) throws InterruptedException {
		//Pongo el cerrojo para que no pase ningun otro hilo mientras el actual se encuentra dentro
		cerrojo.lock();
		//Se intenta ejecutar el siguiente bloque
		try {
			//Mientras la pila siga vacia el hilo espera
			while (pila_secos.size() >= LIMITE) {
				//Si la bandeja de platos limpios esta vacia el hilo se duerme hasta que haya alguno
				System.out.printf("Hora %s: %s esta esperando a que haya hueco en la pila de platos......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				pilaLlena.await();
			}
			//Se coloca el plato
			pila_secos.addFirst(plato);
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha colocado el plato #%d......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//Se avisa a los demas hilos de que pueden continuar
			pilaLlena.signalAll();
		} finally {
			//Quito el cerrojo para que pase el siguiente hilo
			cerrojo.unlock();
		}
	}
	
	public Plato cogerPlatoSeco() throws InterruptedException {
		Plato plato;
		//Pongo el cerrojo para que no pase ningun otro hilo mientras el actual se encuentra dentro
		cerrojo.lock();
		//Se intenta ejecutar el siguiente bloque
		try {
			//Mientras la pila siga vacia el hilo espera
			while (pila_secos.isEmpty()) {
				//Si la bandeja de platos limpios esta vacia el hilo se duerme hasta que haya alguno
				System.out.printf("Hora %s: %s esta esperando a que haya platos en la pila de platos......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				pilaVacia.await();
			}
			//Se recoge el plato
			plato = pila_secos.removeFirst();
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha recogido el plato #%d de la pila......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//Se avisa a los demas hilos de que pueden continuar
			pilaVacia.signalAll();
			//Si coloco la expresion "return x" dentro del bloque try se sigue ejecutando el bloque finally?
			return plato;
		} finally {
			//Quito el cerrojo para que pase el siguiente hilo
			cerrojo.unlock();
		}
	}
	
	public void dejarPlatoAlacena(Plato plato) throws InterruptedException {
		//Pongo el cerrojo para que no pase ningun otro hilo mientras el actual se encuentra dentro
		cerrojo.lock();
		//Se intenta ejecutar el siguiente bloque
		try {
			//Mientras la pila siga llena el hilo espera
			while (pila_alacena.size() >= LIMITE) {
				//Si la bandeja de platos limpios esta vacia el hilo se duerme hasta que haya alguno
				System.out.printf("Hora %s: %s esta esperando a que haya hueco en la pila de platos......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName());
				pilaLlena.await();
			}
			//Se coloca el plato
			pila_alacena.addFirst(plato);
			//Se muestra un mensaje por pantalla
			System.out.printf("Hora %s: %s ha colocado el plato #%d......\n", LocalDateTime.now().format(formatoHora).toString(), Thread.currentThread().getName(), plato.getNumSerie());
			//Se avisa a los demas hilos de que pueden continuar
			pilaLlena.signalAll();
		} finally {
			//Quito el cerrojo para que pase el siguiente hilo
			cerrojo.unlock();
		}
	}
}
