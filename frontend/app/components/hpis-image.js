import Ember from 'ember';

let HpisImageComponent = Ember.Component.extend({
  tagName: 'img',
  attributeBindings: ['_src:src', 'style'],

  url: null,
  square: false,
  size: 512,

  _src: Ember.computed('url', 'square', 'size', function() {
    let url = this.get('url'),
        size = Math.round(this.get('size') * 1.5), // Bump for retinas
        square = this.get('square');
    url += `=s${size}`;
    if (square) {
      url += `-c`;
    }
    return url;
  }),

  style: Ember.computed('size', function() {
    let size = this.get('size');
    return `max-width: ${size}px; max-height: ${size}px;`;
  })
});

export default HpisImageComponent;
