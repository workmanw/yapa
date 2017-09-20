import Ember from 'ember';

let HpisImageComponent = Ember.Component.extend({
  tagName: 'img',
  attributeBindings: ['_src:src', 'style'],

  url: null,
  square: false,
  size: 512,
  height: 0,

  _src: Ember.computed('url', 'square', 'size', function() {
    let url = this.get('url'),
        height = Math.round(this.get('height') * 1.5), // Bump for retinas
        size = Math.round(this.get('size') * 1.5), // Bump for retinas
        square = this.get('square');

    if (height) {
      url += `=h${height}`;
    } else {
      url += `=s${size}`;
    }

    if (square) {
      url += `-c`;
    }
    return url;
  }),

  style: Ember.computed('size', 'height', function() {
    let size = this.get('size'),
        height = this.get('height');
    if (height) {
      return `max-height: ${height}px;`;
    }
    return `max-width: ${size}px; max-height: ${size}px;`;
  }),

  click(evt) {
    if (this.attrs['on-click']) {
      Ember.tryInvoke(this.attrs, 'on-click');
      evt.stopPropagation();
      return false;
    }
  }
});

export default HpisImageComponent;
