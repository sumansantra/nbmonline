(function() {
    'use strict';
    angular
        .module('nbmonlineApp')
        .factory('News', News);

    News.$inject = ['$resource', 'DateUtils'];

    function News ($resource, DateUtils) {
        var resourceUrl =  'api/news/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.publishDate = DateUtils.convertDateTimeFromServer(data.publishDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
