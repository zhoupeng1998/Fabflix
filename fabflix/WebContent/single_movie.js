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

function handleMovieResult( resultData ){
    console.log("json->string->movieInfo");
 
    // find the movie_table_body tag
    let movIt = jQuery("#movie_content"), j;
    movIt.append("<h1>" + resultData[0].MOVIE[0].TITLE + "<br></h1>"); // Display name
    movIt.append( "<button onclick=addCart({'type':'1','item':'"+resultData[0].MOVIE[0].ID+"'})>Add to 🛒</button><br>");
    movIt.append("<b>Director: </b>" + resultData[0].MOVIE[0].DIRECTOR + "<br>"); // Display director
    movIt.append("<b>ID: </b>" + resultData[0].MOVIE[0].ID + "<br>"); // Display movie ID
    movIt.append("<b>Year: </b>" + resultData[0].MOVIE[0].YEAR + "<br>"); // Display movie year
    movIt.append("<b>Rating: </b>" + resultData[0].MOVIE[0].RATING + "<br>"); // Display movie rating
    movIt.append("<b>Votes #: </b>" + resultData[0].MOVIE[0].NUMVOTES + "<br>"); // Display votes of ratings
    movIt.append("<b>Genre: </b>" ); // Display movie genres
    for( j = 0; j < resultData[2].GENRELIST.length - 1; ++j ){
        movIt.append(resultData[2].GENRELIST[j].NAME);
        movIt.append(", ");
    }
    movIt.append(resultData[2].GENRELIST[j].NAME);
    movIt.append("<br><b>Stars: </b>" );
    for( j = 0; j < resultData[1].STARLIST.length - 1; ++j ){
        movIt.append('<a href="single_star.html?id=' +resultData[1].STARLIST[j].ID + '">' + resultData[1].STARLIST[j].NAME + '</a>');
        movIt.append(", ");
    }
    movIt.append('<a href="single_star.html?id=' +resultData[1].STARLIST[j].ID + '">' + resultData[1].STARLIST[j].NAME + '</a>');
    // reset title
    $("title").text(resultData[0].MOVIE[0].TITLE + " - Fabflix");
 }

// set return link to movie list page
//let last_serach = getCookie("last_search");
if (getCookie("last_search") != null || getCookie("last_search") != "") {
	$("#back_link").attr("href",getCookie("last_search"));
} else {
	$("#back_link").text("");
}

let movieId = getParameterByName('id');
 
 jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/single_movie?id=" + movieId,
    success: (resultData) => handleMovieResult( resultData )
 });