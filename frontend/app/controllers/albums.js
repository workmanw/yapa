import Ember from 'ember';

export default Ember.Controller.extend({
  store: Ember.inject.service(),

  actions: {
    showCreateAlbum() {
      this.set('isCreatingAlbum', true);
    },

    hideCreateAlbum() {
      this.set('isCreatingAlbum', false);
    },

    createAlbum() {
      let album = this.get('store').createRecord('album', { name: this.get('newAlbumName') });
      album.save().then(() => {
        this.transitionToRoute('album', album.get('id'));
        this.set('isCreatingAlbum', false);
      });
    }
  }
});
