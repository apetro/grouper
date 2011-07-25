package edu.internet2.middleware.grouper.ws.soap_v2_0;


/**
 * <pre>
 * results for the has member call.
 * 
 * result code:
 * code of the result for this group overall
 * SUCCESS: means everything ok
 * GROUP_NOT_FOUND: cant find the group
 * GROUP_DUPLICATE: found multiple groups
 * </pre>
 * @author mchyzer
 */
public class WsHasMemberLiteResult {

  /**
   * results for each assignment sent in
   */
  private WsHasMemberResult[] results;

  /**
   * group that we are checking 
   */
  private WsGroup wsGroup;

  /**
   * metadata about the result
   */
  private WsResultMeta resultMetadata = new WsResultMeta();

  /**
   * attributes of subjects returned, in same order as the data
   */
  private String[] subjectAttributeNames;

  /**
   * metadata about the result
   */
  private WsResponseMeta responseMetadata = new WsResponseMeta();

  /**
   * sujbect info for hasMember 
   */
  private WsSubject wsSubject;

  /**
   * results for each assignment sent in
   * @return the results
   */
  public WsHasMemberResult[] getResults() {
    return this.results;
  }

  /**
   * results for each assignment sent in
   * @param results1 the results to set
   */
  public void setResults(WsHasMemberResult[] results1) {
    this.results = results1;
  }

  /**
   * @return the wsGroup
   */
  public WsGroup getWsGroup() {
    return this.wsGroup;
  }

  /**
   * @param wsGroup1 the wsGroup to set
   */
  public void setWsGroup(WsGroup wsGroup1) {
    this.wsGroup = wsGroup1;
  }

  /**
   * @return the resultMetadata
   */
  public WsResultMeta getResultMetadata() {
    return this.resultMetadata;
  }

  /**
   * attributes of subjects returned, in same order as the data
   * @return the attributeNames
   */
  public String[] getSubjectAttributeNames() {
    return this.subjectAttributeNames;
  }

  /**
   * attributes of subjects returned, in same order as the data
   * @param attributeNamesa the attributeNames to set
   */
  public void setSubjectAttributeNames(String[] attributeNamesa) {
    this.subjectAttributeNames = attributeNamesa;
  }

  /**
   * @see edu.internet2.middleware.grouper.ws.rest.WsResponseBean#getResponseMetadata()
   * @return the response metadata
   */
  public WsResponseMeta getResponseMetadata() {
    return this.responseMetadata;
  }

  /**
   * @param responseMetadata1 the responseMetadata to set
   */
  public void setResponseMetadata(WsResponseMeta responseMetadata1) {
    this.responseMetadata = responseMetadata1;
  }

  /**
   * @return the wsSubject
   */
  public WsSubject getWsSubject() {
    return this.wsSubject;
  }

  /**
   * @param wsSubjectResult1 the wsSubject to set
   */
  public void setWsSubject(WsSubject wsSubjectResult1) {
    this.wsSubject = wsSubjectResult1;
  }

  /**
   * construct from results of other
   */
  public WsHasMemberLiteResult() {
    //empty
  }
  /**
   * @param resultMetadata1 the resultMetadata to set
   */
  public void setResultMetadata(WsResultMeta resultMetadata1) {
    this.resultMetadata = resultMetadata1;
  }

}