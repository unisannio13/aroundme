package it.unisannio.aroundme.model.test.serializer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** 
 * @author Michele Piccirillo <michele.piccirillo@gmail.com>
 */
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(CompatibilitySerializerTest.class);
		suite.addTestSuite(InterestSerializerTest.class);
		suite.addTestSuite(NeighbourhoodSerializerTest.class);
		suite.addTestSuite(PositionSerializerTest.class);
		suite.addTestSuite(PreferencesSerializerTest.class);
		suite.addTestSuite(UserQueySerializerTest.class);
		suite.addTestSuite(UserSerializerTest.class);
		//$JUnit-END$
		return suite;
	}

}