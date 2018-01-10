(function() {
    'use strict';
    angular
        .module('nbmonlineApp')
        .factory('FlowerFestivalImage', FlowerFestivalImage);

    FlowerFestivalImage.$inject = ['$resource'];

    function FlowerFestivalImage ($resource) {
        var resourceUrl =  'api/flower-festival-images/:id';

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
