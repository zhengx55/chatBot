import React, { Component } from 'react';
import './App.css';
import Admin from './components/Admin';
import Chatbot from './components/Chatbot';
import Pullbar from './components/Menu/Pullbar/Pullbar';
import Feedback from './components/Feedback';
import {BrowserRouter as Router, Route} from 'react-router-dom';
import Login from "./components/Login";
import Logout from "./components/Logout";
import Signup from "./components/Signup";

class App extends Component{

    render(){
        return (
            <Router>
                <div className="App">
                    <Pullbar />
                    <Route exact path="/" component={Login}/>
                    <Route path="/login" component={Login}/>
                    <Route path="/signup" component={Signup}/>
                    <Route path="/logout" component={Logout}/>
                    <Route path="/admin" component={Admin} />
                    <Route path="/chatbot" component={Chatbot} />
                    <Route path="/feedback" component={Feedback} />
                </div>
            </Router>
        );
    }
}
export default App;
