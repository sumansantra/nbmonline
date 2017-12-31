(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sub-tender', {
            parent: 'entity',
            url: '/sub-tender?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'nbmonlineApp.subTender.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sub-tender/sub-tenders.html',
                    controller: 'SubTenderController',
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
                    $translatePartialLoader.addPart('subTender');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sub-tender-detail', {
            parent: 'sub-tender',
            url: '/sub-tender/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'nbmonlineApp.subTender.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sub-tender/sub-tender-detail.html',
                    controller: 'SubTenderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('subTender');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SubTender', function($stateParams, SubTender) {
                    return SubTender.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sub-tender',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sub-tender-detail.edit', {
            parent: 'sub-tender-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sub-tender/sub-tender-dialog.html',
                    controller: 'SubTenderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SubTender', function(SubTender) {
                            return SubTender.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sub-tender.new', {
            parent: 'sub-tender',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sub-tender/sub-tender-dialog.html',
                    controller: 'SubTenderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                subTenderName: null,
                                publishDate: null,
                                submitDate: null,
                                endDate: null,
                                subTenderFilePath: null,
                                subTenderFile: null,
                                subTenderFileContentType: null,
                                isActive: false,
                                isDeleted: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sub-tender', null, { reload: 'sub-tender' });
                }, function() {
                    $state.go('sub-tender');
                });
            }]
        })
        .state('sub-tender.edit', {
            parent: 'sub-tender',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sub-tender/sub-tender-dialog.html',
                    controller: 'SubTenderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SubTender', function(SubTender) {
                            return SubTender.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sub-tender', null, { reload: 'sub-tender' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sub-tender.delete', {
            parent: 'sub-tender',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sub-tender/sub-tender-delete-dialog.html',
                    controller: 'SubTenderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SubTender', function(SubTender) {
                            return SubTender.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sub-tender', null, { reload: 'sub-tender' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
