import org.jetbrains.annotations.Nullable;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUCache<K, V> {
	private final Map<K, V> map = new HashMap<>();
	private final Deque<K> queue = new LinkedList<>();
	private final int capacity;

	// pre: capacity > 0
	public LRUCache(int capacity) {
		assert capacity > 0;
		this.capacity = capacity;
	}
	// post: this.capacity = capacity && map.size = queue.size = 0

	@Nullable
	public V get(K key) {
		if (map.containsKey(key)) {
			queue.remove(key);
			queue.add(key);
		}
		return map.get(key);
	}
	// note: m is such that queue'[m] = key, n = queue.size
	// post: if key ∈ map then (key, result) ∈ map &&
	// for i = 0 .. (m - 1) : queue[i] = queue'[i]; for i = m .. (n - 2) : queue[i] = queue[i + 1]; queue[n - 1] = key
	// && size = size' && capacity = capacity' && map = map'
	//                    else result = null && Immutable

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}
	// result = (key ∈ map)

	public void put(K key, V value) {
		if (map.containsKey(key)) {
			map.replace(key, value);
			return;
		}

		if (queue.size() == capacity) {
			K delKey = queue.remove();
			map.remove(delKey);
		}
		queue.add(key);
		map.put(key, value);
	}
	// post: if key ∈ map' || size' == capacity
	// 		then queue.size = queue.size' && map.size = map.size' && (key, value) ∈ map
	//      else queue.size = queue.size' + 1 && map.size = map.size' + 1 && (key, value) ∈ map

	public int size() {
		return queue.size();
	}
	// post: result = queue.size = map.size <= capacity && Immutable


	public int getCapacity() {
		return capacity;
	}
	// post: result = capacity && Immutable
}
