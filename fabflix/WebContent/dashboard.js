function handleStarInsertResult(resultDataString) {
   resultDataJSON = JSON.parse(resultDataString);
   console.log("Handling Insert Response...");
   console.log(resultDataJSON["status"]);
   if(resultDataJSON["status"] === "success"){
      alert("Inserting Star success!");
      window.location.replace("dashboard.html");
   }
   else{
      console.log(resultDataJSON["message"]);
      jQuery("#form_table").append("<font color='red'>ERROR</font><br>");
      jQuery("#form_table").append("<font color='red'>"+resultDataJSON["message"]+"</font>");
   }
}

function handleMovieInsertResult(resultDataString) {
	   resultDataJSON = JSON.parse(resultDataString);
	   console.log("Handling Insert Response...");
	   console.log(resultDataJSON["status"]);
	   if(resultDataJSON["status"] === "success"){
	      // success and fail
		  var rtn = "";
		  var str = resultDataJSON["info"];
		  if( str[0] === 'T' ){
			  rtn += "Add movie success!\n";
		  }
		  else{
			  rtn += "Movie has already existed!\n";
		  }
		  if( str[1] === 'T' ){
			  rtn += "Add Star success!\n";
		  }
		  else{
			  rtn += "Star has already existed!\n";
		  }
		  if( str[2] === 'T' ){
			  rtn += "Add stars_in_movies success!\n";
		  }
		  else{
			  rtn += "Stars_in_movies has already existed!\n";
	      }
		  if( str[3] === 'T' ){
			  rtn += "Add genre success!\n";
		  }
		  else{
			  rtn += "Genre has already existed!\n";
		  }
		  if( str[4] === 'T' ){
			  rtn += "Add genres_in_movies success!\n";
		  }
		  else{
			  rtn += "Genres_in_movies has already existed!\n";
		  }
		  alert(rtn);
	      window.location.replace("dashboard.html");
	   }
	   else{
	      console.log(resultDataJSON["message"]);
	      jQuery("#star_form").append("<font color='red'>ERROR</font><br>");
	      jQuery("#star_form").append("<font color='red'>"+resultDataJSON["message"]+"</font>");
	   }
}

function submitMovieInsertForm( formSubmitEvent ){
	   console.log("Submitting Event Form");
	   formSubmitEvent.preventDefault();
	   $.post(
	      "api/dashboard",
	      $("#movie_info").serialize(), (resultDataString) => handleMovieInsertResult(resultDataString)
	   );
}

function submitStarInsertForm( formSubmitEvent ){
   console.log("Submitting Event Form");
   formSubmitEvent.preventDefault();
   $.post(
      "api/dashboard",
      $("#star_info").serialize(), (resultDataString) => handleStarInsertResult(resultDataString)
   );
}

function handleInfoResult(resultData) {
   var str = "ttf";
   console.log("Handling Check Response...");
   console.log(resultData["status"]);
   if(resultData["status"] === "success") {
	  var rawdata = resultData["info"];
	  let target = jQuery("#dbinfo");
	  var table;
	  var table_title = "<table style='width:100%'>" +
							"<thead>" +
								"<tr><th width=50>Name</th>" +
									"<th width=50>Type</th>" +
									"<th width=50>NULLable?</th>" +
									"<th width=50>Key</th>" +
									"<th width=50>extra?</tr>" +
							"</thead>" +
							"<tbody><tr>";
	  var table_end = "</tr></tbody></table><br><br><br><br><br>"
	  if( rawdata.length != 0 ){
		  for( i = 0; i < rawdata.length; ++i ){
			 table = "<h3><font color='light blue'>"+rawdata[i].table_name+"</font></h3><br>"+table_title;
			 for( j = 0; j < rawdata[i].contents.length; ++j )
				table += "<tr><th>"+rawdata[i].contents[j].COLUMN_NAME+"</th>" +
						 "<th>"+rawdata[i].contents[j].COLUMN_TYPE+"</th>" +
						 "<th>"+rawdata[i].contents[j].IS_NULLABLE+"</th>" +
						 "<th>"+rawdata[i].contents[j].COLUMN_KEY+"</th>" +
						 "<th>"+rawdata[i].contents[j].EXTRA+"</th></tr>";
			 table += table_end;
			 target.append(table);
		  }
	  }
	  else{
		 // error msg
		 jQuery("#bdinfo").append("<font color='red'>ERROR</font>");
	  }
   }
   else{
      console.log(resultDataJSON["message"]);
   }
}

function activateInfoCheck(){
   document.getElementById("form_table").style.visibility = "hidden";
   document.getElementById("dbinfo").style.visibility = "visible";
}

function activateStarInsertion(){
   document.getElementById("form_table").style.visibility = "visible";
   document.getElementById("dbinfo").style.visibility = "hidden";
   var form = "<form id='star_info' method='post' align='center' action='#'>" +
				"<table align='center'>" +
					"<tr>" +
						"<th><label><font color='red'>*</font><b>Name: </b></label></th>" +
						"<th><input type='text' placeholder='Enter a valid Name' name='name' size='20'></th>" +
					"</tr>" +
					"<tr>" +
						"<th><label><b>Star Birth Year: </b></label></th>" +
						"<th><input type='number' placeholder='ENTER BETWEEN (1900-2019)' name='birthYear' size='20' min=1900 max=2019></th>" +
					"</tr>" +
				"</table>" +
				"<input type='submit' value='submit'>" +
			  "</form>";
   jQuery("#form_table").html(form);
}

function activateMovieInsertion(){
   document.getElementById("form_table").style.visibility = "visible";
   document.getElementById("dbinfo").style.visibility = "hidden";
   var form = "<form id=movie_info method='post' align='center' action='#'>" +
					"<table align='center'>" +
						"<tr>" +
							"<th><label><font color='red'>*</font><b>Movie Name: </b></label></th>" +
							"<th><input type='text' placeholder='Enter a valid Name' name='movName' size='20'></th>" +
						"</tr>" +
						"<tr>" +
							"<th><label><font color='red'>*</font><b>Movie Year: </b></label></th>" +
							"<th><input type='number' placeholder='Enter a valid Name' name='movYear' size='20'></th>" +
						"</tr>" +
						"<tr>" +
							"<th><label><font color='red'>*</font><b>Director: </b></label></th>" +
							"<th><input type='text' placeholder='Enter a valid Name' name='director' size='20'></th>" +
						"</tr>" +
						"<tr>" +
							"<th><label><font color='red'>*</font><b>Star Name: </b></label></th>" +
							"<th><input type='text' placeholder='Enter a valid Name' name='starName' size='20'></th>" +
						"</tr>" +
						"<tr>" +
							"<th><label><b>Star BirthYear: </b></label></th>" +
							"<th><input type='number' placeholder='Enter a valid Name' name='starBirthY' size='20'></th>" +
						"</tr>" +
						"<tr>" +
							"<th><label><font color='red'>*</font><b>Genre: </b></label></th>" +
							"<th><input type='text' placeholder='Enter a valid genre' name='genre' size='20'></th>" +
						"</tr>" +
					"</table>" +
					"<input type='submit' value='submit'>" +
			 "</form>";
   jQuery("#form_table").html(form);
}


// $("#star_info").submit((event) => submitStarInsertForm(event));

$(document).on('submit','#star_info',(event)=>submitStarInsertForm(event)); 

$(document).on('submit','#movie_info',(event) => submitMovieInsertForm(event));

jQuery.ajax({
   dataType: "json",
   method: "GET",
   url : "api/dashboard",
   success: (resultData) => handleInfoResult(resultData)
});

