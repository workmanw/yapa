import Ember from 'ember';

export default Ember.Route.extend({
  store: Ember.inject.service(),

  setupController(controller, model) {
    controller.set('album', model);

    controller.set('isLoadingPhotos', true);
    this.get('store').query('photo', { album: model.get('id') }).then(photos => {
      controller.set('photos', photos);
    }).finally(() => {
      controller.set('isLoadingPhotos', false);
    });
  },

  model(params) {
    return this.get('store').findRecord('album', params.album_id);
  }
});
