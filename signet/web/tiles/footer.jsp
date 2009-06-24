<!--
  $Id: footer.jsp,v 1.7 2007-05-09 04:40:53 lmcrae Exp $
  $Date: 2007-05-09 04:40:53 $
  
  Copyright 2007 Internet2 and Stanford University.  All Rights Reserved.
  Licensed under the Signet License, Version 1,
  see doc/license.txt in this distribution.
-->
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="edu.internet2.middleware.signet.resource.ResLoaderUI" %>
<%@ page import="edu.internet2.middleware.signet.resource.ResLoaderApp" %>

<DIV id="Footer">
  <P>
    <%=ResLoaderUI.getString("footer.copyright.txt") %>
    <BR />
    <%="Signet " + ResLoaderApp.getString("signet_version") %>
    |
    <A href=<%=ResLoaderUI.getString("footer.terms.href") %> target="_blank">
      <%=ResLoaderUI.getString("footer.terms.txt") %>
    </A>
    |
    <A href=<%=ResLoaderUI.getString("footer.feedback.href") %> title="Signet feedback">
      <%=ResLoaderUI.getString("footer.feedback.txt") %>
    </A>
  </P>
</DIV>