/*         \\\\\\\\\\\\\\\\\\\\|||||||////////////////////  
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *             The Buddha Blesses Us There's Never Bugs!
 */

function addCart( movieID ){
	$.ajax({
		type: 'GET',
		url: 'api/index',
		data: movieID,
		success: alert("add movie success")
	});
}

function updateCart( movieID ){
	$.ajax({
		type: 'GET',
		url: 'api/index',
		data: movieID,
		success: alert("update movie success")
	});
}

function removeCart( movieID ){
	$.ajax({
		type: 'GET',
		url: 'api/index',
		data: movieID,
		success: alert("remove movie success")
	});
}

// adopted from https://blog.csdn.net/ for future use
function overShow(){
   var showDiv = document.getElementById('showDiv');
   showDiv.style.left = event.clientX;
   showDiv.style.top = event.clientY;
   showDiv.style.display = 'block';
   showDiv.innerHTML = 'buy me !!';
}

function outHide(){
	var showDiv = document.getElementById('showDiv');
	showDiv.style.display = 'none';
	showDiv.innerHTML = '';
}

// adopted from UCI CS122B Winter 2019 Project 1 example
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

// adopted from runoob.com
function getCookie(cname)
{
  var name = cname + "=";
  var ca = document.cookie.split(';');
  for(var i=0; i<ca.length; i++) 
  {
    var c = ca[i].trim();
    if (c.indexOf(name)==0) return c.substring(name.length,c.length);
  }
  return "";
}