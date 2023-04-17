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

// html tags arrangement
function handleConfResult( resultData ){
    console.log("json->string->table")
    let movIt = jQuery("#conf_table_body");
    var movie;
    if( resultData.length != 0){
        // loop thru each of the movie record from json
        for (i = 0; i < resultData.length; ++i ) {
           movie = "<tr><th>" + resultData[i].SIA + "</th>" 
                 + "<th>" + '<a href="single_movie.html?id=' + resultData[i].movid + '">' + resultData[i].title + "</a></th>" // link to singleMovie.html
                 + "<th>" + resultData[i].amount + "</th></tr>";
           movIt.append(movie); // append
        }
    } else {
        // error msg
        jQuery("#conf_info").append("<div align='center'>ERROR</div>");
    }
}

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url : "api/confirm",
    success: (resultData) => handleConfResult(resultData)
});