(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('FlowerFestivalImageDetailController', FlowerFestivalImageDetailController);

    FlowerFestivalImageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'FlowerFestivalImage', 'FlowerFestival'];

    function FlowerFestivalImageDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, FlowerFestivalImage, FlowerFestival) {
        var vm = this;

        vm.flowerFestivalImage = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('nbmonlineApp:flowerFestivalImageUpdate', function(event, result) {
            vm.flowerFestivalImage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
