<div class="panel panel-default" id="ViewComments">
    <div class="panel-heading">
        <h3 class="panel-title">ViewComments</h3>
        <button class="ViewComments-goBack">Go Back</button>
    </div>
    <table class="table">
        <tbody>
            {{#each mData}}
            <tr>
                <td><img src={{this.photoURL}} width="50"></td>
                <td>{{this.displayName}}</td>
                <td>{{breaklines this.comment}}</td>
                <td><button class="ViewComments-editbtn" data-value="{{this.commentId}}">Edit</button></td>
            </tr>
            {{/each}}
        </tbody>
    </table>
    </div}>