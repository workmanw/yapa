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

    this._bodyDragOver = (evt) => {
      if(dragDropEventHasFiles(evt)) {
        this.set('showDropZone', true);
        // If it's a file drop, go a head and eat it to prevent navigation
        return false;
      }
    };

    this._bodyDragLeave = (evt) => {
      if(dragDropEventHasFiles(evt)) {
        this.set('showDropZone', false);
        // If it's a file drop, eat it to prevent navigation
        return false;
      }
    };

    this._bodyDrop = (evt) => {
      if(dragDropEventHasFiles(evt)) {
        this.set('showDropZone', false);

        let fileUploads = FileUploadModel.fromHtml5Files(evt.dataTransfer.files);
        Ember.tryInvoke(this.attrs, 'dropped-files', [ fileUploads ]);

        // If it's a file drop, eat it to prevent navigation
        return false;
      }
    };

    $('body').on('dragover', this._bodyDragOver);
    $('body').on('dragleave', this._bodyDragLeave);
    $('body').on('drop', this._bodyDrop);
  },

  willDestroyElement() {
    if (this._bodyDragOver) {
      $('body').off('dragover', this._bodyDragOver);
    }
    if (this._bodyDragLeave) {
      $('body').off('dragleave', this._bodyDragLeave);
    }
    if (this._bodyDrop) {
      $('body').off('drop', this._bodyDrop);
    }

    this._super(...arguments);
  }
});
