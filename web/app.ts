/// <reference path="ts/NewLogin.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/ViewComments.ts"/>
/// <reference path="ts/Profile.ts"/>
/// <reference path="ts/NewCommentForm.ts"/>


//var backendUrl = "https://subzer0.herokuapp.com/";

// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;
// Prevent compiler errors when using Handlebars
let Handlebars: any;

// Run some configuration code when the web page loads
$(document).ready(function () {

    Navbar.refresh();
    NewEntryForm.refresh();
    NewCommentForm.refresh();
    EditEntryForm.refresh();
    NewLogin.refresh();

    if (localStorage.getItem("state") === "logged-in") {
        NewLogin.hide();
        ElementList.refresh();
    } else {
        Navbar.hide();
    }

});

/**
 * This allows line breaks to be displayed correctly
 */
Handlebars.registerHelper('breaklines', function (text) {
    text = Handlebars.Utils.escapeExpression(text);
    text = text.replace(/(\r\n|\n|\r)/gm, '<br>');
    return new Handlebars.SafeString(text);
});

Handlebars.registerHelper('ifEquals', function (arg1, options) {
    return (arg1 === localStorage.getItem("ID")) ? options.fn(this) : options.inverse(this);
});

Handlebars.registerHelper('notNull', function (arg1, options) {
    return (arg1) ? options.fn(this) : options.inverse(this);
});

Handlebars.registerHelper('isPDF', function (mimeType, options) {
    return (mimeType === "application/pdf") ? options.fn(this) : options.inverse(this);
});