
angular
	.module('app')
	.service(
		'commonService',
		function($q,logger) {
			// Return public API.
			return ({
				handleError: handleError,
				handleSuccess: handleSuccess,
				getTodayDatetime: getTodayDatetime,
				getFutureDatetime: getTodayDatetime,
				formatDate: formatDate

			});

			function handleError(response) {



				if (!angular.isObject(response.data)
					|| !response.data.message) {
					return ($q.reject(response.data));
				}
				//							// Otherwise, use expected error message.
				return ($q.reject(response.data.message));
			}

			function handleSuccess(response) {
				return (response.data);
			}

			function getTodayDatetime() {
				var currDate = new Date();

				var finalDateTime = formatDate(currDate, currDate);

				return finalDateTime;
			}

			function getFutureDatetime() {
				var currDate = new Date();
				currDate.setDate(currDate.getDate() + 1);
				var finalDateTime = formatDate(currDate, currDate);

				return finalDateTime;

			}

			function formatDate(date, time) {
				var month = parseInt(date.getMonth()) + 1;
				var date = date.getDate() + "/" + month + "/"
					+ date.getFullYear() + " "
					+ time.getHours() + ":" + time.getMinutes();
				return date;
			}

			
		});
