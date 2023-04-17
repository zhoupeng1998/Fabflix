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

function handleLoginResult (resultDataString) {
    // get POST result from database
    resultDataJSON = JSON.parse(resultDataString);
    console.log("Handling Login Response...");
    console.log(resultDataJSON["status"]);
    // if login success
    if (resultDataJSON["status"] === "success") {
        window.location.replace("index.html");
    } // if fail, output the reason
    else { 
        console.log(resultDataJSON["message"]);
        $("#login_error_message").html(resultDataJSON["message"]);
    }
}

function submitLoginForm (formSubmitEvent) {
    console.log("Submitting Event Form");
    formSubmitEvent.preventDefault();
    $.post(
        "api/login",
        $("#login_form").serialize(), (resultDataString) => handleLoginResult(resultDataString)
    );
    grecaptcha.reset();
}

$("#login_form").submit((event) => submitLoginForm(event));
