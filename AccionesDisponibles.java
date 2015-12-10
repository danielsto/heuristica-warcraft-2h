package aima.core.environment.warcraft;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import java.util.LinkedHashSet;

/**
 * Clase que implementa la funcion que devuelve las acciones disponibles en un
 * estado dado.
 */
public class AccionesDisponibles implements ActionsFunction {
	public Set<Action> actions(Object state) {
		Estado estado = (Estado) state;
		Set<Action> acciones = new LinkedHashSet<Action>();

		int coordX = estado.x;
		int coordY = estado.y;

		if (coordY != 511
				&& Ejecutar.getObstaculo(coordX, coordY + 1) != '@') {
			acciones.add(new Accion(Accion.tipoAccion.ESTE));
		}
		if (coordX != 0
				&& Ejecutar.getObstaculo(coordX - 1, coordY) != '@') {
			acciones.add(new Accion(Accion.tipoAccion.NORTE));
		}
		if (coordX != 511
				&& Ejecutar.getObstaculo(coordX + 1, coordY) != '@') {
			acciones.add(new Accion(Accion.tipoAccion.SUR));
		}
		if (coordY != 0
				&& Ejecutar.getObstaculo(coordX, coordY - 1) != '@') {
			acciones.add(new Accion(Accion.tipoAccion.OESTE));
		}
		return acciones;
	}

}