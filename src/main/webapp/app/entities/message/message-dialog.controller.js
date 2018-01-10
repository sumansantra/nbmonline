(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .controller('MessageDialogController', MessageDialogController);

    MessageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Message'];

    function MessageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Message) {
        var vm = this;

        vm.message = entity;
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
            if (vm.message.id !== null) {
                Message.update(vm.message, onSaveSuccess, onSaveError);
            } else {
                Message.save(vm.message, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nbmonlineApp:messageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
