(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('SubTenderDeleteController',SubTenderDeleteController);

    SubTenderDeleteController.$inject = ['$uibModalInstance', 'entity', 'SubTender'];

    function SubTenderDeleteController($uibModalInstance, entity, SubTender) {
        var vm = this;

        vm.subTender = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SubTender.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
