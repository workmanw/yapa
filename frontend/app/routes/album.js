import Ember from 'ember';

export default Ember.Route.extend({
  store: Ember.inject.service(),

  setupController(controller, model) {
    controller.set('album', model);
  },

  model(params) {
    return this.get('store').findRecord('album', params.album_id);
  }
});
