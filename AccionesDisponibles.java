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

		/*
		 * Recogemos las coordenadas (x,y) del estado actual para facilitar la
		 * lectura
		 */
		int coordX = estado.x;
		int coordY = estado.y;

		/*
		 * Se comprueba que al realizar la acción no exista un muro o se acabe
		 * el mapa, de forma que:
		 */

		/*
		 * No se puede subir si hay un muro en la posición superior o se está en
		 * el lado superior del mapa.
		 */
		if (coordX != 0 && Ejecutar.getObstaculo(coordX - 1, coordY) != '@') {
			acciones.add(new Accion(Accion.tipoAccion.NORTE));
		}
		/*
		 * No se puede bajar si hay un muro en la posición inferior o se está en
		 * el lado inferior del mapa.
		 */
		if (coordX != 511 && Ejecutar.getObstaculo(coordX + 1, coordY) != '@') {
			acciones.add(new Accion(Accion.tipoAccion.SUR));
		}
		/*
		 * No se puede ir a la derecha si hay un muro en la posición derecha o
		 * se está en el lado derecho del mapa.
		 */
		if (coordY != 511 && Ejecutar.getObstaculo(coordX, coordY + 1) != '@') {
			acciones.add(new Accion(Accion.tipoAccion.ESTE));
		}
		/*
		 * No se puede ir a la izquierda si hay un muro en la posición izquierda
		 * o se está en el lado izquierdo del mapa.
		 */
		if (coordY != 0 && Ejecutar.getObstaculo(coordX, coordY - 1) != '@') {
			acciones.add(new Accion(Accion.tipoAccion.OESTE));
		}
		return acciones;
	}

}