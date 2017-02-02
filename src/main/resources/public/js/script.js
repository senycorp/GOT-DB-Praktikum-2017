/**
 * Created by selcuk on 30.01.17.
 */

/**
 * Search for type
 *
 * @param type
 * @param button
 */
function search(type, button) {
    var keyword = $(button).parent().parent().find("input").val();

    if (keyword == '') keyword = 'all'

    window.location.href = '/search/' + type + '/' + keyword;
}

/**
 * Save Rating
 *
 * @param rating
 * @param text
 */
function saveRating(id, rating, text) {
    $.ajax({
        method: "GET",
        url: "/rate/" + id,
        data: { rating: rating, text: text }
    }).done(function( msg ) {
        if (msg == "OK") {
            window.location.href = window.location.href;
        } else {
            alert(msg);
        }
    });

    return false;
}
