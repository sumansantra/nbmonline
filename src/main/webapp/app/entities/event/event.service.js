(function() {
    'use strict';
    angular
        .module('nbmonlineApp')
        .factory('Event', Event);

    Event.$inject = ['$resource', 'DateUtils'];

    function Event ($resource, DateUtils) {
        var resourceUrl =  'api/events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.eventDate = DateUtils.convertDateTimeFromServer(data.eventDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
