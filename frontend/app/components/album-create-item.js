import Ember from 'ember';

let AlbumCreateItemComponent = Ember.Component.extend({
  classNames: 'album-create-item',

  click(evt) {
    Ember.tryInvoke(this.attrs, 'on-click');
    evt.stopPropagation();
    return false;
  }
});

export default AlbumCreateItemComponent;
