import React from 'react';
import ReactDOM from 'react-dom';
import './styles/index.css';
import { hashHistory, Router, Route, IndexRoute } from 'react-router'
import App from './pages/App';
import Home from './pages/Home';
import Login from "./pages/Login";
import Logout from "./pages/Logout";
import Documentation from "./pages/Documentation";
import AdminPage from "./pages/AdminPage";
import Details from "./pages/Details";
import Products from "./pages/Products";
import Company from "./pages/Company"
import bookFacade from "./stores/bookStore"
import auth from "./authorization/auth";


ReactDOM.render((
  <Router history={hashHistory}>
    <Route path="/" component={App}>
      <IndexRoute component={Home}/>
      <Route path="login" component={Login} />
      <Route path="logout" component={Logout} />
      <Route path="documentation" component={Documentation} />
      {auth.isAdmin ? <Route path="admin" component={AdminPage} /> : null}
      <Route path="products" component={Products} bookFacade={bookFacade}  />
      <Route path="products/details/:id" component={Details} bookFacade={bookFacade}/>
      {auth.isUser ? <Route path="products/details" component={Details} bookFacade={bookFacade}/> : null}
      <Route path="company" component={Company} />
    </Route>
  </Router>
), document.getElementById('root'))