
angular.module('app').controller(
	'JobCtrl',
	[
		'$scope',
		'jobService',
		'$filter', 'logger', '$interval',
		function($scope, jobService, $filter, logger,$interval) {

			$scope.request = {};
			$scope.request.job = {};
			$scope.request.state = "";
			$scope.request.crawlId = "All";
			$scope.request.iteration = 2;
			$scope.request.noofurl = 1;
			$scope.request.type = "";
			$scope.fetch = {};
			$scope.fetch.removequery = 1;
			$scope.fetch.removepath = 0;
			$scope.currentJob = null;
			$scope.jobs = [];
			$scope.iterate = false;
			$scope.interval = null;
			
			$scope.retry = 0;
			
			$scope.tasks = [{value:'',name:"All"},{value:'INJECT',name:"INJECT"},{value:'GENERATE',name:"GENERATE"},{value:'FETCH',name:"FETCH"},{value:'PARSE',name:"PARSE"},{value:'UPDATEDB',name:"UPDATEDB"},{value:'INVERTLINKS',name:'INVERTLINKS'},{value:'DEDUP',name:'DEDUP'},{value:'INDEX',name:"INDEX"}]; 
			
			$scope.myFilter = function (item) { 
			  if($scope.request.crawlId === "All" && $scope.request.state === '' && $scope.request.type === '')
			  {
				return item;
				}	
			  else if ((item.crawlId === $scope.request.crawlId ||  $scope.request.crawlId === 'All') && (item.state === $scope.request.state || $scope.request.state === '') && (item.type === $scope.request.type || $scope.request.type === '')) {
			  return item;
			 }
			};
			
			$scope.clear = function(type) {
				$scope.jobs = [];
				$scope.iterate = false;
			}
			
			$scope.list = function() {


				return jobService
					.list()
					.then(
						function(response) {
							if (response != null && response.length == 0) {
								logger.logError("No Jobs Running");
							}
							else {
								
								$scope.jobs = response;
								return response;
							}
						});




			}
			
			
			$scope.loadUrls = function() {
				var fetch = {};
				fetch.url = $scope.fetch.urls.split("\n");
				fetch.pattern = $scope.fetch.pattern;
				fetch.removequery = $scope.fetch.removequery;
				fetch.removepath = $scope.fetch.removepath;
				fetch.jsmode = $scope.fetch.jsmode;
				fetch.waitsec = $scope.fetch.waitsec;
				fetch.visibilitySelector = $scope.fetch.visibilitySelector;
				 jobService
					.childurls(fetch)
					.then(
						function(response) {
							
							$scope.request.seeds = response.childurls.join("\n");
							 
							logger.logSuccess(response.childurls.length +" Urls loaded");
							
						},function(failure)
						{
							logger.logError($scope.request.type + " Job failed");
						});
			}
			
			
			$scope.createJob = function(type) {
				
				
				
				var request = {};
				if(typeof type == 'undefined' || type == null)
				{
					request.type = $scope.request.type;
				}
				else{
					request.type = type;
				}
				request.confId = 'default';
				request.crawlId = $scope.request.crawlId;
				request.args = {};
				request.args.seedName = $scope.request.crawlId;
				if(type == 'GENERATE'|| request.type == 'GENERATE')
				{
					request.args.topN = $scope.request.noofurl+"";
					request.args.filter = "true";
				}
				if(type == 'FETCH' || request.type == 'FETCH')
				{
					request.args.parse = "true";
					request.args['fetcher.follow.outlinks.depth'] = 2;
				}	
				console.log(request);
				return jobService
					.createJob(request)
					.then(
						function(response) {
							
						
							$scope.jobs.push(response);
							return response;
							
						},function(failure)
						{
							logger.logError($scope.request.type + " Job failed");
						});
				

			}
			
			$scope.auto = function(type) {
				
				
				
				$scope.jobiteration = 1;
				
				//$scope.currentJob = $scope.checkCurrentJob();
				
				$scope.currentJob = {};
				$scope.currentJob.type = '';//no status
				$scope.clear();
				
				if($scope.request.crawlId === 'All')
				{
					logger.logError("Please set the Job Name");
					return;
				}
				
				$scope.interval = $interval( function(){ $scope.checkstatus(); }, 5000);
				
				$scope.invokeNext();
				
			}
			
			$scope.$watch('currentJob.state', function() {
       			 
       			 if($scope.currentJob != null && typeof($scope.currentJob.state ) != undefined && $scope.currentJob.state == 'FINISHED')
       			 {
						$scope.updateJob($scope.currentJob);
						$scope.invokeNext();
				 }
       			 
       			 
       			 
    		});
			
			$scope.checkCurrentJob = function() {
				return $scope.list().then(function(jobs){
					
					for(var i =0;i<$scope.jobs.length;i++)
					{
						if((jobs[i].crawlId === $scope.request.crawlId) && (jobs.state === "FAILED" || jobs.state === "RUNNING"))
						{
							return currentjob;
							
						}
						else{
							return null;
						}
					}	
				})
				
			}
			
			$scope.updateJob = function(currentjob) {
				
				for(var i =0;i<$scope.jobs.length;i++)
				{
					if($scope.jobs[i].id === currentjob.id)
					{
						$scope.jobs[i] = currentjob;
						break;
					}
				}
				
				
			}
			
			$scope.cancel = function() {
				
				$interval.cancel($scope.interval);
				$scope.interval = null;
				
				
				
			}
    		 
    		  $scope.invokeNext = function() {
					
						 var nexttask = null;
						for(var i =0;i<$scope.tasks.length;i++)
						{
							if($scope.currentJob != null && $scope.tasks[i].value === $scope.currentJob.type)
							{
								if(typeof $scope.tasks[i+1] != 'undefined')
								nexttask = $scope.tasks[i+1].value;
								break;
								
							}
						}
						
						if(nexttask != null)
						{
							$scope.createJob(nexttask).then((currentjob) =>{
								$scope.currentJob = currentjob;
								
							});
						}
						else{
							
							
							if($scope.jobiteration < $scope.request.iteration)
							{
								logger.logSuccess("Iteration completed - " + $scope.jobiteration);
								$scope.jobiteration = $scope.jobiteration + 1;
								//no need for inject
								$scope.createJob('GENERATE').then((currentjob) =>{
									$scope.currentJob = currentjob;
								
								});
								
							}
							else{
								logger.logSuccess("Job completed!");
								$interval.cancel($scope.interval);
								$scope.currentJob = null;
								$scope.jobiteration =  1;
								
							}
							
							
						}
					
				}
			
			  $scope.checkstatus = function() {
       				if($scope.currentJob == null || typeof $scope.currentJob.id == 'undefined' || $scope.currentJob.id == null || $scope.currentJob.id == '')
       				{
						return;
					}
       				 
       				jobService
					.status($scope.currentJob.id)
					.then(
						function(response) {
							$scope.currentJob = response;
								
		        			
						}); 
   			  }

    
			

			$scope.setSeeds = function() {

				var seed = {};
				seed.name = $scope.request.crawlId;
				seed.seedUrls = $scope.request.seeds.split("\n");
				if(seed.seedUrls.length > 50)
				{
					logger.logError("50 urls per batch is allowed.");
					return;
				}
				$scope.request.seed_data = seed;

				$scope.seedfile = {};
				jobService
					.setSeed($scope.request.seed_data)
					.then(
						function(response) {
							if (response == null && response.length == 0) {
								logger.logError("Seed Data is not configured configured");
							}
							else {
								$scope.seedfile = response.seedfile;
								console.log($scope.seedfile);
							}
						}, function(failure) {
							console.log(failure);
							logger.logError("Please check the values....");
						});


			}

			

			$scope.canSubmit = function() {
				return $scope.form_job.$valid && $scope.request.crawlId != 'All';
			};

		
			
			

			$scope.command = function(command) {


				jobService
					.command(command)
					.then(
						function(response) {
							
							logger.logSuccess(response.message);
							
						});




			}

			//						
		}]);




angular.module('app').service(
	'jobService',
	function($http, commonService) {
		// Return public API.
		return ({
			list: list,
			setSeed: setSeed,
			command: command,
			createJob: createJob,
			status: status,
			childurls: childurls,
		});



		function list() {



			var request = $http({
				method: "GET",
				url: "crawler/job"
			});

			return (request.then(commonService.handleSuccess,
				commonService.handleError));

		}
		
		function status(id) {



			var request = $http({
				method: "GET",
				url: "crawler/generic/job/"+id
			});

			return (request.then(commonService.handleSuccess,
				commonService.handleError));

		}

		function setSeed(seeds) {



			var request = $http({
				method: "POST",
				url: "crawler/seed/create",
				data: seeds
			});

			return (request.then(commonService.handleSuccess,
				commonService.handleError));

		}
		
		function childurls(caturls) {



			var request = $http({
				method: "POST",
				url: "crawler/childurls",
				data: caturls
			});

			return (request.then(commonService.handleSuccess,
				commonService.handleError));

		}

		function command(command) {



			var request = $http({
				method: "GET",
				url: "crawler/" + command

			});

			return (request.then(commonService.handleSuccess,
				commonService.handleError));

		}
		
		function createJob(request) {



			var request = $http({
				method: "POST",
				url: "crawler/job/create",
				data: request

			});

			return (request.then(commonService.handleSuccess,
				commonService.handleError));

		}

	});



