<div class="panel panel-default" id="Profile">
    <div class="panel-heading">
        <h3 class="panel-title">Profile</h3>
        <button class="Profile-goBack">Go Back</button>
    </div>
     <button class="btn btn-success" id="Profile-settings" data-toggle="button" aria-pressed="false" autocomplete="off">Settings </button>
     <button class="btn btn-success" id="Profile-follow" data-toggle="button" aria-pressed="false" autocomplete="off">Follow! </button>
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
                {{#notNull this.link}}
                <td><button class="Profile-linkbtn" data-value="{{this.link}}">Link</button></td>
                {{/notNull}}
            </tr>
            {{#notNull this.photoString}}
            <tr>
                <td></td>
                <td></td>
                <td>
                    {{#isPDF this.mimeType}}
                    <embed src="data:{{this.mimeType}};base64,{{this.photoString}}" width="500" height="375"
                        type="application/pdf">
                    {{else}}
                    <img src="data:{{this.mimeType}};base64,{{this.photoString}}" height=200px>
                    {{/isPDF}}
                </td>
                <td>
            </tr>
            {{/notNull}}
            {{/each}}
        </tbody>
    </table>
    </div}>