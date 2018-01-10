(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('GalaryDeleteController',GalaryDeleteController);

    GalaryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Galary'];

    function GalaryDeleteController($uibModalInstance, entity, Galary) {
        var vm = this;

        vm.galary = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Galary.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
