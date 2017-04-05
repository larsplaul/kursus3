import { observable, computed, action } from "mobx";
import fetchHelper from "./fetchHelpers";
const URL = require("../../package.json").serverURL;

class BookFacade {
  @observable
  _books = [];

  @observable
  errorMessage = "";

  constructor() {
    this.getBooks();
  }

  @computed
  get books() {
    return this._books;
  }

  @action
  setErrorMessage = (err) => {
    this.errorMessage = err;
  }

  @action
  getBooks = () => {
    this.messageFromServer = "";
    let errorCode = 200;
    console.log(URL + "api/book");
    fetch(URL + "api/book")
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
        else {
          //console.log(res);
          this._books.replace(res);
        }
      })).catch(err => {
        //This is the only way (I have found) to verify server is not running
        this.setErrorMessage(fetchHelper.addJustErrorMessage(err));
      })
  }

  @action
  addEditBook = (book) => {
    let errorCode = 200;
    const method = book.id ? "PUT" : "POST";
    if(method==="POST"){
      delete book.id; //Remove id
    }
    const options = fetchHelper.makeOptions(method, true, book);
    var me = this;
    console.log(URL + "api/book");
    fetch(URL + "api/book", options)
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
        else {
          if (method === "PUT") {
            let book = this._books.find((b) => b.id === res.id);
            book.title = res.title;
            book.info = res.info;
            book.moreInfo = res.moreInfo;
          }
          else {
            this._books.push(res);
          }
        }
      })).catch(err => {
        me.setErrorMessage(fetchHelper.addJustErrorMessage(err));
      })
  }

  @action
  deleteBook = (id) => {
    const options = fetchHelper.makeOptions("DELETE", true);
    var me = this;
    fetch(URL + "api/book/" + id, options)
      .then((res) => {
        if (res.status > 210 || !res.ok) {
           throw new Error(`${res.error.message} (${res.error.code})`);
        }
        let book = this._books.find((b) => b.id === id);
        this._books.remove(book);

      })
      .catch(err => {
        me.setErrorMessage(fetchHelper.addJustErrorMessage(err));
      })
  }
}

export default new BookFacade();