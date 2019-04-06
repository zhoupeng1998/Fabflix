
queryCache = {}

function handleLookup(query, doneCallback) {
	query = query.toLowerCase().trim()
	if (String(query).length >= 3) {
		console.log("Handling query " + query)
		if (query in queryCache) {
			console.log("Query " + query + " is already cached! No need to request.")
			handleLookupSuccess(queryCache[query], query, doneCallback) 
		} else {
			console.log("Query " + query + " is never requested! Sending request.")
			jQuery.ajax({
				"method": "GET",
				"url": "api/movie_suggest?name=" + escape(query),
				"success": function(data) {
					queryCache[query] = data;
					handleLookupSuccess(data, query, doneCallback) 
				},
				"error": function(errorData) {
					console.log("lookup ajax error")
					console.log(errorData)
				}
			})
		}
	} else {
		console.log("Query length too short! (length <= 2)")
	}
}

function handleLookupSuccess(data, query, doneCallback) {
	console.log("Lookup successful!")
	console.log(data)
	doneCallback( { suggestions: data } );
}

function handleSelectSuggestion(suggestion) {
	console.log("Jumping to single movie page " + suggestion["value"] + " with ID " + suggestion["data"]["movieId"])
	// clean "back" button
	document.cookie = "last_search=index.html; path=/";
	window.location.href = "single_movie.html?id="+suggestion["data"]["movieId"]
}

function handleNormalSearch (query) {
	if (query == "") {
		console.log("Handling empty search query")
		window.location.href = "movie_list.html?method=S&title=&year=&director=&star=&sort=R&order=D&page=0&limit=20"
	} else {
		console.log("Handling normal search for " + query)
		window.location.href = "movie_list.html?method=F&title=" + query + "&sort=T&order=A&page=0&limit=20"
	}
}

$('#autocomplete').autocomplete({
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    deferRequestBy: 300,
});

$('#autocomplete').keypress(function(event) {
	if (event.keyCode == 13) {
		handleNormalSearch($('#autocomplete').val())
	}
})