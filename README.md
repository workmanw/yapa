YAPA (Yet Another Photo App)
============================

## Required Tools

* `gcloud`
* `ember`

## Setup

### Create Google Cloud Project

`gcloud init`

### Enable App Engine

`gcloud app create`

### Create Cloud Storage bucket

First, enable billing: https://support.google.com/cloud/answer/6293499#enable-billing

`gsutil mb gs://yapa-assets`

### Enable the Datastore API

https://console.cloud.google.com/datastore/entities/query

`gcloud service-management enable datastore.googleapis.com`

### Turn on Vision API

https://cloud.google.com/vision/docs/before-you-begin

`gcloud service-management enable vision.googleapis.com`

### [Optional] Check Configuration

`gcloud config list`

## Deploying

### Full build and deploy

`./buildDeploy.sh`

### [OPTIONAL] Deploy Indexes

`mvn appengine:deployIndex`

###  [OPTIONAL] Deploy Task Queue

`mvn appengine:deployQueue`

###  [OPTIONAL] Deploy App Engine

`mvn appengine:deploy`
