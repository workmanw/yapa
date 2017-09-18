import Ember from 'ember';

export default Ember.Route.extend({
  store: Ember.inject.service(),

  setupController(controller, albums) {
    controller.set('albums', albums);
  },

  model() {
    return this.get('store').findAll('album');
  }
});
