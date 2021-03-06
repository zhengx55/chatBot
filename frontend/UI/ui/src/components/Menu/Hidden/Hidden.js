import React, {Component} from 'react';
import './Hidden.css';
import Button from '@material-ui/core/Button';
import Cookies from "js-cookie";

class Hidden extends Component {
    constructor(props) {

        super(props);
        this.state = {
            role: Cookies.get('role')
        };

    }

    renderAdmin = () => (
        <nav className="hidden_pull_down">
            <ul>
                <li>
                    <Button href="/chatbot">Chatbot</Button>
                </li>
                <li>
                    <Button href="/feedback">Give feedback</Button>
                </li>
                <li>
                    <Button href="/admin">Admin</Button>
                </li>
            </ul>
        </nav>
    );

    renderUser = () => (
        <nav className="hidden_pull_down">
            <ul>
                <li>
                    <Button href="/chatbot">Chatbot</Button>
                </li>
                <li>
                    <Button href="/feedback">Give feedback</Button>
                </li>
            </ul>
        </nav>
    );

    render() {
        return(
            <div>
                {/* check if there is a user info inside web's cookie to
                                show different content */}
                {Cookies.get('role')==="ADMIN" ? this.renderAdmin() : this.renderUser()}
            </div>
        );
    }
}

export default Hidden;

