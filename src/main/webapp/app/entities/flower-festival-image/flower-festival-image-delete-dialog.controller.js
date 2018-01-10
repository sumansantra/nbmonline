(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('FlowerFestivalImageDeleteController',FlowerFestivalImageDeleteController);

    FlowerFestivalImageDeleteController.$inject = ['$uibModalInstance', 'entity', 'FlowerFestivalImage'];

    function FlowerFestivalImageDeleteController($uibModalInstance, entity, FlowerFestivalImage) {
        var vm = this;

        vm.flowerFestivalImage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FlowerFestivalImage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
