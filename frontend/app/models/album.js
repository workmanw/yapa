import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr('string'),
  previewImageUrls: DS.attr(),

  uploadPhotos(files) {
    let photoAdapter = this.store.adapterFor('photo');
    return photoAdapter.uploadPhotos(this.store, this, files);
  }
});
