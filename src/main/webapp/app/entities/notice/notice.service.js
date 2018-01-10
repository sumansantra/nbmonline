(function() {
    'use strict';
    angular
        .module('nbmonlineApp')
        .factory('Notice', Notice);

    Notice.$inject = ['$resource', 'DateUtils'];

    function Notice ($resource, DateUtils) {
        var resourceUrl =  'api/notices/:id';

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
