(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('TenderDetailController', TenderDetailController);

    TenderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Tender'];

    function TenderDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Tender) {
        var vm = this;

        vm.tender = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('nbmonlineApp:tenderUpdate', function(event, result) {
            vm.tender = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
