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

// handle Textfield change
function changeAmount( movieID ){
	document.getElementById("error").style.visibility = "hidden";
	console.log("Event Form");
	// get the new num value from text input
	var num = document.getElementById(movieID).value;
	// check if it is int and in the correct range
	if( num % 1 == 0 && num > -1 && num < 11){
		if( num == 0 )
			removeCart({'type':'2','item':movieID});
		else
			updateCart({'type':'3','item':movieID,'amount':num});
		window.history.go(0);
	}
	// not valid
	else{
		document.getElementById("error").style.visibility = "visible";
	}
}

// html tags arrangement
function handleCartResult( resultData ){
	console.log("json->string->table")
	document.getElementById("error").style.visibility = "hidden";
	let movIt = jQuery("#cart_table_body");
	var movie;
	if( resultData.length != 0){
	    // loop thru each of the movie record from json
		var sum = 0;
	    for (i = 0; i < resultData.length; ++i) {
	       movie = "<tr><th>" + '<a href="single_movie.html?id=' + resultData[i].ID + '">'
    	   	     + resultData[i].TITLE + '   </a>'+"<button onclick=removeCart({'type':'2','item':'"+resultData[i].ID+"'});window.history.go(0)>➖</button><br></th>" // link to singleMovie.html
	             + "<th>" + resultData[i].YEAR + "</th>"
	             + "<th>" + resultData[i].DIRECTOR + "</th>"
	             // text field for item quantity
	             + "<th>" + "<input type = 'number' onchange=changeAmount('"+resultData[i].ID+"') value='"+resultData[i].AMOUNT+"' min = '0', max='10' id='"+resultData[i].ID+"'>" 
	             		//"<input class = 'amount' name="+resultData[i].ID+" type='number' onchange='changeAmount('"+resultData[i].ID+"')' " +
	             		//	"value='"+resultData[i].AMOUNT+"' min='0' max='10'>" +
	             +	"</th><th>" + resultData[i].AMOUNT * 300 + "</th></tr>";
	       sum += resultData[i].AMOUNT * 300;
	       movIt.append(movie); // append
    	}
	    jQuery("#container").append("<div>Total Price :"+sum+"</div>");
	    $("#proceed_link").text("Checkout")
	}else{
		jQuery("#cart_info").append("<div align='center'>Hon, Your cart is empty! Go and grab something</div>");
	}
}

jQuery.ajax({
	dataType: "json",
	method: "GET",
	url : "api/shop_cart",
	success: (resultData) => handleCartResult( resultData )
});