package aima.core.environment.warcraft;

import aima.core.agent.Action;
import aima.core.search.framework.StepCostFunction;

/**
 * Clase que implementa la funcion que decide el coste de una accion.
 */
public class CosteAccion implements StepCostFunction {
	@Override
	public double c(Object s, Action a, Object sDelta) {
		Estado estado = (Estado) s; // Estado sobre el que se aplica la acción

		double cost = 1.0;

		/**
		 * El coste de moverse por el mapa corresponde al de traspasar el
		 * obstáculo de la casilla actual. Es por eso que no son necesarios ni
		 * la accion a ejecutarse ni el estado sucesor.
		 */
		switch (Ejecutar.getObstaculo(estado.x, estado.y)) {
		/* El coste de traspasar el agua es 2 */
		case 'W':
			cost = 2.0;
			break;
		/* El coste de traspasar arena es 2 */
		case 'S':
			cost = 2.0;
			break;
		/* El coste de traspasar un bosque es 4 */
		case 'T':
			cost = 4.0;
			break;
		/* El coste no traspasar ningún obstáculo es 1 */
		case '.':
			cost = 1.0;
			break;
		default:
			break;
		}

		return cost;

	}

}
