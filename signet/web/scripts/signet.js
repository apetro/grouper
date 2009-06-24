function openWindow(theURL,winName,features) { 
  var newWin=window.open(theURL,winName,features);
  newWin.focus();
}

function showSearch() {
	var toga = document.getElementById('Mylist');
	var togb = document.getElementById('Search');	
		toga.style.display = 'none';	
		togb.style.display = 'block';			
}

function showResult() {
	var toga = document.getElementById('Results');
	var togb = document.getElementById('Browse');
		toga.style.display = 'block';	
		togb.style.display = 'none';
}

function showBrowse() {
	var toga = document.getElementById('Results');
	var togb = document.getElementById('Browse');
		toga.style.display = 'none';	
		togb.style.display = 'block';
}

function CheckAll(){
	document.checkform.row1.checked = document.checkform.allbox.checked;
	document.checkform.row2.checked = document.checkform.allbox.checked;
	document.checkform.row3.checked = document.checkform.allbox.checked;
	document.checkform.row4.checked = document.checkform.allbox.checked;
	document.checkform.row5.checked = document.checkform.allbox.checked;
	document.checkform.row6.checked = document.checkform.allbox.checked;
}

var personSearchFieldHasFocus = false;
var personSearchButtonHasFocus = false;

function personSearchShouldBePerformed(searchStrFieldName)
{
  if (cursorInPersonSearch() && !personSearchStrIsEmpty(searchStrFieldName))
  {
    return true;
  }
  else
  {
    return false;
  }
}

function performPersonSearch
  (personSearchPage,
   searchStrFieldName,
   destDivId)
{
  loadXMLDoc
    (personSearchPage
     + '?searchString='
     + document.getElementById(searchStrFieldName).value,
     destDivId);
}

// If the text-cursor is in the person-search field, this method
// executes the person-search, and returns false. Otherwise, it returns
// true.
function checkForCursorInPersonSearch
  (personSearchPage,
   searchStrFieldName,
   destDivId)
{
  if (cursorInPersonSearch() == true)
  {
    if (!personSearchStrIsEmpty(searchStrFieldName))
    {
      performPersonSearch(personSearchPage, searchStrFieldName, destDivId);
    }
       
    // Disallow the form submission.
    return false;
  }
  
  // Allow the form-submission.
  return true;
}

function personSearchStrIsEmpty(searchStrFieldName)
{
  searchStr = document.getElementById(searchStrFieldName).value;
  if (searchStr.length > 0)
  {
    return false;
  }
  else
  {
    return true;
  }
}

// Returns true if the text-cursor is in the person-search field, and false
// otherwise.
function cursorInPersonSearch()
{
  returnVal = (personSearchFieldHasFocus || personSearchButtonHasFocus);

  return (returnVal);
}

var requestObject;
var destinationDivId;

// This code was adapted from the following web pages:
//
//   www.xml.com/pub/a/2005/02/09/xml-http-request.html
//   www.francisshanahan.com/zuggest.aspx

function loadXMLDoc(url, destDivId)
{
  // Set the cursor to an hourglass, to show we're really doing some serious
  // computin'.
  document.body.style.cursor = "wait";

  url = "jsp/" + url;
  
  destinationDivId = destDivId;
  
  if (window.XMLHttpRequest)
  {
    requestObject = new XMLHttpRequest();

    if (requestObject)
    {
      requestObject.onreadystatechange = processReqChange;
      requestObject.open("GET", url, true);
      
      // This line prevents any browser from using its internal cache to satisfy
      // this request. This sidesteps a bug in Safari (June 2005).
      // For more information, see this URL:
      //    http://www.bitterpill.org/logid/1117777362000
      requestObject.setRequestHeader
        ('If-Modified-Since', 'Wed, 15 Nov 1995 00:00:00 GMT');

      requestObject.send(null);
    }
  }
  else if (window.ActiveXObject)
  {
    requestObject = new ActiveXObject("Microsoft.XMLHTTP");

    if (requestObject)
    {
      requestObject.onreadystatechange = processReqChange;
      requestObject.open("GET", url, true);
      
      // This line prevents any browser from using its internal cache to satisfy
      // this request. This sidesteps a bug in Safari (June 2005).
      // For more information, see this URL:
      //    http://www.bitterpill.org/logid/1117777362000
      requestObject.setRequestHeader
        ('If-Modified-Since', 'Wed, 15 Nov 1995 00:00:00 GMT');
        
      requestObject.send();
    }
  }
  
  // This return value always prevents form-submission of the calling form,
  // thereby allowing us to just edit the existing page in place.
  return false;
}

function processReqChange()
{
  if (requestObject.readyState == 4) // Request is complete
  {
    // Set the cursor back from an hourglass to its normal shape.
    document.body.style.cursor = "default";
    
    if (requestObject.status == 200) // Status is OK
    {
      var searchResultsElement = document.getElementById(destinationDivId);
      searchResultsElement.style.display = 'block';
      searchResultsElement.innerHTML = requestObject.responseText;
    }
    else
    {
      alert
        ("There was a problem retrieving the data: requestObject="
         + requestObject
         + "\nrequestObject.status = "
         + requestObject.status
         + "\nrequestObject.statusText = "
         + requestObject.statusText);
    }
  }
}