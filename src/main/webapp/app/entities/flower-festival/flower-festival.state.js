(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('flower-festival', {
            parent: 'entity',
            url: '/flower-festival?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'nbmonlineApp.flowerFestival.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flower-festival/flower-festivals.html',
                    controller: 'FlowerFestivalController',
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
                    $translatePartialLoader.addPart('flowerFestival');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('flower-festival-detail', {
            parent: 'flower-festival',
            url: '/flower-festival/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'nbmonlineApp.flowerFestival.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flower-festival/flower-festival-detail.html',
                    controller: 'FlowerFestivalDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('flowerFestival');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FlowerFestival', function($stateParams, FlowerFestival) {
                    return FlowerFestival.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'flower-festival',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('flower-festival-detail.edit', {
            parent: 'flower-festival-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flower-festival/flower-festival-dialog.html',
                    controller: 'FlowerFestivalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FlowerFestival', function(FlowerFestival) {
                            return FlowerFestival.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flower-festival.new', {
            parent: 'flower-festival',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flower-festival/flower-festival-dialog.html',
                    controller: 'FlowerFestivalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                festivalName: null,
                                year: null,
                                description: null,
                                isActive: false,
                                isDeleted: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('flower-festival', null, { reload: 'flower-festival' });
                }, function() {
                    $state.go('flower-festival');
                });
            }]
        })
        .state('flower-festival.edit', {
            parent: 'flower-festival',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flower-festival/flower-festival-dialog.html',
                    controller: 'FlowerFestivalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FlowerFestival', function(FlowerFestival) {
                            return FlowerFestival.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flower-festival', null, { reload: 'flower-festival' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flower-festival.delete', {
            parent: 'flower-festival',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flower-festival/flower-festival-delete-dialog.html',
                    controller: 'FlowerFestivalDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FlowerFestival', function(FlowerFestival) {
                            return FlowerFestival.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flower-festival', null, { reload: 'flower-festival' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
