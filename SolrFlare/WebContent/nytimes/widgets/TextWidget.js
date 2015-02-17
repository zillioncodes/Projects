(function ($) {

AjaxSolr.TextWidget = AjaxSolr.AbstractTextWidget.extend({
  init: function () {
    var self = this;
    alert("here12345");
    $(this.target).find('input').bind('keydown', function(e) {
      if (e.which == 13) {
        var value = $(this).val();
        alert(value);
        alert("here1");
        if (value && self.set(value)) {
          self.doRequest();
        }
      }
    });
  },

  afterRequest: function () {
	  alert("here2");
    $(this.target).find('input').val('');
  }
});

})(jQuery);
