<div class="panel panel-default" id="Profile">
    <div class="panel-heading">
        <h3 class="panel-title">Profile for User</h3>
    </div>
    <table class="table">
        <tbody>
            {{#each mData}}
            <tr>
                <td><img src={{this.photoURL}} width="50"></td>
                <td>{{this.displayName}}</td>
                <td>{{breaklines this.message}}</td>
                <td><button class="Profile-likebtn" data-value="{{this.msgId}}">Like</button>
                    <p>{{this.likes}}</p>
                </td>
                <td><button class="Profile-dislikebtn" data-value="{{this.msgId}}">Dislike</button>
                    <p>{{this.dislikes}}</p>
                </td>
                <td><button class="Profile-commentbtn" data-value="{{this.msgId}}">{{this.comments}}
                        comments</button></td>
            </tr>
            {{/each}}
        </tbody>
    </table>
    </div}>