import React, {Component} from 'react';
import Cookies from "js-cookie";
import Login from './Login';


class Logout extends Component {

    constructor(props) {
        super(props);

        Cookies.remove('token');
        Cookies.remove('username');
        Cookies.remove('role');
        sessionStorage.clear();
    }

    render() {
        return(
            <Login { ...this.props}/>
        )
    }
}

export default Logout;
