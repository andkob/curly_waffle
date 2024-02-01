package cache;

import java.util.LinkedList;

/**
 * This class defines a generic class Cache<T> that represents a cache data structure.
 * This cache is implemented using a linked list to store elements of type T. 
 * The cache has a maximum size specified during its instantiation, and it 
 * maintains counts for the number of cache reads (NR), the number of cache 
 * hits (NH), and provides a hit ratio (HR).
 * This cache implementation follows a basic Least Recently Used (LRU) eviction policy.
 * 
 * @author Andrew Kobus
 * @see Test
 *
 * @param <T>
 */
public class Cache<T> {
	private LinkedList<T> cache;
	private int cacheSize;
	private int NR, NH;
	
	public Cache(int size) {
		cache = new LinkedList<T>();
		cacheSize = size;
		NR = NH = 0;
	}
	
	public T getObject(int index) {
		return cache.get(index);
	}
	
	public void addObject(T obj) {
		if (cache.size() == cacheSize) {
			cache.removeLast();
		}
		cache.addFirst(obj);
	}
	
	public boolean removeObject(T obj) {
		return cache.remove(obj);
	}
	
	public void clearCache() {
		cache.clear();
	}
	
	public boolean search(T obj) {
		NR++;
		boolean isHit = false;
		// search cache
		for (T element : cache) {
			if (element.equals(obj)) { // hit
				isHit = true;
				NH++;
				moveToTop(obj);
				break;
			}
		}
//		if (!isHit) {
//			addObject(obj);
//		}
		return isHit;
	}
	
	public void moveToTop(T obj) {
		cache.remove(obj);
		cache.addFirst(obj);
	}

	public int getNR() {return NR;}
	
	public int getNH() {return NH;}
	
	public double getHR() {return (double) NH / (double) NR;}
}
