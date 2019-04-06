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

/*
function redirectPageLimitLink (params, num) {
	console.log("num: ", num);
	window.location.href = new_url+params[params.length-4]+"&"+params[params.length-3]+"&page=0&limit="+num;
}
*/

function handleMovieResult( resultData ) {
	
	//console.log(window.location.href);
	document.cookie = "last_search=" + window.location.href + "; path=/";
	
	function redirectPageLimitLink (num) {
		console.log("num: ", num);
		window.location.href = new_url+allparams[allparams.length-4]+"&"+allparams[allparams.length-3]+"&page=0&limit="+num;
	}
	
	console.log("json->string->table");

   // find the movie_table_body tag
   let movIt = jQuery("#movie_table_body");
   var movie;
   
   // loop thru each of the movie record from json
   for (i = 0; i < resultData.length; ++i) {
	   movie = "<tr><th>" + '<a href="single_movie.html?id=' + resultData[i].ID + '">' + resultData[i].TITLE + '</a><br>'
	   		     + "<font size=2><button onclick=addCart({'type':'1','item':'"+resultData[i].ID+"'})>Add to 🛒</button><br>" +'</font></th>' // link to singleMovie.html
	   		     + "<th>" + resultData[i].ID + "</th>"
                 + "<th>" + resultData[i].RATING+ "</th>"
                 + "<th>" + resultData[i].YEAR + "</th>"
                 + "<th>" + resultData[i].DIRECTOR + "</th><th>";
       for (j = 0; j < resultData[i].STARLIST.length - 1; ++j) {
    	   movie += '<a href="single_star.html?id=' + resultData[i].STARLIST[j].ID + '">' 
    	   + resultData[i].STARLIST[j].NAME + "</a>, ";
       }
       if (resultData[i].STARLIST.length > 0) {
    	   movie += '<a href="single_star.html?id=' + resultData[i].STARLIST[j].ID + '">'
    	   + resultData[i].STARLIST[j].NAME + "</a></th><th>";
       } else {
    	   movie += "</th><th>";
       }
      
       for (j = 0; j < resultData[i].GENRELIST.length - 1; ++j) {
    	   movie += resultData[i].GENRELIST[j].NAME + ", ";
       }
       if (resultData[i].GENRELIST.length > 0) {
    	   movie += resultData[i].GENRELIST[j].NAME + "</th></tr>";
       } else {
    	   movie += "</th></tr>";
       }
	   movIt.append(movie); // append
   }
   
   var allparams = params.split("&");
   console.log(allparams);
   // used when change ordering / page number / page size
   var new_url = "movie_list.html?";
   for (k = 0; k < allparams.length - 4; k++) {
	   new_url += allparams[k] + "&";
   }
   if (getParameterByName('sort') === "T") {
	   $("#change_sort_link").text("View by Rating");
	   $("#change_sort_link").attr("href",new_url+"sort=R&order=D&"+allparams[allparams.length-2]+"&"+allparams[allparams.length-1]);
   } else {
	   $("#change_sort_link").text("View by Title");
	   $("#change_sort_link").attr("href",new_url+"sort=T&order=A&"+allparams[allparams.length-2]+"&"+allparams[allparams.length-1]);
   }
   if (getParameterByName('order') === "A") {
	   $("#change_order_link").text("View by Descending Order");
	   $("#change_order_link").attr("href",new_url+allparams[allparams.length-4]+"&order=D&"+allparams[allparams.length-2]+"&"+allparams[allparams.length-1]);
   } else {
	   $("#change_order_link").text("View by Ascending Order");
	   $("#change_order_link").attr("href",new_url+allparams[allparams.length-4]+"&order=A&"+allparams[allparams.length-2]+"&"+allparams[allparams.length-1]);
   }
   
   if (getParameterByName('page') != 0) {
	   $("#prev_page_link").text("Previous Page");
	   $("#prev_page_link").attr("href",new_url+allparams[allparams.length-4]+"&"+allparams[allparams.length-3]+"&page="+String(Number(getParameterByName('page'))-1)+"&"+allparams[allparams.length-1]);
   }
   if (i == Number(getParameterByName('limit'))) {
	   $("#next_page_link").text("Next Page");
	   $("#next_page_link").attr("href",new_url+allparams[allparams.length-4]+"&"+allparams[allparams.length-3]+"&page="+String(Number(getParameterByName('page'))+1)+"&"+allparams[allparams.length-1]);
   }
   let new_limit_link = new_url+allparams[allparams.length-4]+"&"+allparams[allparams.length-3]+"&page=0&limit=";
   $("#limit_10_choice").attr("value",new_limit_link+"10");
   $("#limit_20_choice").attr("value",new_limit_link+"20");
   $("#limit_50_choice").attr("value",new_limit_link+"50");
   $("#limit_100_choice").attr("value",new_limit_link+"100");
   $("#page_limit_button").attr("onclick","window.location.href = page_limit_select.value ")
   console.log("Set page complete");
   // reset title
   if (getParameterByName('method') == "S") {
	   $("title").text("Search - Fabflix");
   } else {
	   $("title").text("Browse - Fabflix");
   }
}

let url = window.location.href;
var split = url.split("?");
var params = split[1];
let api_url = "api/movie_search?" + params;

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: api_url,
    success: (resultData) => handleMovieResult( resultData )
 });
