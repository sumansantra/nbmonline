(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('FlowerFestivalDialogController', FlowerFestivalDialogController);

    FlowerFestivalDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'FlowerFestival', 'FlowerFestivalImage'];

    function FlowerFestivalDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, FlowerFestival, FlowerFestivalImage) {
        var vm = this;

        vm.flowerFestival = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.flowerfestivalimages = FlowerFestivalImage.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.flowerFestival.id !== null) {
                FlowerFestival.update(vm.flowerFestival, onSaveSuccess, onSaveError);
            } else {
                FlowerFestival.save(vm.flowerFestival, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nbmonlineApp:flowerFestivalUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
