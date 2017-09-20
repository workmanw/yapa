import Ember from 'ember';

export default Ember.Component.extend({
  classNames: 'search-field',
  router: Ember.inject.service(),

  actions: {
    doSearch() {
      let searchValue = this.get('searchValue');
      if (searchValue) {
        this.get('router').transitionTo('search', { queryParams: { text: searchValue } });
        this.set('searchValue', '');
      }
    }
  }
});
