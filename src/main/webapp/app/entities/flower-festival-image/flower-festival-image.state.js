(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('flower-festival-image', {
            parent: 'entity',
            url: '/flower-festival-image?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'nbmonlineApp.flowerFestivalImage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flower-festival-image/flower-festival-images.html',
                    controller: 'FlowerFestivalImageController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('flowerFestivalImage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('flower-festival-image-detail', {
            parent: 'flower-festival-image',
            url: '/flower-festival-image/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'nbmonlineApp.flowerFestivalImage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flower-festival-image/flower-festival-image-detail.html',
                    controller: 'FlowerFestivalImageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('flowerFestivalImage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FlowerFestivalImage', function($stateParams, FlowerFestivalImage) {
                    return FlowerFestivalImage.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'flower-festival-image',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('flower-festival-image-detail.edit', {
            parent: 'flower-festival-image-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flower-festival-image/flower-festival-image-dialog.html',
                    controller: 'FlowerFestivalImageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FlowerFestivalImage', function(FlowerFestivalImage) {
                            return FlowerFestivalImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flower-festival-image.new', {
            parent: 'flower-festival-image',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flower-festival-image/flower-festival-image-dialog.html',
                    controller: 'FlowerFestivalImageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                imageName: null,
                                imageFile: null,
                                imageFileContentType: null,
                                isActive: false,
                                isDeleted: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('flower-festival-image', null, { reload: 'flower-festival-image' });
                }, function() {
                    $state.go('flower-festival-image');
                });
            }]
        })
        .state('flower-festival-image.edit', {
            parent: 'flower-festival-image',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flower-festival-image/flower-festival-image-dialog.html',
                    controller: 'FlowerFestivalImageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FlowerFestivalImage', function(FlowerFestivalImage) {
                            return FlowerFestivalImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flower-festival-image', null, { reload: 'flower-festival-image' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flower-festival-image.delete', {
            parent: 'flower-festival-image',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flower-festival-image/flower-festival-image-delete-dialog.html',
                    controller: 'FlowerFestivalImageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FlowerFestivalImage', function(FlowerFestivalImage) {
                            return FlowerFestivalImage.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flower-festival-image', null, { reload: 'flower-festival-image' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
