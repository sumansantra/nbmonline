(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('FlowerFestivalDeleteController',FlowerFestivalDeleteController);

    FlowerFestivalDeleteController.$inject = ['$uibModalInstance', 'entity', 'FlowerFestival'];

    function FlowerFestivalDeleteController($uibModalInstance, entity, FlowerFestival) {
        var vm = this;

        vm.flowerFestival = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FlowerFestival.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
