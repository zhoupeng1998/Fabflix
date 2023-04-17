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

function handleStarResult( resultData ){
    console.log("json->string->table");
 
    // find the movie_table_body tag
    let movIt = jQuery("#star_profile"), j;
    movIt.append("<h1>" + resultData[0].STAR[0].NAME + "<br></h1>"); // Display STAR name
    movIt.append("<b>ID: </b>" + resultData[0].STAR[0].ID + "<br>"); // Display movie ID
    movIt.append("<b>Birthyear: </b>" + resultData[0].STAR[0].BIRTHYEAR + "<br>"); // Display star birthyear
    movIt.append("<b>Known for: </b>")
    for( j = 0; j < resultData[1].MOVIELIST.length - 1; ++j ){
        movIt.append('<a href="single_movie.html?id=' + resultData[1].MOVIELIST[j].ID + '">' + resultData[1].MOVIELIST[j].TITLE + '</a>');
        movIt.append(", ");
    }
    movIt.append('<a href="single_movie.html?id=' + resultData[1].MOVIELIST[j].ID + '">' + resultData[1].MOVIELIST[j].TITLE + '</a>');
    $("title").text(resultData[0].STAR[0].NAME + " - Fabflix");
 }
 
//set return link to movie list page
//let last_serach = getCookie("last_search");
if (getCookie("last_search") != null || getCookie("last_search") != "") {
    $("#back_link").attr("href",getCookie("last_search"));
} else {
    $("#back_link").text("");
}

 let starId = getParameterByName('id');
 //console.log(starId);
 
 jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "api/single_star?id=" + starId,
    success: (resultData) => handleStarResult( resultData )
 });
 