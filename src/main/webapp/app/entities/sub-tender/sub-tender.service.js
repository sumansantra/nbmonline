(function() {
    'use strict';
    angular
        .module('nbmonlineApp')
        .factory('SubTender', SubTender);

    SubTender.$inject = ['$resource', 'DateUtils'];

    function SubTender ($resource, DateUtils) {
        var resourceUrl =  'api/sub-tenders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.publishDate = DateUtils.convertDateTimeFromServer(data.publishDate);
                        data.submitDate = DateUtils.convertDateTimeFromServer(data.submitDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
