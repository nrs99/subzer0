<div class="panel panel-default" id="ElementList">
    <div class="panel-heading">
        <h3 class="panel-title">Messages</h3>
    </div>
    <label for="ElementList-sort">Sort by:</label>
    <select id="ElementList-sort">
        <option value="">Newest</option>
        <option value="/oldest">Oldest</option>
        <option value="/popular">Popular</popular>
    </select>
    <table class="table">
        <tbody>
            {{#each mData}}
            <tr>
                <td><img class="ElementList-photo" data-value="{{this.userId}}" src={{this.photoURL}} width="50"></td>
                <td>
                    <p class="ElementList-displayName" data-value="{{this.userId}}">{{this.displayName}}</p>
                </td>
                <td>{{breaklines this.message}}</td>
                <td><button class="ElementList-likebtn" data-value="{{this.msgId}}">Like</button>
                    <p>{{this.likes}}</p>
                </td>
                <td><button class="ElementList-dislikebtn" data-value="{{this.msgId}}">Dislike</button>
                    <p>{{this.dislikes}}</p>
                </td>
                <td><button class="ElementList-commentbtn" data-value="{{this.msgId}}">{{this.comments}}
                        comments</button></td>
                {{#notNull this.link}}
                <td><button class="ElementList-linkbtn" data-value="{{this.link}}">Link</button></td>
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