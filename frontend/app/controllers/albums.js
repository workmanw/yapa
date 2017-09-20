import Ember from 'ember';
import sortBy from 'ember-computed-sortby';

export default Ember.Controller.extend({
  store: Ember.inject.service(),

  isCreatingAlbum: false,

  sortedAlbums: sortBy('albums', 'name'),

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
