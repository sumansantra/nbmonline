(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('FlowerFestivalDetailController', FlowerFestivalDetailController);

    FlowerFestivalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'FlowerFestival', 'FlowerFestivalImage'];

    function FlowerFestivalDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, FlowerFestival, FlowerFestivalImage) {
        var vm = this;

        vm.flowerFestival = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('nbmonlineApp:flowerFestivalUpdate', function(event, result) {
            vm.flowerFestival = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
