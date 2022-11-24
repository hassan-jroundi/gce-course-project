(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["src-app-features-login-login-module"],{

/***/ "./src/app/features/login/login.component.ts":
/*!***************************************************!*\
  !*** ./src/app/features/login/login.component.ts ***!
  \***************************************************/
/*! exports provided: LoginComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LoginComponent", function() { return LoginComponent; });
/* harmony import */ var _core_services_site_service__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./../../core/services/site.service */ "./src/app/core/services/site.service.ts");
/* harmony import */ var _core_services_http_service__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./../../core/services/http.service */ "./src/app/core/services/http.service.ts");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var src_app_core_services_user_data_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! src/app/core/services/user-data.service */ "./src/app/core/services/user-data.service.ts");
/* harmony import */ var src_app_core_services_toast_service__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! src/app/core/services/toast.service */ "./src/app/core/services/toast.service.ts");
/* harmony import */ var src_app_core_services_route_state_service__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! src/app/core/services/route-state.service */ "./src/app/core/services/route-state.service.ts");
/* harmony import */ var src_app_core_services_session_service__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! src/app/core/services/session.service */ "./src/app/core/services/session.service.ts");
/* harmony import */ var _ngx_translate_core__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! @ngx-translate/core */ "./node_modules/@ngx-translate/core/__ivy_ngcc__/fesm2015/ngx-translate-core.js");
/* harmony import */ var src_app_core_services_user_context_service__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! src/app/core/services/user-context.service */ "./src/app/core/services/user-context.service.ts");
/* harmony import */ var src_environments_environment__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! src/environments/environment */ "./src/environments/environment.ts");
/* harmony import */ var primeng__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(/*! primeng */ "./node_modules/primeng/__ivy_ngcc__/fesm2015/primeng.js");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__(/*! @angular/forms */ "./node_modules/@angular/forms/fesm2015/forms.js");
/* harmony import */ var primeng_inputtext__WEBPACK_IMPORTED_MODULE_12__ = __webpack_require__(/*! primeng/inputtext */ "./node_modules/primeng/__ivy_ngcc__/fesm2015/primeng-inputtext.js");
/* harmony import */ var primeng_button__WEBPACK_IMPORTED_MODULE_13__ = __webpack_require__(/*! primeng/button */ "./node_modules/primeng/__ivy_ngcc__/fesm2015/primeng-button.js");























class LoginComponent {
    constructor(userService, toastService, routeStateService, sessionService, translate, userContextService, http, siteService) {
        this.userService = userService;
        this.toastService = toastService;
        this.routeStateService = routeStateService;
        this.sessionService = sessionService;
        this.translate = translate;
        this.userContextService = userContextService;
        this.http = http;
        this.siteService = siteService;
        this.sitesResult = [];
    }
    ngOnInit() {
        this.getAllPistes();
        this.userName = "";
        this.password = "";
        this.locale = this.sessionService.getItem("ng-prime-language");
        this.version = src_environments_environment__WEBPACK_IMPORTED_MODULE_9__["environment"].version;
        this.msgs = [{ severity: 'info', detail: 'Utilisateur : admin' }, { severity: 'info', detail: 'Mot de passe : password' }];
    }
    getAllPistes() {
        this.http
            .get(src_environments_environment__WEBPACK_IMPORTED_MODULE_9__["environment"].apiSite)
            .subscribe((result) => {
            this.sitesResult = result;
            let emptySite = {
                "adresse": "",
                "id": "",
                "nom": "",
                "ville": ""
            };
            this.sitesResult.splice(0, 0, emptySite);
        });
    }
    onClickLogin() {
        let user = this.userService.getUserByUserNameAndPassword(this.userName, this.password);
        if (user) {
            this.userContextService.setUser(user);
            this.routeStateService.add("Dashboard", '/main/dashboard', null, true);
            this.siteService.site = this.site;
            this.sessionService.setItem("site", this.site);
            return;
        }
        this.toastService.addSingle('error', '', 'Invalid user.');
        return;
    }
    onLanguageChange($event) {
        this.locale = $event.target.value;
        if (this.locale == undefined || this.locale == null || this.locale.length == 0) {
            this.locale = "en";
        }
        // the lang to use, if the lang isn't available, it will use the current loader to get them
        this.translate.use(this.locale);
        this.sessionService.setItem("ng-prime-language", this.locale);
    }
    getUrl() {
        return "url('assets/images/bg-home.png')";
    }
}
LoginComponent.ɵfac = function LoginComponent_Factory(t) { return new (t || LoginComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_app_core_services_user_data_service__WEBPACK_IMPORTED_MODULE_3__["UserDataService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_app_core_services_toast_service__WEBPACK_IMPORTED_MODULE_4__["ToastService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_app_core_services_route_state_service__WEBPACK_IMPORTED_MODULE_5__["RouteStateService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_app_core_services_session_service__WEBPACK_IMPORTED_MODULE_6__["SessionService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](_ngx_translate_core__WEBPACK_IMPORTED_MODULE_7__["TranslateService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_app_core_services_user_context_service__WEBPACK_IMPORTED_MODULE_8__["UserContextService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](_core_services_http_service__WEBPACK_IMPORTED_MODULE_1__["HttpService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](_core_services_site_service__WEBPACK_IMPORTED_MODULE_0__["SiteService"])); };
LoginComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdefineComponent"]({ type: LoginComponent, selectors: [["app-login"]], decls: 24, vars: 5, consts: [[1, "image"], [1, "p-grid", "p-nogutter", "p-justify-center", "p-align-center", 2, "height", "100%"], [1, "centertest"], [1, "p-grid", "p-justify-center", "p-align-center"], ["src", "assets/images/logo-original.png", "width", "200px", "height", "auto", 2, "opacity", "0.85"], [1, "p-col-12"], [1, "ui-float-label"], [1, "foalting-label"], ["optionLabel", "nom", "optionValue", "id", 3, "options", "ngModel", "ngModelChange"], [1, "ui-inputgroup"], [1, "ui-inputgroup-addon"], [1, "fa", "fa-user"], ["id", "input-username", "type", "text", "size", "30", "pInputText", "", "placeholder", "Enter User name", 3, "ngModel", "ngModelChange"], [1, "fa", "fa-key"], ["id", "input-password", "type", "password", "size", "30", "pInputText", "", "placeholder", "Enter Password", 3, "ngModel", "ngModelChange"], [1, "p-grid"], [1, "p-col-12", "center"], ["pButton", "", "type", "button", "label", "CONNEXION", 1, "ui-button-raised", "rougebutton", 3, "disabled", "click"]], template: function LoginComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](0, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](1, "div", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](2, "div", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](3, "div", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](4, "img", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](5, "div", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](6, "span", 6);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](7, "span", 7);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](8, "Site");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](9, "p-dropdown", 8);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("ngModelChange", function LoginComponent_Template_p_dropdown_ngModelChange_9_listener($event) { return ctx.site = $event; });
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](10, "div", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](11, "div", 9);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](12, "span", 10);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](13, "i", 11);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](14, "input", 12);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("ngModelChange", function LoginComponent_Template_input_ngModelChange_14_listener($event) { return ctx.userName = $event; });
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](15, "div", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](16, "div", 9);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](17, "span", 10);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](18, "i", 13);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](19, "input", 14);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("ngModelChange", function LoginComponent_Template_input_ngModelChange_19_listener($event) { return ctx.password = $event; });
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](20, "br");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](21, "div", 15);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](22, "div", 16);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](23, "button", 17);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("click", function LoginComponent_Template_button_click_23_listener() { return ctx.onClickLogin(); });
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](9);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("options", ctx.sitesResult)("ngModel", ctx.site);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngModel", ctx.userName);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngModel", ctx.password);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](4);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("disabled", !ctx.userName || !ctx.password);
    } }, directives: [primeng__WEBPACK_IMPORTED_MODULE_10__["Dropdown"], _angular_forms__WEBPACK_IMPORTED_MODULE_11__["NgControlStatus"], _angular_forms__WEBPACK_IMPORTED_MODULE_11__["NgModel"], _angular_forms__WEBPACK_IMPORTED_MODULE_11__["DefaultValueAccessor"], primeng_inputtext__WEBPACK_IMPORTED_MODULE_12__["InputText"], primeng_button__WEBPACK_IMPORTED_MODULE_13__["ButtonDirective"]], styles: [".language-menu[_ngcontent-%COMP%] {\r\n    position: absolute;\r\n    right: 0px;\r\n    padding: 10px;\r\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbInNyYy9hcHAvZmVhdHVyZXMvbG9naW4vbG9naW4uY29tcG9uZW50LmNzcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtJQUNJLGtCQUFrQjtJQUNsQixVQUFVO0lBQ1YsYUFBYTtBQUNqQiIsImZpbGUiOiJzcmMvYXBwL2ZlYXR1cmVzL2xvZ2luL2xvZ2luLmNvbXBvbmVudC5jc3MiLCJzb3VyY2VzQ29udGVudCI6WyIubGFuZ3VhZ2UtbWVudSB7XHJcbiAgICBwb3NpdGlvbjogYWJzb2x1dGU7XHJcbiAgICByaWdodDogMHB4O1xyXG4gICAgcGFkZGluZzogMTBweDtcclxufSJdfQ== */"] });
/*@__PURE__*/ (function () { _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵsetClassMetadata"](LoginComponent, [{
        type: _angular_core__WEBPACK_IMPORTED_MODULE_2__["Component"],
        args: [{
                selector: 'app-login',
                templateUrl: 'login.component.html',
                styleUrls: ['login.component.css']
            }]
    }], function () { return [{ type: src_app_core_services_user_data_service__WEBPACK_IMPORTED_MODULE_3__["UserDataService"] }, { type: src_app_core_services_toast_service__WEBPACK_IMPORTED_MODULE_4__["ToastService"] }, { type: src_app_core_services_route_state_service__WEBPACK_IMPORTED_MODULE_5__["RouteStateService"] }, { type: src_app_core_services_session_service__WEBPACK_IMPORTED_MODULE_6__["SessionService"] }, { type: _ngx_translate_core__WEBPACK_IMPORTED_MODULE_7__["TranslateService"] }, { type: src_app_core_services_user_context_service__WEBPACK_IMPORTED_MODULE_8__["UserContextService"] }, { type: _core_services_http_service__WEBPACK_IMPORTED_MODULE_1__["HttpService"] }, { type: _core_services_site_service__WEBPACK_IMPORTED_MODULE_0__["SiteService"] }]; }, null); })();


/***/ }),

/***/ "./src/app/features/login/login.module.ts":
/*!************************************************!*\
  !*** ./src/app/features/login/login.module.ts ***!
  \************************************************/
/*! exports provided: HttpLoaderFactory, LoginModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "HttpLoaderFactory", function() { return HttpLoaderFactory; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LoginModule", function() { return LoginModule; });
/* harmony import */ var primeng__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! primeng */ "./node_modules/primeng/__ivy_ngcc__/fesm2015/primeng.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common */ "./node_modules/@angular/common/fesm2015/common.js");
/* harmony import */ var src_app_features_login_login_component__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! src/app/features/login/login.component */ "./src/app/features/login/login.component.ts");
/* harmony import */ var src_app_features_login_login_routing__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! src/app/features/login/login.routing */ "./src/app/features/login/login.routing.ts");
/* harmony import */ var src_app_app_common_module__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! src/app/app.common.module */ "./src/app/app.common.module.ts");
/* harmony import */ var _ngx_translate_core__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! @ngx-translate/core */ "./node_modules/@ngx-translate/core/__ivy_ngcc__/fesm2015/ngx-translate-core.js");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! @angular/common/http */ "./node_modules/@angular/common/fesm2015/http.js");
/* harmony import */ var _ngx_translate_http_loader__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! @ngx-translate/http-loader */ "./node_modules/@ngx-translate/http-loader/__ivy_ngcc__/fesm2015/ngx-translate-http-loader.js");











function HttpLoaderFactory(http) {
    return new _ngx_translate_http_loader__WEBPACK_IMPORTED_MODULE_8__["TranslateHttpLoader"](http);
}
class LoginModule {
}
LoginModule.ɵmod = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineNgModule"]({ type: LoginModule });
LoginModule.ɵinj = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineInjector"]({ factory: function LoginModule_Factory(t) { return new (t || LoginModule)(); }, imports: [[
            _angular_common__WEBPACK_IMPORTED_MODULE_2__["CommonModule"],
            src_app_features_login_login_routing__WEBPACK_IMPORTED_MODULE_4__["LoginRoutingModule"],
            src_app_app_common_module__WEBPACK_IMPORTED_MODULE_5__["AppCommonModule"],
            primeng__WEBPACK_IMPORTED_MODULE_0__["DropdownModule"],
            primeng__WEBPACK_IMPORTED_MODULE_0__["PanelModule"],
            _ngx_translate_core__WEBPACK_IMPORTED_MODULE_6__["TranslateModule"].forChild({
                loader: {
                    provide: _ngx_translate_core__WEBPACK_IMPORTED_MODULE_6__["TranslateLoader"],
                    useFactory: HttpLoaderFactory,
                    deps: [_angular_common_http__WEBPACK_IMPORTED_MODULE_7__["HttpClient"]]
                },
                isolate: false
            })
        ],
        _ngx_translate_core__WEBPACK_IMPORTED_MODULE_6__["TranslateModule"]] });
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵsetNgModuleScope"](LoginModule, { declarations: [src_app_features_login_login_component__WEBPACK_IMPORTED_MODULE_3__["LoginComponent"]], imports: [_angular_common__WEBPACK_IMPORTED_MODULE_2__["CommonModule"],
        src_app_features_login_login_routing__WEBPACK_IMPORTED_MODULE_4__["LoginRoutingModule"],
        src_app_app_common_module__WEBPACK_IMPORTED_MODULE_5__["AppCommonModule"],
        primeng__WEBPACK_IMPORTED_MODULE_0__["DropdownModule"],
        primeng__WEBPACK_IMPORTED_MODULE_0__["PanelModule"], _ngx_translate_core__WEBPACK_IMPORTED_MODULE_6__["TranslateModule"]], exports: [_ngx_translate_core__WEBPACK_IMPORTED_MODULE_6__["TranslateModule"]] }); })();
/*@__PURE__*/ (function () { _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵsetClassMetadata"](LoginModule, [{
        type: _angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"],
        args: [{
                imports: [
                    _angular_common__WEBPACK_IMPORTED_MODULE_2__["CommonModule"],
                    src_app_features_login_login_routing__WEBPACK_IMPORTED_MODULE_4__["LoginRoutingModule"],
                    src_app_app_common_module__WEBPACK_IMPORTED_MODULE_5__["AppCommonModule"],
                    primeng__WEBPACK_IMPORTED_MODULE_0__["DropdownModule"],
                    primeng__WEBPACK_IMPORTED_MODULE_0__["PanelModule"],
                    _ngx_translate_core__WEBPACK_IMPORTED_MODULE_6__["TranslateModule"].forChild({
                        loader: {
                            provide: _ngx_translate_core__WEBPACK_IMPORTED_MODULE_6__["TranslateLoader"],
                            useFactory: HttpLoaderFactory,
                            deps: [_angular_common_http__WEBPACK_IMPORTED_MODULE_7__["HttpClient"]]
                        },
                        isolate: false
                    })
                ],
                declarations: [src_app_features_login_login_component__WEBPACK_IMPORTED_MODULE_3__["LoginComponent"]],
                exports: [_ngx_translate_core__WEBPACK_IMPORTED_MODULE_6__["TranslateModule"]]
            }]
    }], null, null); })();


/***/ }),

/***/ "./src/app/features/login/login.routing.ts":
/*!*************************************************!*\
  !*** ./src/app/features/login/login.routing.ts ***!
  \*************************************************/
/*! exports provided: LoginRoutingModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LoginRoutingModule", function() { return LoginRoutingModule; });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm2015/core.js");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/router */ "./node_modules/@angular/router/fesm2015/router.js");
/* harmony import */ var src_app_features_login_login_component__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! src/app/features/login/login.component */ "./src/app/features/login/login.component.ts");





const routes = [
    {
        path: '',
        component: src_app_features_login_login_component__WEBPACK_IMPORTED_MODULE_2__["LoginComponent"]
    }
];
class LoginRoutingModule {
}
LoginRoutingModule.ɵmod = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineNgModule"]({ type: LoginRoutingModule });
LoginRoutingModule.ɵinj = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineInjector"]({ factory: function LoginRoutingModule_Factory(t) { return new (t || LoginRoutingModule)(); }, imports: [[_angular_router__WEBPACK_IMPORTED_MODULE_1__["RouterModule"].forChild(routes)],
        _angular_router__WEBPACK_IMPORTED_MODULE_1__["RouterModule"]] });
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵsetNgModuleScope"](LoginRoutingModule, { imports: [_angular_router__WEBPACK_IMPORTED_MODULE_1__["RouterModule"]], exports: [_angular_router__WEBPACK_IMPORTED_MODULE_1__["RouterModule"]] }); })();
/*@__PURE__*/ (function () { _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵsetClassMetadata"](LoginRoutingModule, [{
        type: _angular_core__WEBPACK_IMPORTED_MODULE_0__["NgModule"],
        args: [{
                imports: [_angular_router__WEBPACK_IMPORTED_MODULE_1__["RouterModule"].forChild(routes)],
                exports: [_angular_router__WEBPACK_IMPORTED_MODULE_1__["RouterModule"]]
            }]
    }], null, null); })();


/***/ })

}]);
//# sourceMappingURL=src-app-features-login-login-module-es2015.js.map