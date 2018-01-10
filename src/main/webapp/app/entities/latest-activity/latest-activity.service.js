(function() {
    'use strict';
    angular
        .module('nbmonlineApp')
        .factory('LatestActivity', LatestActivity);

    LatestActivity.$inject = ['$resource'];

    function LatestActivity ($resource) {
        var resourceUrl =  'api/latest-activities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
