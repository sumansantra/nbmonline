'use strict';

describe('Controller Tests', function() {

    describe('FlowerFestival Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFlowerFestival, MockFlowerFestivalImage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFlowerFestival = jasmine.createSpy('MockFlowerFestival');
            MockFlowerFestivalImage = jasmine.createSpy('MockFlowerFestivalImage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'FlowerFestival': MockFlowerFestival,
                'FlowerFestivalImage': MockFlowerFestivalImage
            };
            createController = function() {
                $injector.get('$controller')("FlowerFestivalDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'nbmonlineApp:flowerFestivalUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
