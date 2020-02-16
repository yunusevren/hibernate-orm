/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.orm.test.query.hql;

import org.hibernate.testing.junit5.SessionFactoryBasedFunctionalTest;
import org.hibernate.testing.orm.domain.gambit.SimpleEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Gavin King
 */
public class SubqueryOperatorsTest extends SessionFactoryBasedFunctionalTest {

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				SimpleEntity.class,
		};
	}

	@Test
	public void testEvery() {
		inTransaction(
				session -> {
					List results = session.createQuery(
							"from SimpleEntity o where o.someString >= every (select someString from SimpleEntity)" )
							.list();
					assertThat( results.size(), is( 1 ) );
				} );
	}

	@Test
	public void testAny() {
		inTransaction(
				session -> {
					List results = session.createQuery(
							"from SimpleEntity o where o.someString >= any (select someString from SimpleEntity)" )
							.list();
					assertThat( results.size(), is( 2 ) );
				} );
	}

	@Test
	public void testExists() {
		inTransaction(
				session -> {
					List results = session.createQuery(
							"from SimpleEntity o where exists (select someString from SimpleEntity where someString>o.someString)" )
							.list();
					assertThat( results.size(), is( 1 ) );
					results = session.createQuery(
							"from SimpleEntity o where not exists (select someString from SimpleEntity where someString>o.someString)" )
							.list();
					assertThat( results.size(), is( 1 ) );
				} );
	}

	@BeforeEach
	public void setUp() {
		inTransaction(
				session -> {
					SimpleEntity entity = new SimpleEntity(
							1,
							Calendar.getInstance().getTime(),
							null,
							Integer.MAX_VALUE,
							Long.MAX_VALUE,
							"aaa"
					);
					session.save( entity );

					SimpleEntity second_entity = new SimpleEntity(
							2,
							Calendar.getInstance().getTime(),
							null,
							Integer.MIN_VALUE,
							Long.MAX_VALUE,
							"zzz"
					);
					session.save( second_entity );

				} );
	}

	@AfterEach
	public void tearDown() {
		inTransaction(
				session -> {
					session.createQuery( "delete SimpleEntity" ).executeUpdate();
				} );
	}
}