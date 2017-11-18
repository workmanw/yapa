YAPA (Yet Another Photo App)
============================

## Required Tools

* `gcloud` - https://cloud.google.com/sdk/gcloud/
* `node` and `npm` - https://nodejs.org/en/
* `ember` - `npm install -g ember-cli`
* `yarn` - `npm install -g yarn`

## Setup

### Create Google Cloud Project

`gcloud init`

*Note: When initializing your project, you will provide a project ID. Remember that.*

### Enable App Engine

`gcloud app create`

### Enable the Datastore API

https://console.cloud.google.com/datastore/entities/query

`gcloud services enable datastore.googleapis.com`

### Create Cloud Storage bucket

First, enable billing: https://support.google.com/cloud/answer/6293499#enable-billing

`gsutil mb gs://yapa-devfest-assets`

*Note: Your GCS bucket will need a different name than `yapa-devfest-assets`. It doesn't matter what it is, it just needs to be unique.*

Update `src/main/java/io/workmanw/yapa/Constants.java` with your bucket name.

### Turn on Analysis APIs

https://cloud.google.com/vision/docs/before-you-begin

`gcloud services enable vision.googleapis.com`
`gcloud services enable speech.googleapis.com`
`gcloud services enable videointelligence.googleapis.com`
`gcloud services enable language.googleapis.com`


## Deploying

### Full build and deploy

`./buildDeploy.sh`


## Optional Maven Utils

### Deploy Indexes

`mvn appengine:deployIndex`

### Deploy Task Queue

`mvn appengine:deployQueue`

###  Deploy App Engine

`mvn appengine:deploy`

## Questions / Help

Open an issue!
