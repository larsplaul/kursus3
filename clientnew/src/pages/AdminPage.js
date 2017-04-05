import React, { Component } from 'react'
import { observer } from "mobx-react";
import userStore from "../stores/adminStore";
import DevTools from "mobx-react-devtools";
import Select from "react-select";
import 'react-select/dist/react-select.css';

@observer
class AdminPage extends Component {

  constructor() {
    super();
    this.state = { user: this.newUser() }
  }

  options =() => [
    { value: 'User', label: 'User' },
    { value: 'Admin', label: 'Admin' }
  ];

  newUser = () => {
    return { userName: "", password: "", roles: ["User"] };
  }

  componentWillMount() {
    /*
    This will fetch data each time you navigate to this route
    Move to constructor, if only required once, or add "logic" to determine when data should be "refetched"
    */
    userStore.getData();
    userStore.validationError = "";
  }

  removeUser = (userName) => {
    userStore.removeUser(userName);
  }

  saveUser = () => {
    if(!userStore.validateUser(this.state.user)){
      return;
    }
    let user = this.state.user;
    userStore.addUser(user);
    this.setState({user: this.newUser()});
  }

  clearFields = () => {
    this.setState({ user: this.newUser() })
  }

  onChange = (event) => {
    var user = this.state.user;
    var id = event.target.id;
    user[id] = event.target.value;
    this.setState({ user });
    event.preventDefault();
  }

  onSelectChange = (roles) => {
    var user = this.state.user;
    let rolesStr = [];
    roles.forEach(role=>{
       rolesStr.push(role.value);
    })
    user.roles = rolesStr;
    this.setState({user});
  }

  checkUsername = () => {
    userStore.checkUsername(this.state.user.userName);
  }

  render() {
    var rows = userStore.users.map(user => (
      <tr key={user.userName}><td>{user.userName}</td><td>{user.roles.join(",")}</td>
        <td><button className="btn btn-link" onClick={this.removeUser.bind(this, user.userName)}>Remove</button></td>
      </tr>
    ))
    let user = this.state.user;
    return (
      <div>
        {/*<DevTools />*/}
        <h2>User Administration</h2>
        <hr />
        <div className="row">
          <div className="col-md-4">
            <table className="table">
              <thead>
                <tr><th>User Name</th><th>Roles</th><th>&nbsp;</th></tr>
              </thead>
              <tbody>
                {rows}
              </tbody>
            </table>
          </div>
          <div className="col-md-7" style={{ backgroundColor: "floralWhite", minHeight: 200, padding: 10,marginLeft:15 }}>
            <h2 style={{marginTop: -8}}>Add New User</h2>
            <form>
              <div className="row form-group">
                <div className="col-sm-2">User Name</div>
                <div className="col-sm-6"><input id="userName" type="text" className="form-control"
                onBlur={this.checkUsername} onChange={this.onChange} value={user.userName} /></div>
              </div>
              <div className="row form-group">
                <div className="col-sm-2">Password (temp)</div>
                <div className="col-sm-6"><input id="password" type="text" onChange={this.onChange} className="form-control"
                     value={user.password} /></div>
              </div>
              <div className="row">
                <div className="col-sm-2">Roles</div>
                <div className="col-sm-6"><Select onChange={this.onSelectChange}
                  options={this.options()} value={user.roles} multi={true}/></div>
              </div>
            </form>
            <p style={{ color: "red", fontStyle: "italic", height: "1.5em" }}>{userStore.validationError}</p>
            <div>
              <br />
              <button className="btn btn-primary" style={{ marginLeft: 7 }} onClick={this.clearFields}>New User</button>
              <button className="btn btn-success" style={{ marginLeft: 7 }} onClick={this.saveUser}>Save User</button>
              <button className="btn btn-default" style={{ marginLeft: 7 }} onClick={this.clearFields}>Cancel</button>
            </div>
          </div>
        </div>

        <h4 style={{ color: "red" }}>{userStore.errorMessage}</h4>
      </div>
    )
  }

}

export default AdminPage;