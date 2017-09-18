import Ember from 'ember';
import config from './config/environment';

const Router = Ember.Router.extend({
  location: config.locationType,
  rootURL: config.rootURL
});

Router.map(function() {
  this.route('albums', { path: '/albums' });
  this.route('album', { path: '/album/:album_id' });
});

export default Router;
