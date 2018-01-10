'use strict';

describe('Controller Tests', function() {

    describe('FlowerFestivalImage Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFlowerFestivalImage, MockFlowerFestival;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFlowerFestivalImage = jasmine.createSpy('MockFlowerFestivalImage');
            MockFlowerFestival = jasmine.createSpy('MockFlowerFestival');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'FlowerFestivalImage': MockFlowerFestivalImage,
                'FlowerFestival': MockFlowerFestival
            };
            createController = function() {
                $injector.get('$controller')("FlowerFestivalImageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'nbmonlineApp:flowerFestivalImageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
