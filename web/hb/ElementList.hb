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
                <td><img src={{this.photoURL}}" width="50"></td>
                <td>{{this.displayName}}<td>
                <td>{{this.message}}</td>
		<td><button class="ElementList-likebtn" data-value="{{this.msgId}}">Like</button><p>{{this.likes}}</p></td>
                <td><button class="ElementList-dislikebtn" data-value="{{this.msgId}}">Dislike</button><p>{{this.dislikes}}</p></td>
		<td><p>{{this.comments}} comments</p></td>
            </tr>
            {{/each}}
        </tbody>
    </table>
</div}>
