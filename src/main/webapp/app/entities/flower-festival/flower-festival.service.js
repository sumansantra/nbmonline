(function() {
    'use strict';
    angular
        .module('nbmonlineApp')
        .factory('FlowerFestival', FlowerFestival);

    FlowerFestival.$inject = ['$resource'];

    function FlowerFestival ($resource) {
        var resourceUrl =  'api/flower-festivals/:id';

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
