
angular.module('app').controller(
	'GenDataCtrl',
	[
		'$scope',
		'genService',
		'$filter', 'logger', 'uibDateParser',
		function($scope, genService, $filter, logger, uibDateParser) {
			
			
			
				
			

			$scope.popup1 = {
				opened: false
			};

			$scope.popup2 = {
				opened: false
			};

			$scope.format = 'dd/MM/yyyy';

			$scope.today = function() {
				$scope.fromdate = new Date();
				$scope.todate = new Date();
			};
			$scope.today();

			$scope.clear = function() {
				$scope.dt = null;
			};

			$scope.open1 = function() {
				$scope.popup1.opened = true;
			};

			$scope.open2 = function() {
				$scope.popup2.opened = true;
			};

			$scope.dateOptions = {

				formatYear: 'yy',
				startingDay: 0
			};


			$scope.hosts = [];



			$scope.loadhosts = function(command) {


				genService
					.host(command)
					.then(
						function(response) {
							
							$scope.hosts = response;

							


						}, function(failure) {
							logger.logError("Connectivity Issue....");
						});




			}

			$scope.canSubmit = function() {
				return $scope.form_gen.$valid;
			};


			$scope.search = function() {


				var filter = {};
				filter.fromdate = new Date($scope.fromdate).setHours(0, 0, 0, 0);
				filter.todate = new Date($scope.todate).setHours(0, 0, 0, 0);
				filter.host = $scope.host;
				filter.formatExport = $scope.formatExport;
				filter.pattern = $scope.pattern;
				filter.category = $scope.category;
				filter.noofrecs = $scope.noofrecs;
				filter.start = $scope.start - 1;


				genService
					.search(filter)
					.then(
						function(response) {
							if (typeof response == 'undefined'
								|| response == null || response == "") {
								logger.logError("No Data Exist");
								return;
							}
									var link = document.createElement("a");
									link.download = filter.formatExport + ".csv";
									var data = "text/csv;charset=utf-8,"
										+ encodeURIComponent(response);
									link.href = "data:" + data;
									link.click();
						}, function(failure) {
							logger.logError("Connectivity Issue....");
						});




			}


			$scope.loadhosts();


		


		}]);




angular.module('app').service(
	'genService',
	function($http, commonService) {
		// Return public API.
		return ({
			host: host,
			search: search,

		});



		function host() {



			var request = $http({
				method: "GET",
				url: "gendata/host"
			});

			return (request.then(commonService.handleSuccess,
				commonService.handleError));

		}

		function search(filter) {



			var request = $http({
				method: "GET",
				url: "gendata/search",
				params: filter
			});

			return (request.then(commonService.handleSuccess,
				commonService.handleError));

		}



	});



