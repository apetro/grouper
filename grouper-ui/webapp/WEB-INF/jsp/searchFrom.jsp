<%-- @annotation@
		Tile which displays a select list of parent stems to scope a search
		to a branch of the groups hierarchy
--%><%--
  @author Gary Brown.
  @version $Id: searchFrom.jsp,v 1.4 2008-04-10 19:50:25 mchyzer Exp $
--%>
<%@include file="/WEB-INF/jsp/include.jsp"%>
<grouper:recordTile key="Not dynamic" tile="${requestScope['javax.servlet.include.servlet_path']}">
<c:if test="${!empty browsePath}">
<tr class="formTableRow">
  <td class="formTableLeft">
   	<label for="searchFrom"><grouper:message bundle="${nav}" key="find.search-from"/></label>
  </td>
  <td class="formTableRight">
  	<select name="searchFrom" id="searchFrom">
    	<c:forEach var="stem" items="${browsePath}">
    		<option value="<c:out value="${stem.name}"/>">
    			<c:choose>
    				<c:when test="${empty stem.displayExtension}">
    					<grouper:message bundle="${nav}" key="stem.root.display-name"/>
    				</c:when>
    				<c:otherwise>
    					<c:out value="${stem.displayExtension}"/>
    				</c:otherwise>
    			</c:choose>
    			
    		</option>
    	</c:forEach>
    	
    	<c:if test="${currentLocation.isStem}">
    		<option value="<c:out value="${currentLocation.name}"/>">
    			<c:out value="${currentLocation.displayExtension}"/>
    	</c:if>
    </select>
  </td>
</tr>

</c:if>
</grouper:recordTile>