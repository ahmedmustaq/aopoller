
angular.module('app').controller(
	'GenerateCtrl',
	[
		'$scope',
		'generateService',
		'$filter', 'logger',
		function($scope, generateService, $filter, logger) {

		

			$scope.add = function() {
				var f = document.getElementById('file').files[0], r = new FileReader();

				r.onloadend = function(e) {
					var data = e.target.result;
					$scope.request = JSON.parse(data);

					$scope.generate($scope.request);
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

			$scope.generate = function(request) {
				
					generateService
					.generate($scope.request)
					.then(
						function(response) {
							logger.logSuccess("Please check the console");
							console.log(response);
							},function (error)
							{
								logger.logError("Please check the console");
								console.log(error);
							});

		
			}

			//						
		}]);




angular.module('app').service(
	'generateService',
	function($http, commonService) {
		// Return public API.
		return ({
			generate: generate,
		});



		function generate(data) {

			//console.log(og);

			var request = $http({
				method: "POST",
				url: "plugin/generate",
				data: data
			});

			return (request.then(commonService.handleSuccess,
				commonService.handleError));

		}

	});



