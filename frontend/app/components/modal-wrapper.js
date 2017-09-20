import Ember from 'ember';
import $ from 'jquery';

let ModalWrapperComponent = Ember.Component.extend({
  classNames: 'modal-wrapper',
  classNameBindings: 'hidden',

  visible: false,
  hidden: Ember.computed.not('visible'),

  click(evt) {
    if ($(evt.target).hasClass('modal-wrapper')) {
      Ember.tryInvoke(this.attrs, 'on-close');
    }
  }
});

export default ModalWrapperComponent;
