package aima.core.environment.warcraft;
public class Estado {

	int x;
	int y;

	public Estado(int[] posicion) {
		this.x = posicion[0];
		this.y = posicion[1];
	}

	public Estado(Estado otro) {
		this.x = otro.x;
		this.y = otro.y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estado other = (Estado) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Estado [x=" + x + ", y=" + y + "]";
	}

}