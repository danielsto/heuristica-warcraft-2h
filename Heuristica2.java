package aima.core.environment.warcraft;
import aima.core.search.framework.HeuristicFunction;

public class Heuristica2 implements HeuristicFunction{

	public Heuristica2() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double h(Object state) {
		// TODO Auto-generated method stub
		Estado estado = (Estado)state;
		int restaX = Ejecutar.estadoFinal.x - estado.x;
		int restaY = Ejecutar.estadoFinal.y - estado.y;
		double valorHeuristica = restaX + restaY;
		
		return valorHeuristica;
	}

}
