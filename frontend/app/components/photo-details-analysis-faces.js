import Ember from 'ember';

export default Ember.Component.extend({
  tagName: 'canvas',
  classNames: 'photo-details-analysis-faces',
  visionData: null,
  isDocked: true,

  _didChange: Ember.observer('visionData', 'isDocked', function() {
    // Ember.run.once(this, this.drawCanvas);
  }),

  drawCanvas() {
    let containerElem = window.$('.details-image'),
        imgElem = this.$(containerElem.children('img')[0]),
        canvasElem = this.$(),
        imgWt = imgElem.width(),
        imgHt = imgElem.height(),
        visionData = this.get('visionData'),
        scaleFactor;

    if (containerElem.length === 0) {
      return;
    }

    let r = imgElem[0].getBoundingClientRect();
    let parentR = imgElem.parent()[0].getBoundingClientRect();
    canvasElem = this.$()
      .addClass('vision-annotation-overlay')
      .attr({
        width: imgWt,
        height: imgHt
      })
      .css({
        top: (r.y - parentR.y) + 'px',
        left: (r.x - parentR.x) + 'px',
        width: imgWt + 'px',
        height: imgHt + 'px'
      });

    this.set('canvasElem', canvasElem[0]);
    let ctx = this.get('canvasElem').getContext('2d');
    this.set('canvasContext', ctx);

    // Clear it out
    ctx.clearRect(0, 0, canvasElem[0].width, canvasElem[0].height);

    // Make sure we have vision data
    if (!visionData) {
      return;
    }

    let fullWt = imgElem[0].naturalWidth;
    let fullHt = imgElem[0].naturalHeight;

    if (imgWt > imgHt) {
      scaleFactor = imgWt / fullWt;
    } else {
      scaleFactor = imgHt / fullHt;
    }
    this.set('scaleFactor', scaleFactor);

    this.get('visionData.faces').forEach(annotation => {
      let vertices = JSON.parse(annotation.faceVertices);
      this.drawPolygon(vertices, '#00AEEF', 'Face ');
    });
  },

  drawPolygon(vertices, color) {
    let ctx = this.get('canvasContext'),
        scaleFactor = this.get('scaleFactor');

    ctx.fillStyle = color;
    ctx.beginPath();
    vertices.forEach((vertex, idx) => {
      let x = (vertex['x'] || vertex['X'] || 0) * scaleFactor,
          y = (vertex['y'] || vertex['Y'] || 0) * scaleFactor;
      if (idx === 0) {
        ctx.moveTo(x, y);
      } else {
        ctx.lineTo(x, y);
      }
    });
    ctx.lineWidth = '2';
    ctx.strokeStyle = color;

    ctx.closePath();
    ctx.stroke();
  }
});
