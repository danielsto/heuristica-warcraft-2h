package aima.core.environment.warcraft;

import aima.core.agent.impl.DynamicAction;

/**
 * La clase accion representa las acciones del dominio. Pueden incluirse
 * atributos y/o metodos auxiliares para poder representar las acciones o bien
 * utilizar el nombre de la accion.
 */
public class Accion extends DynamicAction {

	public enum tipoAccion {
		OESTE, ESTE, NORTE, SUR
	}

	public tipoAccion tipo;
	public String nombre;

	/**
	 * Crea la accion a partir de su nombre
	 */

	public Accion(tipoAccion tipo) {
		super(tipo.toString());
		this.tipo = tipo;
		this.nombre = tipo.toString();
	}

	public String toString() {
		return this.nombre;
	}

}
