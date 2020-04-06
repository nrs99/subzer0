<div class="panel panel-default" id="ViewComments">
    <div class="panel-heading">
        <h3 class="panel-title">Comments</h3>
        <button class="ViewComments-newComment">New Comment</button>
        <button class="ViewComments-goBack">Go Back</button>
    </div>
    <table class="table">
        <tbody>
            {{#each mData}}
            <tr>
                <td><img class="ViewComments-photo" data-value="{{this.userId}}" src={{this.photoURL}} width="50"></td>
                <td><p class="ViewComments-displayName" data-value="{{this.userId}}">{{this.displayName}}</p></td>
                <td>{{breaklines this.comment}}</td>
                <td><button class="ViewComments-editbtn" data-value="{{this.commentId}}">Edit</button></td>
            </tr>
            {{/each}}
        </tbody>
    </table>
    </div}>