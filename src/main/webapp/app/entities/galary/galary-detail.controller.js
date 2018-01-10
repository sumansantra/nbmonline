(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('GalaryDetailController', GalaryDetailController);

    GalaryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Galary'];

    function GalaryDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Galary) {
        var vm = this;

        vm.galary = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('nbmonlineApp:galaryUpdate', function(event, result) {
            vm.galary = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
