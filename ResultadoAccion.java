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
		Accion accion = (Accion) a; // Accion a aplicar sobre el estado

		Estado sucesor = new Estado(estado);

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

		/*
		 * Realizar el cambio de estado sobre el estado 'sucesor' al ejecutar la
		 * accion 'a' en el estado anterior 's'
		 */

		return sucesor;
	}

}
