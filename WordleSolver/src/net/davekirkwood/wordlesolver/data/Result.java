package net.davekirkwood.wordlesolver.data;

import java.util.Arrays;

public class Result {

	public enum Element { MISS, LETTER, HIT };
	
	private Element[] elements;
	
	public Result(Element[] elements) {
		this.elements = elements;
	}
	
	public Element getLetter(int index) {
		return elements[index];
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(elements);
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
		Result other = (Result) obj;
		if (!Arrays.equals(elements, other.elements))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Result [elements=" + Arrays.toString(elements) + "]";
	}

	
}
