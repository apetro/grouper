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

import edu.internet2.middleware.grouper.exception.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.registry.RegistryReset;
import edu.internet2.middleware.grouper.util.GrouperUtil;

/**
 * @author  blair christensen.
 * @version $Id: TestHSubject1.java,v 1.7 2008-09-29 03:38:27 mchyzer Exp $
 * @since   1.1.0
 */
public class TestHSubject1 extends GrouperTest {

  // Private Static Class Constants
  private static final Log LOG = GrouperUtil.getLog(TestHSubject1.class);

  public TestHSubject1(String name) {
    super(name);
  }

  protected void setUp () {
    LOG.debug("setUp");
    RegistryReset.reset();
  }

  protected void tearDown () {
    LOG.debug("tearDown");
  }

  public void testFailToAddRegistrySubjectAsNonRoot() {
    LOG.info("testFailToAddRegistrySubjectAsNonRoot");
    try {
      RegistrySubject.add(
        GrouperSession.start( SubjectFinder.findAllSubject() ),
        "subj id", "subj type", "subj name"
      );
      fail("added hibernate subject as !root");
    }
    catch (InsufficientPrivilegeException eIP) {
      assertTrue(true);
    }
    catch (Exception e) {
      unexpectedException(e);
    }
  } // public void testFailToAddRegistrySubjectAsNonRoot()

} // public class TestHSubject1
