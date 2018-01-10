(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('EventDetailController', EventDetailController);

    EventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Event'];

    function EventDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Event) {
        var vm = this;

        vm.event = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('nbmonlineApp:eventUpdate', function(event, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
