#!/bin/bash

pushd frontend

ember build --environment=production

popd

rm -Rf src/main/webapp/ember-dist
cp -R frontend/dist src/main/webapp/ember-dist

mvn appengine:deploy
# mvn appengine:deployQueue
