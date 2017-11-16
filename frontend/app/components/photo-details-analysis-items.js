import Ember from 'ember';

const COLLAPSE_COUNT = 10;

export default Ember.Component.extend({
  classNames: 'photo-details-analysis-items',

  items: null,
  isLoading: false,
  type: '', // vision | speech | videoIntel

  isSpeech: Ember.computed.equal('type', 'speech'),

  typePrefix: Ember.computed('type', function() {
    let type = this.get('type');
    return ({
      'vision': 'Vision',
      'speech': 'Natural Language',
      'videoIntel': 'Video Intelligence'
    }[type] || '');
  }),

  sortedItems: Ember.computed('items.[]', 'isSpeech', function() {
    let items = this.get('items') || [],
        isSpeech = this.get('isSpeech');
    return items.sort((a, b) => {
      return isSpeech ? (b.salience - a.salience) : (b.score - a.score);
    });
  }),

  isCollapsed: true,
  canExpandCollapse: Ember.computed.gt('sortedItems.length', COLLAPSE_COUNT),
  filteredItems: Ember.computed('isCollapsed', 'sortedItems.[]', function() {
    let sortedItems = this.get('sortedItems'),
        isCollapsed = this.get('isCollapsed');
    return isCollapsed ? sortedItems.slice(0, COLLAPSE_COUNT) : sortedItems;
  }),

  actions: {
    toggleCollapse() {
      this.toggleProperty('isCollapsed');
    }
  }
});
