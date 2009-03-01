package org.programmerplanet.crm.model;

import java.util.UUID;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public abstract class Entity {

	private UUID id;

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Entity) {
			if (this.getClass().equals(obj.getClass())) {
				Entity other = (Entity)obj;
				if (this.id == null && other.id == null) {
					return super.equals(obj);
				}
				else if (this.id == null || other.id == null) {
					return false;
				}
				else {
					return this.id.equals(other.id);
				}
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	public int hashCode() {
		if (this.id != null) {
			return this.id.hashCode();
		}
		else {
			return super.hashCode();
		}
	}

	public String toString() {
		return this.getClass().getName() + (this.id != null ? ": " + this.id.toString() : "");
	}

}
