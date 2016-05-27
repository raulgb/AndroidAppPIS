package edu.ub.pis2016.pis16.strikecom.engine.util;

import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Array;

public class Pool<T> {

	public interface PoolObjectFactory<T> {
		T createObject();
	}


	private final Array<T> freeObjects;
	private final PoolObjectFactory<T> factory;
	private final int maxSize;

	public Pool(PoolObjectFactory<T> factory, int maxSize) {
		this.factory = factory;
		this.maxSize = maxSize;
		this.freeObjects = new Array<>(maxSize);
	}

	public T newObject() {
		T object = null;
		if (freeObjects.size == 0)
			object = factory.createObject();
		else
			object = freeObjects.removeIndex(freeObjects.size - 1);
		return object;
	}

	public void free(T object) {
		if (freeObjects.contains(object))
			return;

		if (freeObjects.size < maxSize)
			freeObjects.add(object);
	}
}
