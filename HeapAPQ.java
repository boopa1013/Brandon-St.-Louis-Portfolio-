package cs2321;

import java.util.Comparator;

import net.datastructures.*;
/**
 * A Adaptable PriorityQueue based on an heap. 
 * 
 * Course: CS2321 Section ALL
 * Assignment: #3
 * @author
 */

public class HeapAPQ<K,V> implements AdaptablePriorityQueue<K,V> {
	
	
	
	public static class DefaultComparator<K> implements Comparator<K> {
		
		
		// This compare method simply calls the compareTo method of the argument. 
		// If the argument is not a Comparable object, and therefore there is no compareTo method,
		// it will throw ClassCastException. 
		public int compare(K a, K b) throws IllegalArgumentException {
			if (a instanceof Comparable ) {
			   return ((Comparable <K>) a).compareTo(b);
			} else {
				throw new  IllegalArgumentException();
			}
		}
	}
	//arraylist for the pq entries 
	public ArrayList<apqEntry<K,V>> heap = new ArrayList<>();
	
	private static class apqEntry<K,V> implements Entry<K,V> {
		private K k;
		private V v;
		private int i;
		
		public apqEntry(K key,V value, int index) {
			k = key;
			v = value;
			i = index;
		}
		public K getKey() {
			
			return k;
		}

		@Override
		public V getValue() {
			
			return v;
		}
		public int getIndex() {
			return i;
		}
		public void setValue(V value) {
			v = value;
		}
		public void setKey(K key) {
			k = key;
		}
		public void setIndex(int index) {
			i = index;
		}
		
	}
	//comparator used to define order of keys in the PQ
	private Comparator<K> comp ;
	private int size = 0;
	private int cap = 0;
	//Next we compare two entries using the keys
	protected int compare(Entry<K,V> a, Entry<K,V> b) {
		return comp.compare (a.getKey(), b.getKey());
	}
	//this method in the book is shown to be O(1)

	protected boolean checkKey(K key) throws IllegalArgumentException{
		try {
			return (comp.compare(key, key) == 0);
			
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Incompatible Key");
		}
	}
	
	/* If no comparator is provided, use the default comparator. 
	 * See the inner class DefaultComparator above. 
	 * If no initial capacity is specified, use the default initial capacity.
	 */
	public HeapAPQ() {
		this(new DefaultComparator<K>());
	}
	
	/* Start the PQ with specified initial capacity */
	public HeapAPQ(int capacity) {
		cap = capacity;
	}
	
	
	/* Use specified comparator */
	public HeapAPQ(Comparator<K> c) {
		comp = c;
	}
	
	/* Use specified comparator and the specified initial capacity */
	public HeapAPQ(Comparator<K> c, int capacity) {
		cap = capacity;
		comp = c;;
	}
	
	/* 
	 * Return the array in arraylist that is used to store entries  
	 * This method is purely for testing purpose of auto-grader
	 */
	//this method in the book is shown to be O(n)
	
	public Object[] data() {
		
		return heap.toArray();
	}
	protected int parent(int i) {
		return (i-1)/2;
	}
	protected int left(int i) {
		return 2*i+1;
	}
	protected int right(int i) {
		return 2*i+2;
	}
	protected boolean hasLeft(int i) {
		return left(i) < heap.size();
	}
	protected boolean hasRight(int i) {
		return right(i) < heap.size();
	}
	//swaps two entries in PQ by using their index
	//this method in the book is shown to be O(1)

	protected void swap(int i, int j) {
		apqEntry<K,V> temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
		heap.get(i).setIndex(i);
		heap.get(j).setIndex(j);
		
	}
	//fixes issues in order until tree meets heap property 
	//this method in the book is shown to be O(logn)

	protected void upheap(int i) {
		while(i > 0) {
			int p = parent(i);
			if(comp.compare(heap.get(i).getKey(), heap.get(p).getKey()) >= 0) {
				break;
			}
			swap(p,i);
			i = p;
		}
		
	}
	//fixes issues in order until tree meets heap property 
	//this method in the book is shown to be O(logn)
	
	protected void downheap(int i) {
		while(hasLeft(i)) {
			int leftIndex=left(i);
			int smallChildIndex = leftIndex;
			if(hasRight(i)) {
				int rightIndex = right(i);
				if(comp.compare(heap.get(smallChildIndex).getKey(), heap.get(rightIndex).getKey()) > 0) {
					smallChildIndex = rightIndex;
				}
			}
			if((comp.compare(heap.get(smallChildIndex).getKey(), heap.get(i).getKey()) >= 0)) {
				break;
			}
			swap(i,smallChildIndex);
			i = smallChildIndex;
		}
		
	}
	//this method in the book is shown to be O(1)
	
	public void Bubble(int i) {
		if(i > 0 && comp.compare(heap.get(i).getKey(), heap.get(parent(i)).getKey()) < 0) {
			upheap(i);
		}else
			downheap(i);
		
	}
	//this method in the book is shown to be O(1)
	
	public apqEntry<K,V> validate(Entry<K,V> entry) throws IllegalArgumentException{
		apqEntry<K,V> locator = (apqEntry<K,V>) entry;
		if (!(entry instanceof apqEntry)) throw new IllegalArgumentException("Not APQ entry");
		return locator;
	}

	@Override
	public int size() {
		return heap.size();
	}

	@Override
	public boolean isEmpty() {
		
		return size() == 0;
	}
	//this method in the book is shown to be O(logn)
	//inserts entry key and value, at end of heap then restores heap order
	@Override
	
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
		checkKey(key);
		apqEntry<K,V> newest = new apqEntry<>(key,value, heap.size());
		heap.add(heap.size(), newest);
		upheap(heap.size()-1);
		return newest;
	}
	//gets min/root of the heap
	//this method in the book is shown to be O(1)
	@Override
	
	public Entry<K, V> min() {
		if(heap.isEmpty()) {
			return null;
		}
		return heap.get(0);
	}
	//removes min/root of the heap
	//this method in the book is shown to be O(logn)
	@Override
	
	public Entry<K, V> removeMin() {
		if(heap.isEmpty()) {
			return null;
		}
		Entry<K,V> answer = heap.get(0);
		swap(0, heap.size() - 1);
		heap.remove(heap.size() - 1);
		downheap(0);
		return answer;
	}
	//removes selected entry from heap
	//this method in the book is shown to be O(logn)
	@Override
	
	public void remove(Entry<K, V> entry) throws IllegalArgumentException {
	apqEntry<K,V> locator = validate(entry);
	int i = locator.getIndex();
	if(i == heap.size() - 1) {
		heap.remove(heap.size() - 1);
	}else {
		swap(i,heap.size() - 1);
		heap.remove(heap.size() - 1);
		Bubble(i);
	}
	}
   //replaces key in entry
	@Override
	
	//this method in the book is shown to be O(logn)
	public void replaceKey(Entry<K, V> entry, K key) throws IllegalArgumentException {
		apqEntry<K,V> locator = validate(entry);
		checkKey(key);
		locator.setKey(key);
		Bubble(locator.getIndex());
		
		
	}
	//replaces value in entry
	//this method in the book is shown to be O(1)
	@Override

	public void replaceValue(Entry<K, V> entry, V value) throws IllegalArgumentException {
		apqEntry<K,V> locator = validate(entry);
		locator.setValue(value);
		
	}
	
	


}
