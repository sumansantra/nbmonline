(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('galary', {
            parent: 'entity',
            url: '/galary?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'nbmonlineApp.galary.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/galary/galaries.html',
                    controller: 'GalaryController',
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
                    $translatePartialLoader.addPart('galary');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('galary-detail', {
            parent: 'galary',
            url: '/galary/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'nbmonlineApp.galary.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/galary/galary-detail.html',
                    controller: 'GalaryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('galary');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Galary', function($stateParams, Galary) {
                    return Galary.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'galary',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('galary-detail.edit', {
            parent: 'galary-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/galary/galary-dialog.html',
                    controller: 'GalaryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Galary', function(Galary) {
                            return Galary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('galary.new', {
            parent: 'galary',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/galary/galary-dialog.html',
                    controller: 'GalaryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                imageName: null,
                                imageFile: null,
                                imageFileContentType: null,
                                description: null,
                                isActive: false,
                                isDeleted: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('galary', null, { reload: 'galary' });
                }, function() {
                    $state.go('galary');
                });
            }]
        })
        .state('galary.edit', {
            parent: 'galary',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/galary/galary-dialog.html',
                    controller: 'GalaryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Galary', function(Galary) {
                            return Galary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('galary', null, { reload: 'galary' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('galary.delete', {
            parent: 'galary',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/galary/galary-delete-dialog.html',
                    controller: 'GalaryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Galary', function(Galary) {
                            return Galary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('galary', null, { reload: 'galary' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
