import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr('string'),

  uploadPhotos(files) {
    let photoAdapter = this.store.adapterFor('photo');
    return photoAdapter.uploadPhotos(this.store, this, files);
  }
});
