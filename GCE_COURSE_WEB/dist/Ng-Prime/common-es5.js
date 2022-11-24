function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["common"], {
  /***/
  "./src/app/core/models/user.model.ts":
  /*!*******************************************!*\
    !*** ./src/app/core/models/user.model.ts ***!
    \*******************************************/

  /*! exports provided: User */

  /***/
  function srcAppCoreModelsUserModelTs(module, __webpack_exports__, __webpack_require__) {
    "use strict";

    __webpack_require__.r(__webpack_exports__);
    /* harmony export (binding) */


    __webpack_require__.d(__webpack_exports__, "User", function () {
      return User;
    });

    var User = function User() {
      _classCallCheck(this, User);

      this.userId = null;
      this.userName = null;
      this.password = null;
      this.emailId = null;
      this.birthDate = null;
    };
    /***/

  },

  /***/
  "./src/app/core/services/toast.service.ts":
  /*!************************************************!*\
    !*** ./src/app/core/services/toast.service.ts ***!
    \************************************************/

  /*! exports provided: ToastService */

  /***/
  function srcAppCoreServicesToastServiceTs(module, __webpack_exports__, __webpack_require__) {
    "use strict";

    __webpack_require__.r(__webpack_exports__);
    /* harmony export (binding) */


    __webpack_require__.d(__webpack_exports__, "ToastService", function () {
      return ToastService;
    });
    /* harmony import */


    var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(
    /*! @angular/core */
    "./node_modules/@angular/core/fesm2015/core.js");
    /* harmony import */


    var primeng_api__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(
    /*! primeng/api */
    "./node_modules/primeng/__ivy_ngcc__/fesm2015/primeng-api.js");
    /**
     * Toast service class
     * This class provides methods to add single, multiple alerts as a toast
     */


    var ToastService = /*#__PURE__*/function () {
      function ToastService(messageService) {
        _classCallCheck(this, ToastService);

        this.messageService = messageService;
      }
      /**
       * add single toast message
       * @param severity Severity level of the message, valid values are "success", "info", "warn" and "error"
       * @param summary Summary text of the message.
       * @param detail Detail text of the message.
       */


      _createClass(ToastService, [{
        key: "addSingle",
        value: function addSingle(severity, summary, detail) {
          this.messageService.add({
            severity: severity,
            summary: summary,
            detail: detail
          });
        }
        /**
         * add multiple toast messages
         * @param messages
         * array of message type {severity:'success', summary:'Service Message', detail:'Via MessageService'}
         */

      }, {
        key: "addMultiple",
        value: function addMultiple(messages) {
          this.messageService.addAll(messages);
        }
        /**
         * clear all toast messages
         */

      }, {
        key: "clear",
        value: function clear() {
          this.messageService.clear();
        }
      }]);

      return ToastService;
    }();

    ToastService.ɵfac = function ToastService_Factory(t) {
      return new (t || ToastService)(_angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵinject"](primeng_api__WEBPACK_IMPORTED_MODULE_1__["MessageService"]));
    };

    ToastService.ɵprov = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineInjectable"]({
      token: ToastService,
      factory: ToastService.ɵfac,
      providedIn: 'root'
    });
    /*@__PURE__*/

    (function () {
      _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵsetClassMetadata"](ToastService, [{
        type: _angular_core__WEBPACK_IMPORTED_MODULE_0__["Injectable"],
        args: [{
          providedIn: 'root'
        }]
      }], function () {
        return [{
          type: primeng_api__WEBPACK_IMPORTED_MODULE_1__["MessageService"]
        }];
      }, null);
    })();
    /***/

  },

  /***/
  "./src/app/core/services/user-data.service.ts":
  /*!****************************************************!*\
    !*** ./src/app/core/services/user-data.service.ts ***!
    \****************************************************/

  /*! exports provided: UserDataService */

  /***/
  function srcAppCoreServicesUserDataServiceTs(module, __webpack_exports__, __webpack_require__) {
    "use strict";

    __webpack_require__.r(__webpack_exports__);
    /* harmony export (binding) */


    __webpack_require__.d(__webpack_exports__, "UserDataService", function () {
      return UserDataService;
    });
    /* harmony import */


    var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(
    /*! @angular/core */
    "./node_modules/@angular/core/fesm2015/core.js");
    /* harmony import */


    var src_app_core_models_user_model__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(
    /*! src/app/core/models/user.model */
    "./src/app/core/models/user.model.ts");
    /**
     * user service class
     */


    var UserDataService = /*#__PURE__*/function () {
      function UserDataService() {
        _classCallCheck(this, UserDataService);

        this.users = [];
        var user = {
          userId: 1,
          userName: "admin",
          password: "password",
          emailId: "admin@admin.com",
          birthDate: new Date('10/28/1992')
        };
        this.users.push(user);
      }
      /**
       * get user by user name and password
       * @param userName
       * @param password
       */


      _createClass(UserDataService, [{
        key: "getUserByUserNameAndPassword",
        value: function getUserByUserNameAndPassword(userName, password) {
          var user = null;
          this.users.forEach(function (element) {
            if (element.userName === userName && element.password === password) {
              user = element;
            }
          });
          return user;
        }
        /**
         * add new user
         * @param userName
         * @param password
         * @param emailId
         * @param birthDate
         */

      }, {
        key: "addUser",
        value: function addUser(userName, password, emailId, birthDate) {
          var userId = this.users.length + 1;
          var user = new src_app_core_models_user_model__WEBPACK_IMPORTED_MODULE_1__["User"]();
          user.userId = userId;
          user.userName = userName;
          user.password = password;
          user.emailId = emailId;
          user.birthDate = birthDate;
          this.users.push(user);
          return true;
        }
      }]);

      return UserDataService;
    }();

    UserDataService.ɵfac = function UserDataService_Factory(t) {
      return new (t || UserDataService)();
    };

    UserDataService.ɵprov = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineInjectable"]({
      token: UserDataService,
      factory: UserDataService.ɵfac,
      providedIn: 'root'
    });
    /*@__PURE__*/

    (function () {
      _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵsetClassMetadata"](UserDataService, [{
        type: _angular_core__WEBPACK_IMPORTED_MODULE_0__["Injectable"],
        args: [{
          providedIn: 'root'
        }]
      }], function () {
        return [];
      }, null);
    })();
    /***/

  }
}]);
//# sourceMappingURL=common-es5.js.map