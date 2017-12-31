(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('TenderDeleteController',TenderDeleteController);

    TenderDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tender'];

    function TenderDeleteController($uibModalInstance, entity, Tender) {
        var vm = this;

        vm.tender = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tender.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
