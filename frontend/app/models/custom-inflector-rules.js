import Inflector from 'ember-inflector';

const inflector = Inflector.inflector;

inflector.uncountable('album');
inflector.uncountable('photo');

// Meet Ember Inspector's expectation of an export
export default {};
