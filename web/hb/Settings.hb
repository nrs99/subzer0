<div class="panel panel-default" id="Settings">
    <div class="panel-heading">
        <h3 class="panel-title">Settings</h3>
        <button class="Settings-goBack">Go Back</button>
    </div>

    <span class="border border-primary"></span>
 <div id = settings_toggle_button>
 <p2>Post Notifications?</p2>
    <div class="btn-group btn-group-toggle" data-toggle="buttons">
  <label class="btn btn-secondary active" for="Profile-options1">
    <input type="radio" name="Settings-options-like" id="Profile-options1" aria-pressed="false" autocomplete="off"  value="1" >Yes
  </label>
  <label class="btn btn-secondary" for="Profile-options2">
    <input type="radio" name="Settings-options-like" id="Profile-options2" autocomplete="off"  value="0"> No
  </label>
    </div>
  </div>
</span>


 <div id = settings_toggle_button>
 <p2>Follow Notifications?</p2>
    <div class="btn-group btn-group-toggle" data-toggle="buttons">
  <label class="btn btn-secondary active" for="Profile-options1">
    <input type="radio" name="Settings-options-follow" id="Profile-options1" aria-pressed="false" autocomplete="off"  value="1" >Yes
  </label>
  <label class="btn btn-secondary" for="Profile-options2">
    <input type="radio" name="Settings-options-follow" id="Profile-options2" autocomplete="off" value="0"> No
  </label>
    </div>
</div>

 <div id = settings_toggle_button>
 <p2>Comments On Posts Notifications?</p2>
    <div class="btn-group btn-group-toggle" data-toggle="buttons">
  <label class="btn btn-secondary active" for="Profile-options1">
    <input type="radio" name="Settings-options-comments" id="Profile-options1" aria-pressed="false" autocomplete="off" value="1" >Yes
  </label>
  <label class="btn btn-secondary" for="Profile-options2">
    <input type="radio" name="Settings-options-comments" id="Profile-options2" autocomplete="off"value="0"> No
  </label>
    </div>
     </div>

<button class="btn btn-success" id="Settings-submit" data-toggle="button" aria-pressed="false" autocomplete="off" onclick="Settings.followOption()">Submit.</button>
</div>
</div>