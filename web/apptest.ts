/// <reference path="ts/NewLogin.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/ViewComments.ts"/>
/// <reference path="ts/Profile.ts"/>
/// <reference path="ts/NewCommentForm.ts"/>


var describe: any;
var it: any;
var expect: any;
var $: any;

describe("Tests of basic math functions", function() {
    it("Adding 1 should work", function() {
        var foo = 0;
        foo += 1;
        expect(foo).toEqual(1);
    });

    it("Subtracting 1 should work", function () {
        var foo = 0;
        foo -= 1;
        expect(foo).toEqual(-1);
    });

    it("UI Test: New Entry Form", function() {
        $("#Navbar-add").click();
        expect($("#NewEntryForm-messages").val()).toEqual("");
        $("#NewEntryForm-Close").click();
    });

});