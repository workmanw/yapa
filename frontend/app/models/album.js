import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr('string'),
  previewImageUrls: DS.attr(),

  uploadPhoto(file) {
    let photoAdapter = this.store.adapterFor('photo');
    return photoAdapter.uploadPhoto(this.store, this, file);
  }
});
