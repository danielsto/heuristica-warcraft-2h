package aima.core.environment.warcraft;

import aima.core.agent.Action;
import aima.core.search.framework.ResultFunction;

/**
 * Clase que implementa la funcion que devuelve el estado resultante de aplicar
 * una accion sobre un estado.
 */
public class ResultadoAccion implements ResultFunction {

	@Override
	public Object result(Object s, Action a) {
		Estado estado = (Estado) s; // Estado sobre el que se aplica la accion
		Accion accion = (Accion) a; // Acción a aplicar sobre el estado

		/*
		 * El estado sucesor comienza siendo una copia del estado actual.
		 * Posteriormente se cambiará dependiendo de la acción escogida.
		 */
		Estado sucesor = new Estado(estado);

		/*
		 * Se comprueba antes de realizar cambios en el estado sucesor si el
		 * estado actual está ubicado en algún lado del mapa. La modificación
		 * del estado sucesor será simplemente sumar o restar una posicion a la
		 * coordenada X o Y correspondiente a la acción a relizar.
		 */
		switch (accion.tipo) {
		case OESTE:
			if (sucesor.y != 0) {
				sucesor.y = sucesor.y - 1;
			}
			break;
		case ESTE:
			if (sucesor.y != 511) {
				sucesor.y = sucesor.y + 1;
			}
			break;
		case NORTE:
			if (sucesor.x != 0) {
				sucesor.x = sucesor.x - 1;
			}
			break;
		case SUR:
			if (sucesor.x != 511) {
				sucesor.x = sucesor.x + 1;
			}
			break;
		}

		return sucesor;
	}

}
