function showbox(context, x) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            $("#" + x).html(xhttp.responseText);
            document.getElementById(x).style.display = 'block';
        }
    };
    xhttp.open("GET", context + "/customer/hover?movie_id=" + x, true);
    xhttp.send();
}
function hidebox(id) {
    //var delay= 1800;
    //timer= setTimeout( function() {
    //    document.getElementById(x).style.display = 'none';
    //}, delay);
    $("#" + id).hide();
}

// url: /customer/autocomplete_search
// input_id: autocomplete_title
// output_id: autocomplete_box
function updatePage(url, input_id, output_id) {
    // Name the input_id the same as the parameter to be passed
    var input_val = encodeURIComponent($("#" + input_id).val());
    var params = input_id + "=" + input_val;
    console.log("params: " + params);

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            $("#" + output_id).html(xmlhttp.responseText);
            $("#" + output_id).show();
        }
    };
    xmlhttp.open("POST", url, true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.send(params);
}
