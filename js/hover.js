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
function hidebox(x) {
    //var delay= 1800;
    //timer= setTimeout( function() {
    //    document.getElementById(x).style.display = 'none';
    //}, delay);
    document.getElementById(x).style.display = 'none';
}
