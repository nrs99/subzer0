/// <reference path="ts/NewLogin.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/ViewComments.ts"/>


//var backendUrl = "https://subzer0.herokuapp.com/";

// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;
// Prevent compiler errors when using Handlebars
let Handlebars: any;

// Run some configuration code when the web page loads
$(document).ready(function () {

    ElementList.refresh();
    Navbar.refresh();
    NewEntryForm.refresh();
    EditEntryForm.refresh();
    NewLogin.refresh();

    ElementList.hide();
    Navbar.hide();

});

