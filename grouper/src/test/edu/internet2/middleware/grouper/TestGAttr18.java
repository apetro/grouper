/*
  Copyright (C) 2004-2007 University Corporation for Advanced Internet Development, Inc.
  Copyright (C) 2004-2007 The University Of Chicago

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package edu.internet2.middleware.grouper;
import org.apache.commons.logging.Log;

import edu.internet2.middleware.grouper.exception.AttributeNotFoundException;
import edu.internet2.middleware.grouper.privs.AccessPrivilege;
import edu.internet2.middleware.grouper.registry.RegistryReset;
import edu.internet2.middleware.grouper.util.GrouperUtil;
import edu.internet2.middleware.subject.Subject;

/**
 * @author  blair christensen.
 * @version $Id: TestGAttr18.java,v 1.9 2008-09-29 03:38:27 mchyzer Exp $
 * @since   1.1.0
 */
public class TestGAttr18 extends GrouperTest {

  private static final Log LOG = GrouperUtil.getLog(TestGAttr18.class);

  public TestGAttr18(String name) {
    super(name);
  }

  protected void setUp () {
    LOG.debug("setUp");
    RegistryReset.reset();
  }

  protected void tearDown () {
    LOG.debug("tearDown");
  }

  public void testFailDeleteAttributeNullAttributeNotRootButAllHasAdmin() {
    LOG.info("testFailDeleteAttributeNullAttributeNotRootButAllHasAdmin");
    try {
      R       r     = R.populateRegistry(1, 1, 1);
      Group   gA    = r.getGroup("a", "a");
      Subject subjA = r.getSubject("a");
      gA.grantPriv( SubjectFinder.findAllSubject(), AccessPrivilege.ADMIN );
      r.rs.stop();  
      GrouperSession.start(subjA);
      try {
        gA.deleteAttribute(null);
        fail("deleted null attribute");
      }
      catch (AttributeNotFoundException eANF) {
        assertTrue(true);
      }
    }
    catch (Exception e) {
      unexpectedException(e);
    }
  } // public void testFailDeleteAttributeNullAttributeNotRootButAllHasAdmin()

} // public class TestGAttr18
