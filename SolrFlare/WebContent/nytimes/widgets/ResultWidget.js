(function($) {

	AjaxSolr.ResultWidget = AjaxSolr.AbstractWidget
	.extend({
		start : 0,

		beforeRequest : function() {
			// ////alert("here i am 23*");
			$(this.target).html(
					$('<img>').attr('src', 'images/ajax-loader.gif'));
		},

		facetLinks : function(facet_field, facet_values) {
			var links = [];
			if (facet_values) {
				for (var i = 0, l = facet_values.length; i < l; i++) {
					if (facet_values[i] !== undefined) {
						links.push($('<a href="#"></a>').text(
								facet_values[i]).click(
										this.facetHandler(facet_field,
												facet_values[i])));
					} else {
						links
						.push('no items found in current selection');
					}
				}
			}
			return links;
		},

		facetHandler : function(facet_field, facet_value) {
			var self = this;
			return function() {
				self.manager.store.remove('fq');
				self.manager.store.addByValue('fq', facet_field + ':'
						+ AjaxSolr.Parameter.escapeValue(facet_value));
				self.doRequest();
				return false;
			};
		},

		afterRequest : function() {
			$(this.target).empty();
			// alert("here i am 2");
			for (var i = 0, l = this.manager.response.response.docs.length; i < l; i++) {
				var doc = this.manager.response.response.docs[i];
				$(this.target).append(this.template(doc));

				var items = [];
				// items = items.concat(this.facetLinks('topics',
				// doc.topics));%change
				// items = items.concat(this.facetLinks('organisations',
				// doc.organisations));
				// items = items.concat(this.facetLinks('exchanges',
				// doc.exchanges));
				items = items.concat(this.facetLinks('category',
						doc.category));
						var $links = $('#links_' + doc.id);
				$links.empty();
				for (var j = 0, m = items.length; j < m; j++) {
					$links.append($('<li></li>').append(items[j]));
				}
			}
		},

		template : function(doc) {
			var snippet = '';
			var textLen = String(doc.description).length;
			// alert("here i am");
			var str = String(doc.description);
			if (textLen > 300) {
				snippet += doc.dateline + ' '
				+ String(doc.description).substring(0, 300);
				snippet += '<span style="display:none;">'
					+ String(doc.description).substring(300);
//				snippet += '</span> <a href="' + doc.url + '" class="more">more</a>';
				snippet += '</span> <a href=# class="more">more</a>';
			} else {
				snippet += doc.date + ' ' + doc.description;
			}

			var output = '<div><h2> <a href="' + doc.url + '" class="titlelink">' + doc.title + '</a></h2>';
//			output += '<p id="links_' + doc.id + '" class="links"></p>';
			output += '<p>' + snippet + '</p></div>';
//			console.log(output);
			return output;
		},

		init : function() {
			$(document).on(
					'click',
					'a.more',
					function() {
						var $this = $(this), div = $this.closest('div');
						var data = "description="+div.html();
						$.post("/SolrFlare/loadNewsDescription", data, function(result) {
			                if (result.trim() == "success") {
			                	window.location = "/SolrFlare/NewsDescription.jsp";
			                } 
			            });
						return false;
					});
		}
	});

})(jQuery);