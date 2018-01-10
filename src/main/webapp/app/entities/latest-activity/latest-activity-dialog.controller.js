(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('LatestActivityDialogController', LatestActivityDialogController);

    LatestActivityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'LatestActivity'];

    function LatestActivityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, LatestActivity) {
        var vm = this;

        vm.latestActivity = entity;
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
            if (vm.latestActivity.id !== null) {
                LatestActivity.update(vm.latestActivity, onSaveSuccess, onSaveError);
            } else {
                LatestActivity.save(vm.latestActivity, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nbmonlineApp:latestActivityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, latestActivity) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        latestActivity.image = base64Data;
                        latestActivity.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
