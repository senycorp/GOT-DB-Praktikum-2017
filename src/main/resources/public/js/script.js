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
