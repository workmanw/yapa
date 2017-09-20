import Ember from 'ember';
import $ from 'jquery';
import FileUploadModel from 'yapa/models/file-upload';


function dataTransferIncludes(dataTransfer, type) {
  let types = dataTransfer.types;
  return types.includes ? types.includes(type) : types.contains(type);
}

export default Ember.Component.extend({
  classNames: 'file-uploader',

  showDropZone: false,

  didInsertElement() {
    let dragDropEventHasFiles = (evt) => {
      try {
        return dataTransferIncludes(evt.dataTransfer, 'Files');
      } catch(e) { /* no-op */ }
      return false;
    };

    $('body').on('dragover', (evt) => {
      if(dragDropEventHasFiles(evt)) {
        this.set('showDropZone', true);
        // If it's a file drop, go a head and eat it to prevent navigation
        return false;
      }
    });

    $('body').on('dragleave', (evt) => {
      if(dragDropEventHasFiles(evt)) {
        this.set('showDropZone', false);
        // If it's a file drop, eat it to prevent navigation
        return false;
      }
    });

    $('body').on('drop', (evt) => {
      if(dragDropEventHasFiles(evt)) {
        this.set('showDropZone', false);

        let fileUploads = FileUploadModel.fromHtml5Files(evt.dataTransfer.files);
        Ember.tryInvoke(this.attrs, 'dropped-files', [ fileUploads ]);

        // If it's a file drop, eat it to prevent navigation
        return false;
      }
    });
  }
});
