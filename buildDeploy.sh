#!/bin/bash

# Build Ember and copy assets
pushd frontend
ember build --environment=production
popd

rm -Rf src/main/webapp/ember-dist
cp -R frontend/dist src/main/webapp/ember-dist

# Everything
mvn appengine:deploy
