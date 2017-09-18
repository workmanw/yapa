import Ember from 'ember';

let FileUploadModel = Ember.Object.extend({
  // TODO
  fileToUpload: null,

  populateFileToUpload(html5file) {
    this.set('fileToUpload', html5file);
  },

  blobToUpload: Ember.computed('fileToUpload', function() {
    let fileToUpload = this.get('fileToUpload'),
        retBlob;

    if (fileToUpload) {
      retBlob = new Blob([ fileToUpload ], { type: fileToUpload.type });
      retBlob.name = fileToUpload.name; // Preserve the original filename
    }

    return retBlob;
  })
});

FileUploadModel.reopenClass({
  fromHtml5Files(html5Files) {
    let files = [];
    if (html5Files) {
      for (let i = 0; i < html5Files.length; i++) {
        let fileUpload = FileUploadModel.create();
        fileUpload.populateFileToUpload(html5Files[i]);
        files.pushObject(fileUpload);
      }
    }
    return files;
  }
});

export default FileUploadModel;
