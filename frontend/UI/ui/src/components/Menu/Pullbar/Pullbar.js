import React, {Component} from 'react';
import Button from '@material-ui/core/Button';
import Cookies from 'js-cookie';
import './Pullbar.css';
import Toggle from '../Toggle/Toggle';

class Pullbar extends Component {
    constructor(props) {

        super(props);
        this.state = {
            username: Cookies.get('username')
        };

    }

    renderVisitor = () => (
        <header className="pullbar">
            <nav className="pullbar_nav">
                <div>
                    <Toggle click={this.props.clickHandler} />
                </div>
                <div className="bar_logo"><a href="/">Hi Guest</a></div>
                <div className="partial" />
                <div className="pull_items">
                    <ul>
                        <li><Button href="/Login">log in</Button></li>
                        <li><Button href="/Signup">sign up</Button></li>
                    </ul>
                </div>
            </nav>
        </header>
    );

    renderUser = () => (
        <header className="pullbar">
            <nav className="pullbar_nav">
                <div>
                    <Toggle click={this.props.clickHandler} />
                </div>
                <div className="bar_logo"><a href="/">Hi {this.state.username}</a></div>
                <div className="partial" />
                <div className="pull_items">
                    <ul>
                        <li><Button href="/Logout">log out</Button></li>
                    </ul>
                </div>
            </nav>
        </header>
    );

    render() {
        return(
            <div>
                {/* check if there is a user info inside web's cookie to
                                show different content */}
                {Cookies.get('username') ? this.renderUser() : this.renderVisitor()}
            </div>
        );
    }
}
export default Pullbar;
