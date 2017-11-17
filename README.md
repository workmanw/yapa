YAPA (Yet Another Photo App)
============================

## Required Tools

* `gcloud`
* `ember`
* `node` & `yarn`

## Setup

### Create Google Cloud Project

`gcloud init`

### Enable App Engine

`gcloud app create`

### Enable the Datastore API

https://console.cloud.google.com/datastore/entities/query

`gcloud service-management enable datastore.googleapis.com`

### Create Cloud Storage bucket

First, enable billing: https://support.google.com/cloud/answer/6293499#enable-billing

`gsutil mb gs://yapa-assets`

### Turn on Analysis APIs

https://cloud.google.com/vision/docs/before-you-begin

`gcloud service-management enable vision.googleapis.com`
`gcloud service-management enable speech.googleapis.com`
`gcloud service-management enable videointelligence.googleapis.com`
`gcloud service-management enable language.googleapis.com`

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
