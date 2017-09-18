import Ember from 'ember';
import $ from 'jquery';
import FileUploadModel from 'yapa/models/file-upload';


export default Ember.Component.extend({
  showDropZone: false,

  didInsertElement() {
    let dragDropEventHasFiles = (evt) => {
      try {
        return evt.dataTransfer.types.contains('Files');
      } catch(e) { /* no-op */ }
      return false;
    };

    $('body').on('dragover', (evt) => {
      if(dragDropEventHasFiles(evt)) {
        // If it's a file drop, go a head and eat it to prevent navigation
        return false;
      }
    });

    $('body').on('dragleave', (evt) => {
      if(dragDropEventHasFiles(evt)) {
        // If it's a file drop, eat it to prevent navigation
        return false;
      }
    });

    $('body').on('drop', (evt) => {
      if(dragDropEventHasFiles(evt)) {
        let fileUploads = FileUploadModel.fromHtml5Files(evt.dataTransfer.files);
        Ember.tryInvoke(this.attrs, 'dropped-files', [ fileUploads ]);

        // If it's a file drop, eat it to prevent navigation
        return false;
      }
    });
  }
});
