(function() {
    'use strict';
    angular
        .module('nbmonlineApp')
        .factory('Message', Message);

    Message.$inject = ['$resource'];

    function Message ($resource) {
        var resourceUrl =  'api/messages/:id';

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
