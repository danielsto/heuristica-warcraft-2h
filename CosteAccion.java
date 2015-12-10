package aima.core.environment.warcraft;

import aima.core.agent.Action;
import aima.core.search.framework.StepCostFunction;

/**
 * Clase que implementa la funcion que decide el coste de una accion.
 */
public class CosteAccion implements StepCostFunction {
	@Override
	public double c(Object s, Action a, Object sDelta) {
		Estado estado = (Estado) s; // Estado sobre el que se aplica la accion
		
		double cost = 1.0;
		
		if (Ejecutar.getObstaculo(estado.x, estado.y) == 'W') {
			cost = 2.0;
		}else if(Ejecutar.getObstaculo(estado.x, estado.y) == 'S'){
			cost = 2.0;
		}else if(Ejecutar.getObstaculo(estado.x, estado.y) == 'T'){
			cost = 4.0;
		}else if(Ejecutar.getObstaculo(estado.x, estado.y) == '.'){
			cost = 1.0;
		}
		return cost;

	}

}
