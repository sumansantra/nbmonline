(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('SubTenderDetailController', SubTenderDetailController);

    SubTenderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'SubTender', 'Tender'];

    function SubTenderDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, SubTender, Tender) {
        var vm = this;

        vm.subTender = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('nbmonlineApp:subTenderUpdate', function(event, result) {
            vm.subTender = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
