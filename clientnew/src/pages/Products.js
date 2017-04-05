import React from "react";
import { Link } from "react-router";
import facade from "../stores/bookStore";
import { observer } from "mobx-react";
//import DevTools from "mobx-react-devtools"

@observer
export default class Components extends React.Component {

  render() {
    //var facade = this.props.route.bookFacade;
    return (
      <div>
        <h2>All our great books </h2>
        <ul>
          {facade.books.map((book, index) => <li key={index}>
            {book.title} <Link to={`products/details/${index}`}>(details)</Link></li>)}
        </ul>
      </div>
    );
  }

}