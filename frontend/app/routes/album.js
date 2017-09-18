import Ember from 'ember';

export default Ember.Route.extend({
  store: Ember.inject.service(),

  setupController(controller, model) {
    controller.set('album', model.album);
    controller.set('photos', model.photos.toArray());
  },

  model(params) {
    return Ember.RSVP.hash({
      album: this.get('store').findRecord('album', params.album_id),
      photos: this.get('store').query('photo', { album: params.album_id })
    });
  }
});
