function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["src-app-shared-error-error-module"], {
  /***/
  "./src/app/shared/error/error.module.ts":
  /*!**********************************************!*\
    !*** ./src/app/shared/error/error.module.ts ***!
    \**********************************************/

  /*! exports provided: ErrorModule */

  /***/
  function srcAppSharedErrorErrorModuleTs(module, __webpack_exports__, __webpack_require__) {
    "use strict";

    __webpack_require__.r(__webpack_exports__);
    /* harmony export (binding) */


    __webpack_require__.d(__webpack_exports__, "ErrorModule", function () {
      return ErrorModule;
    });
    /* harmony import */


    var _shared_layout_header_breadcrumb_header_breadcrumb_module__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(
    /*! ../../shared/layout/header-breadcrumb/header-breadcrumb.module */
    "./src/app/shared/layout/header-breadcrumb/header-breadcrumb.module.ts");
    /* harmony import */


    var primeng_toast__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(
    /*! primeng/toast */
    "./node_modules/primeng/__ivy_ngcc__/fesm2015/primeng-toast.js");
    /* harmony import */


    var primeng_messages__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(
    /*! primeng/messages */
    "./node_modules/primeng/__ivy_ngcc__/fesm2015/primeng-messages.js");
    /* harmony import */


    var _angular_forms__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(
    /*! @angular/forms */
    "./node_modules/@angular/forms/fesm2015/forms.js");
    /* harmony import */


    var primeng_table__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(
    /*! primeng/table */
    "./node_modules/primeng/__ivy_ngcc__/fesm2015/primeng-table.js");
    /* harmony import */


    var primeng__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(
    /*! primeng */
    "./node_modules/primeng/__ivy_ngcc__/fesm2015/primeng.js");
    /* harmony import */


    var _app_common_module__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(
    /*! ../../app.common.module */
    "./src/app/app.common.module.ts");
    /* harmony import */


    var _angular_core__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(
    /*! @angular/core */
    "./node_modules/@angular/core/fesm2015/core.js");
    /* harmony import */


    var _angular_common__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(
    /*! @angular/common */
    "./node_modules/@angular/common/fesm2015/common.js");
    /* harmony import */


    var _error_component__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(
    /*! ./error.component */
    "./src/app/shared/error/error.component.ts");
    /* harmony import */


    var _error_routing__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(
    /*! ./error.routing */
    "./src/app/shared/error/error.routing.ts");

    var ErrorModule = function ErrorModule() {
      _classCallCheck(this, ErrorModule);
    };

    ErrorModule.ɵmod = _angular_core__WEBPACK_IMPORTED_MODULE_7__["ɵɵdefineNgModule"]({
      type: ErrorModule
    });
    ErrorModule.ɵinj = _angular_core__WEBPACK_IMPORTED_MODULE_7__["ɵɵdefineInjector"]({
      factory: function ErrorModule_Factory(t) {
        return new (t || ErrorModule)();
      },
      imports: [[_angular_common__WEBPACK_IMPORTED_MODULE_8__["CommonModule"], _error_routing__WEBPACK_IMPORTED_MODULE_10__["ErrorRoutingModule"], _app_common_module__WEBPACK_IMPORTED_MODULE_6__["AppCommonModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["PanelModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["TabViewModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["ButtonModule"], primeng_table__WEBPACK_IMPORTED_MODULE_4__["TableModule"], _angular_forms__WEBPACK_IMPORTED_MODULE_3__["FormsModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["DialogModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["CheckboxModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["InputTextModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["ConfirmDialogModule"], primeng_messages__WEBPACK_IMPORTED_MODULE_2__["MessagesModule"], primeng_toast__WEBPACK_IMPORTED_MODULE_1__["ToastModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["ProgressSpinnerModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["DropdownModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["AutoCompleteModule"], _shared_layout_header_breadcrumb_header_breadcrumb_module__WEBPACK_IMPORTED_MODULE_0__["HeaderBreadCrumbModule"]]]
    });

    (function () {
      (typeof ngJitMode === "undefined" || ngJitMode) && _angular_core__WEBPACK_IMPORTED_MODULE_7__["ɵɵsetNgModuleScope"](ErrorModule, {
        declarations: [_error_component__WEBPACK_IMPORTED_MODULE_9__["ErrorComponent"]],
        imports: [_angular_common__WEBPACK_IMPORTED_MODULE_8__["CommonModule"], _error_routing__WEBPACK_IMPORTED_MODULE_10__["ErrorRoutingModule"], _app_common_module__WEBPACK_IMPORTED_MODULE_6__["AppCommonModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["PanelModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["TabViewModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["ButtonModule"], primeng_table__WEBPACK_IMPORTED_MODULE_4__["TableModule"], _angular_forms__WEBPACK_IMPORTED_MODULE_3__["FormsModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["DialogModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["CheckboxModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["InputTextModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["ConfirmDialogModule"], primeng_messages__WEBPACK_IMPORTED_MODULE_2__["MessagesModule"], primeng_toast__WEBPACK_IMPORTED_MODULE_1__["ToastModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["ProgressSpinnerModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["DropdownModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["AutoCompleteModule"], _shared_layout_header_breadcrumb_header_breadcrumb_module__WEBPACK_IMPORTED_MODULE_0__["HeaderBreadCrumbModule"]]
      });
    })();
    /*@__PURE__*/


    (function () {
      _angular_core__WEBPACK_IMPORTED_MODULE_7__["ɵsetClassMetadata"](ErrorModule, [{
        type: _angular_core__WEBPACK_IMPORTED_MODULE_7__["NgModule"],
        args: [{
          imports: [_angular_common__WEBPACK_IMPORTED_MODULE_8__["CommonModule"], _error_routing__WEBPACK_IMPORTED_MODULE_10__["ErrorRoutingModule"], _app_common_module__WEBPACK_IMPORTED_MODULE_6__["AppCommonModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["PanelModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["TabViewModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["ButtonModule"], primeng_table__WEBPACK_IMPORTED_MODULE_4__["TableModule"], _angular_forms__WEBPACK_IMPORTED_MODULE_3__["FormsModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["DialogModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["CheckboxModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["InputTextModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["ConfirmDialogModule"], primeng_messages__WEBPACK_IMPORTED_MODULE_2__["MessagesModule"], primeng_toast__WEBPACK_IMPORTED_MODULE_1__["ToastModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["ProgressSpinnerModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["DropdownModule"], primeng__WEBPACK_IMPORTED_MODULE_5__["AutoCompleteModule"], _shared_layout_header_breadcrumb_header_breadcrumb_module__WEBPACK_IMPORTED_MODULE_0__["HeaderBreadCrumbModule"]],
          declarations: [_error_component__WEBPACK_IMPORTED_MODULE_9__["ErrorComponent"]]
        }]
      }], null, null);
    })();
    /***/

  },

  /***/
  "./src/app/shared/error/error.routing.ts":
  /*!***********************************************!*\
    !*** ./src/app/shared/error/error.routing.ts ***!
    \***********************************************/

  /*! exports provided: ErrorRoutingModule */

  /***/
  function srcAppSharedErrorErrorRoutingTs(module, __webpack_exports__, __webpack_require__) {
    "use strict";

    __webpack_require__.r(__webpack_exports__);
    /* harmony export (binding) */


    __webpack_require__.d(__webpack_exports__, "ErrorRoutingModule", function () {
      return ErrorRoutingModule;
    });
    /* harmony import */


    var _angular_router__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(
    /*! @angular/router */
    "./node_modules/@angular/router/fesm2015/router.js");
    /* harmony import */


    var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(
    /*! @angular/core */
    "./node_modules/@angular/core/fesm2015/core.js");
    /* harmony import */


    var _error_component__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(
    /*! ./error.component */
    "./src/app/shared/error/error.component.ts");

    var routes = [{
      path: '',
      component: _error_component__WEBPACK_IMPORTED_MODULE_2__["ErrorComponent"]
    }];

    var ErrorRoutingModule = function ErrorRoutingModule() {
      _classCallCheck(this, ErrorRoutingModule);
    };

    ErrorRoutingModule.ɵmod = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineNgModule"]({
      type: ErrorRoutingModule
    });
    ErrorRoutingModule.ɵinj = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineInjector"]({
      factory: function ErrorRoutingModule_Factory(t) {
        return new (t || ErrorRoutingModule)();
      },
      imports: [[_angular_router__WEBPACK_IMPORTED_MODULE_0__["RouterModule"].forChild(routes)], _angular_router__WEBPACK_IMPORTED_MODULE_0__["RouterModule"]]
    });

    (function () {
      (typeof ngJitMode === "undefined" || ngJitMode) && _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵsetNgModuleScope"](ErrorRoutingModule, {
        imports: [_angular_router__WEBPACK_IMPORTED_MODULE_0__["RouterModule"]],
        exports: [_angular_router__WEBPACK_IMPORTED_MODULE_0__["RouterModule"]]
      });
    })();
    /*@__PURE__*/


    (function () {
      _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵsetClassMetadata"](ErrorRoutingModule, [{
        type: _angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"],
        args: [{
          imports: [_angular_router__WEBPACK_IMPORTED_MODULE_0__["RouterModule"].forChild(routes)],
          exports: [_angular_router__WEBPACK_IMPORTED_MODULE_0__["RouterModule"]]
        }]
      }], null, null);
    })();
    /***/

  }
}]);
//# sourceMappingURL=src-app-shared-error-error-module-es5.js.map