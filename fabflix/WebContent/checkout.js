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

function handleCheckoutResult (resultData) {
    console.log(resultData);
    //resultDataJSON = JSON.parse(resultDataString);
    console.log("Handling Checkout Response...");
    console.log(resultData["status"]);
    if (resultData["message"] === "success") {
        window.location.replace("confirm.html");
    } else {
        console.log("Checkout failed!");
        window.alert("Checkout failed! Check your Credit Card information.");
    }
}



function submitCheckoutForm (formSubmitEvent) {
    console.log("Submitting Checkout Form");
    formSubmitEvent.preventDefault();
    $.post(
        "api/check_out",
        $("#cc_form").serialize(), (resultDataString) => handleCheckoutResult(resultDataString)
    );
}

// TODO: Prevent bad & empty input
$("#cc_form").submit((event) => submitCheckoutForm(event));
