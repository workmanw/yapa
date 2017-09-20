import Ember from 'ember';

export default Ember.Controller.extend({
  store: Ember.inject.service(),

  isCreatingAlbum: false,

  actions: {
    showCreateAlbum() {
      this.set('showCreateAlbumModal', true);
    },

    hideCreateAlbum() {
      this.set('showCreateAlbumModal', false);
    },

    createAlbum() {
      let album = this.get('store').createRecord('album', { name: this.get('newAlbumName') });
      this.set('isCreatingAlbum', true);
      album.save().then(() => {
        this.transitionToRoute('album', album.get('id'));
        this.set('showCreateAlbumModal', false);
      }).finally(() => {
        this.set('isCreatingAlbum', false);
      });
    }
  }
});
