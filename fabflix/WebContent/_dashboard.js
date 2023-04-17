function handleLoginResult (resultDataString) {
    // get POST result from database
    resultDataJSON = JSON.parse(resultDataString);
    console.log("Handling Login Response...");
    console.log(resultDataJSON["status"]);
    // if login success
    if (resultDataJSON["status"] === "success") {
        window.location.replace("dashboard.html");
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
        "api/_dashboard",
        $("#login_form").serialize(), (resultDataString) => handleLoginResult(resultDataString)
    );
    grecaptcha.reset();
}

$("#login_form").submit((event) => submitLoginForm(event));
