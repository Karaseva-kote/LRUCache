import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class LRUCacheTest {
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void initPreConditionTest() { // pre: capacity > 0 (проверка ассерта)
		exception.expect(AssertionError.class);
		LRUCache<Integer, String> lru = new LRUCache<>(-1);
	}

	@Test
	public void initPostConditionTest() { // post: this.capacity = capacity && map.size = queue.size = 0
		LRUCache<Integer, String> lru = new LRUCache<>(3);
		Assert.assertEquals(lru.size(), 0);
		Assert.assertEquals(lru.getCapacity(), 3);
	}

	@Test
	public void containsKeyTest() { // result = (key ∈ map)
		LRUCache<Integer, String> lru = new LRUCache<>(10);
		lru.put(1, "1");
		lru.put(2, "2");
		Assert.assertTrue(lru.containsKey(1));
		Assert.assertTrue(lru.containsKey(2));
		Assert.assertFalse(lru.containsKey(3));
		Assert.assertFalse(lru.containsKey(36));
	}

	@Test
	public void putSimpleTest() { // queue.size = queue.size' + 1 && map.size = map.size' + 1 && (key, value) ∈ map
		LRUCache<Integer, String> lru = new LRUCache<>(2);
		lru.put(1, "______");
		Assert.assertEquals(lru.size(), 1);
		Assert.assertTrue(lru.containsKey(1));
		Assert.assertEquals(lru.get(1), "______");
	}

	@Test
	public void putFullCacheTest() { // if size' == capacity then queue.size = queue.size' && map.size = map.size' && (key, value) ∈ map
		int capacity = 2;
		LRUCache<Integer, String> lru = new LRUCache<>(capacity);
		lru.put(1, "1");
		lru.put(2, "2");
		lru.put(3, "3");
		Assert.assertEquals(lru.size(), capacity);
		Assert.assertFalse(lru.containsKey(1));
		Assert.assertTrue(lru.containsKey(2));
		Assert.assertTrue(lru.containsKey(3));
	}

	@Test
	public void putReplaceTest() { // if key ∈ map' then queue.size = queue.size' && map.size = map.size' && (key, value) ∈ map
		LRUCache<Integer, String> lru = new LRUCache<>(10);
		lru.put(1, "1");
		int expectedSize = lru.size();
		lru.put(1, "one");
		Assert.assertEquals(lru.size(), expectedSize);
		Assert.assertEquals(lru.get(1), "one");
	}

	@Test
	public void getNonExistentKeyTest() { // if key ∉ map then result = null && Immutable
		LRUCache<Integer, String> lru = new LRUCache<>(10);
		lru.put(1, "1");
		lru.put(2, "2");
		Assert.assertNull(lru.get(3));
	}

	@Test
	public void getExistingKeyTest() { // if key ∈ map then (key, result) ∈ map &&
		// for i = 0 .. (m - 1) : queue[i] = queue'[i]; for i = m .. (n - 2) : queue[i] = queue[i + 1]; queue[n - 1] = key
		// && size = size' && capacity = capacity' && map = map'
		LRUCache<Integer, String> lru = new LRUCache<>(2);
		lru.put(1, "first");
		lru.put(2, "second");
		int expectedCapacity = lru.getCapacity();
		int expectedSize = lru.size();

		String result = lru.get(1);

		Assert.assertEquals(expectedSize, lru.size());
		Assert.assertEquals(expectedCapacity, lru.getCapacity());

		Assert.assertTrue(lru.containsKey(1));
		Assert.assertEquals(result, "first");

		lru.put(3, "third");

		// проверка, что key = 1 был не последним в очереди, поэтому состояние (1, 3), а не (2, 3)
		Assert.assertTrue(lru.containsKey(1));
		Assert.assertFalse(lru.containsKey(2));
		Assert.assertTrue(lru.containsKey(3));
	}
}
