(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('FlowerFestivalImageDialogController', FlowerFestivalImageDialogController);

    FlowerFestivalImageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'FlowerFestivalImage', 'FlowerFestival'];

    function FlowerFestivalImageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, FlowerFestivalImage, FlowerFestival) {
        var vm = this;

        vm.flowerFestivalImage = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.flowerfestivals = FlowerFestival.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.flowerFestivalImage.id !== null) {
                FlowerFestivalImage.update(vm.flowerFestivalImage, onSaveSuccess, onSaveError);
            } else {
                FlowerFestivalImage.save(vm.flowerFestivalImage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nbmonlineApp:flowerFestivalImageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImageFile = function ($file, flowerFestivalImage) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        flowerFestivalImage.imageFile = base64Data;
                        flowerFestivalImage.imageFileContentType = $file.type;
                    });
                });
            }
        };

    }
})();
