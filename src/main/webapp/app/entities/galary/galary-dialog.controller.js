(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('GalaryDialogController', GalaryDialogController);

    GalaryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Galary'];

    function GalaryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Galary) {
        var vm = this;

        vm.galary = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.galary.id !== null) {
                Galary.update(vm.galary, onSaveSuccess, onSaveError);
            } else {
                Galary.save(vm.galary, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nbmonlineApp:galaryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImageFile = function ($file, galary) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        galary.imageFile = base64Data;
                        galary.imageFileContentType = $file.type;
                    });
                });
            }
        };

    }
})();
