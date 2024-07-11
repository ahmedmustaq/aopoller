
angular.module('app').controller(
	'ParserCtrl',
	[
		'$scope',
		'parseService',
		'$filter', 'logger',
		function($scope, parseService, $filter, logger) {

			$scope.request = {};
			$scope.request.jsmode = 0;
			$scope.request.url = "https://www.flipkart.com/saara-dyed-solid-embellished-banarasi-cotton-silk-saree/p/itm2c7c60cf01ba3";
			$scope.request.title = 'span.B_NuCI';
			$scope.request.category = 'div._3GIHBu';
			$scope.request.oldprice = '._3I9_wc._2p6lqe';
			$scope.request.currentprice = '._30jeq3._16Jk6d';
			$scope.request.imagepattern = '[0-9]{2,3}[/][0-9]{2,3}';
			$scope.request.heightwidthpattern = '/';
			$scope.request.mainimages = ['._312yBx.SFzpgZ'];
			$scope.request.mainimage_mode = 0;
			$scope.request.thumnails_mode = 1;
			$scope.request.attributes = ['.X3BRps._13swYk > div'];
			$scope.request.attributekeys = ['.X3BRps._13swYk > .row > div:nth-child(1)'];
			$scope.request.description = '._1AN87F';
			$scope.active = 0;

			$scope.request.thumbnails = ['.q6DClP._2_B7hD'];

			$scope.download = function(val) {
				if (val == 0)
					var response = $scope.request;
				if (val == 1)
					var response = $scope.product;
				// if this response is coming through http call than make condition according to http response.statusCode
				//check response is undefined, null or empty
				if (typeof response == 'undefined'
					|| response == null || response == "")
					return;

				var link = document.createElement("a");
				if (val == 0)
					link.download = "plugin.json";
				if (val == 1)
					link.download = "data.json";
				var data = "text/json;charset=utf-8,"
					+ encodeURIComponent(JSON
						.stringify(response));
				link.href = "data:" + data;
				link.click();
			};

			$scope.add = function() {
				var f = document.getElementById('file').files[0], r = new FileReader();

				r.onloadend = function(e) {
					var data = e.target.result;
					$scope.request = JSON.parse(data);
					//send your binary data via $http or $resource or do anything else with it
				}

				r.readAsBinaryString(f);
			}

			$scope.load = function() {
				var response = $scope.request;
				// if this response is coming through http call than make condition according to http response.statusCode
				//check response is undefined, null or empty
				if (typeof response == 'undefined'
					|| response == null || response == "")
					return;

				var link = document.createElement("a");
				link.download = "data.json";
				var data = "text/json;charset=utf-8,"
					+ encodeURIComponent(JSON
						.stringify(response));
				link.href = "data:" + data;
				link.click();
			};

			$scope.parse = function() {

				parseService
					.submit($scope.request)
					.then(
						function(response) {
							
							$scope.product = response;
							$scope.slides = [];
							slides = [];
							if ($scope.product.thumbnails != null
								&& $scope.product.thumbnails.length > 0) {

								setTimeout(
									function() {
										$('#carousel')
											.waltzer(
												{
													auto: false,
												});
									}, 100)
							}

							if ($scope.product.mainimages != null
								&& $scope.product.mainimages.length > 0) {

								setTimeout(
									function() {
										$('#carousel1')
											.waltzer(
												{
													auto: false,
													autoPause: 5000,
													scroll: 3,
													offset: 1
												});
									}, 1000)

							}
							
							logger.logSuccess("Plugin data is stored correctly, you can validate the result");

						},
						function(failure) {
							logger.logError("Please check the values....");
						});
						

			}










			//						
		}]);




angular.module('app').service(
	'parseService',
	function($http, commonService) {
		// Return public API.
		return ({
			
			submit: submit,
			

		});
		
		

		function submit(data) {

			//console.log(og);

			var request = $http({
				method: "POST",
				url: "parse",
				data: data
			});

			return (request.then(commonService.handleSuccess,
				commonService.handleError));

		}

	});



angular.module('app').filter('join', function() {
	return function join(array, separator, prop) {
		if (!Array.isArray(array)) {
			return array; // if not array return original - can also throw error
		}

		return (!angular.isUndefined(prop) ? array.map(function(item) {
			return item[prop];
		}) : array).join(separator);
	};
})

