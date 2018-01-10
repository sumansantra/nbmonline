(function() {
    'use strict';

    angular
        .module('nbmonlineApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('latest-activity', {
            parent: 'entity',
            url: '/latest-activity?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'nbmonlineApp.latestActivity.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/latest-activity/latest-activities.html',
                    controller: 'LatestActivityController',
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
                    $translatePartialLoader.addPart('latestActivity');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('latest-activity-detail', {
            parent: 'latest-activity',
            url: '/latest-activity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'nbmonlineApp.latestActivity.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/latest-activity/latest-activity-detail.html',
                    controller: 'LatestActivityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('latestActivity');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LatestActivity', function($stateParams, LatestActivity) {
                    return LatestActivity.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'latest-activity',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('latest-activity-detail.edit', {
            parent: 'latest-activity-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/latest-activity/latest-activity-dialog.html',
                    controller: 'LatestActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LatestActivity', function(LatestActivity) {
                            return LatestActivity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('latest-activity.new', {
            parent: 'latest-activity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/latest-activity/latest-activity-dialog.html',
                    controller: 'LatestActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                activityName: null,
                                description: null,
                                image: null,
                                imageContentType: null,
                                isActive: false,
                                isDeleted: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('latest-activity', null, { reload: 'latest-activity' });
                }, function() {
                    $state.go('latest-activity');
                });
            }]
        })
        .state('latest-activity.edit', {
            parent: 'latest-activity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/latest-activity/latest-activity-dialog.html',
                    controller: 'LatestActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LatestActivity', function(LatestActivity) {
                            return LatestActivity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('latest-activity', null, { reload: 'latest-activity' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('latest-activity.delete', {
            parent: 'latest-activity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/latest-activity/latest-activity-delete-dialog.html',
                    controller: 'LatestActivityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LatestActivity', function(LatestActivity) {
                            return LatestActivity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('latest-activity', null, { reload: 'latest-activity' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
