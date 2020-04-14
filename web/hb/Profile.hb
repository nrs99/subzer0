<div class="panel panel-default" id="Profile">
    <div class="panel-heading">
        <h3 class="panel-title">Profile</h3>
        <button class="Profile-goBack">Go Back</button>
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
                {{#notNull this.link}}
                <td><button class="Profile-linkbtn" data-value="{{this.link}}">Link</button></td>
                {{/notNull}}
            </tr>
            <tr>
                <td><img src="https://pbs.twimg.com/profile_images/1100094772142841858/r8P4QTkz_400x400.jpg" alt = "Maxim Veznov"></td>
            </tr>
            {{/each}}
        </tbody>
    </table>
    </div}>