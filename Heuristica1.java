package aima.core.environment.warcraft;

import aima.core.search.framework.HeuristicFunction;

public class Heuristica1 implements HeuristicFunction {

	public Heuristica1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public double h(Object state) {
		// TODO Auto-generated method stub
		Estado estado = (Estado) state;
		int restaX = Ejecutar.estadoFinal.x - estado.x;
		int restaY = Ejecutar.estadoFinal.y - estado.y;
		double valorHeuristica = Math.sqrt(Math.pow(restaX, 2) + Math.pow(restaY, 2));
		
		return valorHeuristica;
	}

}