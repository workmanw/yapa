import Ember from 'ember';

export default Ember.Route.extend({
  store: Ember.inject.service(),

  queryParams: {
    text: {
      refreshModel: true
    }
  },

  setupController(controller, model) {
    controller.set('searchPhotos', model.toArray());
  },

  model(params) {
    return this.get('store').query('photo', { search: params.text });
  }
});
