
import { observable, action } from "mobx";
import fetchHelper from "./fetchHelpers"
const URL = require("../../package.json").serverURL;
import auth from "../authorization/auth";

/* encapsulates Data related to Admins */
class AdminStore {
  @observable users = [];
  @observable errorMessage = "";
  @observable validationError = ""

  @action
  setErrorMessage(err) {
    this.errorMessage = err;
  }
  @action
  setMessageFromServer(users) {
    this.users.replace(users);
  }

  @action
  getData = () => {
    this.errorMessage = "";
    let errorCode = 200;

    const options = fetchHelper.makeOptions("GET", true);
    fetch(URL + "api/users", options)
      .then((res) => {
        if (res.status > 200 || !res.ok) {
          errorCode = res.status;
        }
        return res.json();
      })
      .then((res) => {
        if (errorCode !== 200) {
          throw new Error(`${res.error.message} (${res.error.code})`);
        }
        else {
          this.setMessageFromServer(res.users);
        }
      }).catch(err => {
        this.setErrorMessage(fetchHelper.addJustErrorMessage(err));
      })
  }



  @action
  validateUser = (user) => {
    let err = "";
    if (user.userName.length < 2) {
      err += "User name min length is 2";
    }
    if (user.password.length < 4) {
      err += "Password min length:4";
    }
    if (user.roles.length === 0) {
      err += "At least one role must be selected";
    }
    this.validationError = err;
    return err === "";
  }

  @action
  checkUsername = (userName) => {
    this.setErrorMessage("");
    this.errorMessage = "";
    fetch(URL + "api/users/check/" + userName)
      .then(action((res) => {
        if (res.status > 210 || !res.ok) {
          this.validationError = "This user name is taken";
          return;
        }
        this.validationError = "";
      }))

  }

  @action
  removeUser = (userName) => {
    this.setErrorMessage("");
    if (auth.userName === userName) {
      return this.setErrorMessage("You cannot remove yourself!");
    }
    this.errorMessage = "";
    const options = fetchHelper.makeOptions("DELETE", true);
    fetch(URL + "api/users/" + userName, options)
      .then(action((res) => {
        if (res.status > 210 || !res.ok) {
          throw new Error("Could not delete the suggested book");
        }
        let user = this.users.find((user) => user.userName === userName);
        this.users.remove(user);
      }))
      .catch(err => {
        this.setErrorMessage(fetchHelper.addJustErrorMessage(err));
      })
  }
  @action
  addUser = (user) => {
    this.validationError = "";
    if (!this.validateUser(user)) {
      return;
    }
    let errorCode = 200;
    const options = fetchHelper.makeOptions("POST", true, user);
    var me = this;
    fetch(URL + "api/users", options)
      .then((res) => {
        if (res.status > 210 || !res.ok) {
          errorCode = res.status;
        }
        return res.json();
      })
      .then(action((res) => {  //Note the action wrapper to allow for useStrict
        if (errorCode !== 200) {
          throw new Error(`${res.error.message} (${res.error.code})`);
        }
        this.users.push(res);
      }
      )).catch(err => {
        me.setErrorMessage(fetchHelper.addJustErrorMessage(err));
      })
  }



}
let adminStore = new AdminStore(URL);

//Only for debugging
//window.adminStore = adminStore;
export default adminStore;
