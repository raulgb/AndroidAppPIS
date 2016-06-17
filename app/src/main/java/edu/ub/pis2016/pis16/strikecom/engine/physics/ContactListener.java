package edu.ub.pis2016.pis16.strikecom.engine.physics;

public abstract class ContactListener {

	/**
	 * Special class whose hash does not vary if bodies are in inverse other and compares true even if fields
	 * are flipped.
	 */
	public static class Contact {
		public Body a;
		public Body b;
		public float contactX, contactY;
		//public long timestamp;

		@Override
		public int hashCode() {
			return a.hashCode() + b.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Contact) {
				Contact contact = (Contact) o;
				return (contact.a == a && contact.b == b) || (contact.b == a && contact.a == b);
			}
			return false;
		}
	}

	public void beginContact(Contact c) {
	}

	public void endContact(Contact c) {
	}

}
