# # local deploy script for the web front-end

# # This file is responsible for preprocessing all TypeScript files, making
# # sure all dependencies are up-to-date, copying all necessary files into a
# # local deploy directory, and starting a web server

# # This is the resource folder where maven expects to find our files
# TARGETFOLDER=./local

# # step 1: update our npm dependencies
# npm update

# # step 2: make sure we have someplace to put everything.  We will delete the
# #         old folder, and then make it from scratch
# rm -rf $TARGETFOLDER
# mkdir $TARGETFOLDER

# # step 3: copy static html, css, and JavaScript files
# cp index.html $TARGETFOLDER
# cp app.css $TARGETFOLDER
# cp node_modules/jquery/dist/jquery.min.js $TARGETFOLDER
# # cp node_modules/handlebars/dist/handlebars.js $TARGETFOLDER
# cp node_modules/handlebars/dist/handlebars.min.js $TARGETFOLDER
# cp node_modules/handlebars/lodash/template.js $TARGETFOLDER

# # cp node_modules/bootstrap/dist/handlebars.min.js $TARGETFOLDER
# # cp node_modules/handlebars/dist/js/bootstrap.js $TARGETFOLDER
# # cp node_modules/handlebars/dist/js/bootstrap.min.js $TARGETFOLDER



# # step 4: compile TypeScript files
# node_modules/.bin/tsc app.ts --strict --outFile $TARGETFOLDER/app.js

# # step 5: compile tests and copy tests to the local deploy folder
# node_modules/.bin/tsc apptest.ts --strict --outFile $TARGETFOLDER/apptest.js
# cp spec_runner.html $TARGETFOLDER
# cp node_modules/jasmine-core/lib/jasmine-core/jasmine.css $TARGETFOLDER
# cp node_modules/jasmine-core/lib/jasmine-core/jasmine.js $TARGETFOLDER
# cp node_modules/jasmine-core/lib/jasmine-core/boot.js $TARGETFOLDER
# cp node_modules/jasmine-core/lib/jasmine-core/jasmine-html.js $TARGETFOLDER

# # step 6: launch the server.  Be sure to disable caching
# # (Note: we don't currently use -s for silent operation)
# node_modules/.bin/http-server $TARGETFOLDER -c-1

# deploy script for the web front-end

# This file is responsible for preprocessing all TypeScript files, making sure
# all dependencies are up-to-date, and copying all necessary files into the
# web deploy directory.

# This is the resource folder where maven expects to find our files
TARGETFOLDER=../backend/src/main/resources

# This is the folder that we used with the Spark.staticFileLocation command
WEBFOLDERNAME=web

# step 1: make sure we have someplace to put everything.  We will delete the
#         old folder tree, and then make it from scratch
rm -rf $TARGETFOLDER
mkdir $TARGETFOLDER
mkdir $TARGETFOLDER/$WEBFOLDERNAME

# step 2: update our npm dependencies
npm update

# step 3: copy jQuery, Handlebars, and Bootstrap files
cp node_modules/jquery/dist/jquery.min.js $TARGETFOLDER/$WEBFOLDERNAME
cp node_modules/handlebars/dist/handlebars.min.js $TARGETFOLDER/$WEBFOLDERNAME
cp node_modules/bootstrap/dist/js/bootstrap.min.js $TARGETFOLDER/$WEBFOLDERNAME
cp node_modules/bootstrap/dist/css/bootstrap.min.css $TARGETFOLDER/$WEBFOLDERNAME
cp -R node_modules/bootstrap/dist/fonts $TARGETFOLDER/$WEBFOLDERNAME

# step 4: compile TypeScript files
node_modules/typescript/bin/tsc app.ts --strict --outFile $TARGETFOLDER/$WEBFOLDERNAME/app.js

# step 5: copy css files
## Didn't add one for EditEntryForm
cat app.css css/ElementList.css css/NewEntryForm.css css/Navbar.css css/NewLogin.css css/ViewComments.css css/Profile.css > $TARGETFOLDER/$WEBFOLDERNAME/app.css

# step 6: compile handlebars templates to the deploy folder
node_modules/handlebars/bin/handlebars hb/ElementList.hb >> $TARGETFOLDER/$WEBFOLDERNAME/templates.js
node_modules/handlebars/bin/handlebars hb/NewEntryForm.hb >> $TARGETFOLDER/$WEBFOLDERNAME/templates.js
node_modules/handlebars/bin/handlebars hb/Navbar.hb >> $TARGETFOLDER/$WEBFOLDERNAME/templates.js
node_modules/handlebars/bin/handlebars hb/NewLogin.hb >> $TARGETFOLDER/$WEBFOLDERNAME/templates.js
node_modules/handlebars/bin/handlebars hb/EditEntryForm.hb >> $TARGETFOLDER/$WEBFOLDERNAME/templates.js
node_modules/handlebars/bin/handlebars hb/ViewComments.hb >> $TARGETFOLDER/$WEBFOLDERNAME/templates.js
node_modules/handlebars/bin/handlebars hb/Profile.hb >> $TARGETFOLDER/$WEBFOLDERNAME/templates.js


# there are many more steps to be done.  For now, we will just copy an HTML file
cp index.html $TARGETFOLDER/$WEBFOLDERNAME

# set up Jasmine
node_modules/typescript/bin/tsc apptest.ts --strict --outFile $TARGETFOLDER/$WEBFOLDERNAME/apptest.js
cp spec_runner.html $TARGETFOLDER/$WEBFOLDERNAME
cp node_modules/jasmine-core/lib/jasmine-core/jasmine.css $TARGETFOLDER/$WEBFOLDERNAME
cp node_modules/jasmine-core/lib/jasmine-core/jasmine.js $TARGETFOLDER/$WEBFOLDERNAME
cp node_modules/jasmine-core/lib/jasmine-core/boot.js $TARGETFOLDER/$WEBFOLDERNAME
cp node_modules/jasmine-core/lib/jasmine-core/jasmine-html.js $TARGETFOLDER/$WEBFOLDERNAME

node_modules/.bin/http-server $TARGETFOLDER/$WEBFOLDERNAME -c-1