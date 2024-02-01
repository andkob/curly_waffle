package cache;

import java.util.LinkedList;

/**
 * This class defines a generic class Cache<T> that represents a cache data structure.
 * This cache is implemented using a linked list to store elements of type T. 
 * The cache has a maximum size specified during its instantiation, and it 
 * maintains counts for the number of cache references (NR), the number of cache 
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
	
	/**
     * Constructs a Cache object with the specified maximum size.
     *
     * @param size The maximum size of the cache.
     */
	public Cache(int size) {
		cache = new LinkedList<T>();
		cacheSize = size;
		NR = NH = 0;
	}
	
	/**
     * Retrieves the object at the specified index in the cache.
     *
     * @param index The index of the object to retrieve.
     * @return The object at the specified index in the cache.
     */
	public T getObject(int index) {
		return cache.get(index);
	}
	
	/**
     * Adds an object to the cache. If the cache is full, removes the last element to make room for the new one.
     *
     * @param obj The object to be added to the cache.
     */
	public void addObject(T obj) {
		if (cache.size() == cacheSize) {
			cache.removeLast();
		}
		cache.addFirst(obj);
	}
	
	 /**
     * Removes the specified object from the cache.
     *
     * @param obj The object to be removed from the cache.
     * @return true if the object was successfully removed, false otherwise.
     */
	public boolean removeObject(T obj) {
		return cache.remove(obj);
	}
	
	/**
     * Clears all elements from the cache.
     */
	public void clearCache() {
		cache.clear();
	}
	
	/**
     * Searches for an object in the cache. If found (hit), updates hit count and moves the object to the top of the cache.
     *
     * @param obj The object to search for in the cache.
     * @return true if the object is found in the cache (hit), false otherwise.
     */
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
		return isHit;
	}
	
	/**
     * Moves the specified object to the top of the cache.
     *
     * @param obj The object to be moved to the top of the cache.
     */
	public void moveToTop(T obj) {
		cache.remove(obj);
		cache.addFirst(obj);
	}

	/**
     * Gets the number of cache reads (NR).
     *
     * @return The number of cache references.
     */
	public int getNR() {return NR;}
	
	/**
     * Gets the number of cache hits (NH).
     *
     * @return The number of cache hits.
     */
	public int getNH() {return NH;}
	
	/**
     * Gets the cache hit ratio (HR), which is the ratio of cache hits to cache references.
     *
     * @return The cache hit ratio.
     */
	public double getHR() {return (double) NH / (double) NR;}
}
