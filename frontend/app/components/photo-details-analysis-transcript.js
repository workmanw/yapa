import Ember from 'ember';

const TRANSCRIPT_COLLAPSE_COUNT = 330;

export default Ember.Component.extend({
  classNames: 'photo-details-analysis-transcript',

  transcript: '',
  isLoading: false,

  hasTranscript: Ember.computed.bool('transcript'),

  isCollapsed: true,
  canExpandCollapse: Ember.computed.gt('transcript.length', TRANSCRIPT_COLLAPSE_COUNT),
  filteredTranscript: Ember.computed('transcript', 'isCollapsed', function() {
    let transcript = this.get('transcript'),
        isCollapsed = this.get('isCollapsed');
    return isCollapsed ? transcript.slice(0, TRANSCRIPT_COLLAPSE_COUNT) + '...' : transcript;
  }),

  actions: {
    toggleCollapse() {
      this.toggleProperty('isCollapsed');
    }
  }
});
