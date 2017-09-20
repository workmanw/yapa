import DS from 'ember-data';

export default DS.Model.extend({
  photoName: DS.attr('string'),
  album: DS.belongsTo('album'),

  blobKey: DS.attr('string'),
  content: DS.attr('string'),
  filename: DS.attr('string'),
  filesize: DS.attr('number'),
  gcsPath: DS.attr('string'),
  md5: DS.attr('string'),
  servingUrl: DS.attr('string'),
  vision: DS.attr()
});
