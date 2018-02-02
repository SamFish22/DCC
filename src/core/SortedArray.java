package core;

import java.util.ArrayList;
//credit for this class goes to Dr. Ferrer of Hendrix College
public class SortedArray<T extends Comparable<T>> {
	private ArrayList<T> elements = new ArrayList<>();
	
	public T get(int i) {
		return elements.get(i);
	}
	
	public int size() {
		return elements.size();
	}
	
	public void insert(T item) {
		elements.add(item);
		int i = elements.size() - 1;
		while (i > 0 && elements.get(i).compareTo(elements.get(i-1)) < 0) {
			T temp = elements.get(i);
			elements.set(i, elements.get(i-1));
			elements.set(i-1, temp);
			i -= 1;
		}
	}
	
	public void remove(int i) {
		elements.remove(i);
	}
	
	public void remove(T value) {
		elements.remove(value);
	}
}
