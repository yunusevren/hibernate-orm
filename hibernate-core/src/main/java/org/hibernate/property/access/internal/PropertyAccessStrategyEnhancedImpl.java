/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.property.access.internal;

import org.hibernate.property.access.spi.BuiltInPropertyAccessStrategies;
import org.hibernate.property.access.spi.PropertyAccess;
import org.hibernate.property.access.spi.PropertyAccessStrategy;

import jakarta.persistence.AccessType;

/**
 * Defines a strategy for accessing property values via a get/set pair, which may be nonpublic.  This
 * is the default (and recommended) strategy.
 *
 * @author Steve Ebersole
 * @author Gavin King
 */
public class PropertyAccessStrategyEnhancedImpl implements PropertyAccessStrategy {
	public static PropertyAccessStrategyEnhancedImpl with(AccessType getterAccessType) {
		if ( getterAccessType == AccessType.FIELD ) {
			return FIELD;
		}
		return STANDARD;
	}

	private final AccessType getterAccessType;

	public static PropertyAccessStrategyEnhancedImpl STANDARD = new PropertyAccessStrategyEnhancedImpl( null );
	public static PropertyAccessStrategyEnhancedImpl FIELD = new PropertyAccessStrategyEnhancedImpl( AccessType.FIELD );

	public PropertyAccessStrategyEnhancedImpl(AccessType getterAccessType) {
		this.getterAccessType = getterAccessType;
	}

	@Override
	public PropertyAccess buildPropertyAccess(Class<?> containerJavaType, final String propertyName, boolean setterRequired) {
		return new PropertyAccessEnhancedImpl( this, containerJavaType, propertyName, getterAccessType );
	}
}
