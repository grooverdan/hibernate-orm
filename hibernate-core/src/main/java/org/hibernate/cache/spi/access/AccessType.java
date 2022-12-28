/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html.
 */
package org.hibernate.cache.spi.access;

import java.util.Locale;

/**
 * Enumerates the policies for managing concurrent access to the shared
 * second-level cache.
 *
 * @author Steve Ebersole
 */
public enum AccessType {
	/**
	 * Read-only access. Data may be added and removed, but not mutated.
	 */
	READ_ONLY( "read-only" ),
	/**
	 * Read and write access. Data may be added, removed and mutated.
	 * A "soft" lock on the cached item is used to manage concurrent
	 * access during mutation.
	 */
	READ_WRITE( "read-write" ),
	/**
	 * Read and write access. Data may be added, removed and mutated.
	 * The cached item is invalidated before and after transaction
	 * completion to manage concurrent access during mutation. This
	 * strategy is more vulnerable to inconsistencies than
	 * {@link #READ_WRITE}, but may allow higher throughput.
	 */
	NONSTRICT_READ_WRITE( "nonstrict-read-write" ),
	/**
	 * Read and write access. Data may be added, removed and mutated.
	 * Some sort of hard lock is maintained in conjunction with a
	 * JTA transaction.
	 */
	TRANSACTIONAL( "transactional" );

	private final String externalName;

	AccessType(String externalName) {
		this.externalName = externalName;
	}

	/**
	 * Get the external name of this value.
	 *
	 * @return The corresponding externalized name.
	 */
	public String getExternalName() {
		return externalName;
	}

	@Override
	public String toString() {
		return "AccessType[" + externalName + "]";
	}

	/**
	 * Resolve an {@link AccessType} from its external name.
	 *
	 * @param externalName The external representation to resolve
	 * @return The {@link AccessType} represented by the given external name
	 * @throws UnknownAccessTypeException if the external name was not recognized
	 *
	 * @see #getExternalName()
	 */
	public static AccessType fromExternalName(String externalName) {
		if ( externalName == null ) {
			return null;
		}
		for ( AccessType accessType : AccessType.values() ) {
			if ( accessType.getExternalName().equals( externalName ) ) {
				return accessType;
			}
		}
		// Check to see if making upper-case matches an enum name.
		try {
			return AccessType.valueOf( externalName.toUpperCase( Locale.ROOT ) );
		}
		catch ( IllegalArgumentException e ) {
			throw new UnknownAccessTypeException( externalName );
		}
	}
}
