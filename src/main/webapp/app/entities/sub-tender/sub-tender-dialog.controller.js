(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('SubTenderDialogController', SubTenderDialogController);

    SubTenderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'SubTender', 'Tender'];

    function SubTenderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, SubTender, Tender) {
        var vm = this;

        vm.subTender = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.tenders = Tender.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.subTender.id !== null) {
                SubTender.update(vm.subTender, onSaveSuccess, onSaveError);
            } else {
                SubTender.save(vm.subTender, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nbmonlineApp:subTenderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.publishDate = false;
        vm.datePickerOpenStatus.submitDate = false;
        vm.datePickerOpenStatus.endDate = false;

        vm.setSubTenderFile = function ($file, subTender) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        subTender.subTenderFile = base64Data;
                        subTender.subTenderFileContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
