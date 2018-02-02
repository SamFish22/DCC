package core;

import java.util.ArrayList;
import java.util.function.BiFunction;

//credit for this class goes to Dr. Ferrer of Hendrix College
public class StringTrieMap<V> implements Comparable<StringTrieMap<V>> {
	private V value;
	private SortedArray<StringTrieMap<V>> children;
	private char c;
	
	public StringTrieMap() {
		value = null;
		children = new SortedArray<>();
		c = '\0';
	}
	
	private char getChar() {return c;}
	 
	private boolean hasValue() {
		return value != null;
	}
	
	public ArrayList<String> keyList() {
		ArrayList<String> result = new ArrayList<>();
		for (int i = 0; i < children.size(); i++) {
			children.get(i).visit("", result);
		}
		return result;
	}
	
	StringTrieMap<V> find(String key) {
		return find(key, (c, v) -> v.getChildWith(c));
	}
	
	StringTrieMap<V> find(String key, BiFunction<Character,StringTrieMap<V>,StringTrieMap<V>> process) {
		StringTrieMap<V> current = this;
		for (int i = 0; i < key.length() && current != null; i++) {
			current = process.apply(key.charAt(i), current);
		}
		return current;
	}
	
	public ArrayList<String> successorsTo(String prefix) {
		StringTrieMap<V> trie = find(prefix);
		if (trie == null) {
			return new ArrayList<>();
		} else {
			ArrayList<String> result = new ArrayList<>();
			trie.visit(prefix.substring(0, prefix.length() - 1), result);
			return result;
		}
	}
	
	private void visit(String prefix, ArrayList<String> strings) {
		prefix += c;
		if (hasValue()) {
			strings.add(prefix);
		}
		for (int i = 0; i < children.size(); i++) {
			children.get(i).visit(prefix, strings);
		}
	}
	
	public int size() {
		int size = value == null ? 0 : 1;
		for (int i = 0; i < children.size(); i++) {
			size += children.get(i).size();
		}
		return size;
	}
	
	private StringTrieMap<V> getChildWith(char target) {
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).getChar() == target) {
				return children.get(i);
			}
		}
		return null;
	}
	
	public boolean containsKey(String key) {
		StringTrieMap<V> trie = find(key);
		return trie == null ? false : trie.hasValue();
	}
	
	public V get(String key) {
		return find(key).value;
	}
	
	public void put(String key, V value) {
		StringTrieMap<V> current = find(key, (c, v) -> {
			StringTrieMap<V> child = v.getChildWith(c);
			if (child == null) {
				child = new StringTrieMap<>();
				child.c = c;
				v.children.insert(child);
			}
			return child;
		});
		current.value = value;
	}
	
	public void remove(String key) {
		ArrayList<StringTrieMap<V>> path = new ArrayList<>();
		StringTrieMap<V> current = find(key, (c, v) -> {
			path.add(v);
			return v.getChildWith(c);
		});
		if (current != null) {
			current.value = null;
			path.add(current);
		}
		for (int i = path.size() - 2; i >= 0; i--) {
			StringTrieMap<V> child = path.get(i+1);
			StringTrieMap<V> parent = path.get(i);
			if (!child.hasValue() && child.children.size() == 0) {
				parent.children.remove(child);
			}
		}
	}

	@Override
	public int compareTo(StringTrieMap<V> o) {
		return c - o.c;
	}
}
