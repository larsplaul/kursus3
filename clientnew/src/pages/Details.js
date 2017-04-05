import React from "react";
import { Link } from "react-router"
import bookFacade from "../stores/bookStore"
//import DevTools from "mobx-react-devtools"
import { observer } from "mobx-react";
import auth from "../authorization/auth";

@observer
export default class Details extends React.Component {

  componentWillMount() {
    let id = this.props.params.id;
    //let book = this.props.route.bookFacade.books.filter((book, index) => {
    let book = bookFacade.books.filter((book, index) => {
      return index === Number(id);
    })[0];
    const disabled = book;  //Show Editable fields if no book was brought in
    book = book ? {...book} : this.emptyBook();
    this.setState({ book, disabled: disabled });
    bookFacade.setErrorMessage("");
  }

  emptyBook = () => {
    return { id: "", title: "", info: "", moreInfo: "" };
  }

  deleteBook = () => {
    this.setState({ disabled: false });
    bookFacade.deleteBook(this.state.book.id);
    this.setState({ book: this.emptyBook() });
  }

  saveBook = () => {
    bookFacade.addEditBook(this.state.book);
    //Clear input fields, to allow for a new "new book"
    this.setState({ book: this.emptyBook() });
    event.preventDefault();
  }

  handleChange = (event) => {
    var book = this.state.book;
    var id = event.target.id;
    book[id] = event.target.value;
    this.setState({ book });
  }

  newBook = () => {
    this.setState({ book: this.emptyBook(), disabled: false });
  }

  editBook = () => {
    this.setState({ disabled: false });
  }

  render() {

    var book = this.state.book;
    var disabled = this.state.disabled;
    var style = { width: "35em",margin: -3 }
    var styleSmall = { width: "10em" }

    return (
      <div>
        {/*<DevTools />*/}
         {auth.isUser ?  <h2>Add/Edit/Delete Books</h2> :<h2>Book Details</h2> }
        <form>
          <div className="row form-group">
            <div className="col-md-1">Id:</div>
            <div className="col-md-8">
              <input id="id" disabled type="text" value={book.id} style={styleSmall}
              className="form-control" /></div>
          </div>
          <div className="row form-group">
            <div className="col-md-1">Title</div>
            <div className="col-md-8">
              <input id="title" disabled={disabled} onChange={this.handleChange} style={style} className="form-control"
                type="text" value={book.title} /></div>
          </div>
          <div  className="row form-group">
            <div className="col-md-1">Info</div>
            <div className="col-md-8"><input id="info" disabled={disabled} className="form-control"
                 onChange={this.handleChange} style={style} type="text" value={book.info} />
            </div>
          </div>
          <div className="row form-group">
            <div className="col-md-1">More Info</div>
            <div className="col-md-8">
              <input id="moreInfo" disabled={disabled} onChange={this.handleChange} style={style} className="form-control"
                     type="text" value={book.moreInfo} />
            </div>
          </div>
        </form>
        {auth.isUser ? 
          <div>
            <br />
            <button className="btn btn-default" onClick={this.editBook}>Edit Book</button>
            <button className="btn btn-primary" style={{ marginLeft: 7 }} onClick={this.newBook}>Add New Book</button>
            <button className="btn btn-success" style={{ marginLeft: 7 }} onClick={this.saveBook}>Save</button>
            <button className="btn btn-danger" style={{ marginLeft: 7 }} onClick={this.deleteBook}>Delete</button>
            <br/><br/>
            
            <p>Just for debuging:  {JSON.stringify(this.state.book)}</p>
          </div>
          : null}
        <br />
        <Link to="/products">All books</Link>
        <h4 style={{ color: "red" }}>{bookFacade.errorMessage}</h4>
      </div>
    );
  }
}