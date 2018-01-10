(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('LatestActivityDeleteController',LatestActivityDeleteController);

    LatestActivityDeleteController.$inject = ['$uibModalInstance', 'entity', 'LatestActivity'];

    function LatestActivityDeleteController($uibModalInstance, entity, LatestActivity) {
        var vm = this;

        vm.latestActivity = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LatestActivity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
