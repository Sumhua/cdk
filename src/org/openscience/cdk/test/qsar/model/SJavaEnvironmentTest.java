/* 
 * Copyright (C) 2004  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. 
 */

package org.openscience.cdk.test.qsar.model;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.qsar.model.QSARModelException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * TestSuite that checks some SJava requirements
 *
 * @author Rajarshi Guha
 * @cdk.require r-project
 * @cdk.module test
 */
 
public class SJavaEnvironmentTest extends TestCase {
	
	public  SJavaEnvironmentTest() {}
    
	public static Test suite() {
		return new TestSuite(SJavaEnvironmentTest.class);
        }

        public void testSJavaEnvironment()  {
            String rhomevalue = null;
            try {
                rhomevalue = System.getenv("R_HOME");
            } catch (NullPointerException npe) {
            }
            assertTrue("The R_HOME environment variable must point to\nthe location of your R installation.\nOther model tests will also fail.",rhomevalue != null);
            assertTrue("The R_HOME environment variable must point to\nthe location of your R installation.\nOther model tests will also fail.",!rhomevalue.equals(""));
        }

}

