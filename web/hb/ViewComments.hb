<div id="ViewComments" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Comments</h4>
            </div>
            <div class="modal-body">
                <textarea class="form-control" id="ViewComments-message">There will be a message here</textarea>
                <table class="table">
                    <tbody>
                        {{#each mData}}
                        <tr>
                            <td><img src={{this.photoURL}}" width="50"></td>
                            <td>{{this.displayName}}</td>
                            <td>{{this.comment}}</td>
                        </tr>
                        {{/each}}
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" id="ViewComments-Close">Close</button>
            </div>
        </div>
    </div>
</div>