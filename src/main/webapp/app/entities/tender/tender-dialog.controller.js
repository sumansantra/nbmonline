(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('TenderDialogController', TenderDialogController);

    TenderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Tender'];

    function TenderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Tender) {
        var vm = this;

        vm.tender = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.tender.id !== null) {
                Tender.update(vm.tender, onSaveSuccess, onSaveError);
            } else {
                Tender.save(vm.tender, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nbmonlineApp:tenderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.publishDate = false;
        vm.datePickerOpenStatus.submitDate = false;
        vm.datePickerOpenStatus.endDate = false;

        vm.setTenderFile = function ($file, tender) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        tender.tenderFile = base64Data;
                        tender.tenderFileContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
