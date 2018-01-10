(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('LatestActivityDetailController', LatestActivityDetailController);

    LatestActivityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'LatestActivity'];

    function LatestActivityDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, LatestActivity) {
        var vm = this;

        vm.latestActivity = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('nbmonlineApp:latestActivityUpdate', function(event, result) {
            vm.latestActivity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
