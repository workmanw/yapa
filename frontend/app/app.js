import Ember from 'ember';
import Resolver from './resolver';
import loadInitializers from 'ember-load-initializers';
import config from './config/environment';

// sets up Ember.Inflector
import './models/custom-inflector-rules';

const App = Ember.Application.extend({
  init() {
    this._super(...arguments);
    window.app = this;
  },

  modulePrefix: config.modulePrefix,
  podModulePrefix: config.podModulePrefix,
  Resolver
});

loadInitializers(App, config.modulePrefix);

export default App;
