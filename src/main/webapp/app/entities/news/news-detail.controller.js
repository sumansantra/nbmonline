(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('NewsDetailController', NewsDetailController);

    NewsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'News'];

    function NewsDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, News) {
        var vm = this;

        vm.news = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('nbmonlineApp:newsUpdate', function(event, result) {
            vm.news = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
