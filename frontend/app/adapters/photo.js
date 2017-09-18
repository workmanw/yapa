import ApplicationAdapter from './application';
import Ember from 'ember';

export default ApplicationAdapter.extend({
  uploadPhotos(store, album, files) {
    let filePromises = files.map(file => {
      return this.ajax('/api/v1/photo/upload_url', 'GET').then(({ uploadUrl }) => {
        uploadUrl = uploadUrl.match(/\/_ah\/upload.*/)[0];
        return this.sendFileBytes(uploadUrl, { album: album.get('id'), blarg: 'foor bar' }, file);
      });
    });

    return Ember.RSVP.allSettled(filePromises);
  },

  sendFileBytes(url, recordData, fileUpload) {
    let blobToUpload = fileUpload.get('blobToUpload');
    let formData = new FormData();

    // Add the blob
    formData.append('file', blobToUpload, blobToUpload.name);

    // Add the remaining props
    Object.keys(recordData).forEach(function(propName) {
      let propValue = recordData[propName];
      if (Ember.isNone(propValue)) {
        formData.append(propName, '');
      } else if (['string', 'number', 'boolean'].includes(typeof propValue)) {
        formData.append(propName, propValue);
      // } else if (isArray(propValue)) {
      //   // AFAIK this is only used for tags via Clippii.  *hugs* ~Wesley & Jonathan
      //   propValue.forEach(function(value) { formData.append(propName + '[]', value); });
      // } else if (isObject(propValue)) {
      //   formData.append(propName, JSON.stringify(propValue));
      } else {
        Ember.warn(`Unabled to append property "${propName}" because of type. Skipping.`,
                   false, { id: 'ajax.unknown_file_upload_value' });
      }
    });

    return new Ember.RSVP.Promise((resolve, reject) => {
      Ember.$.ajax({
        url: url,
        type: 'POST',
        data: formData,
        processData: false, // Tell jQuery not convert the data payload
        contentType: false, // Not sure why we do this, but most file upload plugins do it.
        xhr: () => {
          // Creates a new XHR object.
          let xhr = Ember.$.ajaxSettings.xhr();
          // Set the onprogress event handler
          xhr.upload.onprogress = function(evt) {
            Ember.run(() => {
              fileUpload.set('uploadProgress', evt.loaded / evt.total * 100);
            });
          };
          // Return it so it can be processed
          return xhr;
        },
        beforeSend: (jqXHR) => {
          // Stash this away incase we need to cancel it.
          fileUpload.set('uploadJqXHR', jqXHR);
        }
      }).done((data, textStatus, jqXHR) => {
        resolve(data);
      }).fail((jqXHR, textStatus, errorThrown) => {
        reject(jqXHR);
      });
    });
  }
});
