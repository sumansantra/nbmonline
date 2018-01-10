(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('NoticeDetailController', NoticeDetailController);

    NoticeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Notice'];

    function NoticeDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Notice) {
        var vm = this;

        vm.notice = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('nbmonlineApp:noticeUpdate', function(event, result) {
            vm.notice = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
