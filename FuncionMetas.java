package aima.core.environment.warcraft;
import aima.core.search.framework.GoalTest;

/**
 * Clase que implementa la funcion que decide si las metas se cumplen en un estado dado o no
 * Puede contener atributos para saber cuales son las metas de este problema o estar incluidas en el estado.
 */
public class FuncionMetas implements GoalTest {
	
	
	public FuncionMetas(){
		
	}

	public boolean isGoalState(Object state) {
		Estado estado = (Estado) state;
		
		if (estado.x == Ejecutar.estadoFinal.x && estado.y == Ejecutar.estadoFinal.y)
			return true;
		else 
			return false;
	}
}
