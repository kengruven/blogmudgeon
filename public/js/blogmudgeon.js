$(document).ready(function () {
    var field = $("div.blogmudgeon-search input.form-control[type='text']");
    var submit = $("div.blogmudgeon-search button.btn[type='button']");

    field.keypress(function(event) {
        if (event.which == 13) {
            start_search();
        }
    });
    submit.click(function() {
        start_search();
    });
});

function start_search() {
    var terms = $("div.blogmudgeon-search input.form-control[type='text']").val();

    if (terms.trim() == "") {
        $("div.blogmudgeon-search-results").hide();
        return;
    }

    jQuery.post("/posts/search", {"query": terms}, search_completed);
}

function search_completed(json) {
    $("div.blogmudgeon-search-results").show();

    json = JSON.parse(json);

    var count = json["count"];
    var title, content;
    if (count == 0) {
        title = "No results";
        content = "";
    } else {
        title = "Results 1-" + count;
        content = json["html"];
    }
    $("div.blogmudgeon-search-results h5").empty().append(title);
    $("div.blogmudgeon-search-results div.target").empty().append(content);
}
