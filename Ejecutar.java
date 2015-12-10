package aima.core.environment.warcraft;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import aima.core.agent.Action;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;

public class Ejecutar {
	/*
	 * Variables estáticas que serán necesarias para la correcta ejecución de
	 * otras clases.
	 */
	public static String mapa = "";
	private final static int WIDTH = 512;
	public static Estado estadoFinal;

	private static Algoritmo getAlgoritmo(String arg) {
		arg = arg.toLowerCase();
		if (arg.equals("amplitud")) {
			return Algoritmo.Amplitud;
		} else if (arg.equals("profundidad")) {
			return Algoritmo.Profundidad;
		} else if (arg.equals("astar")) {
			return Algoritmo.Astar;
		} else if (arg.equals("gbfs")) {
			return Algoritmo.GBFS;
		} else {
			System.err.println("Algoritmo desconocido: " + arg);
			System.exit(-1);
			return null;
		}
	}

	/**
	 * Método que devuelve el enum correspondiente a la heurística pasada por
	 * parámetros.
	 * 
	 * @param arg
	 *            String con el nombre de la heurística a utilizar.
	 * @return Enum con la heurística correspondiente. Null en caso de no
	 *         existir.
	 */
	private static Heuristica getHeuristica(String arg) {
		arg = arg.toLowerCase();
		if (arg.equals("h1")) {
			return Heuristica.H1;
		} else if (arg.equals("h2")) {
			return Heuristica.H2;
		} else {
			System.err.println("Heuristica desconocida: " + arg);
			System.exit(-1);
			return null;
		}
	}

	/**
	 * Método que devuelve la coordenada pasada por parámetros en un vector de
	 * int.
	 * 
	 * @param argumento
	 *            Coordenada en un String de forma X-Y.
	 * @return coordenada Vector de int de tamaño 2 que contiene las
	 *         coordenadas.
	 */
	private static int[] getCoordenada(String argumento) {
		String coordX = "";
		String coordY = "";
		int[] coordenada = new int[2];
		int i;

		/* Recogemos la primera coordenada */
		for (i = 0; i < argumento.length(); i++) {
			if (argumento.charAt(0) == '-') {
				System.err.println("No se admiten coordenadas negativas.");
				System.exit(0);
			}
			if (argumento.charAt(i) != '-') {
				coordX = coordX + argumento.charAt(i);
			} else {
				i++;
				break;
			}
		}
		/* Empezando a contar desde el guión, recogemos la segunda coordenada */
		for (int j = i; j < argumento.length(); j++) {
			if (argumento.charAt(j) == '-') {
				System.err.println("No se admiten coordenadas negativas.");
				System.exit(-1);
			}
			coordY = coordY + argumento.charAt(j);
		}

		int x = Integer.parseInt(coordX);
		int y = Integer.parseInt(coordY);
		coordenada[0] = x;
		coordenada[1] = y;

		return coordenada;
	}

	/* Identificador para los algoritmos que pueden ejecutarse */
	public enum Algoritmo {
		Amplitud, Profundidad, Astar, GBFS
	};

	/* Identificador para las heurísticas creadas */
	public enum Heuristica {
		H1, H2
	};

	/**
	 * Método que recibe la dirección de un archivo de mapa y devuelve su
	 * contenido en un String para su posterior consulta.
	 * 
	 * @param direccion
	 *            String que contiene la ubicación del archivo.
	 * @return mapa String que contiene el mapa en una sola línea.
	 */
	public static String getMapa(String direccion) {
		/*
		 * Se usan FileReader y BufferedReader para la apertura y lectura del
		 * archivo.
		 */
		BufferedReader br = null;
		String mapa = "";

		try {
			br = new BufferedReader(new FileReader(direccion));
			String linea;
			int contadorLineas = 1;
			/*
			 * Lee de linea en linea hasta que llega al final. Empieza a guardar
			 * el mapa desde la linea 4. Guardamos la longitud de las lineas en
			 * la variable columna para su posterior uso.
			 */
			while ((linea = br.readLine()) != null) {
				if (contadorLineas > 4) {
					mapa = mapa + linea;
				}
				contadorLineas++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/* Cerramos el fichero tanto si salta una excepción como si no */
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return mapa;

	}

	/**
	 * Método para obtener el obstáculo asociado a unas coordenadas que han sido
	 * introducidas por parámetros.
	 * 
	 * @param x
	 *            Coordenada x correspondiente a la posición
	 * @param y
	 *            Coordenada y correspondiente a la posición
	 * @return Obstáculo asociado a la coordenada
	 */
	public static char getObstaculo(int x, int y) {
		char caracter;
		int posicion = WIDTH * x + y;
		caracter = mapa.charAt(posicion);
		return caracter;
	}

	/**
	 * Método que se encarga de crear el archivo que contendrá la representación
	 * del camino óptimo obtenido por el algoritmo.
	 * 
	 * @param argumento
	 *            Ubicación del archivo que contiene el mapa
	 * @param estadoInicial
	 *            Estado desde donde se parte
	 * @param actionList
	 *            Lista de acciones que han de realizarse para obtener el camino
	 *            óptimo.
	 * @throws IOException
	 *             si el fichero existe pero es un directorio en vez de un
	 *             archivo, no existe pero no puede ser creado, o no puede ser
	 *             abierto por cualquier otra razón.
	 */
	public static void mapaSalida(String argumento, Estado estadoInicial,
			List<Action> actionList) throws IOException {
		char matriz[][] = new char[512][512];
		int i = 0, j = 0;

		/*
		 * Pasamos el mapa, el cual estaba guardado en un String, a una matriz.
		 * De esta forma será más fácil reemplazar casillas con las letras que
		 * nos interesen para su posterior impresión.
		 * 
		 * Se guardan en las casillas las letras correspondientes a los
		 * obstáculos y se añaden las letras 'I' para indicar la casilla del
		 * estado inicial y la letra 'F' para indicar la casilla del estado
		 * final.
		 */
		for (int k = 0; k < mapa.length(); k++) {
			if (i == estadoInicial.x && j == estadoInicial.y) {
				matriz[i][j] = 'I';
			} else if (i == estadoFinal.x && j == estadoFinal.y) {
				matriz[i][j] = 'F';
			} else {
				matriz[i][j] = mapa.charAt(k);
			}
			if (j == WIDTH - 1) {
				j = 0;
				i++;
			} else {
				j++;
			}
		}

		/*
		 * A partir de la lista de acciones, se va añadiendo una 'X' en las
		 * casillas de la matriz correspondientes marcando el camino óptimo. Se
		 * parte desde el estado inicial y para evitar que se añada una 'X' en
		 * el estado final, se avanza hasta la penúltima acción.
		 */
		int coordX = estadoInicial.x;
		int coordY = estadoInicial.y;
		System.out.println("(" + coordX + ", " + coordY + ")"); //QUITAR ESTO ANTES DE ENVIAR EL CÓDIGO
		for (i = 0; i < actionList.size() - 1; i++) {
			switch (actionList.get(i).toString()) {
			case "NORTE":
				coordX = coordX - 1;
				System.out.println("(" + coordX + ", " + coordY + ")");
				matriz[coordX][coordY] = 'X';
				break;
			case "SUR":
				coordX = coordX + 1;
				System.out.println("(" + coordX + ", " + coordY + ")");
				matriz[coordX][coordY] = 'X';
				break;
			case "OESTE":
				coordY = coordY - 1;
				System.out.println("(" + coordX + ", " + coordY + ")");
				matriz[coordX][coordY] = 'X';
				break;
			case "ESTE":
				coordY = coordY + 1;
				System.out.println("(" + coordX + ", " + coordY + ")");
				matriz[coordX][coordY] = 'X';
			default:
				break;
			}
		}

		/*
		 * Se crea un archivo con el nombre del archivo del mapa sumando la
		 * extensión .output que contiene la matriz impresa, es decir, el mapa
		 * con la representación del camino óptimo.
		 */
		String fichero = argumento + ".output";
		
		/*
		 * Se usan FileReader y BufferedReader para la apertura y lectura del
		 * archivo.
		 */
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fichero));
			for (i = 0; i < matriz.length; i++) {
				for (j = 0; j < matriz.length; j++) {
					bw.write(matriz[i][j]);
				}
				bw.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
	}

	/**
	 * Método que se encarga de crear el archivo que contendrá los datos de la
	 * ejecución del tipo de búsqueda, como el tiempo, coste total, nodos
	 * extandidos, etc.
	 * 
	 * @param argumento
	 *            Ubicación y nombre del fichero a crear
	 * @param time
	 *            Tiempo que tarda en ejecutarse la búsqueda
	 * @param metric
	 *            Metric que contiene los datos de coste total, tamaño de la
	 *            lista, tamaño máximo de la lista y nodos expandidos.
	 * @throws IOException
	 *             si el fichero existe pero es un directorio en vez de un
	 *             archivo, no existe pero no puede ser creado, o no puede ser
	 *             abierto por cualquier otra razón.
	 */
	public static void statsMap(String argumento, String time, Metrics metric)
			throws IOException {
		/*
		 * Se crea un archivo con el nombre del archivo del mapa sumando la
		 * extensión .statistics que contiene los datos de la ejecución de la
		 * búsqueda.
		 */
		
		String fichero = argumento + ".statistics";
		/*
		 * Se usan FileReader y BufferedReader para la apertura y lectura del
		 * archivo.
		 */
		BufferedWriter bw2 = null;
		try {
			bw2 = new BufferedWriter(new FileWriter(fichero));
			bw2.write(time);
			bw2.write("\n");
			for (String key : metric.keySet()) {
				String property = metric.get(key);
				bw2.write(key + " : " + property);
				bw2.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw2 != null) {
				bw2.close();
			}
		}
	}

	public static void main(String args[]) {
		if (args.length != 5) {
			System.out
					.println("Utilizacion: java Ejecutar <mapa> <algoritmo> <heuristica> <pos inicial> <pos final>");
			System.exit(0);
		}
		/*
		 * Se recogen los argumentos que se han recibido de la consola y se
		 * guardan en sus correspondientes variables u objetos.
		 * 
		 * El mapa recogido por el primer arguemento será un String estático, ya
		 * que será necesario acceder a él desde otras clases.
		 */
		mapa = getMapa(args[0]);
		Algoritmo algoritmo = getAlgoritmo(args[1]);
		Heuristica heuristica = getHeuristica(args[2]);
		int[] posInicial = getCoordenada(args[3]);
		int[] posFinal = getCoordenada(args[4]);

		/*
		 * Los estados inicial y final serán creados a través de los vectores
		 * int con las coordenadas de su posición.
		 * 
		 * El estado final será un Estado estático, ya que será necesario
		 * acceder a él desde otras clases.
		 */
		Estado estadoInicial = new Estado(posInicial);
		estadoFinal = new Estado(posFinal);

		/* Se inicializan las funciones que definen el problema */
		ResultadoAccion resAccion = new ResultadoAccion();
		AccionesDisponibles accDisponibles = new AccionesDisponibles();
		FuncionMetas fMetas = new FuncionMetas();
		CosteAccion costeAccion = new CosteAccion();

		/* Imprime el estado para comprobar que se ha cargado correctamente */
		estadoInicial.toString();

		try {
			Problem problem = new Problem(estadoInicial, accDisponibles,
					resAccion, fMetas, costeAccion);

			HeuristicFunction hf = null;
			switch (heuristica) {
			case H1:
				System.out.println("Heuristica: h1");
				hf = new Heuristica1();
				break;
			case H2:
				System.out.println("Heurística: h2");
				hf = new Heuristica2();
				break;
			default:
				System.out.println("Heuristica invalida");
				System.exit(-1);
			}

			Search search = null;
			switch (algoritmo) {
			case Amplitud:
				System.out.println("Algoritmo: Amplitud");
				search = new BreadthFirstSearch();
				break;
			case Profundidad:
				System.out.println("Algoritmo: Profundidad");
				search = new DepthFirstSearch(new GraphSearch());
				break;
			case Astar:
				System.out.println("Algoritmo: A star");
				search = new AStarSearch(new GraphSearch(), hf);
				break;
			case GBFS:
				System.out.println("Algoritmo: GBFS");
				search = new GreedyBestFirstSearch(new GraphSearch(), hf);
				break;
			default:
				System.out.println("Algoritmo invalido");
				System.exit(-1);
			}

			long t = System.currentTimeMillis();
			List<Action> actionList = search.search(problem);
			long t2 = System.currentTimeMillis();

			printActions(actionList);

			try {
				mapaSalida(args[0], estadoInicial, actionList);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String time = "Time: " + (t2 - t) / 1000.0 + " s";
			System.out.println(time);
			printInstrumentation(search.getMetrics());
			Metrics metric = search.getMetrics();
			statsMap(args[0], time, metric);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void printInstrumentation(Metrics metric) {
		for (String key : metric.keySet()) {
			String property = metric.get(key);
			System.out.println(key + " : " + property);
		}
	}

	private static void printActions(List<Action> actions) {
		for (int i = 0; i < actions.size(); i++) {
			String action = actions.get(i).toString();
			System.out.println(action);
		}
		System.out.println("Plan Length: " + actions.size());
	}

}
